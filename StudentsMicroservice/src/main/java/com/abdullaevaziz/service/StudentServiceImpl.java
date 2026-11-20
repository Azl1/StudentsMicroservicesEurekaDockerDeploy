package com.abdullaevaziz.service;

import com.abdullaevaziz.client.AutoClient;
import com.abdullaevaziz.dto.ResponseResult;
import com.abdullaevaziz.model.Auto;
import com.abdullaevaziz.model.Student;
import com.abdullaevaziz.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private AutoClient autoClient;

    @Override
    public void add(Student student) {
        try {
            this.studentRepository.save(student);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Student has already added!");
        }
    }

    @Override
    public List<Student> get() {
        List<Student> students = this.studentRepository.findAll();
        for (Student student : students) {
            setStudentAuto(student);
        }
        return students;
    }

    @Override
    public Student get(long id) {
        Student student =  this.studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student does not exists!"));
        setStudentAuto(student);
        return student;
    }

    @Override
    public Student update(Student student) {
        try {
            Student old = this.get(student.getId());
            old.setFio(student.getFio());
            old.setAge(student.getAge());
            old.setNum(student.getNum());
            old.setSalary(student.getSalary());
            setStudentAuto(old);
            this.studentRepository.save(old);
            return old;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Student has already added!");
        }
    }


    @Override
    public Student delete(long id) {
        Student student = this.get(id);
        this.studentRepository.deleteById(id);
        return student;
    }

    public void setStudentAuto(Student student) {
        try {
            ResponseEntity<ResponseResult<List<Auto>>> response = autoClient.listAutoByStudentId(student.getId());
            if (response.getBody() != null) {
                student.setAutos(response.getBody().getData());
            }
        } catch (Exception e) {
            // Логируем ошибку, но не роняем весь запрос
            System.err.println("Failed to fetch autos for student " + student.getId() + ": " + e.getMessage());
            student.setAutos(List.of()); // Возвращаем пустой список, если не удалось
        }
    }
}

