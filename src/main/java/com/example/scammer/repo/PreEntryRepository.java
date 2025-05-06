package com.example.scammer.repo;

import com.example.scammer.Booking;
import com.example.scammer.DTO.UserDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PreEntryRepository {
    private final JdbcTemplate jdbcTemplate;

    public PreEntryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static String removeNonDigits(String input) {
        if (input == null) return "";
        return input.replaceAll("[^0-9]", "");
    }


    public void addPreEntry(Booking booking) {
        String sql = "{call RSDS600.PREENTRY_PKG.addentry(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        jdbcTemplate.update(sql,
                java.sql.Date.valueOf(booking.getReceiptDate()), // p_receiptdate
                booking.getCustomerName(),                   // p_customername
                booking.getPurposeId(),                                        // p_purposeid
                booking.getInfo(),             // p_info
                removeNonDigits(booking.getPhone()),                          // p_phone
                booking.getRegCode(),                                      // p_regcode (код регистратора
                1,                                        // p_departmentuid (ID отдела)
                removeNonDigits(booking.getPhone()),                          // p_phonenum
                100,                                      // p_purposecode
                null,                                    // p_ATENUMBER
                null,                                      // p_STREETUID
                null,                                       // p_BUILDNUM
                null,                                      // p_BUILDINDEX
                null,                                        // p_CORPNUM
                null,                                      // p_CORPINDEX
                null,                                       // p_FLATNUM
                null,                                      // p_FLATINDEX
                null,                                      // p_OTHERINFO
                null,                                   // p_SSNAME
                null,                                       // p_COMMENTS
                5,                                        // p_action
                null                                      // p_SUBPURPOSEID (null, т.к. purposeid=3)
                // p_state не передаем (по умолчанию 0)
        );
    }


    public UserDTO getUser(String username) {
        String sql = "SELECT * FROM rsds600.users WHERE dbuser = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserDTO.class), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public void addPreEntryAPI(Booking booking) {
        System.out.println("User UID: " + booking.getUserUid());

        String sql = "{call RSDS600.PREENTRY_PKG.addentryAPI(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        jdbcTemplate.update(sql,
                java.sql.Date.valueOf(booking.getReceiptDate()), // p_receiptdate
                booking.getCustomerName(),                   // p_customername
                booking.getPurposeId(),                                        // p_purposeid
                booking.getInfo(),             // p_info
                removeNonDigits(booking.getPhone()),                          // p_phone
                booking.getRegCode(),                                      // p_regcode (код регистратора
                1,                                        // p_departmentuid (ID отдела)
                removeNonDigits(booking.getPhone()),                          // p_phonenum
                100,                                      // p_purposecode
                null,                                    // p_ATENUMBER
                null,                                      // p_STREETUID
                null,                                       // p_BUILDNUM
                null,                                      // p_BUILDINDEX
                null,                                        // p_CORPNUM
                null,                                      // p_CORPINDEX
                null,                                       // p_FLATNUM
                null,                                      // p_FLATINDEX
                null,                                      // p_OTHERINFO
                null,                                   // p_SSNAME
                null,                                       // p_COMMENTS
                5,                                             // p_action
                null,                                          // p_SUBPURPOSEID
                0,                                // p_state
                booking.getUserUid()                           // p_useruid
        );
    }



}