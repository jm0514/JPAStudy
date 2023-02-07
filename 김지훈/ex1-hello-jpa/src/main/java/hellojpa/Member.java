package hellojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

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
public class Member {
	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;

	@Column(name = "USERNAME")
	private String username;

	@OneToOne(mappedBy = "member")
	private Locker locker;

	@ManyToMany
	@JoinTable(name = "MEMBER_PRODUCT",
		joinColumns = @JoinColumn(name = "MEMBER_ID"),
		inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID"))
	private List<Product> products = new ArrayList<>();

	public void addProduct(Product product) {
		products.add(product);
		product.getMembers().add(this);
	}

	public Member(String username) {
		this.username = username;
	}
}
