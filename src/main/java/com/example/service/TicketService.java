package com.example.service;

import com.example.model.Computer;
import com.example.model.Status;
import com.example.model.Ticket;
import com.example.model.User;
import com.example.repo.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private UserService userService;


    public Long createTicket(Ticket ticket, Computer computer) {

        Ticket ticketNew = Ticket.builder()
                .description(ticket.getDescription())
                .createdAt(LocalDateTime.now())
                .problems(ticket.getProblems())
                .status(Status.NEW)
                .userId(userService.getCurrentUser().getId())
                .computer(computer)
                .build();

         ticketRepo.save(ticketNew);
        return ticketNew.getId();
    }

    public List<Ticket> getAllTickets() {
        return ticketRepo.findAll();
    }

    public Ticket getTiketById(Long id){
        return ticketRepo.getTicketById(id);
    }


    // поиск по id администратора и статусу в работе
    public List<Optional<Ticket>> getTiketByIdAndStatus(Long id, Status status){
        return ticketRepo.findTicketByUserIdAndStatus(id, status);
    }

    public List<Ticket> findByUser(User user){
        return ticketRepo.findByUserId(user.getId());
    }

    public List<Long> searchAdminAllTicketInStatusInprogress(Long id, Status status){

        List<Optional<Ticket>> listAdmin = new ArrayList<>( getTiketByIdAndStatus(id, status));

        ArrayList<Long> idAdmin = new ArrayList<>();

        for (Optional<Ticket> ticket:listAdmin) {
           idAdmin.add(ticket.get().getUserId());
        }

        return idAdmin;
    }


// метод изменение статуса работам (статус, id тикета)
    public void jobs( Status status, Long id){
        User admin = userService.getCurrentUser();
        ticketRepo.jobs(status, admin.getId(), id);
    }

    public List<Optional<Ticket>> getStatusTicket(Status status){
        User admin = userService.getCurrentUser();
        return ticketRepo.ticketStatus(status, admin.getId());
    }


}
