package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)//jap로 데이터 입출력시에는 가급적이면 Transcaction하에서 동작하는게 좋아서 붙여주는거임. -> 그래야 LazyLoading이든 다르것도 다 동작함.
@RequiredArgsConstructor//롬복으로 생성자 주입해주는거 다 만들어줌
public class MemberService {
    private final MemberRepository memberRepository;

//    @Autowired
//    private MemberRepository memberRepository; <- 이렇게 필드 인젝션 하면 테스트 코드 짜는게 빡셈
//      @Autowired//이렇게 생성자 주입으로 해주면 테스트 하기도 편하고 어디서 코드 짤 때 컴파일러가 여기 의존하는거 있다고 알려주기도 하고 여러모로 편함.
//      public MemberService(MemberRepository memberRepository){
//          this.memberRepository = memberRepository;
//      } 근데 요즘은 그냥 롬복으로 다 주입해


    //회원가입
    @Transactional(readOnly = false)//데이터 쓰기에는 readOnly=ture 넣으면 데이터 변경이 안되니깐 붙이지 마셈. readOnly=false가 디폴트값이라. 쓰기에는 @Transactional만 붙여줌.
    public Long join(Member member){
        validateDuplicateMember(member);//중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        //Exception
        List<Member> findMembers =  memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체조회

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
