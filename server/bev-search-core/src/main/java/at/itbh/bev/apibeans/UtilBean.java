/* 
 * Util.java
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

package at.itbh.bev.apibeans;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import at.itbh.bev.DataUpdateBean;
import at.itbh.bev.api.Util;

@Stateless
public class UtilBean implements Util {

	@Inject
	DataUpdateBean updateBean;
	
	@Inject
	Logger logger;
	
	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Future<Long> refreshBevData() {
		Long dbUpdateTime = -1l;
		Long indexUpdateTime = -1l;		
		try {
			dbUpdateTime = updateBean.rebuildDatabase();
			indexUpdateTime = updateBean.rebuildIndex();
		}
		catch (Exception e) {
			logger.error(e.getMessage());
		}
		return new AsyncResult<Long>(dbUpdateTime + indexUpdateTime);
	}

}
