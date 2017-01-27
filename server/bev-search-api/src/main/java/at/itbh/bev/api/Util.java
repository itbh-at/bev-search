package at.itbh.bev.api;

import java.util.concurrent.Future;

import javax.ejb.Local;

/**
 * Utility methods
 */
@Local
public interface Util {

	/**
	 * Import data from the BEV CSV files and update all internal caches.
	 * 
	 * @return the duration of the import in ms
	 */
	public Future<Long> refreshBevData();

}
