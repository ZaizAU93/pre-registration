package com.example.service;

import com.example.model.Computer;
import com.example.model.Status;
import com.example.model.Ticket;
import com.example.model.User;
import com.example.repo.TicketRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService implements Serializable {

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

    public List<Ticket> getTiketByUser(User user){
        return ticketRepo.getTicketByUser(user);
    }


    public Ticket getTiketById(Long id){
        return ticketRepo.getTicketById(id);
    }

    // поиск по id администратора и статусу в работе
    public List<Optional<Ticket>> getTiketByIdAndStatus(User user, Status status){
        return ticketRepo.findTicketByUserIdAndStatus(user, status);
    }

    public List<Ticket> findByUser(User user){
        return ticketRepo.findByUserId(user.getId());
    }

    public List<Long> searchAdminAllTicketInStatusInprogress(User user, Status status){

        List<Optional<Ticket>> listAdmin = new ArrayList<>( getTiketByIdAndStatus(user, status));

        ArrayList<Long> idAdmin = new ArrayList<>();

        for (Optional<Ticket> ticket:listAdmin) {
           idAdmin.add(ticket.get().getUser().getId());
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


    @PersistenceContext
    private EntityManager entityManager;
    public List<Ticket> getTikecetStatusProgResol(Status status) {

        Long admin = userService.getCurrentUser().getId();

        String jpql = "SELECT t FROM Ticket t WHERE t.status =:status and t.adminId =:admin";
        TypedQuery<Ticket> query = entityManager.createQuery(jpql, Ticket.class);
        query.setParameter("status", status );
        query.setParameter("admin", admin);

        return query.getResultList(); // Получение списка результатов
    }


    public List<Ticket> getTikecetStatusNew(Status status) {

        Long admin = userService.getCurrentUser().getId();

        String jpql = "SELECT t FROM Ticket t WHERE t.status =:status";
        TypedQuery<Ticket> query = entityManager.createQuery(jpql, Ticket.class);
        query.setParameter("status", status );

        return query.getResultList(); // Получение списка результатов
    }


    public User getUserByTicketId(Long id){

        String jpql = "SELECT t.user FROM Ticket t WHERE t.id =:id";
        TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }


}
