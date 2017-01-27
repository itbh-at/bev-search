package at.itbh.bev;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import at.itbh.bev.jpa.AdresseDenormalized;

/**
 * Do some checks on application startup
 * 
 * @author DI Christoph D. Hermann, ITBH
 *         <a href="mailto:christoph.hermann@itbh.at">&lt;christoph.hermann@itbh
 *         .at&gt;</a>
 *
 */
@Startup
@Singleton
public final class StartupBean {

	@Resource(name = "csvPath")
	private String csvPath;

	@Inject
	private Logger logger;

	@EJB
	private DataBean dataBean;
	
	@PersistenceContext
	private EntityManager em;
	
	
	void analyzeResult(List<AdresseDenormalized> addresses) {
		for (AdresseDenormalized addr : addresses) {
			System.out.println(addr);
			System.out.println(addr.getLatitude() + "N " + addr.getLongitude() + "E ");
		}
	}

	@PostConstruct
	private void init() {
		if (csvPath == null || csvPath.trim().length() == 0) {
			logger.fatal(
					"The context variable 'csvPath' must be set to a non empty value. This is the path where the BEV CSV files and update scripts are located.");
		}
		else {
			logger.infof("The BEV CSV must be located under '%s'.", csvPath);
		}
		/*
		System.out.println(
				"Querying address of the Austrian Parliament (ADRCD=6791990). 1 address with 1 building. (= 1 common address)");
		analyzeResult(dataBean.getAddressesByADRCD("6791990"));

		System.out.println(
				"Querying address of the Bahnhofsplatz 2 in Eisenstadt (ADRCD=5151999). 1 address without a building (= 1 common address)");
		analyzeResult(dataBean.getAddressesByADRCD("5151999"));

		System.out.println(
				"Querying address of the Karl-Marx-Hof in Vienna (ADRCD=6904968). 1 address without 39 buildings (= 39 common addresses)");
		analyzeResult(dataBean.getAddressesByADRCD("6904968"));
		*/
	}
}
