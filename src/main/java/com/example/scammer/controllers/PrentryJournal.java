package com.example.scammer.controllers;

import com.example.scammer.DTO.UpdateRegistrarRequest;
import com.example.scammer.Request;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.*;
import com.example.scammer.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/prentry")
public class PrentryJournal {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private PreEntryRepository preEntryRepository;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private RegistratorRepo registratorRepo;
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

        // Если entrystate не передан, по умолчанию установим 0 (новые)
           if (entrystate == null) {
            entrystate = 0;
        }
        Date startOfDay = null;
        Date startOfNextDay = null;
        if (receiptdate !=null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(receiptdate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
             startOfDay = cal.getTime();

            cal.add(Calendar.DAY_OF_MONTH, 1);
             startOfNextDay = cal.getTime();
        }


        Date startOfDayDataIn = null;
        Date startOfNextDayDataIN = null;
        if (datein !=null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(datein);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            startOfDayDataIn = cal.getTime();

            cal.add(Calendar.DAY_OF_MONTH, 1);
            startOfNextDayDataIN = cal.getTime();
        }



        LocalDate localDate = LocalDate.of(2025, 1, 1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());



        List<Request> requests = requestRepository.findByFilters(
                preentryid, startOfDay, startOfNextDay, purposeid, info, phonenum, regcode, entrystate, registratorName, customername, date, startOfDayDataIn, startOfNextDayDataIN );
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

     //   timeSlotService.updateStateTimeSlot(timeSlot, true); //освобождение таймслота регистратора после отмены записи в журнале

        timeSlot.setPrentryUid(null); // очистить поле
        timeSlot.setFree(true);
        timeSlotRepository.save(timeSlot); // сохранить изменения

        bookingRepository.deleteBooking(timeSlot.getId());

        return ResponseEntity.ok("Запись успешно отменена");
    }

    //  @PostMapping("/replace")
    @ResponseBody
    public ResponseEntity<String> replaceRegistra(@RequestParam Integer id) {

        preEntryRepository.updateState(id);

        TimeSlot timeSlot = timeSlotService.getTimeSlotByPrentryUID(id);

        timeSlotService.updateStateTimeSlot(timeSlot, true); //освобождение таймслота регистратора после отмены записи в журнале

        return ResponseEntity.ok("Запись успешно отменена");
    }


    @PostMapping("/update-registrar")
    public ResponseEntity<?> updateRegistrar(@RequestBody UpdateRegistrarRequest request) {
        Optional<Request> optionalRequest = requestRepository.findById(request.getPreentryId());
        if (!optionalRequest.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Запись не найдена");
        }

        String newRegCodeStr = request.getNewRegCode();
        if (newRegCodeStr != null && newRegCodeStr.equals("-1")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Данный пользователь не является регистратором");
        }

        // Остальной код
        int newRegCode = Integer.parseInt(newRegCodeStr);
        preEntryRepository.replaceRegCodePrentry(newRegCode, request.getPreentryId());
        preEntryRepository.replaceRegCodeTimeSlot(newRegCode, request.getPreentryId());

        return ResponseEntity.ok().build();
    }

}

