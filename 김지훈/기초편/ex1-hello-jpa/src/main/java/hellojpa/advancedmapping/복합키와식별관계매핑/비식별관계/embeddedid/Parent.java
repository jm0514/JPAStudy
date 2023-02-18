package hellojpa.advancedmapping.복합키와식별관계매핑.비식별관계.embeddedid;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Parent {

	@EmbeddedId
	private ParentId id;

	private String name;
}
