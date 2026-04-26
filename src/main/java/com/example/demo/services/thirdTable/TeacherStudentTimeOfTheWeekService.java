package com.example.demo.services.thirdTable;

import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import com.example.demo.repositories.thirdTables.TeacherStudentTimeOfTheWeekRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherStudentTimeOfTheWeekService {
    private TeacherStudentTimeOfTheWeekRepository tSWRepository;

    public TeacherStudentTimeOfTheWeekService(TeacherStudentTimeOfTheWeekRepository tSWRepository) {
        this.tSWRepository = tSWRepository;
    }
    public void create(TeacherStudentTimeOfTheWeek tsw){
        tSWRepository.save(tsw);
    }
    public void removeAllTswByStudentId(int id){
        tSWRepository.removeAllByStudentId(id);
    }
    public TeacherStudentTimeOfTheWeek getById(int id){
        return tSWRepository.findById(id).get();
    }
    public void deleteById(int id){
        tSWRepository.deleteById(id);
    }
    public List<TeacherStudentTimeOfTheWeek> getAll(){
        return tSWRepository.findAll();
    }
    public void removeByTIdAndWId(int tId, int wId){
        tSWRepository.removeAllByTeacherIdAndTimeOfTheWeekId(tId, wId);
    }
    public void removeAllByStIdAndTeachIdAndTswId(int stId, int teachId, int tswId){
        tSWRepository.removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekId(stId, teachId, tswId);
    }
    public List<TeacherStudentTimeOfTheWeek> findAllByTeachId(int id){
        return  tSWRepository.findAllByTeacherId(id);
    }
    public void removeAllByTidAndSid(int idT, int idSt){
        tSWRepository.removeAllByTeacherIdAndStudentId(idT, idSt);
    }
    public void removeAllByIdStAndIsTsw(int idst, int idTsw){tSWRepository.removeAllByStudentIdAndTimeOfTheWeekId(idst, idTsw);}
}
