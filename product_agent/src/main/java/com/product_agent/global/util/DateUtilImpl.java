package com.product_agent.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class DateUtilImpl implements DateUtil {

    @Override
    public String getCurrentDateTime() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss", Locale.KOREA));
    }

    @Override
    public String getCurrentDate() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA));
    }
}
