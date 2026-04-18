package com.example.demo.services;

import com.example.demo.models.TeacherStudentTimeOfTheWeek;
import com.example.demo.repositories.TeacherStudentTimeOfTheWeekRepository;
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

    public TeacherStudentTimeOfTheWeek getById(int id){
        return tSWRepository.findById(id).get();
    }
    public void deleteById(int id){
        tSWRepository.deleteById(id);
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
