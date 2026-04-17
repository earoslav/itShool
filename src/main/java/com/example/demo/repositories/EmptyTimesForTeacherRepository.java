package com.example.demo.repositories;

import com.example.demo.models.EmptyTimesForTeacher;
import com.example.demo.models.TimeOfTheWeek;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmptyTimesForTeacherRepository extends JpaRepository<EmptyTimesForTeacher, Integer> {
    public void removeAllByTeacherId(int id);
    public List<EmptyTimesForTeacher> findAllByTeacherId(int id);
}
