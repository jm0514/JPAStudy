package hellojpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR",
	sequenceName = "MEMBER_SEQ",
	initialValue = 1, allocationSize = 50
)
/*@TableGenerator(
	name = "MEMBER_SEQ_GENERATOR",
	table = "MY_SEQUENCES",
	pkColumnValue = "MEMBER_SEQ", allocationSize = 1
)*/
public class Member {
	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
	// @GeneratedValue(strategy = GenerationType.TABLE,
	// 	generator = "MEMBER_SEQ_GENERATOR")
	private Long id;

	@Column(name = "name", nullable = false, length = 10)
	private String username;

}
