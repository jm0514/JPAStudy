package jpql;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

	@Id
	@GeneratedValue
	private Long id;
	private String username;
	private int age;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	@Enumerated(EnumType.STRING)
	private MemberType type;

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}

	@Override
	public String toString() {
		return "Member{" +
			"id=" + id +
			", username='" + username + '\'' +
			", age=" + age +
			'}';
	}
}
