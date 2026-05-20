package com.example.demo.services.thirdTable;

import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.repositories.thirdTables.EmptyTimesForTeacherRepository;
import java.util.List;
import org.springframework.stereotype.Service;

// EmptyTimesForTeacherService contains business operations for the "teacher's free time" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class EmptyTimesForTeacherService {
    private final EmptyTimesForTeacherRepository emptyTimesForTeacherRepository;
    // Receives EmptyTimesForTeacherRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "teacher's free time" module without manual object creation.
    public EmptyTimesForTeacherService(EmptyTimesForTeacherRepository emptyTimesForTeacherRepository) {
        this.emptyTimesForTeacherRepository = emptyTimesForTeacherRepository;
    }
    // Returns all teacher's free time slots from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
//    public List<EmptyTimesForTeacher> getAll() {
//        return emptyTimesForTeacherRepository.findAll();
//    }
    // Finds a single record in the "teacher's free time" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
//    public EmptyTimesForTeacher getById(Integer id) {
//        return emptyTimesForTeacherRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("EmptyTimesForTeacher not found with id: " + id));
//    }
//    public void removeAllByTeachIdAndTimeId(int techId, int timeId){
//        emptyTimesForTeacherRepository.removeAllByTeacherIdAndTime_Id(techId, timeId);
//    }
    // Saves a new record in the "teacher's free time" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
//    public EmptyTimesForTeacher create(EmptyTimesForTeacher emptyTimesForTeacher) {
//        return emptyTimesForTeacherRepository.save(emptyTimesForTeacher);
//    }


    // Updates an existing record in the "teacher's free time" module.
    // First finds the current entity, transfers teacher and time fields, then saves it back to the repository.
//    public EmptyTimesForTeacher update(Integer id, EmptyTimesForTeacher emptyTimesForTeacher) {
//        EmptyTimesForTeacher existing = getById(id);
//        existing.setTeacher(emptyTimesForTeacher.getTeacher());
//        existing.setTime(emptyTimesForTeacher.getTime());
//        return emptyTimesForTeacherRepository.save(existing);
//    }
    // Searches for records in the "teacher's free time" module by condition: teacher.
    // The actual query is performed by `emptyTimesForTeacherRepository.findAllByTeacherId`, and the controller receives the final result.
//    public List<EmptyTimesForTeacher> findAllByTeachId(int tId){
//        return emptyTimesForTeacherRepository.findAllByTeacherId(tId);
//    }
    // Deletes a record in the "teacher's free time" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
//    public void deleteById(Integer id) {
//        getById(id);
//        emptyTimesForTeacherRepository.deleteById(id);
//    }
    // Deletes records in the "teacher's free time" module by condition: teacher.
    // The operation is delegated to `emptyTimesForTeacherRepository.removeAllByTeacherId` to clear associated data after a user action.
//    public void removeAllByTeacherId(int teacherId){
//        emptyTimesForTeacherRepository.removeAllByTeacherId(teacherId);
//    }
}
