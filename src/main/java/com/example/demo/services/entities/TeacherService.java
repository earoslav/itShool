package com.example.demo.services.entities;

import com.example.demo.dto.entities.TeacherDTO;
import com.example.demo.mapper.entity.TeacherMapper;
import com.example.demo.models.entities.Teacher;
import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.TeacherRepository;
import com.example.demo.repositories.entities.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

// TeacherService contains business operations for the "teachers" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private UserRepository userRepository;
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;
    private TeacherMapper teacherMapper;


    // Receives TeacherRepository and UserRepository dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "teachers" module without manual object creation.
    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository, @Qualifier("schoolRedisTemplate") RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.teacherMapper = teacherMapper;
    }
    // Returns all teachers from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }
    // Finds a single record in the "teachers" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public Teacher getById(Integer id) {
        Teacher obj;
        if(redisTemplate.opsForValue().get("teacherById"+id) == null){
            obj = teacherRepository.findById(id).get();
            redisTemplate.opsForValue().set("teacherById"+id, teacherMapper.mapTeacherToTeacherDTO(obj));
        } else {
            obj = teacherMapper.mapTeacherDTOToTeacher(objectMapper.convertValue(redisTemplate.opsForValue().get("teacherById"+id), new TypeReference<TeacherDTO>() {}));
        }
        return obj;
    }
    // Saves a new record in the "teachers" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
    public Teacher create(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    // Searches for records in the "teachers" module by condition: user account.
    // The actual query is performed by `teacherRepository.findByUserId`, and the controller receives the final result.
    public Teacher findByUserId(int userId){
        Teacher obj;
        if(redisTemplate.opsForValue().get("teacherByUserId"+userId) == null){
            obj = teacherRepository.findByUserId(userId);
            if(obj != null) redisTemplate.opsForValue().set("teacherByUserId"+userId, teacherMapper.mapTeacherToTeacherDTO(obj));
        } else {
            obj = teacherMapper.mapTeacherDTOToTeacher(objectMapper.convertValue(redisTemplate.opsForValue().get("teacherByUserId"+userId), new TypeReference<TeacherDTO>() {}));
        }
        return obj;
    }
    // Updates an existing record in the "teachers" module.
    // First finds the current entity, transfers name, age, email, password, comment, phoneNumber, and unpaidMoney fields, then saves it back to the repository.
    public Teacher update(Integer id, Teacher teacher) {
        Teacher existing = getById(id);
        existing.setName(teacher.getName());
        existing.setAge(teacher.getAge());
        existing.setEmail(teacher.getEmail());
        existing.setPassword(teacher.getPassword());
        existing.setComment(teacher.getComment());
        existing.setPhoneNumber(teacher.getPhoneNumber());
        existing.setUnpaidMoney(teacher.getUnpaidMoney());
        existing.setTgUsername(teacher.getTgUsername());
        existing.setFreeTimeIds(teacher.getFreeTimeIds());
        existing = teacherRepository.save(existing);
        TeacherDTO teacherDTO = teacherMapper.mapTeacherToTeacherDTO(existing);
        redisTemplate.opsForValue().set("teacherById"+id, teacherDTO);
        if(existing.getUser() != null) redisTemplate.opsForValue().set("teacherByUserId"+existing.getUser().getId(), teacherDTO);
        return existing;
    }
    // Checks if a record in the "teachers" module already exists by a condition: email.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByEmail(String email){
        Object obj;
        if(redisTemplate.opsForValue().get("teacherByEmail"+email) == null){
            obj = userRepository.findByEmailAndRole(email, "TEACHER");
            if(obj != null) redisTemplate.opsForValue().set("teacherByEmail"+email, obj);
        } else {
            obj = objectMapper.convertValue(redisTemplate.opsForValue().get("teacherByEmail"+email), new TypeReference<User>() {});
        }
        return obj!=null;
    }
    // Checks if a record in the "teachers" module already exists by a condition: user account.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByUserId(int userId){
        Teacher obj;
        if(redisTemplate.opsForValue().get("teacherByUserId"+userId) == null){
            obj = teacherRepository.findByUserId(userId);
            if(obj != null) redisTemplate.opsForValue().set("teacherByUserId"+userId, teacherMapper.mapTeacherToTeacherDTO(obj));
        } else {
            obj = teacherMapper.mapTeacherDTOToTeacher(objectMapper.convertValue(redisTemplate.opsForValue().get("teacherByUserId"+userId), new TypeReference<TeacherDTO>() {}));
        }
        return obj!=null;
    }
    // Deletes a record in the "teachers" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
    public void deleteById(Integer id) {
        Teacher existing = getById(id);
        teacherRepository.deleteById(id);
        redisTemplate.delete("teacherById"+id);
        if(existing.getUser() != null) redisTemplate.delete("teacherByUserId"+existing.getUser().getId());
        redisTemplate.delete("teacherByEmail"+existing.getEmail());
    }
}
