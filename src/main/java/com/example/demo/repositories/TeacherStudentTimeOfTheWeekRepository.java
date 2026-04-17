package com.example.demo.repositories;

import com.example.demo.models.EmptyTimesForTeacher;
import com.example.demo.models.TeacherStudentTimeOfTheWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface TeacherStudentTimeOfTheWeekRepository extends JpaRepository<TeacherStudentTimeOfTheWeek, Integer> {
    public void removeAllByTeacherIdAndTimeOfTheWeekId(int tId, int wId);
    public List<TeacherStudentTimeOfTheWeek> findAllByTeacherId(int id);
    public void removeAllByTeacherIdAndStudentId(int idT, int idSt);
    public void removeAllByStudentIdAndTimeOfTheWeekId(int idSt, int idTsw);
    public void removeAllByStudentIdAndTeacherIdAndTimeOfTheWeekId(int stId, int teachId, int tswId);
}
