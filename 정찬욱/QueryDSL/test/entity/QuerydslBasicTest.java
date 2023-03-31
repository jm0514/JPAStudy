package study.querydsl.entity;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.Expression;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;



    @BeforeEach
    public void before(){

        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL(){//단점 : 사용자가 이 메서드를 호출 했을때 에러를 알 수 있음
        Member findMember = em.createQuery("select m from Member m where m.username =:username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
;    }

    @Test
    public void startQuerydsl(){// 컴파일시에 쿼리가 바로 에러가 잡혀서 좋음.

        //QMember m = new QMember("m");// 같은 테이블을 조인해야 하는 경우에는 이렇게 선언하고 m을 member자리에 넣어주는게 좋지만
        //그 이외에는 QMember.member를 static import로 불러서 member로만 쓰는게 훨씬 깔끔하고 좋음

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member2"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member2");
    }

    @Test
    public void search(){//검색 조건 쿼리1

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchAndParam(){//검색 조건 쿼리2

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"),//and의 경우 ' , '로 끊을수 있음
                        (member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch(){//결과 조회
        /*
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch(); -> 리스트 조회. 데이터 없으면 빈 리스트 반환

//        Member fetchOne = queryFactory
//                .selectFrom(member)
//                .fetchOne(); -> 단건조회

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst(); -> limit(1).fetchOne();

        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();//이거 보다는 fetch()를 쓰라고 안내나옴. 조회와 페이징 차이 때문에 복잡한 쿼리에서 이상한 쿼리가 나가기때문

        results.getTotal();//total이 붙으면 count쿼리를 암께 실행함 -> 몇개인지 알아야 페이징을 함 쿼리를 총 두번 실행하게됨.
        List<Member> content = results.getResults(); -> 페이징 정보 포함. total count 쿼리 추가 실행

*/
        long total = queryFactory
                .selectFrom(member)
                .fetchCount();//fetch().size()로 대신 쓰라고 안내문 나옴. 암튼 count()수 조회


    }

    /*
    회원 정렬 순서
    1. 회원 나이 내림차순(desc)
    2. 회원 이름 올림차순(asc)
    단, 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    public void sort(){//정렬
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();


    }

    @Test
    public void paging1(){//페이징
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)//앞에 몇개를 스킵할건지를 정하는거임. 0부터 시작
                .limit(2)// 2개를 가져온다
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void paging2(){//페이징
        QueryResults<Member> queryResults = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)//앞에 몇개를 스킵할건지를 정하는거임. 0부터 시작
                .limit(2)// 2개를 가져온다
                .fetchResults();// fetchResults()는 쓰지 말고 그냥 fetch()를 쓰라함

        /*
        로직은 복잡하지만 카운트 쿼리는 단순하게  따로 짜야하는 경우에는 이 방법은 좋지 않음
         */

        assertThat(queryResults.getTotal()).isEqualTo(4);
        assertThat(queryResults.getLimit()).isEqualTo(2);
        assertThat(queryResults.getOffset()).isEqualTo(1);
        assertThat(queryResults.getResults().size()).isEqualTo(2);
    }

    @Test
    public void agrregation(){
        List<Tuple> result = queryFactory//내가 필요한걸 정확하게 찍어서 명시한 경우에는 튜플 형식으로 나옴 -> querydsl의 튜플임. 타입이 여러가지가 들어오기 때문에 튜플을 사용하는거임.
                .select(// 실무에선 튜플이 아닌 dto로 뽑아오는데 그 방법은 뒤에서
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }


    /*
    팀의 이름과 각 팀의 평균 연봉 구하기
     */
    @Test
    public void group() throws Exception{
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /*
     팀 A에 소속된 모든 회원  *join*
     */
    @Test//연관관계가 있는 테이블끼리 조인
    public void join(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");
    }

    /*
    세타조인(연관관계가 없는 테이블끼리 조인)
    회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test// 연관관계가 없는 테입르끼리 조인
    public void theta_join(){
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();// -> 막조인을 하는건데 DB가 알아서 성능 최적화를 함

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");

        /*
        left(right) outer join이 불가능한 단점이 있음 -> 추후에 join on을 사용하면 outer join이 가능함.
         */


    }

    /*
    join on의 사용
    1. 연관관계가 있는 엔티티 외부조인
    //회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
    jpql : select m, t from Member m left join m.team t on t.name = 'teamA'
     */
    @Test
    public void join_on_filtering(){
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                //  그냥 join에서는 on 안써도됨. .join(member.team, team).on(team.name.eq("teamA") == .join(member.team, team).where(team.name.eq("teamA") 임. inner join의 경우 on절이 큰 의미가 없음
                // 하지만 left join을 해야만 한대면 on을 써야함. where로는 안풀림
                .fetch();

        for (Tuple tuple : result){
            System.out.println("tuple = "+ tuple);
            /*
            tuple = [Member(id=3, username=member1, age=10), Team(id=1, name=teamA)]
            tuple = [Member(id=4, username=member2, age=20), Team(id=1, name=teamA)]
            tuple = [Member(id=5, username=member3, age=30), null]
            tuple = [Member(id=6, username=member4, age=40), null]
             */
        }
    }

    // 2. 연관관계가 없는 엔티티의 외부조인
    //회원의 이름이 팀 이름과 같은 대상 외부조인
    @Test
    public void join_on_no_relation(){

        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))  //.leftJoin(member.team, member) -> 이렇게 해주면 join 대상을 id값으로 매칭을 해서 가져옴, leftJoin(team) 이렇게 하면 이름으로만 매칭시켜서 필터링됨.
                .fetch();

        for (Tuple tuple : result){
            System.out.println("tuple = "+ tuple);
            /*
            tuple = [Member(id=3, username=member1, age=10), null]
            tuple = [Member(id=4, username=member2, age=20), null]
            tuple = [Member(id=5, username=member3, age=30), null]
            tuple = [Member(id=6, username=member4, age=40), null]
            tuple = [Member(id=7, username=teamA, age=0), Team(id=1, name=teamA)]
            tuple = [Member(id=8, username=teamB, age=0), Team(id=2, name=teamB)]
            tuple = [Member(id=9, username=teamC, age=0), null]
             */
        }
    }


    @PersistenceUnit
    EntityManagerFactory emf;


    @Test// 페치조인 미적용
    public void fetchJoinNo(){
        em.flush();
        em.clear();
        // -> 영속성 컨택스트에 뭔가가 남아있는 상태에서 fetch join을 하면 결과를 제대로 보기가 힘듦. 영속성 컨택스트 다 날리고 시작


        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 이미 로딩된 엔티티인지 아니면 초기화가 안된 엔티티인지 구별해주는 방법임.
        assertThat(loaded).as("페치조인 미적용").isFalse();


    }

    @Test// 페치조인 적용
    public void fetchJoinUse(){
        em.flush();
        em.clear();
        // -> 영속성 컨택스트에 뭔가가 남아있는 상태에서 fetch join을 하면 결과를 제대로 보기가 힘듦. 영속성 컨택스트 다 날리고 시작


        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()// member를 조회 할 때 연관된 팀을 한방에 다 가져옴
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam()); // 이미 로딩된 엔티티인지 아니면 초기화가 안된 엔티티인지 구별해주는 방법임.
        assertThat(loaded).as("페치조인 적용").isTrue();


    }

    @Test// 서브쿼리 -> com.querydsl.jpa.JPAExpressions를 사용
    //나이가 가장 많은 회원 조회
    public void subQuery() throws Exception{
        QMember memberSub = new QMember("memberSub"); //젤 밖에서 사용되는 member와 subQuery에서 사용되는 member가 중복되면 안되기 때문에 서브쿼리 내에서 사용할 alias를 선언해줌
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();
        assertThat(result).extracting("age")
                .containsExactly(40);
    }
    @Test// 서브쿼리 -> com.querydsl.jpa.JPAExpressions를 사용
    //나이가 평균 이상인 회원
    public void subQueryGoe() throws Exception{ // goe : >=, greater or equal
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();
        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    @Test// 서브쿼리 -> com.querydsl.jpa.JPAExpressions를 사용
    // in을 사용하는 서브쿼리
    public void subQueryIn() throws Exception{
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))// gt : > greater
                ))
                .fetch();
        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test// select 절에서의 서브쿼리 사용예
    public void selectSubQuery() throws Exception{
        QMember memberSub = new QMember("memberSub");
        List<Tuple> result = queryFactory
                //JPAExpressions static import 한거임
                .select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();
        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    /*
    JPA jpql 서브쿼리의 한계로 from절의 서브쿼리는 지원안함.
    당연히 querydsl도 지원안함. 하이버네이트 구현체를 사요하면 select절의 서브쿼리는 지원함.
    querydsl도 하이버네이트 구현체를 사용하면 select절의 서브쿼리는 지원함

    from절 서브쿼리 해결법
    1. 서브쿼리는 join으로 변경함(가능한 상황도 있지만 불가능한 상황도 있다.)
    2. 애플리케이션에서 쿼리를 2번 분리해서 실행한다.
    3. nativeSQL을 사용한다
     */

    @Test//case문 기본
    public void basicCase(){
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타")

                )
                .from(member)
                .fetch();

        for(String s : result) {
            System.out.println("s = "+s);
        }
    }

    @Test// 약간 더 복잡한 case문
    public void complexCase(){
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

        for(String s : result){
            System.out.println("s = "+s);
        }
    }
    //db에서는 이런 작업을 되도록이면 하지말고 서비스단에서 하도록 하자.

    @Test
    public void constant(){//상수 더하기
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetch();

        for(Tuple tuple : result) {
            System.out.println("tuple = "+ tuple);
        }
    }

    @Test
    public void concat(){//문자 더하기
        List<String> result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetch();

        for(String s : result){
            System.out.println("s = "+ s);
        }


        /*
        쿼리문 날아간것 중에 재밌는 부분 :  ((member0_.username||?)||cast(member0_.age as character varying)) as col_0_0_
        나이가 char로 캐스팅 되어있음. member.age.stringValue()라고 지정했기 때문

        참고사항 : 문자가 아닌 다른 타입들을 stringValue()로 문자로 변환할 수 있다. 이방법은 ENUM을 처리할때도 자주 사용한다.
         */
    }

    /*
    중급문법
     */

    // 프로젝션과 결과 반환 - 기본
    //1. 프로젝션 : select 대상 지정
    @Test// 1.1 프로젝션 대상이 하나
    public void simpleProjection(){
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for(String s : result){
            System.out.println("s = "+s);
        }
    }

    @Test //1.2 튜플 프로젝션
    public void tupleProjection(){
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for(Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            System.out.println("username = "+username);
            System.out.println("age = "+age);
        }

        /*
        참고로 여기서의 tuple은 com.querydsl.core임 -> 레파지토리단에서 까지는 괜찮을지는 몰라도 서비스단이나 컨트롤러단 까지 튜플로 가지고 오는건 좋은 설계가 아님
        레파지토리단에서 쓰는 기술이 서비스나 컨트롤러 단에 노출이 되면 나중에 querydsl에서 다른 기술로 바꿀때 엉키는게 많아져서 별로 좋지 않음.
        레파지토리단 이외의 밖에다 결과를 던질때는 dto로 감싸서 던져야함.
         */
    }

    @Test// DTO로 바로 조회하는 방법
    public void findDtoByJPQL(){
        List<MemberDto> result = em.createQuery("select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m ", MemberDto.class)
                .getResultList();

        for(MemberDto memberDto : result){
            System.out.println("memberDto = "+memberDto);
        }

        // 단점이 new 명령어 사용해야함. DTO의 패키지 경로 다 적어줘야 해서 개불편, 생성자 방식만 지원함
    }
    /*
    querydsl로 하면 DTO로 조회하는걸 되게 쉽게 가능함.
    3가지 방법을 지원하는데
    1. 프로퍼티 접근
    2. 필드 직접 접근
    3. 생성자 사용
     */

    //1. 프로퍼티 접근
    @Test
    public void findDtoBySetter(){
        List<MemberDto> result = queryFactory
                .select(Projections.bean(MemberDto.class,
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        for(MemberDto memberDto : result){
            System.out.println("memberDto = "+ memberDto);
        }
    }

    // 2. 필드접근
    @Test
    public void findDtoByField(){
        List<MemberDto> result = queryFactory
                .select(Projections.fields(MemberDto.class,//Dto에 getter setter가 없어도됨 private 선언이라도 다 라이브러리가 알아서 해줌 걱정 ㄴ
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        for(MemberDto memberDto : result){
            System.out.println("memberDto = "+ memberDto);
        }
    }

    //3. 생성자 방식
    @Test
    public void findDtoByConstructor(){
        List<MemberDto> result = queryFactory
                .select(Projections.constructor(MemberDto.class,// 단 이방식은 dto에 선언된 값들의 타입이랑 member.username, member,age 가각의 타입이랑 일치해야함
                        member.username,
                        member.age))
                .from(member)
                .fetch();
        for(MemberDto memberDto : result){
            System.out.println("memberDto = "+ memberDto);
        }
    }

    @Test
    public void findUserDto(){
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,// UserDto로 찾으라고 했는데 찍은건 member.username, member.age라 매칭되는 값이 없어서 다 null로 뜸
                        //member.username -> as 별칭 안줬을때
                        //member.age))
                        member.username.as("name"), //as로 별칭을 찍어주게 되면
                        member.age))
                .from(member)
                .fetch();
        for(UserDto userDto : result){
            System.out.println("memberDto = "+ userDto);
            /*
            as 별칭 안줬을때
            memberDto = UserDto(name=null, age=10)
            memberDto = UserDto(name=null, age=20)
            memberDto = UserDto(name=null, age=30)
            memberDto = UserDto(name=null, age=40)

            as("name") 줬을때
            memberDto = UserDto(name=member1, age=10)
            memberDto = UserDto(name=member2, age=20)
            memberDto = UserDto(name=member3, age=30)
            memberDto = UserDto(name=member4, age=40)
             */
        }
    }

    @Test
    public void findUserDtoSubQuery() {//Dto 조회, 서브쿼리를 곁들인
        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
                .select(Projections.fields(UserDto.class,
                        member.username.as("name"), //as로 별칭을 찍어주게 되면
                        ExpressionUtils.as(JPAExpressions//서브쿼리는 방법이 없음. ExpressionUtils로 감싸는 수 밖에 없음
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                ))
                .from(member)
                .fetch();
        for (UserDto userDto : result) {
            System.out.println("memberDto = " + userDto);
        }
    }


    /*
    @QueryProjection사용 -> MemberDto에 어노테이션 선언하면 됨.-> DTO도 q파일로 생성됨 (compileQuerydsl 눌러줘야함)
     */
    @Test
    public void findDtoByQueryProjection(){
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();
        for(MemberDto memberDto : result){
            System.out.println("memberDto = "+ memberDto);
        }
        /*
        저 위에 constructor 방법이랑 뭐가 다름?
        -> constructor 방법은 쿼리 잘못 짜도 실행이됨. 런타임 오류로 발견하는 방법 밖에 없음

        반면에 쿼리프로젝션은 컴파일에러 내줌. cmd + p 눌러주면 뭐 입력해야 하는지 다 찍어서 보여줌

        근데 단점이 있음
        -> 1. q파일 생성해야함
            2. DTO가 querydsl의존성을 가지게됨. -> 여러 레이어단에 걸쳐서 querydsl에 의존하게 될 가능성이 있음.
         */
    }



    /*
    1. 동적쿼리 - BooleanBuilder사용
    2. 동적쿼리 - Where 다중 파라미터 사용
     */

    @Test//1. 동적쿼리 - BooleanBuilder사용
    public void dynamicQuery_BooleanBuilder(){
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }
    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();
        if(usernameCond != null){
            builder.and(member.username.eq(usernameCond));
        }
        if(ageCond != null){
            builder.and(member.age.eq(ageCond));
        }

        return  queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    @Test //2. 동적쿼리 where- 다중 파라미터 사용
    public void dynamicQuery_WhereParam(){
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        assertThat(result.size()).isEqualTo(1);
    }
    private List<Member> searchMember2(String usernameCond, Integer ageCond){
        return queryFactory
                .selectFrom(member)
                //.where(usernameEq(usernameCond), ageEq(ageCond))
                .where(allEq(usernameCond, ageCond))// -> 이 방법의 최대 장점. 메서드로 따로 뽑았기 때문에 조립이 가능함. 재사용성 up
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond){
        if(usernameCond != null) {
            return member.username.eq(usernameCond);
        } else{
            return null;
        }
    }
    private BooleanExpression ageEq(Integer ageCond){
        if(ageCond != null){
            return member.age.eq((ageCond));
        }
        else{
            return null;
        }

    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond){
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }


    /*
    벌크연산
     */

    @Test
    public void bulkUpdate(){
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();
        /*
        단점은 영속성 컨택스트를 무시하고 바로 db에 업데이트를 쳐서
        영속성 컨택스트가 알고있는 내용과 db가 알고있는 내용이 다름 -> 에러가 날 수 있기 때문에
        벌크 쿼리는 날린후 꼭 flush 해주고 clear날려주자.
         */
        em.flush();
        em.clear();

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();


        /*
        member1 = 10 -> 비회원
        member2 = 20 -> 비휘원
        member3 = 30 -> 그대로 유지
        member4 = 40 -> 그대로 유지
         */
        for(Member member1 : result) {
            System.out.println("member1 = " +member1);
        }
    }


    @Test// 모든 회원의 나이에 한살 더하기 by 벌크쿼리
    public void bulkAdd(){
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))//곱하기는 multiply()
                .execute();

    }

    @Test// 삭제쿼리 by bulk
    public void bulkDelete(){
        queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }


    /*
    SQL 펑션 호출하기
     */

    @Test//member를 M으로 바꾸기
    public void sqlFunction(){
        List<String> result = queryFactory
                .select(Expressions.stringTemplate("function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"))
                .from(member)
                .fetch();

        for (String s : result){
            System.out.println("s = "+s);
        }
    }

    @Test
    public void sqlFunction2(){//회원 이름을 모두 소문자로 바꾸기
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .where(member.username.eq(
                        //Expressions.stringTemplate("function('lower', {0})",
                        //member.username)))
                        member.username.lower())//이런 자주 쓰는 일반적인 함수는 쿼리dsl에서 내장함
                        )
                .fetch();

        /*
        쿼리 날아가는 모습
        select
        member1.username
    from
        Member member1
    where
        member1.username = lower(member1.username)
         */

        for(String s : result){
            System.out.println("s = "+s);
        }
    }



}
