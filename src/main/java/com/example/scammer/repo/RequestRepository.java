package com.example.scammer.repo;

import com.example.scammer.Registrar;
import com.example.scammer.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByPasswordAndPhonenumAndPreentrynum(String password, String phonenum, String preentrynum);

    Optional<Request> findByPhonenum(String phoneNum);
    Optional<Request> findByPassword(String password);
    Optional<Request> findByPreentryid(long preentrynum);

    Optional<Request> findByPreentryidAndRegokrug(long id, int regOkrug);


    @Query("SELECT COUNT(r) FROM Request r WHERE r.phonenum = :phone " +
            "AND r.datein BETWEEN :startDate AND :endDate")
    Integer findCountByPhoneAndDateRange(@Param("phone") String phone,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate);


    @Query("SELECT COUNT(r) FROM Request r WHERE r.phonenum = :phone " +
            "AND r.datein BETWEEN :startDate AND :endDate")
    Integer findBy(@Param("phone") String phone,
                                         @Param("startDate") Date startDate,
                                         @Param("endDate") Date endDate);


    @Query("SELECT r FROM Request r LEFT JOIN FETCH r.user u WHERE "
            + "(:preentryid IS NULL OR r.preentryid = :preentryid) AND "
            + "(:receiptdate IS NULL OR r.receiptdate = :receiptdate) AND "
            + "(:purposeid IS NULL OR r.purposeid = :purposeid) AND "
            + "(:info IS NULL OR r.info LIKE %:info%) AND "
            + "(:phonenum IS NULL OR r.phonenum LIKE %:phonenum%) AND "
            + "(:datein IS NULL OR r.datein = :datein) AND "
            + "(:regcode IS NULL OR r.regcode = :regcode) AND "
            + "(:entrystate IS NULL OR r.entrystate = :entrystate) AND "
            + "(:customername IS NULL OR r.customername LIKE %:customername%) AND "
            + "(:registratorName IS NULL OR u.USERNAME LIKE %:registratorName%)")
    List<Request> findByFilters(
            @Param("preentryid") Long preentryid,
            @Param("receiptdate") Date receiptdate,
            @Param("purposeid") Integer purposeid,
            @Param("info") String info,
            @Param("phonenum") String phonenum,
            @Param("datein") Date datein,
            @Param("regcode") Integer regcode,
            @Param("entrystate") Integer entrystate,
            @Param("registratorName") String registratorName,
            @Param("customername") String customername
    );

}
