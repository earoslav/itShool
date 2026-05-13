package com.example.demo.services.redis;

import com.example.demo.models.entities.Admin;
import com.example.demo.models.entities.Teacher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    public RedisService() {
    }
    //    private RedisTemplate redis;
//
//    public RedisService(RedisTemplate redis) {
//        this.redis = redis;
//    }
//
//    public void addIfEmpty(String key, Object val){
//        if(redis.opsForValue().get(key)==null){
//            redis.opsForValue().set(key, val);
//        }
//    }
//    public void update(String key, Object val){
//        redis.opsForValue().set(key, val);
//    }
//    public Teacher getTeacherById(Long id){
//        Teacher teacher;
//
//        if(redis.opsForValue().get("teacherById" + id) == null){
//            teacher = teacherService.getById(id);
//            redis.opsForValue().set("teacherById" + id, teacher);
//        } else {
//            teacher = (Teacher) redis.opsForValue().get("teacherById" + id);
//        }
//
//        return teacher;
//    }
//
//    public User getUserById(Long id){
//        User user;
//
//        if(redis.opsForValue().get("userById" + id) == null){
//            user = userService.getById(id);
//            redis.opsForValue().set("userById" + id, user);
//        } else {
//            user = (User) redis.opsForValue().get("userById" + id);
//        }
//
//        return user;
//    }
//
//    public List<FreeTime> getFreeTimes(){
//        List<FreeTime> freeTimes = new ArrayList<>();
//
//        if(redis.opsForValue().get("freeTimes") == null){
//            freeTimes = freeTimeService.getAll();
//            redis.opsForValue().set("freeTimes", freeTimes);
//        } else {
//            freeTimes = (List<FreeTime>) redis.opsForValue().get("freeTimes");
//        }
//
//        return freeTimes;
//    }
//
//    public List<Course> getCourses(){
//        List<Course> courses = new ArrayList<>();
//
//        if(redis.opsForValue().get("courses") == null){
//            courses = courseService.getAll();
//            redis.opsForValue().set("courses", courses);
//        } else {
//            courses = (List<Course>) redis.opsForValue().get("courses");
//        }
//
//        return courses;
//    }
//
//    public Lesson getLessonById(Long id){
//        Lesson lesson;
//
//        if(redis.opsForValue().get("lessonById" + id) == null){
//            lesson = lessonService.getById(id);
//            redis.opsForValue().set("lessonById" + id, lesson);
//        } else {
//            lesson = (Lesson) redis.opsForValue().get("lessonById" + id);
//        }
//
//        return lesson;
//    }
//
//    public Admin getAdminById(Long id){
//        Admin admin = new Admin();
//        if(redis.opsForValue().get("adminById" + id) == null){
//            admin = adminService.getById(id);
//            redis.opsForValue().set("adminById" + id, admin);
//        } else {
//            admin = (Admin) redis.opsForValue().get("adminById" + id);
//        }
//
//        return admin;
//    }
//
//    public Student getStudentById(Long id){
//        Student student;
//
//        if(redis.opsForValue().get("studentById" + id) == null){
//            student = studentService.getById(id);
//            redis.opsForValue().set("studentById" + id, student);
//        } else {
//            student = (Student) redis.opsForValue().get("studentById" + id);
//        }
//
//        return student;
//    }
//
//    public List<Teacher> getTeachers(){
//        List<Teacher> teachers = new ArrayList<>();
//
//        if(redis.opsForValue().get("teachers") == null){
//            teachers = teacherService.getAll();
//            redis.opsForValue().set("teachers", teachers);
//        } else {
//            teachers = (List<Teacher>) redis.opsForValue().get("teachers");
//        }
//
//        return teachers;
//    }
//
//    public List<Lesson> getLessonsByTeacherId(Long teacherId){
//        List<Lesson> lessons = new ArrayList<>();
//
//        if(redis.opsForValue().get("lessonsByTeacherId" + teacherId) == null){
//            lessons = lessonService.getLessonsByTeacherId(teacherId);
//            redis.opsForValue().set("lessonsByTeacherId" + teacherId, lessons);
//        } else {
//            lessons = (List<Lesson>) redis.opsForValue().get("lessonsByTeacherId" + teacherId);
//        }
//
//        return lessons;
//    }
//
//    public List<Lesson> getLessonsByStudentId(Long studentId){
//        List<Lesson> lessons = new ArrayList<>();
//
//        if(redis.opsForValue().get("lessonsByStudentId" + studentId) == null){
//            lessons = lessonService.getLessonsByStudentId(studentId);
//            redis.opsForValue().set("lessonsByStudentId" + studentId, lessons);
//        } else {
//            lessons = (List<Lesson>) redis.opsForValue().get("lessonsByStudentId" + studentId);
//        }
//
//        return lessons;
//    }
}
