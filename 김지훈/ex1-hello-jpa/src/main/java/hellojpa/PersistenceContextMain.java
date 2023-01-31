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

			/*Member findMember1 = em.find(Member.class, 101L); // 데이터베이스에 조회 -> 1차 캐시에 저장
			Member findMember2 = em.find(Member.class, 101L); // 1차 캐시에서 조회

			System.out.println(findMember1 == findMember2); // true, 영속 엔티티의 동일성 보장.*/

			// 쓰기 지연
			/*Member member1 = new Member(150L, "A");
			Member member2 = new Member(160L, "B");
			em.persist(member1);
			em.persist(member2);
			// 여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.
			System.out.println("==========================");
			// 커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.*/

			// 변경 감지
			Member member = em.find(Member.class, 150L);
			member.setName("ZZZZZ");
			System.out.println("==========================");

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
