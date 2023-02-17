package hellojpa.advancedmapping.복합키와식별관계매핑.식별관계.embeddedId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Parent {
	@Id
	@Column(name = "PARENT_ID")
	private String id;

	private String name;
}
