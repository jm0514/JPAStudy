package hellojpa.advancedmapping.복합키와식별관계매핑.embeddedid;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParentId implements Serializable {
	@Column(name = "PARENT_ID1")
	private String id1;

	@Column(name = "PARENT_ID2")
	private String id2;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ParentId parentId = (ParentId)o;

		if (id1 != null ? !id1.equals(parentId.id1) : parentId.id1 != null)
			return false;
		return id2 != null ? id2.equals(parentId.id2) : parentId.id2 == null;
	}

	@Override
	public int hashCode() {
		int result = id1 != null ? id1.hashCode() : 0;
		result = 31 * result + (id2 != null ? id2.hashCode() : 0);
		return result;
	}
}
