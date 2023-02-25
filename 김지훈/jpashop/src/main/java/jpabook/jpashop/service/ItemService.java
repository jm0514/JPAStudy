package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.controller.BookForm;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}

	// 변경 감지 기능 사용
	@Transactional
	public void updateItem(Long itemId, BookForm form) {
		Book findItem = (Book) itemRepository.findOne(itemId);
		findItem.setId(form.getId());
		findItem.setName(form.getName());
		findItem.setPrice(form.getPrice());
		findItem.setStockQuantity(form.getStockQuantity());
		findItem.setAuthor(form.getAuthor());
		findItem.setIsbn(form.getIsbn());
	}

	public List<Item> findItems() {
		return itemRepository.findAll();
	}

	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}

}
