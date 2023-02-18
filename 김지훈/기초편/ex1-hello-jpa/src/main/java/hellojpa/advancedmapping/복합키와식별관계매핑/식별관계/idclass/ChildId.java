package hellojpa.advancedmapping.복합키와식별관계매핑.식별관계.idclass;

import java.io.Serializable;

public class ChildId implements Serializable {

	private String parent;  // Child.parent  매핑
	private String childId; // Child.childId 매핑

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ChildId childId1 = (ChildId)o;

		if (parent != null ? !parent.equals(childId1.parent) : childId1.parent != null)
			return false;
		return childId != null ? childId.equals(childId1.childId) : childId1.childId == null;
	}

	@Override
	public int hashCode() {
		int result = parent != null ? parent.hashCode() : 0;
		result = 31 * result + (childId != null ? childId.hashCode() : 0);
		return result;
	}
}
