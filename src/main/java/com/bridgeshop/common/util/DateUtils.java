package com.bridgeshop.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDateTime convertToLocalDateTime(String strDate, DateTimeFormatter formatter, boolean isEndDate) {
        if (isEndDate) {
            return (strDate != null && !strDate.equals("")) ? LocalDate.parse(strDate, formatter).atTime(23, 59, 59) : null;
        } else {
            return (strDate != null && !strDate.equals("")) ? LocalDate.parse(strDate, formatter).atStartOfDay() : null;
        }
    }
}
