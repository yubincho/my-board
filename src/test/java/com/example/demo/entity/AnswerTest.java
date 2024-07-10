package com.example.demo.entity;

import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class AnswerTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;


//    @Transactional
//    @Test
//    public void create() {
//        Optional<Question> question = this.questionRepository.findById(2L);
//        assertTrue(question.isPresent());
//        Question qu = question.get();
//
//        Answer answer = Answer.builder()
//                .question(qu)
//                .content("자동입니다.2")
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        this.answerRepository.save(answer);
//
//        // Question 엔티티 다시 불러와서 검증해야 함
//        Optional<Question> question2 = this.questionRepository.findByIdWithAnswers(2L);
//        assertTrue(question2.isPresent());
//        Question result = question2.get();
//
////        assertEquals(39, answerRepository.count());
//        assertTrue(result.getAnswerList().contains(answer)); // LazyInitializationException 발생
////        System.out.println("[result.getAnswerList().contains(answer)]" + result.getAnswerList().contains(answer));
//
////        System.out.println("[answer]" + answerRepository.findById(38));
//
//    }

}