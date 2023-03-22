package study.datajpa.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)// jpa는 기본적으로 기본생성자가 필요함. private으로 하면 생성을 모해서 protected 레벨로.
@ToString(of = {"id", "username", "age"})// ToString은
public class Member extends BasdEntity{

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")//fk를 지정하는것
    private Team team;


    public Member(String usernmae) {
        this.username = usernmae;
    }

    public Member(String usernmae, int age) {
        this.username = usernmae;
        this.age = age;
    }

    public Member(String usernmae, int age, Team team) {
        this.username = usernmae;
        this.age = age;
        if(team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team){// 연관관계 메소드
        this.team = team;
        team.getMembers().add(this);
    }
}
