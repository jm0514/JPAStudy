# 1. 다대다 M:N
## 결론부터 이야기 하면 실무에서 쓰면 안됨

## 1.1.1 RDB의 다대다
* 관계형 DB에서는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없음
* 연결 테이블을 추가해서 일대다, 다대일 관계로 풀어내야 함 -> 야매방법임.

## 1.1.2 객체의 다대다
* RDB에서 안되는 다대다가 객채에서는 됨. -> 딜레마의 시작

## 1.2 사용법
* @ManyToMany사용
* @JoinTable로 연결 테이블 지정
* 다대다 매핑 : 단방향, 양방향 가능
* 쓰지마셈 제발

## 1.3 한계
* 편리해 보이지만 실무에서는 사용 X
* 연결 테이블이 단순히 연결만 하고 끝나지 않음
* 주문시간, 수량 같은 데이터가 들어올 수 있음.
* 중간테이블이 숨겨져 있어서 쿼리도 막 이상하게 나감.


## 1.4 M:N 극복방법
* 연결 테이블용 엔티티 추가
* @ManyTOMany -> @OneToMany @ManyToOn과 로 풀어서 해결

<img src="Desktop/JPAStudy/정찬욱/JPA기본편/다양한 연관관계 매핑/img/m:n.png">

```java
    public Member{
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();
        }

```
```java
    @Entity
    public class MemberProduct{
    
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
}

```

```java
    public Product{
        @OneToMany(mappedby = "products")
        private List<MemberProduct> members  = new ArrayList<>();
        }

```

* M : N --->  1 : N : 1로 풀어내야 함.

