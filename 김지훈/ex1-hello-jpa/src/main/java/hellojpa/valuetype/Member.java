package hellojpa.valuetype;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(name = "USERNAME")
	private String username;

	// 기간 Period
	@Embedded
	private Period workPeriod;

	// 주소 Address
	@Embedded
	private Address homeAddress;

	public Member(String username, Period workPeriod, Address homeAddress) {
		this.username = username;
		this.workPeriod = workPeriod;
		this.homeAddress = homeAddress;
	}
}
