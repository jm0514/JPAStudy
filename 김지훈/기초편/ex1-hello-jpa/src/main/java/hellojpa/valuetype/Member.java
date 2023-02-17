package hellojpa.valuetype;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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

	@Embedded
	private Address homeAddress;

	@ElementCollection
	@CollectionTable(name = "FAVORITE_FOOD", joinColumns =
		@JoinColumn(name = "MEMBER_ID")
	)
	@Column(name = "FOOD_NAME")
	private Set<String> favoriteFoods = new HashSet<>();

	// @ElementCollection
	// @CollectionTable(name = "ADDRESS", joinColumns =
	// 	@JoinColumn(name = "MEMBER_ID")
	// )
	// private List<Address> addressHistory = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "MEMBER_ID")
	private List<AddressEntity> addressHistory = new ArrayList<>();
}
