package hellojpa.advancedmapping.복합키와식별관계매핑.식별관계.embeddedId;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GrandChildId implements Serializable {

	private ChildId childId; // @MapsId("childId")로 매핑

	@Column(name = "GRANDCHILD_ID")
	private String id;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		GrandChildId that = (GrandChildId)o;

		if (childId != null ? !childId.equals(that.childId) : that.childId != null)
			return false;
		return id != null ? id.equals(that.id) : that.id == null;
	}

	@Override
	public int hashCode() {
		int result = childId != null ? childId.hashCode() : 0;
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}
}
