package com.example.scammer.controllers;

import com.example.scammer.Request;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.PreEntryRepository;
import com.example.scammer.repo.RequestRepository;
import com.example.scammer.repo.TimeSlotRepository;
import com.example.scammer.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prentry")
public class PrentryJournal {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PreEntryRepository preEntryRepository;

    @Autowired
    private TimeSlotService timeSlotService;
    @GetMapping
    public String showEntries(
            // существующие параметры
            @RequestParam(name = "preentryid", required = false) Long preentryid,
            @RequestParam(name = "receiptdate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date receiptdate,
            @RequestParam(name = "purposeid", required = false) Integer purposeid,
            @RequestParam(name = "info", required = false) String info,
            @RequestParam(name = "phonenum", required = false) String phonenum,
            @RequestParam(name = "datein", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date datein,
            @RequestParam(name = "entrystate", required = false) Integer entrystate,
            @RequestParam(name = "regcode", required = false) Integer regcode,
            // добавляем параметр поиска по фамилии регистратора
            @RequestParam(name = "registratorName", required = false) String registratorName,
            @RequestParam(name = "customername", required = false) String customername,
            Model model) {

        List<Request> requests = requestRepository.findByFilters(
                preentryid, receiptdate, purposeid, info, phonenum, datein, regcode, entrystate, registratorName, customername);
        // Передача списка запросов
        model.addAttribute("requests", requests);
        // Передача параметров в модель для заполнения формы
        model.addAttribute("preentryid", preentryid);
        model.addAttribute("receiptdate", receiptdate);
        model.addAttribute("purposeid", purposeid);
        model.addAttribute("info", info);
        model.addAttribute("phonenum", phonenum);
        model.addAttribute("datein", datein);
        model.addAttribute("entrystate", entrystate);
        model.addAttribute("regcode", regcode);
        model.addAttribute("registratorName", registratorName);
        return "prentry"; // имя шаблона
    }


    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateState(@RequestParam Integer id) {

        preEntryRepository.updateState(id);

        TimeSlot timeSlot = timeSlotService.getTimeSlotByPrentryUID(id);

        timeSlotService.updateStateTimeSlot(timeSlot, true); //освобождение таймслота регистратора после отмены записи в журнале

        return ResponseEntity.ok("Запись успешно отменена");
    }


}
