package com.example.demo.services.program;

import com.example.demo.models.programe.Course;
import com.example.demo.repositories.program.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course getById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }
    public List<Course> getCoursesThroughIds(List<Integer> ids){
        if(ids!=null && !ids.isEmpty()){
            List<Course> courses = new ArrayList<>();
            ids.stream().forEach(course-> courses.add(getById(course)));
            return courses;
        }else{
            List<Course> courses = getAll();
            return courses;
        }
    }

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public Course update(Integer id, Course course) {
        Course existing = getById(id);
        existing.setName(course.getName());
        existing.setCostPerLesson(course.getCostPerLesson());
        existing.setDescription(course.getDescription());
        return courseRepository.save(existing);
    }

    public void deleteById(Integer id) {
        getById(id);
        courseRepository.deleteById(id);
    }
}
