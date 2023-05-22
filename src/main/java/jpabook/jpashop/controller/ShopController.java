package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.dto.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShopController {

    private final ItemService itemService;

    private final MemberService memberService;

    private final OrderService orderService;

    /**
     * 회원 등록
     * @param form
     * @return
     */
    @PostMapping(value = "members")
    public String create(@Validated @RequestBody MemberForm form) {
        Address address = new Address(form.getCity(), form.getStreet(),
                form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);
        memberService.join(member);
        return "success";
    }

    /**
     * 회원 목록 조회
     * @param id
     * @return
     */
    @GetMapping("members")
    public List<MemberDto> getMemberInfoList() {
        List<MemberDto> memberList = memberService.findMembers();

        return memberList;
    }

    /**
     * 회원 상세 조회
     * @param id
     * @return
     */
    @GetMapping("members/{id}")
    public Member getMemberInfo(@PathVariable Long id) {
        Member member = memberService.findOne(id);

        return member;
    }

    /**
     * 상품 목록
     */
    @GetMapping(value = "items")
    public List<Item> list() {
        List<Item> items = itemService.findItem();
        return items;
    }

    /**
     * 상품 등록
     * @param form 북폼
     * @return String 성공여부
     */
    @PostMapping(value = "items")
    public String create(@Validated @RequestBody BookForm form) {
        Book book = new Book();
        return getString(form, book);
    }

    private String getString(BookForm form, Book book) {
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        itemService.saveItem(book);
        return "success";
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping(value = "items/{itemId}")
    public BookForm getItemDetail(@PathVariable("itemId") Long itemId) {
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        return form;
    }
    
    /**
     * 상품 수정
     */
    @PatchMapping(value = "items/{itemId}")
    public String updateItem(@Validated @RequestBody BookForm form, @PathVariable String itemId) {
        Book book = new Book();
        book.setId(form.getId());
        return getString(form, book);
    }

    @GetMapping("orders")
    public List<OrderListDto> getOrderList(@Validated @ModelAttribute OrderSearch orderSearch) {

        List<OrderListDto> orderList = orderService.searchOrders(orderSearch);
        return orderList;
    }

    /**
     * 주문
     * @param memberId
     * @param itemId
     * @param count
     * @return
     */
    @PostMapping("orders")
    public String order(@Validated @RequestBody OrderForm orderForm) {

        orderService.order(orderForm.getMemberId(), orderForm.getItemId(), orderForm.getCount());

        return "success";
    }

    /**
     * 주문 취소
     * @param orderId
     * @return
     */
    @PostMapping(value = "orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "success";
    }
}
