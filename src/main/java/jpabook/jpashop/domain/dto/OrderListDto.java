package jpabook.jpashop.domain.dto;

import jpabook.jpashop.domain.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
public class OrderListDto {
    private Long id;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private MemberDto member;

    private List<OrderItemDto> orderItems = new ArrayList<>();

    public OrderListDto(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.status = order.getStatus();
        this.member = new MemberDto(order.getMember());
        this.orderItems = order.getOrderItems().stream().map(item ->
            new OrderItemDto(item)).collect(Collectors.toList());
    }
}
