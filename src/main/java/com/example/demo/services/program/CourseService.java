package com.example.demo.services.program;

import com.example.demo.models.programe.Course;
import com.example.demo.repositories.program.CourseRepository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

// CourseService contains business operations for the "courses" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    // Receives CourseRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "courses" module without manual object creation.
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    // Returns all courses from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<Course> getAll() {
        return courseRepository.findAll();
    }
    // Finds a single record in the "courses" module by ID.
    public Course getById(Integer id) {
        return courseRepository.getById(id);
    }
    // Converts a list of IDs from the form into Course objects.
    // When no IDs are passed, returns all courses so that creation or editing forms can show a complete list.
    public List<Course> getCoursesThroughIds(List<Integer> ids){
        List<Course> courses;
        if(ids!=null && !ids.isEmpty()){
            courses = new ArrayList<>();
            ids.stream().forEach(course-> courses.add(getById(course)));
        }else{
            courses = getAll();
        }
        return courses;
    }
    // Saves a new record in the "courses" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
    public Course create(Course course) {
        return courseRepository.save(course);
    }

    // Updates an existing record in the "courses" module.
    // First finds the current entity, transfers name, costPerLesson, and description fields, then saves it back to the repository.
    public Course update(Integer id, Course course) {
        Course existing = getById(id);
        existing.setName(course.getName());
        existing.setCostPerLesson(course.getCostPerLesson());
        existing.setDescription(course.getDescription());
        return courseRepository.save(existing);
    }
    // Deletes a record in the "courses" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
    public void deleteById(Integer id) {
        getById(id);
        courseRepository.deleteById(id);
    }
}
