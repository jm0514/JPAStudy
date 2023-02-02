# JPA를 사용할 Main Class

```

        package hellojpa;
        import java.util.List;
        
        import javax.persistence.EntityManager;
        import javax.persistence.EntityManagerFactory;
        import javax.persistence.EntityTransaction;
        import javax.persistence.Persistence;
    
        public class JpaMain {
        public static void main(String[] args) {

        //1.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //2.
        EntityManager em = emf.createEntityManager();
        //3
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            tx.commit(); 

        } catch (Exception e) {
            tx.rollback(); 
        } finally {
            em.close();
        }
        //4
        emf.close();
}
```

1. EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");를 하면 커넥션과 관련한 동작들이 로그로 올라옴. db와 연결 및 필요한 준비가 완료됨

2. EntityManager em = emf.createEntityManager(); 실제로 쿼리가 동작하려면 EntityManager를 호출하여 동작을 하는데
3. EntityTransaction tx = em.getTransaction(); 데이터 처리 과정에서의 예외발생을 막기위해 트랜젝션 내에서 모든 삭입 삭제와 같은 데이터 조작이 이뤄짐.
4. 그런 인련의 과정이 있고 난 후에는 팩토리를 닫아준다.

# @Entity
### Member entity 작성
```

    import javax.persistence.Entity;
    import javax.persistence.Id;

    @Entity
    @Table(name="USER")
    public class Member {
        @Id
        private Long id;
        
        @Column(name = "user_name")
        private String name;
        //Getter, Setter …
    }
```

#### @Entity
    JPA에게 해당 클래스와 테이블을 매핑한다는걸 알려주는 어노테이션
#### @Table
    Table 매핑이 없으면 해당 객체의 이름으로 테이블이 작성되지만 매핑명이 있으면 매핑된 이름으로 테이블이 작성됨    

#### @Id
    pk값을 매핑함.
#### @Column
    객체내에는 name으로 돼있지만 테이블 설계가 다르다면 마찬가지로 name="user_name"으로 매핑해주면 "user_name"으로 컬럼이 작성됨.

# 삽입
```

        Member member = new Member();
        member.setId(1L);
        member.setName("Hello"");
        em.persist(member);
```
* em.persist()를 통해 member객체에 등록된 어노테이션을 따라서 테이블을 자동으로 만들고, SQL문을 만들어 등록됨.

# 수정
```java
Member findMember = em.find(Member.class, 1L);//뒤에는 PK넣어준거임, find로 select 하는거임
findMember.setName("HelloJPA");
```
* em.persist 안해도 자동으로 update쿼리 날아감.
* em으로 감싸서 찾아온 객체라서(em.find()) jpa가 변경감지를 해서 자동으로 update 해주는거임.

# 삭제
```java
em.remove(findMember);
```
* remove에 찾은애 넣어주면 삭제쿼리 나감.

# JPQL
```java
    
    //jpql로 리스트 가져오는법
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8) oracleDB로 따지면 5번 RowNum부터 8번 RowNum까지 가져오라는거임
                    //persistence.xml에서 dialect를 오라클로 바꿔주면 오라클로 나가는거 볼 수 있음.
                    .getResultList();
            for(Member member : result){
                System.out.println("member.name = "+member.getName());
            }
```
* 객체지향식 SQL이라 볼 수 있음
  *     select m from Member as m -> Member객체를 전부 다 가져오라는 뜻임. 쿼리문도 객체 중심으로 짜여짐
  * SQL을 추상화 -> 특정 DB의 방언에 얽매이지 않음.
  * 추후 상세히 다룸


    
    




