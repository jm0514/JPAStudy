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
			for (int i = 0; i < 100; i++) {
				Member member = new Member();
				member.setUsername("member" + i);
				member.setAge(i);
				em.persist(member);
			}

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
			/*Member singleResult = em.createQuery("select m from Member m where m.username = :username",
				Member.class)
				.setParameter("username", "member1")
				.getSingleResult();
			System.out.println("singleResult = " + singleResult.getUsername());*/

			em.flush();
			em.clear();

			/*List<Member> result = em.createQuery("select m from Member m", Member.class)
				.getResultList(); // 엔티티 프로젝션

			Member findMember = result.get(0);
			findMember.setAge(20); // 영속성 컨텍스트에 의해 관리 됨.*/
			/*List<Team> resultList = em.createQuery("select m.team from Member m", Team.class)
				.getResultList();*/ // join 발생 아래와 같이 변경해 주는 것이 명시적으로 좋다.
			/*List<Team> resultList = em.createQuery("select t from Member m join m.team t", Team.class)
				.getResultList();*/

			/*em.createQuery("select o.address from Order o", Address.class)
				.getResultList();*/ // 임베디드 타입 프로젝션
			/*List resultList = em.createQuery("select m.username, m.age from Member m")
				.getResultList();

			Object o = resultList.get(0);
			Object[] result = (Object[]) o;*/
			/*List<Object[]> resultList = em.createQuery("select m.username, m.age from Member m")
				.getResultList();

			Object[] result = resultList.get(0);
			System.out.println("username = " + result[0]);
			System.out.println("age = " + result[1]);*/

			/*List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m",
					MemberDTO.class)
				.getResultList();
			MemberDTO memberDTO = result.get(0);
			System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
			System.out.println("memberDTO.getAge() = " + memberDTO.getAge());*/

			List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
				.setFirstResult(1)
				.setMaxResults(10)
				.getResultList();

			System.out.println("result.size()" + result.size());
			for (Member member : result) {
				System.out.println(member);
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
