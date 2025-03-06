package com.example.service;

import com.example.model.JobTitle;
import com.example.repo.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTitleService {

    @Autowired
    private Job job;

    public List<JobTitle> getAllJobTitle(){
        return job.findAllBy();
    }
}
