## 2. 수정 쿼리와 find 커맨드의 분리
```java
@PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
```

* 
  영한님 스타일 : 업데이트 쿼리와 조회 커맨드를 철저하게 분리하여 짬. id값 정도만 반환하거나.
  유지 보수성이 좋아짐. 되게 복잡한게 아니라면 이렇게 분리하여 짜는게 가시성도 좋고 성능 이슈도 없고 좋음.
  