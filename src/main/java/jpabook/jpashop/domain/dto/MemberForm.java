package jpabook.jpashop.domain.dto;

import jpabook.jpashop.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm extends Member {

    private String city;
    private String street;
    private String zipcode;
}
