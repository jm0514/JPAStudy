package hellojpa.valuetype;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
			Address address = new Address("city", "street", "123456");
			Member member1 = new Member("member1", new Period(LocalDateTime.now(), LocalDateTime.now()), address);
			em.persist(member1);

			Member member2 = new Member("member2", new Period(LocalDateTime.now(), LocalDateTime.now()), address);
			em.persist(member2);

			member1.getHomeAddress().setCity("newCity");
			em.persist(member1);

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
