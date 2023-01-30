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
			Member member = new Member();
			member.setId(1L);
			member.setName("HelloA");
			Member member2 = new Member();
			member2.setId(2L);
			member2.setName("HelloB");
			em.persist(member);
			em.persist(member2);

			// 회원 조회 및 수정
			Member findMember = em.find(Member.class, 1L);
			// System.out.println(findMember);
			findMember.setName("HelloJPA");

			// JPQL
			List<Member> result = em.createQuery("select m from Member m", Member.class)
				.setFirstResult(0)
				.setMaxResults(10)
				.getResultList();
			System.out.println(result);

			// 회원 삭제
			Member findMember2 = em.find(Member.class, 1L);
			em.remove(findMember2);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
