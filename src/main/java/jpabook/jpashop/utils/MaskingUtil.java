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
    public static void maskingDto(Object obj) {
        if (Objects.isNull(obj)) return;

        if (obj instanceof Collection) {
			((Collection<?>) obj).forEach(MaskingUtil::mask);
		} else if (obj.getClass().isArray()) {
			Arrays.stream((Object[])obj).forEach(MaskingUtil::mask);
		} else {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (!field.isAnnotationPresent(Masking.class) || !field.getType().equals(String.class))
                    continue;
                maskingField(field, obj);
            }
        }
    }

    private static void maskingField(Field field, Object dto) {
        Masking annotation = field.getAnnotation(Masking.class);
        field.setAccessible(true);
        try {
            field.set(dto, maskingString(MaskingType.valueOf(annotation.type().name()), String.valueOf(field.get(dto))));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
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
            switch (maskingType) {
                case NAME: {
                    if (str.length() > 2)
                        result = str.replaceAll("(?<=.{1,2}).(?=.{1,2})", maskingEntity.getReplacement());
                    else
                        result = str.replaceAll("(?<=.{1,2}).(?=.{0,2})", maskingEntity.getReplacement());
                    break;
                }
                case ADDRESS_BASE: {
                    StringJoiner sj = new StringJoiner(" ");
                    String[] splitStr = str.split("\\s");
                    if (splitStr.length > 2) {
                        sj.add(splitStr[0]).add(splitStr[1].replaceAll("(?<=.).", "*"));
                        Arrays.stream(splitStr).skip(2).forEach(el -> {
                            sj.add(el.replaceAll(".", "*"));
                        });
                    } else {
                        result = str;
                    }
                    break;
                }
            }
        }
        return result;
    }
}
