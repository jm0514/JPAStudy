# 3. 페치조인의 한계
* 패치 조인 대상에는 별칭을 줄 수 없다. -> ~~~ join fetch t.members m 이게 불가능
  * 하이버네이트는 가능하지만 가급적 사용하지 말 것
* 둘 이상의 컬렉션은 페치 조인 할 수 없다.
* 컬렉션을 페치 조인하면 페이징API(setFirstResult, setMaxResult)를 사용할 수 없다.
  * 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
  * 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
  
## 3.1 @BatchSize
과감하게 join fetch를 삭제함
```java
String query = "select t From Team t";

List<Team> result = em.createQuery(query, Team.class)
        .setFirstResult(0)
        .setMaxResults(2)
        .getResultList();
System.out.println("result="+result.size());
        for(Team team : result){
        System.out.println("team = "+team.getName()+"|members="+team.getMembers().size());
        for(Member member : team.getMembers()){
        System.out.println("->member="+member);
        }
        }

```
* 지금 lazy 걸어놔서 반복문 하나 돌 때 마다 team에 select 쿼리문 돔
* 팀 한번 조회 하고 거기 있는 멤버 두명을 각각의 sql로 불러옴 -> 쿼리 세번 날아간거
* 성능에 매우 안좋음
* Team Entity로 가서
```java
@Entity
public class Team{
    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @BatchSize(size=100)// size는 1000이하의 숫자중 적절하게 주면 됨
    @OneToMany(mappedBy="team")
    private List<Member> members = new ArrayList<>();
}
```
* 쿼리가 날아가는데 select 문에 보면 ~~~ where members Team_ID in(?, ?) 라고 나옴
* 이거 물음표 두개가 각각 팀A의 id, 팀B의 id 이렇게 각각 들어있는거임 -> 한번에 팀A와 연관된 멤버, 팀B와 연관된 멤버 다 가져온거임
* size=100 이렇게 줬으니깐 List<>에 담긴 팀을 쿼리 한번에 100개씩 넘기는거임
* 엔티티에서 줘도 되고 persistence.xml에서 글로벌 세팅을 줄 수도 있음
* \<property name="hibernate.default_batch_fetch_size" value="100"/> 이렇게 주면 됨. value는 1000이하의 적절한 숫자


## 3.2 정리
* 모든 것을 페치 조인으로 해결 할 수는 ㄴ없음
* 페치 조인은 객체 그래프를 유지 할 때 사용하면 호과적
* 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 한다면, 페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 변환하는것이 효과적