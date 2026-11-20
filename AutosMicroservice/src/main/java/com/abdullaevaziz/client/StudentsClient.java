package com.abdullaevaziz.client;


import com.abdullaevaziz.dto.ResponseResult;
import com.abdullaevaziz.exeption.FeignException;
import com.abdullaevaziz.model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "students", path = "/students", configuration = FeignException.class)
public interface StudentsClient {

    @GetMapping("/{studentId}")
    ResponseEntity<ResponseResult<Student>> get(@PathVariable("studentId") long studentId);


}
