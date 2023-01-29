# 1.1 SQL을 직접 다룰 때 발생하는 문제점
- 자바로 개발하는 애플리케이션은 대부분 신뢰할 만한 관계형 데이터베이스를 데이터 저장소로 사용.
- 따라서 SQL을 사용해야 하는데, 잡바로 작선한 애플리케이션은 JDBC API로 SQL을 데이터베이스에 전달.

## 1.1.1 반복, 반복 그리고 반복
- SQL을 직접 다룰 때 문제점을 예제로 알아보자. (회원 테이블은 존재한다고 가정.)
- Member 객체
```java
public class Member {
    private String memberId;
    private String name;
    ...
}
```
- 회원용 DAO
```java
public class MmberDAO {
    public Member find(String memberId) {
        ...
    }
}
```
- find() 메소드로 회원 조회 기능 개발은 다음 순서로 진행된다.
1. 회원 조회용 SQL 작성
```sql
SELECT MEMBER_ID, NAME FROM MEMBER M WHERE MEMBER_ID = ?
```
2. JDBC API를 사용해서 SQL을 실행
```java
ResultSet rs = stmt.executeQuery(sql);
```
3. 조회 결과를 Member 객체로 매핑
```java
String memberId = rs.getString("MEMBER_ID");
String name = rs.getString("NAME");

Member member = new Member();
member.setMemberId(memberId);
member.setName(name);
```
- 회원 등록 기능을 추가해보자.
```java
public class MemberDAO {
    public Member find(String memberId) {...}
    public void save(Member member) {...} // 추가
}
```
1. 회원 등록용 SQL 작성
```java
String sql = "INSERT INTO MEMBER(MEMBER_ID, NAME) VALUES (?,?)";
```
2. 회원 객체의 값을 꺼내서 등록 SQL에 전달
```java
pstmt.setString(1, member.getMemberId());
pstmt.setString(2, member.getName());
```
3. JDBC API를 사용해서 SQL 실행
```java
pstmt.executeUpdate(sql);
```
- 회원을 조회하는 기능과 등록하는 기능을 만들었다. 다음으로 회원을 수정하고 삭제하는 기능도 추가하면 된다. 위와 같은 SQL을 작성하고 JDBC API를 사용하는 비슷한 일을 반복하게 될 것이다.
- 회원 객체를 데이터베이스가 아닌 자바 컬렉션에 보관하면 다음 한 줄로 객체를 저장할 수 있을 것이다.
```java
list.add(member);
```
- 하지만 데이터베이스는 객체 구조와는 다른 데이터 중심의 구조로 객체를 데이터베이스에 직접 저장/조회 불가. 따라서 개발자가 중간에서 SQL, JDBC API를 사용해 변환 작업을 직접 해야 함.
- 문제는 CRUD를 위해 너무 많은 SQL, JDBC API를 작성해야 한다는 점이다.

## 1.1.2 SQL에 의존적인 개발
- 앞에서 회원 객체 관리를 완성했는데, 회원의 연락처를 함께 저장하라는 요구사항이 추가되었다.

### 등록 코드 변경
- 회원 클래스에 연락처 필드 추가
```java
public class Member {
    private String memberId;
    private String name;
    private String tel; // 추가
    ...
}
```
- INSERT 문도 수정
```java
String sql = "INSERT INTO MEMBER (MEMBER_ID, NAME, TEL) VALUES (?, ?, ?)";
```
- 회원 객체의 연락처 값을 꺼내 등록 SQL에 전달
```java
pstmt.setString(3, membergetTel());
```

### 조회 코드 변경
- 회원 조회용 SQL 수정
```sql
SELECT MEMBER_ID, NAME, TEL FROM MEMBER WHERE MEMBER_ID = ?
```
- 조회 결과 추가로 매핑
```java
String tel = rs.getString("TEL");
member.setTel(tel);
```

### 수정 코드 변경
- 수정이 되지 않는 버그가 발생했다. 자바 코드를 보니 MemberDAO.update(member) 메소드에 수정할 회원 정보와 연락처를 잘 전달했따. MemberDAO를 열어보니 UPDATE SQL에 TEL 컬럼을 추가하지 않았다.
- UPDATE SQL과 memberDAO.update()의 일부 코드를 변경해서 연락처가 수정되도록 했다.


- 만약 회원 객체를 데이터베이스가 아닌 자바 컬렉션에 보관했다면 필드를 추가한다고 해서 이렇게 많은 코드를 수정할 필요는 없을 것이다.
```java
list.add(member); // 등록
Member member = list.get(xxx); // 조회
member.setTel("xxx") // 수정
```

### 연관된 객체
- 회원은 어떤 한 팀에 필수로 소속되어야 한다는 요구사항이 추가되었고 다음과 같이 Member 객체에 team 필드가 추가되었다.
```java
class Member {
    private String memberId;
    private String name;
    private String tel;
    private Team team; // 추가
    ...
}

// 추가된 팀
class Team {
    ...
    private String teamName;
    ...
}
```
- 다음 코드로 화면에 팀의 이름을 출력할 수 있다.
```java
member.getName(); // 이름
member.getTeam().getTeamName(); // 소속 팀
```
- 코드를 실행해보니 member.getTeam() 값이 항상 null이다. 데이터베이스에는 모든 회원이 팀에 소속되었는데, MemberDAO에 findWithTeam()이라는 메소드가 추가된 것을 확인했다.
```java
public class MemberDAO {
    public Member find(String memberId) {...}
    public Member findWithTeam(String memberId) {...}
}
```
- MemberDAO 코드를 열어서 확인하니 회원 출력시 사용하는 find() 메소드는 회원만 조회하는 다음 SQL 을 그대로 유지했다.
```sql
SELECT MEMBER_ID, NAME, TEL FROM MEMBER M 
```
- 또한 findWithTeam() 메소드는 다음 SQL로 회원과 연관된 팀을 함께 조회했다.
```sql
SELECT M.MEMBER_ID, M.NAME, M.TEL, T.TEAM_ID, T.TEAM_NAME
FROM MEMBER M 
JOIN TEAM T
    ON M.TEAM_ID = T.TEAM_ID
```
- 결국 DAO를 열어 SQL을 확인하고 나서야 원인을 알 수 있었고, 회원 조회 코드를 MemberDAO.find()에서 MemberDAO.findWithTeam()으로 변경해서 문제를 해결했다.


- 위의 Member, Team 처럼 비즈니스 요구사항을 모델링한 객체가 엔티티. 지금처럼 SQL에 모든 것을 의존하는 상황에서 엔티티를 신뢰하고 사용할 수 없다.
- 대신 DAO를 열어 어떤 SQL이 실행되고 어떤 객체들이 함께 조회되는지 일일이 확인 필요. 이것은 진정한 의미의 계층 분할이 아니다. 물리적으로 SQL과 JDBC API를 데이터 접근 계층에 숨기는 데에는 성공이지만, 
논리적으로는 엔티티와 아주 강한 의존관계를 가지고 있다. 
- 이런 강한 의존관계 때문에 회원 조회, 필드 추가 시 DAO의 CRUD, SQL 대부분을 변경해야 하는 문제 발생.
- 애플리케이션에서 SQL을 직접 다룰 때 발생하는 문제점을 정리하면
    - 진정한 의미의 계층 분할이 어렵다.
    - 엔티티를 신뢰할 수 없다.
    - SQL에 의존적인 개발을 피하기 어렵다.

## 1.1.3 JPA와 문제 해결
- JPA를 사용하면 객체를 데이터베이스에 저장하고 관리할 때, 개발자가 직접 SQL을 작성하는 것이 아니라, JPA가 제공하는 API를 사용하면 된다. 그러면 JPA가 대신 SQL을 생성해 데이터베이스에 전달한다.
- JPA가 제공하는 CRUD API를 간단히 알아보자.
### 저장 기능
```java
jpa.persist(member);
```
- persist()는 객체를 데이터베이스에 저장한다. JPA 객체와 매핑정보를 보고 적절한 INSERT SQL 생성 후 데이터베이스에 전달.

### 조회 기능
```java
String memberId = "helloId"l;
Member member = jpa.find(Member.class, memberId);
```
- find()는 객체 하나를 데이터베이스에서 조회. 적절한 SELECT SQL 생성 후 데이터베이스에 전달하고 그 결과로 Member 객체 생성 후 반환.

### 수정 기능
```java
Member member = jpa.find(Member.class, memberId);
member.setName("이름변경");
```
- 별도의 수정 메소드는 사용하지 않고 객페를 조회햇 값을 변경하면 트랜잭션 커밋시 데이터베이스에 적절한 UPDATE SQL이 전달된다.

### 연관된 객체 조회
```java
Memeber member = jpa.find(Member.class, memberId);
Team team = member.getTeam();
```
- 연관된 객체를 사용하는 시점에 적절한 SELECT SQL 실행.


- 이와 같이 JPA는 SQL을 개발자 대신 작성해 실행해주는 것 이상의 기능들을 제공.
