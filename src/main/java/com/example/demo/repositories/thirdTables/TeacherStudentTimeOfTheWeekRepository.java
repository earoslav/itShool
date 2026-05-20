package com.example.demo.repositories.thirdTables;

import com.example.demo.models.thirdTables.TeacherStudentTimeOfTheWeek;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

// Репозиторій TeacherStudentTimeOfTheWeekRepository дає доступ до таблиць модуля «постійний розклад викладача зі студентом» через Spring Data JPA.
// Назви методів нижче перетворюються Spring-ом у SQL-запити без ручного написання query.
@Repository
@EnableJpaRepositories
public interface TeacherStudentTimeOfTheWeekRepository extends JpaRepository<TeacherStudentTimeOfTheWeek, Integer> {
    
    @EntityGraph(attributePaths = {"student", "teacher", "timeOfTheWeek"})
    public List<TeacherStudentTimeOfTheWeek> findAllByTeacherId(int id);

    @EntityGraph(attributePaths = {"student", "teacher", "timeOfTheWeek"})
    public List<TeacherStudentTimeOfTheWeek> findAll();

    public void removeAllByTeacherIdAndTimeOfTheWeek(int tId, int wId);

    public void removeAllByTeacherIdAndStudentId(int idT, int idSt);

    public void removeAllByStudentIdAndTimeOfTheWeek(int idSt, int idTsw);

    public void removeAllByStudentIdAndTeacherIdAndTimeOfTheWeek(int stId, int teachId, int tswId);

    public void removeAllByStudentId(int id);
}
