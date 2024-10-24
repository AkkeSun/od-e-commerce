package com.sweettracker.account.global.util;

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
}
