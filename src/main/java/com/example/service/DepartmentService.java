package com.example.service;

import com.example.model.Department;
import com.example.repo.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepo departmentRepo;

    public List<Department> getAllDepartaments(){
        return departmentRepo.findAll();
    }

}
