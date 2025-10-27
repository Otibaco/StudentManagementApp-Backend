package com.example.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.model.Student;
import com.example.repository.StudentRepository;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    // Save Student with email validation
    public Student saveStudent(Student student) {
        // Check if email already exists
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    // Get All Students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get Student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Update Student with proper validation
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        // Check if new email already exists for different student
        Optional<Student> studentWithEmail = studentRepository.findByEmail(updatedStudent.getEmail());
        if (studentWithEmail.isPresent() && !studentWithEmail.get().getId().equals(id)) {
            throw new RuntimeException("Email already exists: " + updatedStudent.getEmail());
        }

        // Update fields
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setAge(updatedStudent.getAge());
        
        return studentRepository.save(existingStudent);
    }

    // Delete Student with proper validation
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }
}