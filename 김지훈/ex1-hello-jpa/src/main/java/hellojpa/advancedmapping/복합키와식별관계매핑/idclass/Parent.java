package hellojpa.advancedmapping.복합키와식별관계매핑.idclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@IdClass(ParentId.class)
@Getter
@Setter
@ToString
public class Parent {

	@Id
	@Column(name = "PARENT_ID1")
	private String id1; // Parent.id1 매핑

	@Id
	@Column(name = "PARENT_ID2")
	private String id2; // Parent.id2 매핑

	private String name;
}
