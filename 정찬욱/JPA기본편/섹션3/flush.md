# Flush
* 영속성 컨택스의 변경 내용을 DB에 반영함 ->  DB트랜잭션이 커밋되면 Flush가 자동으로 발생함
  * 변경을 감지함.
  * 수정된 Entity를 쓰기지연 SQl저장소에 등록함
  * 쓰기지연 SQl 저장소의 쿼리를 DB에 전송함.

###  영속성 컨택스트를 플러쉬 하는법
* em.flush()// 직접 호출하는 방법 
* 트랜잭션 커밋 -> 플러쉬를 자동으로 호출
* JPQL 쿼리 실행 => 자동으로 플러쉬 호출


/flush
```java
Member member1 = new Member(200L, "asd");
em.persist(member1);

em.flush(); -> //이 시점에 ㅋㅓ밋전에 변경내용을 감지해서 db에 반영함. 이렇게 직접하는경우는 거의 없고 테스트 할 때 씀. 보통은 그냥 em.persist만 써도 알아서 변경감지 해주니 걱정ㄴ
//flush가 호출되면 이 시점에서 쓰기지연 SQL저장소에서 저장된 SQL들이 바로 DB로 쿼리문을 날림.        
-------------
tx.commit()
```
* DB에 반영은 되지만 1차캐시가 지워지는건 아님.


*  중간에 JPQL 실행
```java    
em.persist(memberA);
em.persist(memberB);
em.persist(memberC);

query = em.createQuery("select m from Memberm", Member.class);
List<Member> members = query.getResultList();
```
  * 이것 처럼 DB에 저장도 안된걸 찾아오라는 JPQL이 날아오게 되면 사고가 날 수 있기 때문에 JPA에서는 자동으로 flush를 날려 DB에 저장 후 select쿼리로 찾아옴.