package com.example.demo.services.entities;

import com.example.demo.dto.adminLessons.LessonAdminLessonsDTO;
import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.dto.programe.CourseDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.mapper.programe.CourseMapper;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.programe.Course;
import com.example.demo.services.other.TimeOfTheWeekService;
import com.example.demo.services.program.CourseService;
import com.example.demo.services.program.LessonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModelService {
    private TeacherService teacherService;
    private CourseService courseService;
    private TimeOfTheWeekService theWeekService;
    private StudentService studentService;
    private LessonService lessonService;
    private StudentMapper studentMapper;
    private CourseMapper courseMapper;
    private TeacherMapper teacherMapper;
    public ModelService(TeacherService teacherService, CourseService courseService, TimeOfTheWeekService theWeekService, StudentService studentService, LessonService lessonService, StudentMapper studentMapper, CourseMapper courseMapper, TeacherMapper teacherMapper) {

        this.teacherService = teacherService;
        this.courseService = courseService;
        this.theWeekService = theWeekService;

        this.studentService = studentService;
        this.lessonService = lessonService;
        this.studentMapper = studentMapper;
        this.courseMapper = courseMapper;
        this.teacherMapper = teacherMapper;
    }
    public void compileModelForStudentLessonsInAdmin(Model model, int idSt, String output, Integer days) throws JsonProcessingException {
        int lessonDays = lessonService.normalizeLessonDays(days);
        List<TeacherDTO> teachers = teacherService.getAll().stream().map(el -> teacherMapper.mapTeacherToTeacherDTO(el)).collect(Collectors.toList());
        StudentDTO student = studentMapper.mapStudentToStudentDTO(studentService.getById(idSt));
        HashMap<String, LessonAdminLessonsDTO> lessonHashMap = lessonService.compileLessonsForStudentInAdmin(idSt, lessonDays);
        List<String> weekDays = theWeekService.compileWeekDays(lessonDays);
        List<String> hours = theWeekService.compileHours();
        List<CourseDTO> courses = courseService.getAll().stream().map(el -> courseMapper.mapCourseToCourseDTO(el)).collect(Collectors.toList());

        if(!output.equals("")){model.addAttribute("output", output);}
        model.addAttribute("lessons", lessonHashMap);
        model.addAttribute("courses", courses);
        model.addAttribute("weekDays", weekDays);
        model.addAttribute("lessonDays", lessonDays);
        model.addAttribute("teachers", teachers);
        model.addAttribute("student", student);
        model.addAttribute("hours", hours);
    }
}
