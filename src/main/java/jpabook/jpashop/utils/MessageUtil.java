package jpabook.jpashop.utils;

import jpabook.jpashop.domain.dto.SmsRequestDto;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * !!Template Rule!!
 * 줄바꿈 문자열을 기준으로 각 line 생성
 *
 * 커맨드 옵션
 * $ : required, 없을 시 예외 발생
 * # : not required, 없을 시 Line 삭제
 * @ : not required, 단순 노출 (empty 출력 가능)
 *
 * Example form : ${fieldName}
 * Exception form : ${{test}, $}, ${test~~~, #{}(empty fieldName)
 */

public class MessageUtil {

    private String replaceValAreaMsg(SmsRequestDto smsRequestDto, Object obj) {
        final String line_start = "<div><color>";
        final String line_end = "</color></div>";

        String msg = "";
        Map<String, String> map = new HashMap<>();
        map.put("MSG_001", "Test 문구 #{test}");

        msg = map.get(smsRequestDto.getMessageClCd());

        StringBuilder tempSb = new StringBuilder();
        StringBuilder messageSb = new StringBuilder();

        String commandOpt = null;

        try {
            int sIndex, eIndex = 0;

            tempSb.append(line_start);
            String fieldName, fieldValue = "";

            for (int i = 0 ; i < msg.length(); i++) {
                char iChar = msg.charAt(i);
                if ("$#@".contains(String.valueOf(iChar))) {
                    commandOpt = String.valueOf(iChar);
                    tempSb.append(commandOpt);
                }
                else if ('{' == (iChar)) {
                    sIndex = i + 1;
                    eIndex = msg.indexOf('}', sIndex);

                    // 필수값 여부 option 없는 경우
                    Optional.ofNullable(commandOpt).orElseThrow(() -> new InvalidPropertiesFormatException("Open brace have to got command option"));
                    tempSb.setLength(tempSb.length() - 1);

                    // 닫는 중괄호가 없는 경우
                    if (eIndex == -1) throw new InvalidPropertiesFormatException("Closing brace is missing");
                    fieldName = msg.substring(sIndex, eIndex);

                    // 중괄호가 안맞는 경우 (Ex : {{fieldName})
                    if (fieldName.contains("{")) throw new InvalidPropertiesFormatException("Brace is overlapping, brace must have to matched");

                    // 필드명이 없는 경우
                    if (!StringUtils.hasText(fieldName)) throw new InvalidPropertiesFormatException("FieldName is empty");

                    fieldValue = this.getFieldValue(fieldName, obj);

                    // 필드값이 없는 경우
                    if (!StringUtils.hasText(fieldValue)) {
                        // 필수값 옵션 일 때
                        if ("$".contains(commandOpt)) throw new NoSuchElementException(fieldName + " value is empty");
                        // 라인 삭제 옵션
                        else if ("#".contains(commandOpt)) {
                            tempSb.setLength(0);
                            commandOpt = null;
                            int nextLineIndex = msg.indexOf('\n', sIndex);
                            i = nextLineIndex != -1 ? nextLineIndex : msg.length();
                            continue;
                        }
                    }
                    tempSb.append(fieldValue);
                    i = eIndex;

                    commandOpt = null;
                } else if ('\n' == (iChar)) {
                    tempSb.append(line_end);
                    tempSb.append(line_start);

                    messageSb.append(tempSb);
                    tempSb.setLength(0);
                } else if ('}' != iChar) {
                    commandOpt = null;
                    tempSb.append(iChar);
                } else throw new InvalidPropertiesFormatException("Can't read format, messageFormat is " + msg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 마지막 line start div 제거
        messageSb.setLength(messageSb.length() - line_start.length());

        return messageSb.toString();
    }

    private String getFieldValue(String fieldName, Object obj) throws IllegalAccessException {
        String fieldValue = "";
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (fieldName.equals(field.getName())) {
                fieldValue = Optional.ofNullable(field.get(obj)).orElse("").toString();
                field.setAccessible(false);
                break;
            }
            field.setAccessible(false);
        }

        return fieldValue;
    }
}
