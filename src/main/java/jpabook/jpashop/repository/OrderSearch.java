package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderSearch {

    private String memberName;

    private OrderStatus orderStatus; // 주문상태[ORDER, CANCEL]
}
