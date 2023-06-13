package jpabook.jpashop.domain.dto;


import lombok.Data;

@Data
public class SmsRequestDto {

    private String msg;

    private String phone;

    private String messageClCd;
}
