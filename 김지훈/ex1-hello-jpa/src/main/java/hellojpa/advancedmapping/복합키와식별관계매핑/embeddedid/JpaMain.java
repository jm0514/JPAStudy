package hellojpa.advancedmapping.복합키와식별관계매핑.embeddedid;

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
			ParentId parentId = new ParentId("myId1", "myId2");
			parent.setId(parentId);
			parent.setName("parentName");
			em.persist(parent);

			em.flush();
			em.clear();

			// 조회
			ParentId findParentId = new ParentId("myId1", "myId2");
			Parent findParent = em.find(Parent.class, findParentId);
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
