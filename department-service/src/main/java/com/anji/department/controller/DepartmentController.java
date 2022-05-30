package com.anji.department.controller;

import com.anji.department.entity.Department;
import com.anji.department.repository.DepartmentRepository;
import com.anji.department.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;


    @PostMapping("/")
    public Department saveDepartment(@RequestBody Department department){
        log.info("Post:saveDepartment:"+department);
        return this.departmentRepository.save(department);
    }


    @GetMapping("/{departmentId}")
    public Department findDepartmentId(@PathVariable long departmentId) throws Exception {
        log.info("Get:findDepartmentId:"+departmentId);
        Optional<Department> optional =  this.departmentRepository.findById(departmentId);
        if(optional.isPresent()){
            return optional.get();
        }else{
            throw new Exception("Unable to find with Id:"+departmentId);
        }
    }

}
