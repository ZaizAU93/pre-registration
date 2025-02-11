package com.example.service;

import com.example.model.Computer;
import com.example.repo.ComputerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class ComputerService {

    @Autowired
    private ComputerRepo computerRepo;

    public Computer getSystemInfo() throws UnknownHostException {

        InetAddress localhost = InetAddress.getLocalHost();
        Computer computer = Computer.builder().
                hostName(localhost.getHostName()).
                ipAddress(localhost.getHostAddress()).
                osArchitecture(System.getProperty("os.arch")).
                osVersion(System.getProperty("os.version")).
                name(System.getProperty("os.name")).
                build();


        return computer;
    }


    public Computer getComputerById(Long id){
        return computerRepo.getComputerById(id);
    }


    public Long save(Computer computer){
        Computer saveComputer = computerRepo.save(computer);
        return saveComputer.getId();
    }


}
