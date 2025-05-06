package com.example.scammer.service;

import com.example.model.User;
import com.example.scammer.DTO.UserDTO;
import com.example.scammer.Registrar;
import com.example.scammer.repo.RegistratorRepo;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistratorService {

    @Autowired
    RegistratorRepo registratorRepo;
    @Autowired
    private UserService userService;

    public void save(User user, Long id){
        Registrar registrar = Registrar.builder()
                .regCode(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .fathername(user.getFathername())
                .userIdReg(id)
                .build();
        registratorRepo.save(registrar);
    }

    public Registrar findById(Long id){
        return registratorRepo.findById(id).get();
    }


    public void saveDTO(UserDTO userDto, User user){
        Registrar registrar = Registrar.builder()
                .regCode(userDto.getREGCODE().toString())
                .name(userDto.getUSERNAME())
                .userIdReg(user.getId())
                .build();
        registratorRepo.save(registrar);
    }


}
