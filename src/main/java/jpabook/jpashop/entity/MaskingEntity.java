package jpabook.jpashop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MaskingEntity {

    private String regex;

    private String replacement;

}
