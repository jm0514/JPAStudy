package hellojpa.advancedmapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		try {
			Movie movie = new Movie();
			movie.setDirector("aaaa");
			movie.setActor("bbbb");
			movie.setName("바람과함께사라지다");
			movie.setPrice(10000);

			em.persist(movie);

			em.flush();
			em.clear();

			// Movie findMovie = em.find(Movie.class, movie.getId());
			// System.out.println(findMovie);

			// 부모 타입으로 데이터 조회 시 union all로 item 테이블을 모두 탐색
			// 복잡한 쿼리 발생, 비효율
			Item item = em.find(Item.class, movie.getId());
			System.out.println("item = " + item);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
