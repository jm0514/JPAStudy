package hellojpa.advancedmapping.복합키와식별관계매핑.onetoone식별관계;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {
	@Id
	@GeneratedValue
	@Column(name = "BOARD_ID")
	private Long id;

	private String title;

	@OneToOne(mappedBy = "board")
	private BoardDetail boardDetail;
}
