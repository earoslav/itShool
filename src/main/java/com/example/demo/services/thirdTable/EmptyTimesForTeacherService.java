package com.example.demo.services.thirdTable;

import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import com.example.demo.repositories.thirdTables.EmptyTimesForTeacherRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EmptyTimesForTeacherService {
    private final EmptyTimesForTeacherRepository emptyTimesForTeacherRepository;

    public EmptyTimesForTeacherService(EmptyTimesForTeacherRepository emptyTimesForTeacherRepository) {
        this.emptyTimesForTeacherRepository = emptyTimesForTeacherRepository;
    }

    public List<EmptyTimesForTeacher> getAll() {
        return emptyTimesForTeacherRepository.findAll();
    }

    public EmptyTimesForTeacher getById(Integer id) {
        return emptyTimesForTeacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EmptyTimesForTeacher not found with id: " + id));
    }

    public EmptyTimesForTeacher create(EmptyTimesForTeacher emptyTimesForTeacher) {
        return emptyTimesForTeacherRepository.save(emptyTimesForTeacher);
    }


    public EmptyTimesForTeacher update(Integer id, EmptyTimesForTeacher emptyTimesForTeacher) {
        EmptyTimesForTeacher existing = getById(id);
        existing.setTeacher(emptyTimesForTeacher.getTeacher());
        existing.setTime(emptyTimesForTeacher.getTime());
        return emptyTimesForTeacherRepository.save(existing);
    }
    public List<EmptyTimesForTeacher> findAllByTeachId(int tId){
        return emptyTimesForTeacherRepository.findAllByTeacherId(tId);
    }
    public void deleteById(Integer id) {
        getById(id);
        emptyTimesForTeacherRepository.deleteById(id);
    }
    public void removeAllByTeacherId(int teacherId){
        emptyTimesForTeacherRepository.removeAllByTeacherId(teacherId);
    }
}
