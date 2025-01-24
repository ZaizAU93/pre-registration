package com.example.service;

import com.example.model.Computer;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class SystemInfoService {

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
}
