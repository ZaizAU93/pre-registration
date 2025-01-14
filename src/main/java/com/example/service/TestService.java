package com.example.service;

import com.example.model.Test;
import com.example.repo.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private TestRepo testRepo;

    public void createTest(Test test){
        testRepo.save(test);
    }

    public List<Test> getTests(){
        return testRepo.findAll();
    }

}
