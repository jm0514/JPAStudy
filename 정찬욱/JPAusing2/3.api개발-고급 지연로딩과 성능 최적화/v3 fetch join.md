
## v3 fetch join
* v2에서 발생한 성능이슈(N+1 문제)를 fetch join으로 해결할거임
```java
@GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }
```
* v3에서는 Entity로 조회해서 중간에 DTO로 변환하여 출력함
* v4에서는 이것도 최적화 할거임. jpa에서 한방에 dto로 끄집어내는 방법이 있음.

```java
public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o"+
                        " join fetch o.member m"+
                        " join fetch o.delivery d", Order.class
        ).getResultList();
        
    }
```
* member, delivery eintity에 lazy 걸려있어도 다 무시하고 가져옴.</br>
명시해서 어떤걸 가져오라고 콱 찍어서 쿼리를 내렸기 때문에 프록시 객체가 아닌 진짜 데이터를 다 물어서 한방에 가져옴</br>
fetch join이라고 함.</br></br>



### 나간 쿼리
```java
select
        order0_.order_id as order_id1_6_0_,
        member1_.member_id as member_id1_4_1_,
        delivery2_.delivery_id as delivery_id1_2_2_,
        order0_.delivery_id as delivery_id4_6_0_,
        order0_.member_id as member_id5_6_0_,
        order0_.order_date as order_date2_6_0_,
        order0_.status as status3_6_0_,
        member1_.city as city2_4_1_,
        member1_.street as street3_4_1_,
        member1_.zipcode as zipcode4_4_1_,
        member1_.name as name5_4_1_,
        delivery2_.city as city2_2_2_,
        delivery2_.street as street3_2_2_,
        delivery2_.zipcode as zipcode4_2_2_,
        delivery2_.status as status5_2_2_ 
    from
        orders order0_ 
    inner join
        member member1_ 
            on order0_.member_id=member1_.member_id 
    inner join
        delivery delivery2_ 
            on order0_.delivery_id=delivery2_.delivery_id
```
* 이 쿼리 한방으로 필요한 데이터를 한방에 조인해서 가져오게 됨.
* 단점은 select절에서 모든걸 다 찍어서 가져옴 v4에서는 이것도 최적화 할거임.