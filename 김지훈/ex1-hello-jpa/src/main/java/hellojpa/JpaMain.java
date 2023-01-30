package hellojpa;

import java.util.List;

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
			// 회원 등록
			/*Member member = new Member();
			member.setId(1L);
			member.setName("HelloA");
			member.setId(2L);
			member.setName("HelloB");
			em.persist(member);*/

			// 회원 조회 및 수정
			/*Member findMember = em.find(Member.class, 1L);
			// System.out.println(findMember);
			findMember.setName("HelloJPA");*/

			// 회원 삭제
			/*Member findMember = em.find(Member.class, 1L);
			em.remove(findMember);*/

			// JPQL
			List<Member> result = em.createQuery("select m from Member m", Member.class)
				.setFirstResult(0)
				.setMaxResults(10)
				.getResultList();
			System.out.println(result);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
