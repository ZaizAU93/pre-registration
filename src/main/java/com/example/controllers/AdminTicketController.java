package com.example.controllers;

import com.example.model.Notification;
import com.example.model.Status;
import com.example.model.Ticket;
import com.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin/tickets")
public class AdminTicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JobTitleService jobTitleService;

    @Autowired
    private NotificationsService notificationsService;

    @GetMapping()
    public String getAdminTicketsInProgress(Model model) throws UnknownHostException {

        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("admin", userService.getCurrentUser());
        model.addAttribute("dep", departmentService.getDepById(userService.getCurrentUser().getDepartametId()).get());
        model.addAttribute("adminId", userService.getCurrentUser().getId());
        model.addAttribute("job", jobTitleService.getJobsById(userService.getCurrentUser().getJobTitleId()));
        model.addAttribute("department", userService.getCurrentUser().getDepartametId());

        model.addAttribute("allJobs", jobTitleService.getAllJobTitle());

        List<Notification> notifications = notificationsService.getAllNotificationsRecipient(userService.getCurrentUser().getId());
        List<Long> notificationIds = notifications.stream().map(Notification::getTicketId).collect(Collectors.toList());

        model.addAttribute("notificationIds", notificationIds); // передаем список ID уведомлений

        return "profAdmin"; // имяшаблона  Thymeleaf для админов
    }


    @GetMapping("/api/tickets")
    @ResponseBody
    public List<Ticket> getTickets() {

        List<Ticket> sameTicketList = ticketService.findTicketUserAndAdminDepartmentSame(userService.getCurrentUser().getId());


        List<Ticket> ticketsNew1 = ticketService.getTikecetStatusNew(Status.NEW);
        List<Ticket> ticketsNew = ticketService.searchSameTicket(ticketsNew1, sameTicketList);


        List<Ticket> ticketsInProgress1 = ticketService.getTikecetStatusProgResol(Status.IN_PROGRESS);
        List<Ticket> ticketsInProgress = ticketService.searchSameTicket(ticketsInProgress1, sameTicketList);


        List<Ticket> ticketResolved1 = ticketService.getTikecetStatusProgResol(Status.RESOLVED);
        List<Ticket> ticketResolved = ticketService.searchSameTicket(ticketResolved1, sameTicketList);



        // Здесь можно объединить все тикеты в один список для упрощения обработки на стороне клиента
        List<Ticket> allTickets = new ArrayList<>();
        allTickets.addAll(ticketsNew);
        allTickets.addAll(ticketsInProgress);
        allTickets.addAll(ticketResolved);

        return allTickets;
    }

}
