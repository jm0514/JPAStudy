package hellojpa.advancedmapping.복합키와식별관계매핑.식별관계.idclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@IdClass(ChildId.class)
@Getter
@Setter
@ToString
public class Child {

	@Id
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private Parent parent;

	@Id
	@Column(name = "CHILD_ID")
	private String childId;

	private String name;

}
