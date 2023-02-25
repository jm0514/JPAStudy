package jpabook.jpashop.repository;

import antlr.StringUtils;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch){

         return em.createQuery("select o from " +
                         "Order o join o.member m where o.status = :status and m.name like :name", Order.class)
                 .setParameter("status", orderSearch.getOrderStatus())
                 .setParameter("name", orderSearch.getMemberName())
                 .setMaxResults(1000)//최대 1000건
                 .getResultList();

    }

//    /**
//     * JPA Criteria <- jpa에서 제공하는 동적쿼리 작성법. 근데 영한님은 별로 권하진 않음.
//     * 무슨 쿼리가 생성이 되는지 쿼리가 예측이 안됨. 권장 x
//     */
//    public List<Order> findAllByCriteria(OrderSearch orderSearch){
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
//        Root<Order> o = cq.from(Order.class);
//        Join<Object, Object> m = o.join("member", JoinType.INNER);
//
//        List<Predicate> criteria = new ArrayList<>();
//
//        //주문상태 검색
//        if(orderSearch.getOrderStatus() != null){
//            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
//            criteria.add(status);
//        }
//        //회원 이름 검색
//        if(StringUtils.hasText(orderSearch.getMemberName())){
//            Predicate name =
//                    cb.like(m.get("name"), "%"+ orderSearch.getMemberName()+"%");
//            criteria.add(name);
//        }
//        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
//        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
//        return query.getResultList();
//
//    }

    //쿼리DSL을 사용해서 동적 쿼리를 작성함.
}
