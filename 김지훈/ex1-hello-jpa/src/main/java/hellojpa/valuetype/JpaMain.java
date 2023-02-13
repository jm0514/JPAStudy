package hellojpa.valuetype;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class JpaMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {

			/*Member member = new Member();
			member.setUsername("hello");
			member.setHomeAddress(new Address("city", "street", "123456"));
			member.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now()));
			em.persist(member);*/

			// side effect
			// member1.address 의 city만 변경했음에도, 값을 공유해 member2.address의 city도 같이 변경됨.
			/*Address address = new Address("city", "street", "123456");
			Member member1 = new Member("member1", new Period(LocalDateTime.now(), LocalDateTime.now()), address);
			em.persist(member1);

			// side effect 해결
			// 값 타입 복사
			Address address2 = new Address(address.getCity(), address.getStreet(), address.getZipcode());
			Member member2 = new Member("member2", new Period(LocalDateTime.now(), LocalDateTime.now()), address2);
			em.persist(member2);

			member1.getHomeAddress().setCity("newCity");
			em.persist(member1);*/

			Member member = new Member();
			member.setUsername("member1");
			member.setHomeAddress(new Address("homeCity", "street", "123456"));

			member.getFavoriteFoods().add("치킨");
			member.getFavoriteFoods().add("피자");
			member.getFavoriteFoods().add("족발");

			member.getAddressHistory().add(new AddressEntity("old1", "street", "123456"));
			member.getAddressHistory().add(new AddressEntity("old2", "street", "123456"));

			em.persist(member);

			em.flush();
			em.clear();

			System.out.println("=============== START ===============");
			Member findMember = em.find(Member.class, member.getId());

			/*List<Address> addressHistory = findMember.getAddressHistory(); // 지연 로딩
			for (Address address : addressHistory) {
				System.out.println("address = " + address);
			}

			Set<String> favoriteFoods = findMember.getFavoriteFoods();
			for (String favoriteFood : favoriteFoods) {
				System.out.println("favoriteFood = " + favoriteFood);
			}*/

			// homeCity -> newCity
			// findMember.getHomeAddress().setCity("newCity"); // X -> side effect 발생 가능성.
			/*Address old = findMember.getHomeAddress();
			findMember.setHomeAddress(new Address("newCity", old.getStreet(), old.getZipcode()));

			// 치킨 -> 한식
			findMember.getFavoriteFoods().remove("치킨");
			findMember.getFavoriteFoods().add("한식");

			// 이 경우 addressHistory 를 모두 delete 한 후 다시 데이터를 insert 함.
			// findMember.getAddressHistory().remove(new Address("old1", "street", "123456"));
			// findMember.getAddressHistory().add(new Address("newCity1", "street", "123456"));

			// 일대다 관계를 위한 엔티티를 만들고 값 타입 사용
			findMember.getAddressHistory().remove(new AddressEntity("old1", "street", "123456"));
			findMember.getAddressHistory().add(new AddressEntity("newCity1", "street", "123456"));*/

			// jpql
			/*List<Member> resultList = em.createQuery(
				"select m from Member m where m.username like '%mem%'",
				Member.class
			).getResultList();

			for (Member result : resultList) {
				System.out.println("member = " + result.getUsername());
			}*/

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Member> query = cb.createQuery(Member.class);

			Root<Member> m = query.from(Member.class);

			CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "member1"));
			List<Member> resultList = em.createQuery(cq)
										.getResultList();

			for (Member member1 : resultList) {
				System.out.println("member = " + member1.getUsername());
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
