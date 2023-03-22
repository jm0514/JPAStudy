package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.DTO.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.QueryHint;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberQueryRepository memberQueryRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검즏
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);


        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);


        // 삭제검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long delCount = memberRepository.count();
        assertThat(delCount).isEqualTo(0);
    }

    @Test// 메소드 이름으로 쿼리 만들기
    public void findByUsernameAndAgeGreaterThan(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        for(String s : usernameList){
            System.out.println("s="+s);
        }
    }

    @Test //Dto로 조회
    public void findMemberDto() {

        Team team = new Team("teamA");
        teamRepository.save(team);


        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);


        List<MemberDto> usernameList = memberRepository.findMemberDto();
        for(MemberDto dto: usernameList){
            System.out.println("dto="+dto);
        }
    }

    @Test//컬렉션 파라미터 바인딩 Dto로 조회, 위치기반
    public void findByNames() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);


        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for(Member member : result){
            System.out.println("member="+member);
        }
    }

    @Test// 유연한 반환티입
    public void returnType() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        Member findMember = memberRepository.findMemberByUsername("AAA");
        Optional<Member> bbb = memberRepository.findOptionalByUsername("AAA"); // 데이터가 있는지 없는지 잘 모르겠으면 그냥 옵셔널로 반환하면
        //NPE도 안터지고 좋음.


        //근데 단건 optional인데 두개 조회되면 예외가 터짐


        System.out.println("aaa : "+aaa);
        System.out.println("findMember : "+findMember);
        System.out.println("bbb : "+bbb);
    }
    @Test//스프링 데이터 jpa를 이용한 paging
    public void paging(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        int none = 0;

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "username");//스프링데이터jpa는 페이지를 1부터가 아닌 0부터 시작함


        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);// 반환 타입을 Page로 받으면 totalCount를 알아서 계산해옴

        //엔티티 노출을 막기위해 Dto로 변환해줌
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        //Slice<Member> page1 = memberRepository.findByAge(age, pageRequest);
        /*
        slice는 내가 요청한 칸보다 하나 더 가져와서 미리 대기시켜둠
        그래서 모바일 같은데서 주로 쓰는데 slice로 해서 하나 남아 있으면 더보기 라는 버튼이 뜨게 한다던가 그런걸로 많이 씀
         */


        //page then
        List<Member> content = page.getContent();//페이징 후에 안에 내용들 쓰고싶으면 getContets()로 가져오면 됨.
        System.out.println("========================");
        for (Member member : content) {
            System.out.println(member);
        }
        System.out.println("========================");
        long totalElements = page.getTotalElements();// 얘로 totalCount를 가져오는거임
        /*
        totalCount는 어쨌건 전체 Row의 수를 다 카운팅 해야 하므로 성능을 잡아먹는 요소 중에 하나임
        그래서 Count쿼리를 분리하는 방법이 또 있음
         */

        //slice then
        //List<Member> content1 = page1.getContent();



        //page
        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);// 0번째 페이지를 가져 올 수 있음.
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();//지금 첫번째 페이지이냐
        assertThat(page.hasNext());//그 다음 페이지가 있냐

        //slice
//        assertThat(content1.size()).isEqualTo(3);
//        assertThat(page1.getNumber()).isEqualTo(0);
//        assertThat(page1.isFirst()).isTrue();
    }

    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 21));
        memberRepository.save(new Member("member4", 20));
        memberRepository.save(new Member("member5", 32));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        em.flush(); // 혹시 남아있을지도 모를 db에 반영 안된 잔여 내용까지 싹 다 db에 반영시킴
        em.clear();

        List<Member> result = memberRepository.findByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 : "+ member5); //em.flush, em.clear 안해주면 33살로 나오지 않고 age = 32로 나옴

        //then
        assertThat(resultCount).isEqualTo(3);

        /*
        성능을 위해 사용
        1차캐시에 원본도 가지고 있고 바뀐부분도 가지고 있어야해서 무거워짐

        벌크 연산을 수행하게 되면 영속성 컨택스트를 무시하고 바로 db에 update룰 날려버림 -> 영속성 컨택스트가 해당 data가 db에 들어간 사실을 모름
    그래서 벌크 연산을 하고 난 이후에 혹시 조회를 하거나 한다면  em.flush() em.clear()를 해준 후에 조회, 삭제, 업데이트 등등 작업을 진행
    */
    }

    @Test
    public void findMemberLazy(){//@Entity Graph. -> fetch join의 간단버젼. Left Outer Join을 사용함.
        //given
        //member1 -> teamA
        //member2 -> teamb

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        Member member3 = new Member("member3", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when
        //List<Member> members = memberRepository.findAll();// 페치조인 미적용
        //List<Member> members = memberRepository.findMemberFetchJoin();//페치조인 적용한 쿼리용

        //@EntityGraph로 fetch join을 간편하게 할 수 있다. -> 쿼리가 복잡해지면 jpql로 직접 fetch join을 해야함. 간단한 쿼리에서만 사용
        //List<Member> members = memberRepository.findAll();// @EntityGraph적용하여 회치조인 적용
        List<Member> members = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members) {
            System.out.println("member = "+member.getUsername());
            System.out.println("member.getTeam.getClass = "+member.getTeam().getClass());// -> 레이지로딩 걸어놔서 프록시객체임
            // getTeam.getName해야 그때서야 DB에 실제로 찔러서 진짜 데이터 가져옴
            System.out.println("member = "+ member.getTeam().getName());
        }
    }
    @Test//쿼리힌트 사용
    public void queryHint(){
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");// readOnly를 하게되면 변경감지 안함
        //하이버네이트에서 그냥 읽기 전용이라고 생각하고 스냅샷도 없음. 최적화함. 순수 jpa엔 없고 하이버네이트 구현체에서 지원함.
        //근데 막상 넣는다고 그렇게 비약적으로 성능이 올라기지는 않음. 이걸로 최적화 할 정도면 이미 앞에서 레디스 캐시를 깔고 난리가 날 정도로 트래픽이 많은 서비스이

        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock(){// 쓸 일이 별로 없음. 실시간 트래픽 많은 서비스에서는 락 걸면 안됨.
        //given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        List<Member> findMember = memberRepository.findLockByUsername("member1");
    }



    @Test//커스텀 레파지토리 구현 -> MemberRepositoryCustom 참고
    public void callCustom(){
        List<Member> result = memberRepository.findMemberCustom()
;    }

    @Test//핵심 비즈니스 로직이 아닌 화면단에 맞춴 쿼리들은 그냥 클래스 따로 빼서 분리시키는게 좋다
    //굳이 커스텀 배웠다고 한 화면에 다 때려박는 그런 실수를 하지 말자
    public void callLessImportantBusinessLogic(){
        List<Member> result = memberQueryRepository.findAllMembers();
    }



}