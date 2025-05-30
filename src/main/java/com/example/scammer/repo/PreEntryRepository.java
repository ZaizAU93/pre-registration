package com.example.scammer.repo;

import com.example.scammer.Booking;
import com.example.scammer.DTO.UserDTO;
import com.example.scammer.Registrar;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
public class PreEntryRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private  RegistratorRepo registratorRepo;
    @PersistenceContext
    private EntityManager em;

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
                //  java.sql.Date.valueOf(booking.getReceiptDate()), // p_receiptdate
                booking.getReceiptDate(), // p_receiptdate
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


    public int addPreEntryAPIExemple(Booking booking) {
        List<SqlParameter> parameters = new ArrayList<>();
        // Входные параметры, их порядок должен совпадать с порядком в PL/SQL
        parameters.add(new SqlParameter("p_receiptdate", Types.DATE));
        parameters.add(new SqlParameter("p_customername", Types.VARCHAR));
        parameters.add(new SqlParameter("p_purposeid", Types.NUMERIC));
        parameters.add(new SqlParameter("p_info", Types.VARCHAR));
        parameters.add(new SqlParameter("p_phone", Types.VARCHAR));
        parameters.add(new SqlParameter("p_regcode", Types.NUMERIC));
        parameters.add(new SqlParameter("p_departmentuid", Types.NUMERIC));
        parameters.add(new SqlParameter("p_phonenum", Types.VARCHAR));
        parameters.add(new SqlParameter("p_purposecode", Types.NUMERIC));
        parameters.add(new SqlParameter("p_ATENUMBER", Types.NUMERIC));
        parameters.add(new SqlParameter("p_STREETUID", Types.NUMERIC));
        parameters.add(new SqlParameter("p_BUILDNUM", Types.NUMERIC));
        parameters.add(new SqlParameter("p_BUILDINDEX", Types.VARCHAR));
        parameters.add(new SqlParameter("p_CORPNUM", Types.VARCHAR));
        parameters.add(new SqlParameter("p_CORPINDEX", Types.VARCHAR));
        parameters.add(new SqlParameter("p_FLATNUM", Types.NUMERIC));
        parameters.add(new SqlParameter("p_FLATINDEX", Types.VARCHAR));
        parameters.add(new SqlParameter("p_OTHERINFO", Types.NUMERIC));
        parameters.add(new SqlParameter("p_SSNAME", Types.VARCHAR));
        parameters.add(new SqlParameter("p_COMMENTS", Types.VARCHAR));
        parameters.add(new SqlParameter("p_action", Types.NUMERIC));
        parameters.add(new SqlParameter("p_SUBPURPOSEID", Types.NUMERIC));
        parameters.add(new SqlParameter("p_state", Types.NUMERIC));
        parameters.add(new SqlParameter("p_useruid", Types.NUMERIC));
        // OUT-параметр
        parameters.add(new SqlOutParameter("p_preentryid", Types.NUMERIC));

        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call RSDS600.PREENTRY_PKG.ADDENTRYAPI(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
                CallableStatement cs = con.prepareCall(storedProc);

                int index = 1;
                // Установка входных параметров
          //      cs.setDate(index++, java.sql.Date.valueOf(booking.getReceiptDate()));
                cs.setTimestamp(index++, java.sql.Timestamp.valueOf(booking.getReceiptDate()));
                cs.setString(index++, booking.getCustomerName());
                cs.setBigDecimal(index++, BigDecimal.valueOf(booking.getPurposeId()));
                cs.setString(index++, booking.getInfo());
                cs.setString(index++, removeNonDigits(booking.getPhone()));
                cs.setBigDecimal(index++, BigDecimal.valueOf(booking.getRegCode()));
                cs.setBigDecimal(index++, BigDecimal.ONE); // p_departmentuid фиксировано как 1
                cs.setString(index++, removeNonDigits(booking.getPhone()));
                cs.setBigDecimal(index++, BigDecimal.valueOf(100)); // p_purposecode
                cs.setObject(index++, null, Types.NUMERIC); // p_ATENUMBER null
                cs.setObject(index++, null, Types.NUMERIC); // p_STREETUID null
                cs.setObject(index++, null, Types.NUMERIC); // p_BUILDNUM null
                cs.setNull(index++, Types.VARCHAR); // p_BUILDINDEX null
                cs.setNull(index++, Types.VARCHAR); // p_CORPNUM null
                cs.setNull(index++, Types.VARCHAR); // p_CORPINDEX null
                cs.setObject(index++, null, Types.NUMERIC); // p_FLATNUM null
                cs.setNull(index++, Types.VARCHAR); // p_FLATINDEX null
                cs.setObject(index++, null, Types.NUMERIC); // p_OTHERINFO null
                cs.setNull(index++, Types.VARCHAR); // p_SSNAME null
                cs.setNull(index++, Types.VARCHAR); // p_COMMENTS null
                cs.setBigDecimal(index++, BigDecimal.valueOf(1)); // p_action
                cs.setNull(index++, Types.NUMERIC); // p_SUBPURPOSEID null
                cs.setBigDecimal(index++, BigDecimal.ZERO); // p_state
                cs.setBigDecimal(index++, BigDecimal.valueOf(booking.getUserUid())); // p_useruid

                // Регистрация OUT-параметра
                cs.registerOutParameter(index, Types.NUMERIC);

                return cs;
            }
        }, parameters);

        // Получение OUT-параметра
        Object preentryIdObj = result.get("p_preentryid");
        if (preentryIdObj != null) {
            if (preentryIdObj instanceof BigDecimal) {
                return ((BigDecimal) preentryIdObj).intValue();
            } else if (preentryIdObj instanceof Number) {
                return ((Number) preentryIdObj).intValue();
            } else {
                return Integer.parseInt(preentryIdObj.toString());
            }
        } else {
            return 0; // или выбросить исключение
        }
    }

    public void updateState(Integer prentryId) {
        System.out.println("User UID: " + prentryId);

        String sql = "{call RSDS600.PREENTRY_PKG.addresultapi(?, ?, ?, ?)}";

        jdbcTemplate.update(sql,
                prentryId, // id записи
                600,
                "",
                2 // код статуса
        );
    }


    @Transactional
    public void replaceRegCodePrentry(Integer regCode, Long prentryId) {
        String jpql = "UPDATE Request e SET e.regcode = :newRegCode WHERE e.preentryid = :preentryid";

        em.createQuery(jpql)
                .setParameter("newRegCode", regCode)
                .setParameter("preentryid", prentryId)
                .executeUpdate();
    }

    @Transactional
    public void replaceRegCodeTimeSlot(Integer regCode, Long prentryId) {

        Registrar registrar = registratorRepo.findByRegCode(regCode.toString());

        String jpql = "UPDATE TimeSlot t SET t.registrar = :newRegistrar WHERE t.prentryUid = :prentryUid";

        // Создаем и настраиваем запрос
        em.createQuery(jpql)
                .setParameter("newRegistrar", registrar)
                .setParameter("prentryUid", prentryId)
                .executeUpdate();
    }

}