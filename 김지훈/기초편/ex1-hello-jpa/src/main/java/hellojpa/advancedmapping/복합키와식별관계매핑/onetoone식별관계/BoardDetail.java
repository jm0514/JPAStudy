package hellojpa.advancedmapping.복합키와식별관계매핑.onetoone식별관계;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BoardDetail {
	@Id
	private Long id;

	@MapsId // BoardDetail.boardId 메핑
	@OneToOne
	@JoinColumn(name = "BOARD_ID")
	private Board board;

	private String content;
}
