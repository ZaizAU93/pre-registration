package com.example.scammer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "preentryjournal", schema = "rsds600")
@Entity
@Component
public class Request {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preentryid")
    private Long preentryid;
    @Column(name = "regokrug")
    private int regokrug;
    @Column(name = "receiptdate")
    @Temporal(TemporalType.TIMESTAMP) // Для java.util.Date
    private Date receiptdate;
    @Column(name = "purposeid")
    private Integer purposeid;
    @Column(name = "info")
    private String info;
    @Column(name = "phone")
    private String phone;
    @Column(name = "datein")
    private Date datein;
    @Column(name = "resultinfo")
    private String resultinfo;
    @Column(name = "resultdate")
    private Date resultdate;
    @Column(name = "editdate")
    private Date editdate;
    @Column(name = "entrystate")
    private Integer entrystate;
    @Column(name = "phonenum")
    private String phonenum;
    @Column(name = "password")
    private String password;
    @Column(name = "preentrynum")
    private String preentrynum;

    // @Column(name = "statetext")
   // private String statetext;

    //  @Column(name = "location")
    //private String location;

    @Column(name = "regcode")
    private Integer regcode;

    @Column(name = "customername")
    private String customername;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regcode", referencedColumnName = "regcode", insertable = false, updatable = false)
    private Users user;
}
