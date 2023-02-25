package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional//스프링의 트랜잭션은 롤백을 해버림. -> insert쿼리 안날림. 어짜피 롤백해버릴건데 뭐하러 날림.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    //@Rollback(false)//테스트 단계에서 데이터 보고싶으면 롤백false해버리면 됨.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
        em.flush();// ->이걸 해줘야 영속성이 DB에 반영됨. db에 insert날리는 쿼리문 볼 수 있음.
        assertEquals(member, memberRepository.findOne(savedId));

    }

  @Test(expected = IllegalStateException.class)
  public void 회원_중복_테스트() throws Exception{
      //given
      Member member1 = new Member();
      member1.setName("kim1");

      Member member2 = new Member();
      member2.setName("kim1");

      //when
      memberService.join(member1);
      memberService.join(member2);




      //then
      fail("예외가 발생해야 한다.");
  }


}