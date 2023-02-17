# 1.2 패러다임의 불일치
- 객체와 관계형 데이터베이스는 지향하는 목적이 서로 다르므로 둘의 기능과 표현 방법도 다르다. 이것을 객체와 관계형 데이터베이스의 패러다임 불일치 문제라 한다. 따라서 객체 구조를 테이블 구조에 저장하는 데에는 한계가 있다.
- 이러한 패러다임 불일치 문제를 개발자가 중간에서 해결해야 한다. 문제는 해결 과정에서 너무 많은 시간과 코드를 소비하는 데 있다.

## 1.2.1 상속
- 객체는 상속이라는 기능을 가지고 있지만 테이블은 없다. 그나마 슈퍼타입 서브타입 관계로 객체 상속과 가장 유사한 형태로 테이블 설계 가능.
- 객체 모델 예제
```java
abstract class Item {
    Long id;
    String name;
    int price;
}

class Album extends Item {
    String artist;
}

class Movie extends Item {
    String director;
    String actor;
}

class Book extends Item {
    String author;
    String isbn;
}
```
- 데이터베이스에서는 ITEM 테이블의 DTYPE 컬럼을 사용해 어떤 자식 테이블과 관계가 있는지 정의. 예를 들어 DTYPE이 MOVIE면 영화 테이블과 관계가 있다.
- Album 객체를 저장하려면 이 객체를 분해해 두 SQL을 만들어야 한다. (Movie, Book도 마찬가지)
```sql
INSERT INTO ITEM ...;
INSERT INTO ALBUM ...;
```
- JDBC API를 사용해 완성하려면 부모 객체에서 부모 데이터만 꺼내 ITEM 용 INSERT, 자식 객체에서 자식 객체용 INSERT 를 따로 작성해야 하는데, 양이 매우 많고, 자식 타입에 따라 DTYPE도 저장해야 한다.
- 조회하는 것도 어렵다. 예를 들어 Album 조회한다면 ITEM 과 ALBUM 테이블을 조인해서 조회한 다음 그 결과로 Album 객체를 생성해야 한다.
- 이런 과정이 모두 패러다임 불일치 해결을 위한 소모 비용. 만약 해당 객체를 데이터베이스가 아닌 자바 컬렉션에 보관한다면 다음 같이 해당 컬렉션을 그냥 사용하면 된다.
```java
list.add(album);
list.add(movie);

Album album = list.get(albumId);
```

### JPA와 상속
- JPA는 상속과 관련된 패러다임 불일치 문제를 해결해줌. 자바 컬렉션에 객체 저장하듯이 JPA에게 객체를 저장하면 된다.
- Item 상속한 Album 객체 저장 예제
```java
jpa.persist(album);
```
- JPA는 다음 SQL 실행을 대신 해준다.
```sql
INSERT INTO ITEM ...;
INSERT INTO ALBUM ...;
```
- Album 객체 조회 예제는 다음과 같다.
```java
String albumId = "id100";
Album album = jpa.find(Album.class, albumId);
```
- JPA는 ITEM 과 ALBUM 두 테이블을 조인해서 필요한 데이터를 조회해 그 결과를 반환한다.
```sql
SELECT I.*, A.*
    FROM ITEM I
    JOIN ALBUM A ON I.ITEM_ID = A.ITEM_ID
```

## 1.2.2 연관관계
- 객체는 참조를 사용해서 다른 객체와 연관관계를 가지고 참조에 접근해서 연관된 객체를 조회한다. 반면 테이블은 외래키를 사용해서 다른 테이블과 연관관계를 가지고 조인을 사용해서 연관된 테이블을 조회한다.
- 참조를 사용하는 객체와 외래 키를 사용하는 관계형 데이터베이스 사이의 패러다임 불일치는 객체지향 모델링을 거의 포기하게 만들 정도로 극복하기 어렵다. 
- Member 객체는 Member.team 필드에 Team 객체의 참조를 보관해 Team 객체와 관계를 맺는다. 이 참조 필드로 Member 와 연관된 Team 조회 가능
```text
class Member {
    Team team;
    ...
    Team getTeam() {
        return team;
    }
}

class Team {
    ...
}

member.getTeam(); // member -> team 접근
```
- MEMBER 테이블은 MEMBER.TEAM_ID 외래 키 컬럼으로 TEAM 테이블과 관계를 맺는다. 이 외래키로 MEMBER 테이블과 TEAM 테이블을 조인하면 MEMBER 테이블과 연관된 TEAM 테이블 조회 가능
```sql
SELECT M.*, T.*
    FROM MEMBER M 
    JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID;
```
- 조금 어려운 문제도 존재. 객체는 참조가 있는 방향으로만 조회 가능. member.getTeam()은 가능하지만 team.getMember()는 참조가 없어 불가능. 반면 테이블은 외래 키 하나로 MEMBER JOIN TEAM 도 가능하지만
TEAM JOIN MEMBER도 가능

### 객체를 테이블에 맞추어 모델링
- 테이블에 맞춘 객체 모델
```java
class Member {
    String id;          // MEMBER_ID 컬럼 사용
    Long teamId;        // TEAM_ID FK 컬럼 사용
    String username;    // USERNAME 컬럼 사용
}

class Team {
    Long id;            // TEAM_ID PK 사용
    String name;        // NAME 컬럼 사용
}
```
- MEMBER 테이블 컬럼을 그대로 가져와 Member 클래스를 만들었다. 이렇게 객체를 테이블에 맞춰 모델링시 저장, 조회는 편하다. 하지만 TEAM_ID 외래 키의 값을 그대로 보관하는 teamId 필드에는 문제가 있는데, 관계형
데이터베이스의 조인 기능으로 외래 키를 그대로 보관해도 된다. 하지만 객체는 연관된 객체의 참조를 보관해야 다음처럼 참조를 통해 연관된 객체를 찾을 수 있다.
```java
Team team = member.getTeam();
```
- 특정 회원이 소속된 침을 조회하는 가장 객체지향적인 방법은 이처럼 참조를 사용하는 것.
- Member.teamId 필드처럼 외래 키까지 데이터베이스 방식에 맞추면 Member 객체와 연관된 Team 객체를 참조를 통해 조회 불가. 이런 방식을 따르면 좋은 객체 모델링은 기대하기 어렵고 객체지향의 특징을 잃을 수 있음.

### 객체지향 모델링
- 객체는 참조를 통해 관계를 맺는다.
- 참조를 사용하는 객체 모델
```java
class Member {
    String id;          // MEMBER_ID 컬럼 사용
    Team team;          // 참조로 연관관계를 맺는다.
    String username;    // USERNAME 컬럼 사용
    
    Team getTeam() {
        return team;
    }
}

class Team {
    Long id;            // TEAM_ID PK 사용
    String name;        // NAME 컬럼 사용
}
```
- Member.team 필드를 보면 외래 키의 값을 그대로 보관하는 것이 아니라 연관된 Team의 참조를 보관. 이제 회원과 연관된 팀 조회 가능.
```java
Team team = member.getTeam();
```
- 그런데 이처럼 객체지향 모델링을 사용하면 객체를 테이블에 저장하거나 조회하기가 어렵. 객체 모델은 외래 키 필요 없이 참조만 있으면 되고, 테이블은 참조가 필요 없고 외래 키만 있으면 된다. 결국 개발자가 중간 변환 역할.

#### 저장
- 객체를 데이터베이스에 저장하려면 team 필드를 TEAM_ID 외래 키 값으로 변환해야 한다.
```java
member.getId();             // Member_ID PK에 저장
member.getTeam().getId();   // TEAM_ID PK에 저장
member.getUsername();       // USERNAME 컬럼에 저장
```

#### 조회
- 조회할 때는 TEAM_ID 외래 키 값을 Member 객체의 team 참조로 변환해 객체로 보관해야 한다.
```sql
SELECT M.*, T.*
    FROM MEMBER M
    JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID
```
- SQL의 결과로 객체 생성 후 개발자가 직접 연관관계 설정
```java
public Member find(String memberId) {
    // SQL 실행
    ...
    Member member = new Member();
    ...
    
    // 데이터베이스에서 조회한 회원 관련 정보를 모두 입력
    Team team = new Team();
    ...
    // 데이터베이스에서 조회한 팀 고나련 정보를 모두 입력
    
    // 회원과 팀 관계 설정
    member.setTeam(team);
    return member;
}
```
- 이런 과정들은 모두 패러다임 불일치 해결 위한 소모 비용.

### JPA와 연관관계
- JPA 는 연관관계와 관련된 패러다임의 불일치 문제를 해결해준다.
```java
member.setTeam(team); // 회원과 팀 연관관계 설정
jpa.persist(member);
```
- 개발자는 회원과 팀의 관계를 설정하고 회원 객체를 저장하면 된다. JPA 는 team 의 참조를 외래 키로 변환해서 적절한 INSERT SQL을 데이터베이스에 전달.
- 조회 역시 처리해준다.
```java
Member member = jpa.find(Member.class, memberId);
Team team = member.getTeam();
```
- 위의 문제들은 SQL을 직접 다루어도 열심히 코드만 작성하면 어느 정도 극복 가능. but, 극복이 어려운 패러다임 불일치 문제 또한 존재.

## 1.2.3 객체 그래프 탐색
- 객체에서 회원이 소속된 침을 조회할 떄는 참조를 사용해 연관된 팀을 찾으면 되는데, 이를 객체 그래프 탐색이라 한다.
```java
Team team = member.getTeam();
```
- 객체 연관 관계가 다음과 같이 설계되어 있다고 가정하자.
```text
Member - Team
|
Order  - OrderItem - Item - Category
|
Delivery
```
- 다음은 객체 그래프를 탐색하는 코드
```java
member.getOrder().getOrderItem()... // 자유로운 객체 그래프 탐색
```
- 객체는 마음껏 객체 그래프를 탐색할 수 있어야 한다. 그런데 가능할까?
```sql
SELECT M.*, T.*
    FROM MEMBER M 
    JOIN TEAM T ON M.TEAM_ID = T.TEAM_ID;
```
- 예를 들어 MemberDAO 에서 member 객체를 조회할 때 이런 SQL을 실행해서 회원과 팀에 대한 데이터를 조회했다면 다른 객체 그래프는 데이터가 없어 탐색 불가.
```java
member.getOrder(); // null
```
- SQL을 직접 다루면 처음 실행하는 SQL에 따라 객체 그래프를 어디까지 탐색할 수 있는지 정해진다. 이것은 객체지향 개발자에게는 큰 제약. 비즈니스 로직에 따라 사용하는 객체가 다른데 언제 끊어질지 모를 그래프를 함부로
탐색할 수 없기 때문.
```java
class MemberService {
    ...
    public void process() {
        Member member = memberDAO.find(memberId);
        member.getTeam(); // member -> team 객체 그래프 탐색이 가능한가?
        member.getOrder().getDelivery(); // ??
    }
}
```
- 이 객체와 연관된 Team, Order, Delivery 방향으로 객체 그래프를 탐색할 수 있을지 없을지 예측 불가. 결국 DAO를 열어 SQL을 직접 확인해야 한다. 이는 엔티티가 SQL에 논리적으로 종속되어 발생하는 문제.
- 그렇다고 member와 연관된 모든 객체 그래프를 데이터베이스에서 조회해서 애플리케이션 메모리에 올려두는 것은 현실성이 없다. 결국 MemberDAO에 회원을 조회하는 메소드를 상황에 따라 여러 벌 만들어 사용해야 함.
```java
memberDAO.getMember(); // Member 만 조회
memberDAO.getMemberWithTeam(); // Member 와 Team 조회
memberDAO.getMemberWithOrderWithDelivery(); // Member, Order, Delivery 조회
```
- 객체 그래프를 신뢰하고 사용할 수 있으면 이런 문제를 어느 정도 해소 가능. JPA는 어떻게 해결할까?

### JPA와 객체 그래프 탐색
- JPA를 사용하면 객체 그래프를 마음껏 탐색 가능.
```java
member.getOrder().getOrderItem()... // 자유로운 객체 그래프 탐색
```
- JPA는 연관된 객체를 사용하는 시점에 적절한 SQL 실행. 따라서 JPA를 사용하면 연관된 객체를 신뢰하고 마음껏 조회 가능. 이 기능은 실제 객체를 사용 시점까지 데이터베이스 조회를 미룬다고 해서 지연 로딩이라 한다.
- JPA는 지연 로딩을 투명(transparent)하게 처리. Member 객체를 보면 getOrder() 구현부에 JPA와 관련된 어떤 코드도 포함하지 않는다.
```java
class Member {
    private Order order;

    public Order getOrder() {
        return order;
    }
}
```
- 아래는 지연 로딩을 사용하는 코드로 orfer.getOrderDate() 같이 실제 Order 객체를 사용하는 시점에 JPA는 데이터베이스에서 ORDER 테이블을 조회.
```java
// 처음 조회 시점에 SELECT MEMBER SQL
Member member = jpa.find(Member.class, memberId);

Order order = member.getOrder();
order.getOrderDate(); // Order를 사용하는 시점에 SELECT ORDER SQL
```
- Member를 사용할 때마다 Order를 함께 사용하면, 이렇게 한 테이블씩 조회하는 것보다는 Member 조회 시점에 SQL 조인을 사용해 Member와 Order를 함께 조회하는 것이 효과적.
- JPA는 연관된 객체를 즉시 함께 조회할지 아니면 실제 사용되는 시점에 지연해서 조회할지를 간단히 설정 가능. 만약 Member와 Order를 즉시 함께 조회하겠다고 설정시 JPA는 Member를 조회할 때 다음 SQL 실행해 연관된
Order 함께 조회
```sql
SELECT M.*, O.*
    FROM MEMBER M
    JOIN ORDER O ON M.MEMBER_ID = O.MEMBER_ID
```

## 1.2.4 비교
- 데이터베이스는 기본 키의 값으로 각 로우(row) 구분. 객체는 동일성(identity) 비교와 동등성(equality) 비교라는 두 가지 비교 방법 존재.
  - 동일성 비교는 == 비교. 객체 인스턴스의 주소 값 비교
  - 동등성 비교는 equals() 메소드로 객체 내부 값 비교
- 따라서 테이블의 로우를 구분하는 방법과 객체를 구분하는 방법에는 차이 존재.
- MemberDAO 코드
```java
class MemberDAO {
    public Member getMember(String memberId) {
        String sql = "SELECT * FROM MEMBER WHERE MEMBER_ID = ?";
        ...
        // JDBC API, SQL 실행
        return new Member(...);
    }
}
```
- 조회한 회원 비교하기
```java
String memberId = "100";
Member member1 = memberDAO.getMember(memberId);
Member member2 = memberDAO.getMember(memberId);

member1 == member2; // 다르다
```
- 기보 키 값이 같은 회원 객체를 두 번 조회했는데 동일성(==) 비교시 false 반환. 왜냐하면 member1과 member2는 같은 데이터베이스 로우에서 조회했지만, 객체 측면에서 둘은 다른 인스턴스. (MemberDAO.getMember()
호출시마다 new Member()로 인스턴스가 새로 생성됨.)
- 따라서 데이터베이스의 같은 로우를 조회했지만 객체의 동일성 비교에는 실패. 만약 객체를 컬렉션에 보관했다면 다음과 같이 동일성 비교에 성공했을 것이다.
```java
Member member1 = list.get(0);
Member member2 = list.get(0);

member1 == member2 // 같다.
```
- 이런 패러다임의 불일치 문제를 해결하기 위해 데이터베이스의 같은 로우를 조회할 때마다 같은 인스턴스를 반환하도록 구현하는 것은 쉽지 않다. 여기에 여러 트랜잭션이 동시에 실행되는 과정까지 고려하면 문제는 더 어려워진다.

### JPA와 비교
- JPA는 같은 트랜잭션일 때 같은 객체가 조회되는 것을 보장. 그러므로 다음 코드에서 member1과 member2는 동일성 비교에 성공.
```java
String memberId = "100";
Member member1 = jpa.find(Member.class, memberId);
Member member2 = jpa.find(Member.class, memberId);

member1 == member2; // 같다.
```
- 객체 비교하기는 분산 환경이나 트랜잭션이 다른 상황까지 고려시 더 복잡. 

## 1.2.5 정리
- 객체 모델과 관계형 데이터베이스 모델은 지향하는 패러다임이 서로 다르다. 문제는 이 패러다임의 차이 극복 위한 개발자가 너무 많은 시간, 코드 소비한다는 점.
- 더 어려운 문제는 객체지향 애플리케이션답게 정교한 객체 모델링을 할수록 패러다임의 불일치 문제가 더 커진다는 점. 결국 객체 모델링은 힘을 잃고 점점 데이터 중심의 모델로 변해간다.
- 자바 진영의 오랜 숙제였고, 많은 노력을 기울여왔다. 그리고 그 결과물이 JPA. JPA는 패러다임의 불일치 문제를 해결해주고 정교한 객체 모델링을 유지하게 도와준다.
