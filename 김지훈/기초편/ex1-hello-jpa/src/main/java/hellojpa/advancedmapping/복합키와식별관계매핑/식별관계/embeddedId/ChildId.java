package hellojpa.advancedmapping.복합키와식별관계매핑.식별관계.embeddedId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ChildId implements Serializable {

	private String parentId; // @MapsId("parentId")로 매핑

	@Column(name = "CHILD_ID")
	private String id;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ChildId childId = (ChildId)o;

		if (parentId != null ? !parentId.equals(childId.parentId) : childId.parentId != null)
			return false;
		return id != null ? id.equals(childId.id) : childId.id == null;
	}

	@Override
	public int hashCode() {
		int result = parentId != null ? parentId.hashCode() : 0;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}
}
