package com.example.demo.repository;

import com.example.demo.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuerydslPredicateExecutor<Question> {

    Question findBySubject(String subject);
    List<Question> findBySubjectLike(String subject);

    @Query("SELECT q FROM Question q JOIN FETCH q.answerList WHERE q.id = :id")
    Optional<Question> findByIdWithAnswers(Long id);


}
