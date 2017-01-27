package at.itbh.bev;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.itbh.bev.jpa.AdresseDenormalized;

@Stateless
public class DataBean {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Query the database for all addresses (<code>BEV.ADRESSE</code>) and
	 * buildings (<code>BEV.GEBAEUDE</code>) with the address code
	 * <code>adrcd</code>
	 * 
	 * <p>
	 * It is important to know that what is commonly referred to as address
	 * corresponds to a combination of address and building in the BEV data set.
	 * There are addresses with buildings and without buildings. The table
	 * <code>BEV.ADRESSE_DENORMALIZED</code> manages this combination and
	 * introduces a new unique key which is not part of the BEV data set.
	 * </p>
	 * 
	 * <p>
	 * <b>It is wrong to expect a unique address record when querying the BEV
	 * data set with an <code>ADRCD</code>. <code>ADRCD</code> my be unique if
	 * there are no buildings. If there are buildings the combination of
	 * <code>ADRCD</code> and <code>SUBCD</code> from table
	 * <code>BEV.GEBAEUDE</code> is a unique identifier.
	 * </p>
	 * 
	 * @param adrcd
	 *            the BEV identifier of an address
	 * @return the list of flattened addresses
	 */
	public List<AdresseDenormalized> getAddressesByADRCD(String adrcd) {
		TypedQuery<AdresseDenormalized> query = em.createNamedQuery("AdresseDenormalized.findByADRCD",
				AdresseDenormalized.class);
		query.setParameter("adrcd", adrcd);
		return query.getResultList();
	}

}
