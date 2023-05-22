package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class OrderSearch {

    @Size(min = 0, max = 4)
    private String memberName;

    private OrderStatus orderStatus; // 주문상태[ORDER, CANCEL]
}
