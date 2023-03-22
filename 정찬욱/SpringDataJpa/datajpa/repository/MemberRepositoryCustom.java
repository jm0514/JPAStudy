package study.datajpa.repository;
/*
- 인터페이스내 특정한 기능만 몇개 상속받아서 사용하거나
- 순수 jpa 구현체를 쓰고싶다거나
- QueryDSL을 사용하고 싶거나
- MyBatis를 섞어서 쓰고 싶다거나
- 아니면 JDBC를 직접 연결하여 쓰고싶다거나
- 전혀 다른 작업을 하고 싶다거나

등등등 여러 이유들로 인해서 인터페이스르 개조하여 쓰고싶을때 사용하는 방법임
 */

/*
여기서 인터페이스를 만들고 구현체를 또 만들어야 함

그 후 MemberRepository에 가서 extends MemberRepositoryCustom을 해주면 됨.
자바에서 기능을 지원해주는게 아니라 스프링data jpa에서 지원하는 기능임

 */

/*
한가지 규칙이 있음
구현체 명명법임
MemberRepository라고 Jpa 인터페이스를 만들어줬다면 커스텀의 구현체는 반드시
MemberRepositoryImpl로 명명해야 함. 그래야 spring data jpa가 보고 알아서 다 해줌
 */

import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();

}
