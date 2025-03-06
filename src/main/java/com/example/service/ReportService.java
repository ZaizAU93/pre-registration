package com.example.service;

import com.example.model.Report;
import com.example.repo.ReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

    @Autowired
    private ReportRepo reportRepo;

    public Report save(Report report) {
            return reportRepo.save(report);
    }

    public Long findReport(Report report){
        return reportRepo.findByTicketId(report.getTicketId()).getId();
    }
}
