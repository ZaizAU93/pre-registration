package com.example.controllers;

import com.example.DTO.ReportDTO;
import com.example.model.Report;
import com.example.repo.TicketRepo;
import com.example.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private TicketRepo ticketRepo;

    @PostMapping
    public ResponseEntity<String> saveReport(@RequestBody ReportDTO request) {

        System.out.println("Что приходит на сервер " + request.getContent() + " " + request.getId());

        Report reportNew = Report.builder()
                .content(request.getContent())
                .ticketId(request.getId())
                .build();

        System.out.println("репорт перед сохранением  " + reportNew.getContent() + " " + reportNew.getTicketId());

        try {
            reportService.save(reportNew);
            Long reportId = reportService.findReport(reportNew);
            System.out.println("id сохраненного отчета :" + reportId);
            ticketRepo.updateReport(reportId, request.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при сохранении отчета");
        }

        return ResponseEntity.ok("Отчет успешно сохранен, заявка закрыта");
    }
}
