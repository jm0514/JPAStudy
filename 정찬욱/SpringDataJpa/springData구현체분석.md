## Spring Data Jpa 구현체 분석

* @Repository 적용 : Jpa 예외를 스프링이 추상화 한 예외로 변환
* @Transactional : 트랜잭션 적용
  * Jpa의 모든 변경은 트랜잭션 안에서 동작
  * 스프링 데이터 Jpa는 변경 메서드를 트랜잭션 처리
  * 서비스 계층에서 트랜잭션을 시작하지 않으면 레파지토리에서 트랜잭션 시작
  * 서비스 계층에서 트래잭션을 시작하면 레파지토리는 해당 트랜잭션을 전파 받아서 사요ㅕㅇ
  * 그래서 스프링 데이터 jpa를 사용할 때 트랜잭션ㅇ이 없어도 데이터 등록, 변경이 가능함( 사실은 트랜잭션이 레파지토리 계층에 깔려있는거임)



* @Transactional(readOnly = true)
  *  데이터를 단순히 조회만 하고 변경하지 않는 트랜잭션에서 readOnly = true 옵션을 사용하면 플러시를 생략해서 약간의 성능 향상 가능
  * 책 15.4.2 읽기 전용 쿼리의 성능 최적화 참고


* save()메서드
  * 새로운 엔티티면 저장(persist)
  * 새로운 엔티티가 아니면 병합(merge) -> 영속성을 잃은 객체가 다시 영속성 컨택스트의 관리를 받아야 할 때 사용함
    * 업데이트의 목적이 아님.



## 새로운 엔티티를 판단하는 방법
* 객체는 flush 하기 전까지는 사실 null값임 -> null이면 새로운 값이라고 생각함.
* 근데 만약에 @GeneratedValue를 안쓰면 -> pk에 값이 이미 있기때문에 persist가 안먹힘. -> merge로 가버림
  * merge는 db에 값이 있을거라 가정하고 사용함. db에서 그래서 값을 가져옴. 없으면 새거라고 인식하고 그제서야 넣음.
  * merge는 쓸 일이 별로 없음. 그냥 쓸 일이 없다고 가정해야 함.
  
*persistable구현
```java
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {
  @Id
  private String id;
  @CreatedDate
  private LocalDateTime createdDate;
  public Item(String id) {
    this.id = id;
  }
  @Override
  public String getId() {
    return id;
  }
  @Override
  public boolean isNew() {
    return createdDate == null;
  }
}

```