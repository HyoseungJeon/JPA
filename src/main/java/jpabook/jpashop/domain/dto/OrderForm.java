package jpabook.jpashop.domain.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class OrderForm {

    @Min(1)
    private Long memberId;
    @Min(1)
    private Long itemId;
    @Min(1)
    @Max(100)
    private int count;
}
