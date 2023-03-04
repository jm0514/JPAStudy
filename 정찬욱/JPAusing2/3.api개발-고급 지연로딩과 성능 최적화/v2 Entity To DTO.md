## 3.2 Entity To DTO
```java
@GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){// 지난번에도 말했지만 실전에서는 리스트 그대로가 아니라 Result라는걸로 {}에 감싸서 내보내야 -> MemberApiController 참고
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))// order o 를 SimpleorderDto(o)로 바꾼다는 말
                .collect(Collectors.toList());
        return collect;
        
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();// Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();// Lazy초기화
        }
    }
```

* v1, v2의 공통적 문제는 너무 많은 조회쿼리가 나간다는것.</br>
* member, order, delivery(주소) 이렇게 세개의 테이블을 건들임 </br>
* order조회 1번
* order -> member 지연로딩 조회 N번
* order -> delivery 지연로딩 조회 N번
-> N+1 문제가 터짐</br>
* 지금 총 주문 갯수가 2개니깐 N=2. 1+2+2 =>총 5개의 쿼리가 나간것.</Br>
Lazy대신에 EAGER를 써도 최적화가 안됨.
* 해결방법 : fetch join
