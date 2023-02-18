# 6. named 쿼리
* 미리  쿼리를 저장해두고 이름으로 쿼리를 불러오는 방법
* 진짜 편하고 좋음 
* 정적쿼리
* 어노테이션, XML에 저장됨
* 애플리케이션 로딩 시점에 초기화 후 재사용 가능 -> sql로 한번 번역한 뒤에는 캐싱해서 계속 재사용하는거임
* 애플리캐이션 로딩 시점에 쿼리를 검증 -> 컴파일 시점에 에러를 볼 수 있어서 굉장히 좋음

```java
@Entity
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username =:username"
)

public class Member{
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private int age;

    @ManyToOne
    @JoinColumn("team_id")
    private Team team;

    //getter setter 생성자
}
```
```java
public class Main {
    public static void main(String[] args) {
        Member member = new Member();
        member.setUsername("member1");
        member.setAge(10);
        em.persist(member);

        em.flush();
        em.cleaer();

        em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", "회원1")
                .getResultList();


        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
}
```

* 근데 나중에 스프링 DATA JPA가 인터페이스 메서드 위에다 바로 선언 가능.
* 스프링 데이터 jpa로 사용하는게 좋음. 