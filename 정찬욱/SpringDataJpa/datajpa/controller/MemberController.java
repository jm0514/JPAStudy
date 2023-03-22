package study.datajpa.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.DTO.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")//도메인 클래스 컨버터 별로 권장안함.
    public String findMember(@PathVariable("id") Member member){
        /*
        스프링이 중간에서 컨버팅 하는걸 다 알아서 해주고 member를 파라미터 injection 해주는거임
        간단한 범위에서만 제한적으로 사용가능함
        별로 권장안함.
        조회용으로만 써야함 -> 트랜잭션 내가 아니라서 데이터 변경에는 무리가 있음.
         */
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size=5, sort="username")Pageable pageable){
        /*
        이 메서드에 한해서만 페이징 사이즈, sort규칙 걸어주는 방법임
         */

          /*
        이상태에서 members?page=0으로 하면 0번부터 20개씩 잘라서 가져옴
        0~19번, page=1 20~39이런식으로
        근데 10개만 가져오고 싶다 이러면 yml에다가 글로벌 세팅을 해줄수 있음

            data:
                web:
                  pageable:
                    default-page-size: 10
                    max-page-size: 2000 이렇게 넣어주면 됨


        또 page=0$size=3으로 하면 첫번째 페이지에 내용 3개만 가져올 수 있음

        page=0$size=3&sort=userId,desc 하면 정렬까지 가능함.
         */


        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(MemberDto::new);
        /*
        page를 1부터 시작하기
        스프링 데이터는 Page를 0부터 시작한다
        1부터 시작하는 법은 Pageable, Page를 파라미터와 응답 값으로 사용하지 않고 직접 클래스를만들어서 처리한다. 그리고 직접 pageRequest(Pageable구현체)를
        생성하여 레파지토리에 넘긴다. 물론 응답값도 page대신에 직접 만들어서 제공해야 한다.

        PageRequest request = PageRequest.of(1, 2);
        Page<MemberDto> map = memberRepository.findAll(request)
                .map(memberDto::new);

        -> 클라이언트가 알아서 하게 한다. 0부터 던져주면 알아서 가공 할 줄 알아야지 어딜
         */
        return map;

    }

    @PostConstruct
    public void init(){
         for(int i=0; i<100; i++){
             memberRepository.save(new Member("user"+i,i));
         }



    }
}
