package ru.pozitp.weblab2.util;

import java.math.BigDecimal;

public class NumberFormatter {
    public static String format(BigDecimal number) {
        if (number == null) {
            return "";
        }
        return number.toPlainString();
    }
}
