package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }


    @GetMapping("items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
         Book item = (Book) itemService.findOne(itemId);

         BookForm form = new BookForm();
         form.setId(item.getId());
         form.setName(item.getName());
         form.setPrice(item.getPrice());
         form.setAuthor(item.getAuthor());
         form.setStockQuantity(item.getStockQuantity());
         form.setIsbn(item.getIsbn());

         model.addAttribute("form", form);
         return "items/updateItem";

    }

    @PostMapping("items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId, @ModelAttribute("form") BookForm form){
        Book book = new Book();// 얘는 사실 jpa에서 관리하는 애가 아님. 준영속성 객체임. Book객체는 new라도 이미 DB에 한번 들어갔다 나온 애라서 식별자가 있음.
        // 임의로 만들어낸 엔티티도 식별자 갖고있으면 준영속성 엔티티임. ->Jpa에서 관리 안함. 자동으로 db에 변경이 반영되지 않음. ->ItemService참고하셈.
    //        book.setId(form.getId());
    //        book.setName(form.getName());
    //        book.setPrice(form.getPrice());
    //        book.setStockQuantity(form.getStockQuantity());
    //        book.setAuthor(form.getAuthor());
    //        book.setIsbn(form.getIsbn());
    //        // setter열어두면 어디서 변경이 일어나는지 추적하기가 쉽지 않음. 그래서 변경하는 전용 메서드 만들어서 해주는게 젤 좋음.
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        //만약에 받아야 할 데이터가 많아서 파라미터가 많아질거 같다면 따로 dto만들어서 넣어주는게 좋음.
        itemService.saveItem(book);
        return "redirect:/items";
    }


}
