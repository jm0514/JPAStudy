package jpabook.jpashop.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
@Getter
public class Address {

	@Column(length = 10)
	private String city;
	@Column(length = 20)
	private String street;
	@Column(length = 5)
	private String zipcode;

	public String fullAddress() {
		return getCity() + " " + getStreet() + " " + getZipcode();
	}

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
