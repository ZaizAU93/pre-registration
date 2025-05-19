package com.example.scammer.controllers;

import com.example.scammer.Request;
import com.example.scammer.repo.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prentry")
public class PrentryJournal {

    @Autowired
    private RequestRepository requestRepository;
    @GetMapping
    public String showEntries(
            @RequestParam(name = "preentryid", required = false) Long preentryid,
            @RequestParam(name = "receiptdate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date receiptdate,
            @RequestParam(name = "purposeid", required = false) Integer purposeid,
            @RequestParam(name = "info", required = false) String info,
            @RequestParam(name = "phone", required = false) String phone,
            @RequestParam(name = "datein", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date datein,
            @RequestParam(name = "entrystate", required = false) Integer entrystate,
            @RequestParam(name = "regcode", required = false) Integer regcode,
            Model model) {

        List<Request> requests = requestRepository.findByFilters(preentryid, receiptdate, purposeid, info, phone, datein, regcode, entrystate);
        // Передача списка запросов
        model.addAttribute("requests", requests);
        // Передача параметров в модель для заполнения формы
        model.addAttribute("preentryid", preentryid);
        model.addAttribute("receiptdate", receiptdate);
        model.addAttribute("purposeid", purposeid);
        model.addAttribute("info", info);
        model.addAttribute("phone", phone);
        model.addAttribute("datein", datein);
        model.addAttribute("entrystate", entrystate);
        model.addAttribute("regcode", regcode);
        return "prentry"; // имя шаблона
    }
}
