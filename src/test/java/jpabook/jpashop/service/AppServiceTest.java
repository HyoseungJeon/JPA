package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AppServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @Test
    public void 주문후조회() throws Exception {

        // 회원 등록
        Member member = new Member();
        member.setName("전효승");
        Long memberId = memberService.join(member);

        // 상품 등록
        Book book = new Book();
        book.setName("테스트 상품");
        book.setStockQuantity(100);
        book.setPrice(100);
        Long itemId = itemService.saveItem(book);

        // 주문
        orderService.order(memberId, itemId, 1);

        // 회원 조회
        memberService.findOne(memberId);
    }

}