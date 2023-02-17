package hellojpa.advancedmapping.복합키와식별관계매핑.식별관계.idclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@IdClass(GrandChildId.class)
@Getter
@Setter
@ToString
public class GrandChild {

	@Id
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "PARENT_ID"),
		@JoinColumn(name = "CHILD_ID")
	})
	private Child child;

	@Id
	@Column(name = "GRANDCHILD_ID")
	private String id;

	private String name;
}
