package com.abdullaevaziz.client;

import com.abdullaevaziz.dto.ResponseResult;
import com.abdullaevaziz.exeption.FeignClientConfig;
import com.abdullaevaziz.model.Auto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "autos", path = "/autos", configuration = FeignClientConfig.class)
public interface AutoClient {

    @GetMapping("/listAutoByStudentId/{studentId}")
    ResponseEntity<ResponseResult<List<Auto>>> listAutoByStudentId(@PathVariable("studentId") long studentId);

}
