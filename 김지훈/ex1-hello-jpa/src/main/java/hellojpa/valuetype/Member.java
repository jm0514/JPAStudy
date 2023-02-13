package hellojpa.valuetype;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "city",
			column = @Column(name = "WORK_CITY")),
		@AttributeOverride(name = "street",
			column = @Column(name = "WORK_STREET")),
		@AttributeOverride(name = "zipcode",
			column = @Column(name = "WORK_ZIPCODE"))
	})
	private Address workAddress;
}
