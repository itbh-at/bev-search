package at.itbh.bev.jpa;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class PreventAnyUpdate {

	@PrePersist
	void onPrePersists(Object o) {
		throw new RuntimeException("Persisting an entity is strictly forbidden.");
	}
	
	@PreUpdate
	void onPreUpdate(Object o) {
		throw new RuntimeException("Updating an entity is strictly forbidden.");
	}
	
	@PreRemove
	void onPreRemove(Object o) {
		throw new RuntimeException("Removing an entity is strictly forbidden.");
	}
}