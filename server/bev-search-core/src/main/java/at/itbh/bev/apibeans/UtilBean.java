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
