package com.example.controllers;

import com.example.service.PrinterInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.print.PrintService;

@Controller
public class PrinterInfoController {

    @Autowired
    private PrinterInfoService printerInfoService;


    @GetMapping("/printer")
    @ResponseBody
    public void jobPrinter(){

    printerInfoService.getPrintInfo();


    }

}
