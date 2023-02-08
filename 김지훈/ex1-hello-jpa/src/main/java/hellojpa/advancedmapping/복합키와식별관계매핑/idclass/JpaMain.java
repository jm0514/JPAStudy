package hellojpa.advancedmapping.복합키와식별관계매핑.idclass;

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

			// 저장
			Parent parent = new Parent();
			parent.setId1("id1");
			parent.setId2("id2");
			parent.setName("parentName");
			em.persist(parent);

			em.flush();
			em.clear();

			// 조회
			ParentId parentId = new ParentId("id1", "id2");
			Parent findParent = em.find(Parent.class, parentId);
			System.out.println(findParent);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
