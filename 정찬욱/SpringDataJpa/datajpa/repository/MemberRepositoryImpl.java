package study.datajpa.repository;


import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;



@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final EntityManager em;

    @Override// 순수 jpa 예시
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
