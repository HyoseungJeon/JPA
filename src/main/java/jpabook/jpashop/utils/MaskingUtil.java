package jpabook.jpashop.utils;

import jpabook.jpashop.annotation.Masking;
import jpabook.jpashop.constant.MaskingType;
import jpabook.jpashop.entity.MaskingEntity;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class MaskingUtil {

    /**
     * Dto class 마스킹 처리
     *
     * @param dto
     * @return 성공여부
     */
    public static boolean maskingDto(Object dto) {
        Field[] fields = dto.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Masking.class) || field.getType().equals(String.class))
                break;
            maskingField(field, dto);
        }

        return true;
    }

    private static void maskingField(Field field, Object dto) {
        Masking annotation = field.getAnnotation(Masking.class);
        field.setAccessible(true);
        try {
            field.set(dto, maskingString(MaskingType.valueOf(annotation.type().name()), String.valueOf(field.get(dto))));
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            field.setAccessible(false);
            throw new RuntimeException(e);
        }
    }

    private static String maskingString(MaskingType maskingType, String str) {
        String result = "";
        MaskingEntity maskingEntity = MaskingType.map.get(maskingType.name());

        if("null".equals(str))
            return result;

        if(Objects.nonNull(maskingEntity) && StringUtils.hasText(maskingEntity.getRegex())) {
            result = str.replaceAll(maskingEntity.getRegex(), maskingEntity.getReplacement());
        } else {
            StringJoiner stringJoiner = new StringJoiner(" ");
            switch (maskingType) {
            }
        }
        return result;
    }
}
