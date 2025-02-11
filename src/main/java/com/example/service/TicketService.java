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
import java.util.stream.Collectors;

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
                .user(userService.getCurrentUser())
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
        return ticketRepo.findByUser(user);
    }

    public List<Long> searchAdminAllTicketInStatusInprogress(Long id, Status status){

        List<Optional<Ticket>> listAdmin = new ArrayList<>( getTiketByIdAndStatus(id, status));

        ArrayList<Long> idAdmin = new ArrayList<>();

        for (Optional<Ticket> ticket:listAdmin) {
           idAdmin.add(ticket.get().getUser().getId());
        }

        return idAdmin;
    }


// взять в работу
    public void hire(){



        return;
    }


}
