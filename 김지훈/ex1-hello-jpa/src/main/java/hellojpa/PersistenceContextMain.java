package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class PersistenceContextMain {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			// 엔티티를 생성한 상태 (비영속)
			/*Member member = new Member();
			member.setId(101L);
			member.setName("HelloJPA");

			// 엔티티를 영속 (DB에 저장되지 않는다. 쿼리가 여기서 나오지 않는다.)
			System.out.println("=== BEFORE ===");
			em.persist(member);
			System.out.println("=== AFTER ===");

			Member findMember = em.find(Member.class, 101L); // 1차 캐시에서 조회.
			System.out.println(findMember);*/

			Member findMember1 = em.find(Member.class, 101L); // 데이터베이스에 조회 -> 1차 캐시에 저장
			Member findMember2 = em.find(Member.class, 101L); // 1차 캐시에서 조회

			System.out.println(findMember1 == findMember2); // true, 영속 엔티티의 동일성 보장.

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
