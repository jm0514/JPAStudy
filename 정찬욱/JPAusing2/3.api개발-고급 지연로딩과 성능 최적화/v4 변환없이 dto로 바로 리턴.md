## v4. 다이렉트로 dto로 출력


```java
@GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderRepository.findOrderDtos();
    }
```

```java
public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select  new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        "from Order o"+
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDto.class)
                /*
                기본적으로 jpa는 엔티티나 vo만 반환이 가능함. dto의 경우엔 안됨
                하고싶으면 new operation을 꼭 써야함
                 */
                .getResultList();
    }
```

* 반환티입인 OrderSimpleQueryDto를 만들어줌
```java
@Data
  public class OrderSimpleQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address){
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```

* 나가는 쿼리
```java
select
        order0_.order_id as col_0_0_,
        member1_.name as col_1_0_,
        order0_.order_date as col_2_0_,
        order0_.status as col_3_0_,
        delivery2_.city as col_4_0_,
        delivery2_.street as col_4_1_,
        delivery2_.zipcode as col_4_2_ 
        from
        orders order0_
        inner join
        member member1_
        on order0_.member_id=member1_.member_id
        inner join
        delivery delivery2_
        on order0_.delivery_id=delivery2_.delivery_id
```
* from절 부터는 v3와 같음. 다른점은 select절에서 내가 명시한 값만 딱 찍어서 가져옴
* v3에서는 db에서 select하는 값이 더 많음. 성능에서 아무래도 차이가 남
* 그렇다고 무조건 v4가 좋은것도 아님. v3와 v4가 서로 트레이드오프 관계가 있음.
* v3는 모두 긁어온 후 내가 원하는것만 골라서 o.member, o.delivery로 골라온거임 -> 외부는 건들이지 않고 내부에서만 내가 원하는것만 골라온거임.
* v4는 정확하게 특정값만 찍어서 가져옴 -> 다른곳에서 이용이 안됨 재사용성이 떨어짐
* v4가 코드상 좀 더 지저분함
* 장잔점이 있으니 v3와 v4중에 잘 골라서 씁시다.

## Repository의 진짜 의미
* Repository는 순수한 엔티티를 조회하는데 써야함. v2나 v3정도는 써도 됨.
* Repository 패키지 밑에 하위 패키지를 따로 둬서 그 안에 성능 최적화된 DTO를 따로 만들어서 관리하는게 좋음 -> SimpleQuery의 이름을 달고