package hellojpa.advancedmapping.복합키와식별관계매핑.onetoone식별관계;

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
			Board board = new Board();
			board.setTitle("제목");
			em.persist(board);

			BoardDetail boardDetail = new BoardDetail();
			boardDetail.setContent("내용");
			boardDetail.setBoard(board);
			em.persist(boardDetail);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
