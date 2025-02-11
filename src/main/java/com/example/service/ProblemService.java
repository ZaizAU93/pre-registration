package com.example.service;

import com.example.model.Problem;
import com.example.repo.ProblemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemService {

   @Autowired
   private ProblemRepo problemRepo;

   public List<Problem> getAllProblems(){
       return problemRepo.findAll();
   }


}
