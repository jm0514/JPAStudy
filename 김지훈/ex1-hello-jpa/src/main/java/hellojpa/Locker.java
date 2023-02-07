package hellojpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Locker {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@OneToOne
	@JoinColumn(name = "MEMBER_ID")
	private Member member;
}
