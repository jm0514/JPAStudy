package jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class JpaMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Member member = new Member();
			member.setUsername("member1");
			member.setAge(10);
			em.persist(member);

			// TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);

			// TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
			// Query query3 = em.createQuery("select m.username, m.age from Member m"); // 타입 정보를 받을 수 없을 때
			// List<Member> resultList = query.getResultList();
			// Member singleResult = query.getSingleResult();
			// System.out.println("singleResult = " + singleResult);

			/*TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username",
				Member.class);
			query.setParameter("username", "member1");
			Member singleResult = query.getSingleResult();*/
			Member singleResult = em.createQuery("select m from Member m where m.username = :username",
				Member.class)
				.setParameter("username", "member1")
				.getSingleResult();
			System.out.println("singleResult = " + singleResult.getUsername());

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
