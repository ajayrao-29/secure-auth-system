package com.secureauth.system.util;

import org.springframework.web.util.HtmlUtils;

public final class InputSanitizer {

    private InputSanitizer() {
    }

    public static String sanitize(String value) {
        if (value == null) {
            return null;
        }
        return HtmlUtils.htmlEscape(value.trim());
    }

    public static String sanitizeEmail(String value) {
        return sanitize(value).toLowerCase();
    }
}
