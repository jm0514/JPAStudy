package hellojpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
	@Id
	@GeneratedValue
	private Long id;

	private String name;

	@ManyToMany(mappedBy = "products") // 역방향 추가
	private List<Member> members = new ArrayList<>();

}
