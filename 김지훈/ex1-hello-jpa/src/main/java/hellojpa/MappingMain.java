package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class MappingMain {
	public static void main(String[] args) {

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {

			/*Member member = new Member();
			member.setId(1L);
			member.setUsername("A");
			member.setRoleType(RoleType.USER);
			em.persist(member);

			Member member2 = new Member();
			member2.setId(2L);
			member2.setUsername("B");
			member2.setRoleType(RoleType.ADMIN);
			em.persist(member2);*/

			// 	@Enumerated(EnumType.STRING) String 으로 써야 하는 이유.
			// Ordinal일 경우 새로 앞에 추가된 GUEST와 USER가 같은 0 값을 갖게 된다.
			// 그리고 전체적인 순서가 뒤죽박죽되어 데이터가 엉킬 수 있음.
			Member member3 = new Member();
			member3.setId(3L);
			member3.setUsername("C");
			member3.setRoleType(RoleType.GUEST);
			em.persist(member3);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
