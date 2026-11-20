package com.abdullaevaziz.service;


import com.abdullaevaziz.client.StudentsClient;
import com.abdullaevaziz.dto.ResponseResult;
import com.abdullaevaziz.model.Auto;
import com.abdullaevaziz.model.Student;
import com.abdullaevaziz.repository.AutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AutoServiceImpl implements AutoService {

    private AutoRepository autoRepository;

    private StudentsClient studentsClient;


    @Override
    public void add(Auto auto, long id) {
        try {
            ResponseResult<Student> result = this.studentsClient.get(id).getBody();
            Student student = result.getData();
            auto.setStudentId(student.getId());

            this.autoRepository.save(auto);
        }  catch (DataAccessException e) { //TODO тут обработать исключение от базы
            throw new IllegalArgumentException("Auto has already added!"); //TODO дать сообщение что такакя тачка уже есть
        }
    }

    //TODO создать отдельный класс который ловит именно исключение фейгн эксеппшн
    //TODO считывает сообщение об ошибке которое пришло от друого микросервиса
    //TODO формирует новый респонсерезульт куда посылается это сообщение
    //TODO ставится статус 400

    //TODO данный класс прицепляется (регистрируется) у фегнклайента

    @Override
    public List<Auto> get() {
        return this.autoRepository.findAll();
    }

    @Override
    public Auto get(long id) {
        return this.autoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Auto does not exists!"));
    }

    @Override
    public Auto update(Auto auto) {
        try {
            Auto old = this.get(auto.getId());
            old.setBrand(auto.getBrand());
            old.setPower(auto.getPower());
            old.setYear(auto.getYear());
            this.autoRepository.save(old);
            return old;
        } catch (Exception e) {
            throw new IllegalArgumentException("Auto has already added!");
        }
    }

    @Override
    public Auto delete(long id) {
        Auto auto = this.get(id);
        this.autoRepository.delete(auto);
        return auto;
    }

    @Override
    public List<Auto> listAutoByStudentId(long studentId) {
         return this.autoRepository.findByStudentId(studentId);
    }
}

