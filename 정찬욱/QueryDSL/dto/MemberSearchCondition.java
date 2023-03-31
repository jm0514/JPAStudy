package study.querydsl.dto;

import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;

@Data
public class MemberSearchCondition {
    //회원명, 팀명, 나이(ageGoe, AgeLoe)


    private String username;
    private String teamName;
    private Integer ageGoe;
    private Integer ageLoe;



}
