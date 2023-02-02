package hellojpa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

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
@Table(name = "MEMBER", uniqueConstraints = {@UniqueConstraint(
	name = "NAME_AGE_UNIQUE",
	columnNames = {"NAME", "AGE"}
)})
public class Member {
	@Id
	private Long id;

	@Column(name = "name", nullable = false, length = 10)
	private String username;

	private Integer age;

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	private LocalDate testLocalDate;
	private LocalDateTime testLocalDateTime;

	@Lob
	private String description;

	public Member(Long id, String username) {
		this.id = id;
		this.username = username;
	}
}
