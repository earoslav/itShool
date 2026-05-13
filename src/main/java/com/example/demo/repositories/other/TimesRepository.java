package com.example.demo.repositories.other;

import com.example.demo.models.other.TimeOfTheWeek;
import com.example.demo.models.other.Times;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface TimesRepository  extends JpaRepository<Times, Integer> {
    public Times findById(int id);
}
