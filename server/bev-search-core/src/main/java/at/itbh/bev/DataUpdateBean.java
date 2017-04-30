/* 
 * DataUpdateBean.java
 *  
 * Copyright (C) 2017 Christoph D. Hermann <christoph.hermann@itbh.at>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.itbh.bev;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.CacheMode;
import org.hibernate.search.batchindexing.impl.SimpleIndexingProgressMonitor;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.jboss.logging.Logger;

import at.itbh.bev.jpa.AdresseDenormalized;
import at.itbh.bev.utils.Properties;
import at.itbh.bev.utils.SystemProperty;

/**
 * Read the BEV data from the CSV files and trigger a mass rebuild of the
 * Hibernate Search index.
 * 
 * <p>
 * The update operations are managed by a singleton EJB to ensure that only one
 * update operation is running at a time.
 * </p>
 * 
 * @author DI Christoph D. Hermann, ITBH
 *         <a href="mailto:christoph.hermann@itbh.at">&lt;christoph.hermann@itbh
 *         .at&gt;</a>
 */
@Singleton
public class DataUpdateBean {

	@Inject
	@SystemProperty(Properties.CSV_LOCATION)
	private String csvPath;

	@PersistenceContext
	private EntityManager em;

	@Inject
	private Logger logger;

	@Resource
	private EJBContext context;
	
	@Inject
	@SystemProperty(Properties.INDEX_LOCATION)
	private String indexLocation;

	private boolean rebuildRunning = false;

	/**
	 * Delete all data in the database and rebuild it with BEV data from the CSV
	 * files.
	 * 
	 * @throws IOException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@TransactionTimeout(value = 2, unit = TimeUnit.HOURS)
	public Long rebuildDatabase() {
		Long startTime = System.currentTimeMillis();

		if (rebuildRunning) {
			logger.warn(
					"There is already an rebuild operation running. Only one rebuild operation is allowed at a time.");
			throw new EJBException(
					"There is already an rebuild operation running. Only one rebuild operation is allowed at a time.");
		}

		rebuildRunning = true;
		try {
			Path importPath = Paths.get(csvPath, "import_data.sql");
			logger.infof("Start importing BEV data from the CSV files in '%s' using the script '%s'.", Paths.get(csvPath),
					importPath);

			String importScript = new String(Files.readAllBytes(importPath));

			// workaround since H2 does not support parameters in prepared
			// statements with multiple statements.
			importScript = importScript.replace(":path", "'" + Paths.get(csvPath) + "/'");
			Query updateStatement = em.createNativeQuery(importScript);
			updateStatement.executeUpdate();

			Path preparePath = Paths.get(csvPath, "prepare_data.sql");
			logger.infof("Optimizing the database using the script '%s'.", preparePath);

			String prepareScript = new String(Files.readAllBytes(preparePath));
			Query prepareStatement = em.createNativeQuery(prepareScript);
			prepareStatement.executeUpdate();
			rebuildRunning = false;

			logger.info("Finished importing the BEV data.");
		} catch (Exception e) {
			rebuildRunning = false;
			context.setRollbackOnly();
			logger.errorf("Exception while importing the BEV CSV data: '%s'", e.getMessage());
			throw new EJBException(e);
		} finally {
			rebuildRunning = false;
		}
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Create or refresh the spatial search index based on the address data in
	 * the database.
	 * @return 
	 * @return 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
//	@TransactionTimeout(value = 2, unit = TimeUnit.HOURS)
	public Long rebuildIndex() {
		Long startTime = System.currentTimeMillis();
		
		if (rebuildRunning) {
			logger.warn(
					"There is already an rebuild operation running. Only one rebuild operation is allowed at a time.");
			throw new EJBException(
					"There is already an rebuild operation running. Only one rebuild operation is allowed at a time.");
		}

		rebuildRunning = true;
		try {
			logger.info("Start the mass index rebuild.");
			
			FullTextEntityManager searchEm = Search.getFullTextEntityManager(em);
			searchEm.createIndexer(AdresseDenormalized.class)
			.batchSizeToLoadObjects( 1000 )
			.cacheMode( CacheMode.NORMAL )
			.threadsToLoadObjects( 12 )
			.idFetchSize( 1000 )
			.progressMonitor(new SimpleIndexingProgressMonitor(100000))
			.transactionTimeout( 7200 ).startAndWait();

			rebuildRunning = false;
			logger.info("Finished the mass index rebuild.");
		} catch (Exception e) {
			rebuildRunning = false;
			context.setRollbackOnly();
			logger.errorf("Exception while updating the search index: '%s'", e.getMessage());
			throw new EJBException(e);
		} finally {
			rebuildRunning = false;
		}
		return System.currentTimeMillis() - startTime;
	}
}
