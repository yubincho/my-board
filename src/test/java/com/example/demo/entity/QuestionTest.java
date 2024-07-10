package com.example.demo.entity;

import com.example.demo.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

//    @Test
//    public void create() {
//
//
//        Question q1 = Question.builder()
//                .subject("스프링이 무엇인가요?")
//                .content("스필에 대해 설명해 주세요.")
//                .createdAt(LocalDateTime.now())
//                .build();
//        questionRepository.save(q1);
//
//
//        Question q2 = Question.builder()
//                .subject("몇시까지 운영하나요?")
//                .content("운영 시간에 대해 알려 주세요.")
//                .createdAt(LocalDateTime.now())
//                .build();
//        questionRepository.save(q2);
//
//
//        List<Question> questionList = questionRepository.findAll();
//        System.out.println("[questionList]" + Arrays.toString(questionList.toArray()));
//        assertEquals(2, questionList.size());
//
//        Question q = questionList.get(0);
//        assertEquals("스프링이 무엇인가요?", q.getSubject());
//
//    }

    @Test
    public void update() {
        Optional<Question> question = this.questionRepository.findById(3L);

        if (question.isPresent()) {
            Question result = question.get();

            result.changeSubject("제목 수정");
            result.changeContent("내용 수정");

            this.questionRepository.save(result);
        }
    }



}