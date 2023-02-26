package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){//준영속성 객체 관리법
        Item findItem = itemRepository.findOne(itemId);//그냥 db에 있는애 불러오는거임.
        // db에 있는 쌩자 영속성 객체를 불러와서 처리하면 변경되는건 jpa가 관리해줌
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        //이렇게 해주면 itemRepository.save(); 얘 호출 할 필요가 전혀 없음. itemRepository참고.

    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
