package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Course;
import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getAllStudentsWithCourses() {
        List<Student> students = studentRepository.findAll();
        Map<Long, List<Course>> studentCoursesMap = new HashMap<>();
        for (Student student : students) {
            List<StudentCourse> studentCourses = studentCourseRepository.findByStudentId(student.getId());
            List<Course> courses = new ArrayList<>();
            for (StudentCourse studentCourse : studentCourses) {
                courses.add(studentCourse.getCourse());
            }
            studentCoursesMap.put(student.getId(), courses);
        }
        List<StudentCourse> studentCourses = new ArrayList<>();
        for (Student student : students) {
            List<Course> coursesList = studentCoursesMap.get(student.getId());
            if(coursesList != null) {
                for (Course course : coursesList) {
                    StudentCourse studentCourse = new StudentCourse();
                    studentCourse.setStudent(student);
                    studentCourse.setCourse(course);
                    studentCourses.add(studentCourse);
                }
            }
        }
        return studentCourses;
    }

    public Optional<Student> findStudentWithHighestGpa() {
        List<Student> students = studentRepository.findAll();
        Student highestGpaStudent = null;
        double highestGpa = 0.0;
        for (Student student : students) {
            if (student.getGpa() > highestGpa) {
                highestGpa = student.getGpa();
                highestGpaStudent = student;
            }
        }
        return Optional.ofNullable(highestGpaStudent);
    }

    public String joinStudentNames() {
        List<Student> students = studentRepository.findAll();
        StringBuilder result = new StringBuilder();
        for (Student student : students) {
            result.append(student.getName()).append(", ");
        }
        if (result.length() > 0) {
            result.delete(result.length() - 2, result.length());
        }
        return result.toString();
    }
}