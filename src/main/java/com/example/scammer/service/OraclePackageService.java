package com.example.scammer.service;
import com.example.scammer.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class OraclePackageService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OraclePackageService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void callAddEntryProcedure(Booking booking) {
        MapSqlParameterSource params = prepareParameters(booking);

        String sql = """
            BEGIN RSDS600.PREENTRY_PKG.addentry(
                p_receiptdate   => :p_receiptdate,
                p_customername  => :p_customername,
                p_purposeid    => :p_purposeid,
                p_info         => :p_info,
                p_phone        => :p_phone,
                p_regcode      => :p_regcode,
                p_departmentuid => :p_departmentuid,
                p_phonenum     => :p_phonenum,
                p_purposecode  => :p_purposecode,
                p_atenumber    => :p_atenumber,
                p_streetuid    => :p_streetuid,
                p_buildnum     => :p_buildnum,
                p_buildindex   => :p_buildindex,
                p_corpnum      => :p_corpnum,
                p_corpindex    => :p_corpindex,
                p_flatnum      => :p_flatnum,
                p_flatindex    => :p_flatindex,
                p_otherinfo    => :p_otherinfo,
                p_ssname      => :p_ssname,
                p_comments     => :p_comments,
                p_action      => :p_action,
                p_subpurposeid => :p_subpurposeid,
                p_state        => :p_state
            ); END;
        """;

        jdbcTemplate.update(sql, params);
    }

    private MapSqlParameterSource prepareParameters(Booking booking) {
        return new MapSqlParameterSource()
                .addValue("p_receiptdate", booking.getReceiptDate())
                .addValue("p_customername", booking.getCustomerName())
                .addValue("p_purposeid", booking.getPurposeId())
                .addValue("p_info", booking.getInfo())
                .addValue("p_phone", booking.getPhone())
                .addValue("p_regcode", booking.getRegCode())
                .addValue("p_departmentuid", 600)
                .addValue("p_phonenum", booking.getPhone()) // Используем тот же телефон
                .addValue("p_purposecode", null)
                .addValue("p_atenumber", null)
                .addValue("p_streetuid", null)
                .addValue("p_buildnum", null)
                .addValue("p_buildindex", null)
                .addValue("p_corpnum", null)
                .addValue("p_corpindex", null)
                .addValue("p_flatnum", null)
                .addValue("p_flatindex", null)
                .addValue("p_otherinfo", null) // Дублируем info
                .addValue("p_ssname", null)
                .addValue("p_comments", null)
                .addValue("p_action", null)
                .addValue("p_subpurposeid", null)
                .addValue("p_state", null);
    }

}