package hellojpa.valuetype;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(name = "USERNAME")
	private String username;

	// 기간 Period
	private LocalDateTime startDate;
	private LocalDateTime endDate;

	// 주소 Address
	private String city;
	private String street;
	private String zipcode;
}
