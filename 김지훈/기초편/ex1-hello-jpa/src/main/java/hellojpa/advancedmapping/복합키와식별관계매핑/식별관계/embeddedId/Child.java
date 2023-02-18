package hellojpa.advancedmapping.복합키와식별관계매핑.식별관계.embeddedId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class Child {
	@EmbeddedId
	private ChildId id;

	@MapsId("parentId") // ChildId.parentId 매핑
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	public Parent parent;

	private String name;
}
