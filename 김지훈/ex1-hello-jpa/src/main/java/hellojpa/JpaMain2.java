package hellojpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain2 {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			// 일대다 단방향 매핑의 단점
			/*Member member1 = new Member("member1");
			Member member2 = new Member("member2");

			Team team1 = new Team("team1");
			team1.getMembers().add(member1);
			team1.getMembers().add(member2);

			em.persist(member1);
			em.persist(member2);
			em.persist(team1);
			// team을 persist시 member 업데이트 쿼리가 나옴.*/

			// 다대다 단방향
			/*Product productA = new Product();
			productA.setName("상품A");
			em.persist(productA);

			Member member1 = new Member();
			member1.setUsername("회원1");
			member1.getProducts().add(productA);
			em.persist(member1);
			em.flush();
			em.clear();

			Member member = em.find(Member.class, 2L);
			List<Product> products = member.getProducts();
			for (Product product : products) {
				System.out.println("product.name=" + product.getName());
			}*/

			// 다대다 양방향
			Product productA = new Product();
			productA.setName("상품A");
			em.persist(productA);

			Member member1 = new Member();
			member1.setUsername("회원1");
			member1.addProduct(productA);
			em.persist(member1);

			em.flush();
			em.clear();

			Product product = em.find(Product.class, 1L);
			List<Member> members = product.getMembers();
			for (Member member : members) {
				System.out.println("member = " + member.getUsername());
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
