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
			/*Member member = new Member();
			member.setUsername("member1");
			member.setAge(10);
			em.persist(member);*/


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

			/*List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
				.setFirstResult(1)
				.setMaxResults(10)
				.getResultList();

			System.out.println("result.size()" + result.size());
			for (Member member : result) {
				System.out.println(member);
			}*/

			Team team = new Team();
			team.setName("teamA");
			em.persist(team);

			Member member = new Member();
			// member.setUsername(null);
			member.setUsername("관리자");
			member.setAge(10);
			member.setType(MemberType.ADMIN);
			member.changeTeam(team);

			em.persist(member);

			em.flush();
			em.clear();

			// 조인
			// String query = "select m from Member m inner join m.team t"; // inner 조인
			// String query = "select m from Member m left join m.team t"; // left 조인
			// String query = "select m from Member m, Team t where m.username = t.name"; // 세타조인
			// String query = "select m from Member m left join m.team t on t.name = 'teamA'"; // 조인 대상 필터링
			/*String query = "select m from Member m left join Team t on m.username = t.name"; // 연관관계 없는 엔티티 외부 조인
			List<Member> result = em.createQuery(query, Member.class)
				.getResultList();*/

			// JPQL 타입 표현과 기타식
			/*String query = "select m.username, 'HELLO', TRUE from Member m"
				+ "    where m.type = :userType";
			List<Object[]> result = em.createQuery(query)
							.setParameter("userType", MemberType.ADMIN)
							.getResultList();
			for (Object[] objects : result) {
				System.out.println("objects[0] = " + objects[0]);
				System.out.println("objects[1] = " + objects[1]);
				System.out.println("objects[2] = " + objects[2]);
			}*/

			// 기본 CASE 식
			/*String query =
						"select "
							+ "case when m.age <= 10 then '학생요금'"
							+ " 	when m.age >= 60 then '경로요금'"
							+ "		else '일반요금' end "
						+ "from Member m";*/

			// COALESCE
			/*String query =
						"select coalesce(m.username, '이름 없는 회원') as username from Member m";*/
			String query =
						"select nullif(m.username, '관리자') from Member m";

			List<String> result = em.createQuery(query, String.class)
				.getResultList();

			for (String s : result) {
				System.out.println("s = " + s);
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
