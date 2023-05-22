package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.dto.OrderListDto;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static jpabook.jpashop.utils.MaskingUtil.maskingDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        // 회원 조회
        Member member = memberRepository.findOne(memberId);
        // 상품 조회
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        
        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        
        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        // Order entity 내 cascade 설정으로 배송과 주문상품도 동시 persist. 단, 해당 entity 관계가 명확함으로 사용 가능
        orderRepository.save(order);

        return order.getId();
    }
    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    // 검색
    public List<OrderListDto> searchOrders(OrderSearch orderSearch) {
        List<OrderListDto> orderListDtoList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();
        orderList = orderRepository.findAll(orderSearch);
        orderList.forEach(el -> {
            OrderListDto orderListDto = new OrderListDto(el);
            orderListDtoList.add(orderListDto);
        });
        maskingDto(orderListDtoList);
        return orderListDtoList;
    }
}
