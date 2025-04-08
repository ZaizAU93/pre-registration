package com.example.service;

import com.example.model.TicketAdmin;
import com.example.repo.TicketAdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketAdminService {
    @Autowired
    private TicketAdminRepo repo;

    @Autowired
    private UserService userService;

    public void save(TicketAdmin ticketAdmin){

        TicketAdmin saveAdminTicket = TicketAdmin.builder()
                .ticket(ticketAdmin.getTicket())
            //    .rupAdmin(userService.getCurrentUser().getId())
                .build();

        repo.save(saveAdminTicket);
    }


    public void delete(TicketAdmin ticketAdmin){
        repo.delete(ticketAdmin);
    }

    public TicketAdmin findById(Long id){
        return repo.findById(id).get();
    }

    public List<TicketAdmin> findAll(){
        return repo.findAll();
    }


    public void updateTcketAdminRup(TicketAdmin ticketAdmin, Long id){
        repo.updateRupTicketAdmin(ticketAdmin, id);
    }


}
