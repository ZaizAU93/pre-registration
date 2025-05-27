package com.example.scammer.service;
import com.example.scammer.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


@Repository
public class OraclePackageService {

    private final SimpleJdbcCall addEntryProcedure;

    @Autowired
    public OraclePackageService(DataSource dataSource) {
        this.addEntryProcedure = new SimpleJdbcCall(dataSource)
                .withCatalogName("RSDS600.PREENTRY_PKG")
                .withProcedureName("addentry");
    }

    public void callAddEntryProcedure(Booking booking) {
        try {
            Map<String, Object> params = prepareParameters(booking);
            addEntryProcedure.execute(params);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка вызова процедуры addentry", e);
        }
    }


    private Map<String, Object> prepareParameters(Booking booking) {
        // Валидация обязательных полей
        validateBooking(booking);

        // Обработка номера телефона
        String cleanedPhone = processPhoneNumber(booking.getPhone());

        Map<String, Object> params = new HashMap<>();

        // Обязательные параметры
    //    params.put("p_receiptdate", java.sql.Date.valueOf(booking.getReceiptDate()));
        params.put("p_regokrug",600);

        params.put("p_customername", booking.getCustomerName());
        params.put("p_purposeid", booking.getPurposeId());
        params.put("p_phone", cleanedPhone);
        params.put("p_regcode", booking.getRegCode());
        params.put("p_departmentuid", 1);
        params.put("p_phonenum", cleanedPhone);


        // Необязательные параметры (NULL)
        addNullableParameters(params);

        return params;
    }

    private void validateBooking(Booking booking) {
        if (booking.getReceiptDate() == null) {
            throw new IllegalArgumentException("Receipt date is required");
        }
        if (booking.getCustomerName() == null || booking.getCustomerName().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (booking.getPurposeId() == null) {
            throw new IllegalArgumentException("Purpose ID is required");
        }
        if (booking.getPhone() == null || booking.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (booking.getRegCode() == null) {
            throw new IllegalArgumentException("Reg code is required");
        }
    }

    private String processPhoneNumber(String phone) {
        String cleaned = removeNonDigits(phone);
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        // Обрезаем до 12 символов для поля PHONENUM
        return cleaned.length() > 12 ? cleaned.substring(0, 12) : cleaned;
    }

    private static String removeNonDigits(String input) {
        if (input == null) return "";
        return input.replaceAll("[^0-9]", "");
    }

    private void addNullableParameters(Map<String, Object> params) {
        // Все NULL-параметры
        String[] nullParams = {
                "p_purposecode", "p_atenumber", "p_streetuid", "p_buildnum",
                "p_buildindex", "p_corpnum", "p_corpindex", "p_flatnum",
                "p_flatindex", "p_otherinfo", "p_ssname", "p_comments",
                "p_subpurposeid"
        };

        for (String param : nullParams) {
            params.put(param, null);
        }
    }


}


