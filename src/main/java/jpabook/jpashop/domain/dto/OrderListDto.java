package jpabook.jpashop.domain.dto;

import jpabook.jpashop.annotation.Masking;
import jpabook.jpashop.constant.MaskingType;
import jpabook.jpashop.domain.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderListDto {
    private Long id;

    private LocalDateTime orderDate;

    private OrderStatus status;

    @Masking(type = MaskingType.NAME)
    private String memberName;

    private OrderItemDto stanOrderItem;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderListDto(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.memberName = order.getMember().getName();
        this.stanOrderItem = new OrderItemDto(order.getOrderItems().get(0));
        this.itemName = stanOrderItem.getItem().getName();
        this.orderPrice = stanOrderItem.getOrderPrice();
        this.count = stanOrderItem.getCount();
    }
}
