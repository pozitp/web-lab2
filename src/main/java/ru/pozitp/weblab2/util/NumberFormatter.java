package ru.pozitp.weblab2.util;

import java.math.BigDecimal;

public class NumberFormatter {
    private NumberFormatter() {
        throw new IllegalStateException("Utility class");
    }

    public static String format(BigDecimal number) {
        if (number == null) {
            return "";
        }
        return number.toPlainString();
    }
}
