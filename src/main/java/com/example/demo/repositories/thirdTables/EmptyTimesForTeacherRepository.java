package com.example.demo.repositories.thirdTables;

import com.example.demo.models.thirdTables.EmptyTimesForTeacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmptyTimesForTeacherRepository extends JpaRepository<EmptyTimesForTeacher, Integer> {
    public void removeAllByTeacherId(int id);
    public List<EmptyTimesForTeacher> findAllByTeacherId(int id);
}
