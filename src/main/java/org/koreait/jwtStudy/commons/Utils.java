package org.koreait.jwtStudy.commons;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.*;

public class Utils {
    private static ResourceBundle validationsBundle;
    private static ResourceBundle errorsBundle;

    static {
        validationsBundle = ResourceBundle.getBundle("messages.validations");
        errorsBundle = ResourceBundle.getBundle("messages.errors");
    }

    public static String getMessage(String code, String bundleType) {
        bundleType = Objects.requireNonNullElse(bundleType, "validation");
        ResourceBundle bundle = bundleType.equals("error")? errorsBundle:validationsBundle;
        try {
            return bundle.getString(code);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String, List<String>> getMessages(Errors errors) {

        //field가 key값, error가 value
        Map<String, List<String>> data = new HashMap<>();
        for (FieldError error : errors.getFieldErrors()) {
            String field = error.getField();

            // Not Blank, Not Blank.email, Not Blank.requestJon.email
            // 가장 긴 오류코드부터 적용되도록 역순으로 정렬한다.
            List<String> messages = Arrays.stream(error.getCodes()).sorted(Comparator.reverseOrder())
                    .map(c -> getMessage(c, "validation"))
                    .filter(c -> c != null)
                    .toList();

            data.put(field, messages);

        }
        return data;
    }
}
