package hellojpa.valuetype;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ADDRESS")
public class AddressEntity {
	@Id
	@GeneratedValue
	private Long id;

	private Address address;

	public AddressEntity(Address address) {
		this.address = address;
	}

	public AddressEntity(String city, String street, String zipcode) {
		this.address = new Address(city, street, zipcode);
	}
}
