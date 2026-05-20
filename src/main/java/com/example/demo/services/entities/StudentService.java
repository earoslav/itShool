package com.example.demo.services.entities;

import com.example.demo.dto.entities.StudentDTO;
import com.example.demo.dto.entities.UserDTO;
import com.example.demo.mapper.entity.StudentMapper;
import com.example.demo.mapper.entity.UserMapper;
import com.example.demo.models.entities.Student;
import com.example.demo.models.entities.User;
import com.example.demo.repositories.entities.StudentRepository;
import com.example.demo.repositories.entities.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

// StudentService contains business operations for the "students" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private UserRepository userRepository;
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;
    private StudentMapper studentMapper;
    private UserMapper userMapper;
    // Receives StudentRepository and UserRepository dependencies through Spring.
    // These services and mappers are required by the class methods to work with the "students" module without manual object creation.
    public StudentService(StudentRepository studentRepository, UserRepository userRepository, @Qualifier("schoolRedisTemplate") RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper, StudentMapper studentMapper, UserMapper userMapper) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.studentMapper = studentMapper;
        this.userMapper = userMapper;
    }
    // Returns all students from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
    // Finds a single record in the "students" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public Student getById(Integer id) {
        Student obj;
        if(redisTemplate.opsForValue().get("studentById"+id) == null){
            obj = studentRepository.findById(id).get();
            redisTemplate.opsForValue().set("studentById"+id, studentMapper.mapStudentToStudentDTO(obj));
        } else {
            obj = studentMapper.mapStudentDTOToStudent(objectMapper.convertValue(redisTemplate.opsForValue().get("studentById"+id), new TypeReference<StudentDTO>() {}));
        }
        return obj;
    }
    // Saves a new record in the "students" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
    public Student create(Student student) {
        return studentRepository.save(student);
    }
    // Checks if a record in the "students" module already exists by a condition: user account.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByUserId(int userId){
        Student obj;
        if(redisTemplate.opsForValue().get("studentByUserId"+userId) == null){
            obj = studentRepository.findByUserId(userId);
            if(obj != null) redisTemplate.opsForValue().set("studentByUserId"+userId, studentMapper.mapStudentToStudentDTO(obj));
        } else {
            obj = studentMapper.mapStudentDTOToStudent(objectMapper.convertValue(redisTemplate.opsForValue().get("studentByUserId"+userId), new TypeReference<StudentDTO>() {}));
        }
        return obj!=null;
    }
    // Checks if a record in the "students" module already exists by a condition: email.
    // Returns a boolean for creation and editing forms to prevent duplicates or incorrect selections.
    public boolean checkIfExistsByEmail(String email){
        User obj;
        if(redisTemplate.opsForValue().get("studentByEmail"+email) == null){
            obj = userRepository.findByEmailAndRole(email, "STUDENT");
            if(obj != null) redisTemplate.opsForValue().set("studentByEmail"+email, userMapper.mapUserToUserDTO(obj));
        } else {
            obj = userMapper.mapUserDTOToUser(objectMapper.convertValue(redisTemplate.opsForValue().get("studentByEmail"+email), new TypeReference<UserDTO>() {}));
        }
        return obj!=null;
    }
    // Searches for records in the "students" module by condition: user account.
    // The actual query is performed by `studentRepository.findByUserId`, and the controller receives the final result.
    public  Student findByUserId(int id){
        Student obj;
        if(redisTemplate.opsForValue().get("studentByUserId"+id) == null){
            obj = studentRepository.findByUserId(id);
            if(obj != null) redisTemplate.opsForValue().set("studentByUserId"+id, studentMapper.mapStudentToStudentDTO(obj));
        } else {
            obj = studentMapper.mapStudentDTOToStudent(objectMapper.convertValue(redisTemplate.opsForValue().get("studentByUserId"+id), new TypeReference<StudentDTO>() {}));
        }
        return obj;
    }
    // Updates student data.
    // The method updates an existing profile with new data and resets the corresponding caches in Redis.
    public Student update(Integer id, Student student) {
        Student existing = getById(id);
        existing.setName(student.getName());
        existing.setAge(student.getAge());
        existing.setEmail(student.getEmail());
        existing.setPhoneNumber(student.getPhoneNumber());
        existing.setPassword(student.getPassword());
        existing.setComment(student.getComment());
        existing = studentRepository.save(existing);
        StudentDTO studentDTO = studentMapper.mapStudentToStudentDTO(existing);
        redisTemplate.opsForValue().set("studentById"+id, studentDTO);
        if(existing.getUser() != null) redisTemplate.opsForValue().set("studentByUserId"+existing.getUser().getId(), studentDTO);
        return existing;
    }
    // Deletes a record in the "students" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
    public void deleteById(Integer id) {
        Student existing = getById(id);
        studentRepository.deleteById(id);
        redisTemplate.delete("studentById"+id);
        if(existing.getUser() != null) redisTemplate.delete("studentByUserId"+existing.getUser().getId());
        redisTemplate.delete("studentByEmail"+existing.getEmail());
    }
}
