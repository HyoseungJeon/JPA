package jpabook.jpashop.domain.dto;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderItemDto {

    private Long id;

    private ItemDto item;

    private int orderPrice;

    private int count;

    public OrderItemDto(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.item = new ItemDto(orderItem.getItem());
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }
}
