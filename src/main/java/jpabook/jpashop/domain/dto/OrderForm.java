package jpabook.jpashop.domain.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class OrderForm {
    private Long memberId;
    private Long itemId;
    private int count;
}
