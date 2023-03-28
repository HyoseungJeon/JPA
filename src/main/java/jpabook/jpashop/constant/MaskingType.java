package jpabook.jpashop.constant;


import jpabook.jpashop.entity.MaskingEntity;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Masking 처리 위한 Type 정의 enum class
 *
 * regex: String 정규식
 * replacement: String 치환문자열
 * 
 * 단, regex empty 시 정규식 아닌 custom rule 존재
 * -> MaskingUtil 내 정의
 */
@AllArgsConstructor
public enum MaskingType {
    NAME("", "*"),
    EMAIL("(?<=.{4}).(?=.*@)", "*"),
    ZIPCODE("(?<=.{3}.", "*"),
    PHONE("(\\d{2,3})-?(\\d{1,2})(\\d{2})-?(\\d{1})(\\d{3})$", "$1$2**$4***"),
    ADDRESS_BASE("", "*"),
    ADDRESS_DETAIL(".", "*")
    ;
    private final String regex;

    private final String replacement;

    /* Map 형태로 반환 */
    public static final Map<String, MaskingEntity> map = Arrays.stream(values()).collect(
            Collectors.toMap(String::valueOf, el -> new MaskingEntity(el.regex, el.replacement))
    );
}
