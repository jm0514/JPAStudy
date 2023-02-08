package hellojpa.advancedmapping.복합키와식별관계매핑.식별관계.embeddedId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class GrandChild {
	@EmbeddedId
	private GrandChildId id;

	@MapsId("childId") // GrandChildId.childId 매핑ㄴ
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "PARENT_ID"),
		@JoinColumn(name = "CHILD_ID")
	})
	private Child child;

	private String name;
}
