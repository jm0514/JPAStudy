package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.DTO.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    //메서드 이름으로 쿼링 -> 길어지면 노답
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);


    //네임 파라미터
    List<Member> findByUsername(@Param("username") String username);

    //레파지토리 메소드에 쿼리 정의하기
    @Query("select  m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
    //이 방법이 좋은게 쿼리에 오타나면 애플리케이션 로딩시점에 에러로 미리 알려줌
    // 이게 쿼리라서 로딩 시점에 미리 다 파싱 해봄 미리 sql을 다 만들어둠. 그러다가 안되네? 하고 에러 내는거임

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO로 조회하는 방법
    //추후 QueryDSL로 간단하게 다루기 가능
    @Query("select new study.datajpa.DTO.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();


    //컬렉션 파라미터 바인딩, 위치기반은 사용하지 말것. 이름기반이 여러모로 좋음
    //마찬가지로 DTO로 조회
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    /*
    반환 티입이 굉장히 유연함
     */
    List<Member> findListByUsername(String username);//컬렉션
    Member findMemberByUsername(String username);// 단건
    Optional<Member> findOptionalByUsername(String username);// 단건 Optional


    @Query(value="select  m from Member m left join m.team", countQuery = "select count(m) from Member m")
    //left join이라 어째피 갯수가 같아서 countQuery는 join을 할 필요가 없으므로 쿼리가 안나가도록 따로 빼는거임
    Page<Member> findByAge(int age, Pageable pageable);

    //Slice<Member> findByAge(int age, Pageable pageable);


    @Modifying(clearAutomatically = true)//이 옵션 걸어주면 벌크연산 후에 em.flush, em.clear안해줘도 됨
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    @Query("select m from Member m left join fetch m.team")// 페치조인 엔티티 그래프 미적용
    List<Member> findMemberFetchJoin();


    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();


    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")//jpql도 짜고 페치조인도 하고싶고 그럴때 사용
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("team"))//회원 조회 할 때 팀 정보를 뽑을 일이 너무 많다면 파라미터로 team을 받고 EntityGraph로 한방에 조회
    List<Member> findEntityGraphByUsername(@Param("username") String username);


    //JPA Hint
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String usernmae);

    @Lock(LockModeType.PESSIMISTIC_WRITE)// select for update 비관적 락. 다른 사람들이 손대지 말라고 다 락 걸어버리는거임
    List<Member> findLockByUsername(String username);







}
