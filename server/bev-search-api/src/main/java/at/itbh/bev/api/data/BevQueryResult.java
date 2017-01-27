package at.itbh.bev.api.data;

import javax.xml.bind.annotation.XmlSeeAlso;

public class BevQueryResult extends QueryResult {

	private BevAddress address;
	
	public BevAddress getAddress() {
		return address;
	}
	
	public void setAddress(BevAddress address) {
		this.address = address;
	}
	
}
