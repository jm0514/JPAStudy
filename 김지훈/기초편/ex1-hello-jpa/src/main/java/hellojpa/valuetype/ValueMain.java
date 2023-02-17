package hellojpa.valuetype;

public class ValueMain {
	public static void main(String[] args) {
		int a = 10;
		int b = 10;

		System.out.println("(a == b) = " + (a == b));

		Address address1 = new Address("city", "street", "12345");
		Address address2 = new Address("city", "street", "12345");

		System.out.println("(address1 == address2) = " + (address1 == address2)); // false
		System.out.println("address1.equals(address2) = " + address1.equals(address2));
		// equals, hashCode 오버라이딩 전에는 false, 후에는 true
	}
}
