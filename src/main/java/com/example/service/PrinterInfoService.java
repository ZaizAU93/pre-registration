package com.example.service;
import org.springframework.stereotype.Service;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;

@Service
public class PrinterInfoService {

    public String getPrintInfo() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);

        // Получаем принтер по умолчанию
        PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
        String defaultPrinterName = defaultPrinter != null ? defaultPrinter.getName() : "Нет принтера по умолчанию";

        return defaultPrinterName;
    }
}


