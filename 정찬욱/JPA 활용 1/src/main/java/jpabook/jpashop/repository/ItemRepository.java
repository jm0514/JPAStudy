package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){//jpa에 저장하기 전까진 id값이 없음. id가 null이란건 완전히 새로 생성된 객체라는 말임. 그래서 em.persist로 완전 신규러 등록하는거임
            em.persist(item);
        } else{
            em.merge(item); //이미 한번 저장을 했다는 말임. 그래서 merge는 수정이라고 생각하면 됨.
            /*
            merge는 간단하게 이야기 해서 준영속성 객체를 영속성 객체로 관리해주느거임.
            Item findItem = itemRepository.findOne(itemId);/
            findItem.setPrice(param.getPrice());
            findItem.setName(param.getName());
            findItem.setStockQuantity(param.getStockQuantity());
            ItemService에 있는 얘랑 똑같은 동작 해주는거임.
            근데 주의해야함. -> 모든 파라미터에 넘어온 것들로 속성이 그냥 다 갈아엎어져서 db에 반영되는거임. 만약 파라미터에 어떤 값이 Null로 들어오면 null로 업데이트 해버림. 존나 위험. 선택을 할 수 있는게 아님. 그래서 조심해야 함.
            귀찮더라도 ItemService에 있는 변경감지 방법을 사용해야 덜 위험함.

             */
        }
    }
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){//단건은 그냥 찾아도 되지만 여러개, 리스트로 찾는건 쿼리문 작성해야 함.
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
