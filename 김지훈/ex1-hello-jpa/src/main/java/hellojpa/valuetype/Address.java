package hellojpa.valuetype;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	private String city;
	private String street;
	private String zipcode;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Address address = (Address)o;

		if (getCity() != null ? !getCity().equals(address.getCity()) : address.getCity() != null)
			return false;
		if (getStreet() != null ? !getStreet().equals(address.getStreet()) : address.getStreet() != null)
			return false;
		return getZipcode() != null ? getZipcode().equals(address.getZipcode()) : address.getZipcode() == null;
	}

	@Override
	public int hashCode() {
		int result = getCity() != null ? getCity().hashCode() : 0;
		result = 31 * result + (getStreet() != null ? getStreet().hashCode() : 0);
		result = 31 * result + (getZipcode() != null ? getZipcode().hashCode() : 0);
		return result;
	}
}
