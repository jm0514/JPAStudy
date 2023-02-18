package jpql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class BulkMain {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Team teamA = new Team();
			teamA.setName("팀A");
			em.persist(teamA);

			Team teamB = new Team();
			teamB.setName("팀B");
			em.persist(teamB);

			Member member1 = new Member();
			member1.setUsername("회원1");
			member1.setTeam(teamA);
			member1.setAge(0);
			em.persist(member1);

			Member member2 = new Member();
			member2.setUsername("회원2");
			member2.setTeam(teamA);
			member2.setAge(0);
			em.persist(member2);

			Member member3 = new Member();
			member3.setUsername("회원3");
			member3.setTeam(teamB);
			member3.setAge(0);
			em.persist(member3);

			/*em.flush();
			em.clear();*/

			// FLUSH 자동 호출...
			int resultCount = em.createQuery("update Member m set m.age = 20")
				.executeUpdate();
			System.out.println("resultCount = " + resultCount);

			// 주의 ! DB에는 업데이트가 되었지만, 영속성 컨텍스트 내에서는 바뀌지 않았음
			// 벌크 연산 수행 후 영속성 컨텍스트 초기화 필수
			em.clear();
			Member memberOne = em.find(Member.class, member1.getId());
			Member memberTwo = em.find(Member.class, member2.getId());
			Member memberThree = em.find(Member.class, member3.getId());

			System.out.println("memberOne.getAge() = " + memberOne.getAge());
			System.out.println("memberTwo.getAge() = " + memberTwo.getAge());
			System.out.println("memberThree.getAge() = " + memberThree.getAge());

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
