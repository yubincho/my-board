package com.example.demo.repository;

import com.example.demo.entity.QQuestion;
import com.example.demo.entity.Question;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;


    @Test
    public void testJpaRepository1() {

        Question question = this.questionRepository.findBySubject("Spring Boot는 무엇인가요?");
        assertEquals(1, question.getId());
        System.out.println(question);

    }

    @Test
    public void testJpaRepository2() {

        List<Question> questionList = this.questionRepository.findBySubjectLike("%AOP%");
        Question question = questionList.get(0);
        System.out.println(question);
        assertEquals("스프링 AOP란?", question.getSubject());

    }


//    @Test
//    public void update() {
//        Optional<Question> optionalQuestion = this.questionRepository.findById(2L);
//        assertTrue(optionalQuestion.isPresent());
//        Question question = optionalQuestion.get();
//        Question updated = question.builder()
//                .id(question.getId())
//                .subject("수정된 제목2")
//                .content("수정된 내용2")
//                .createdAt(question.getCreatedAt())
//                .build();
//
//        this.questionRepository.save(updated);
//
//        assertEquals("수정된 제목2", updated.getSubject());
//
//    }


    @DisplayName("단일 항목 검색")
    @Test
    public void 단일_항목_검색() {
        // 1. Pageable 설정: 0번째 페이지, 한 페이지당 10개의 항목, id 필드로 정렬
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        // 2. Querydsl Q 클래스 설정: Question 엔티티에 대한 메타데이터 클래스
        QQuestion qQuestion = QQuestion.question;

        // 3. 검색 키워드 설정
        String keyword = "1";

        // 4. BooleanBuilder 생성 및 조건 추가
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = qQuestion.subject.contains(keyword);
        builder.and(expression);

        // 5. Repository를 사용하여 검색 수행
        Page<Question> result = questionRepository.findAll(builder, pageable);

        // 6. 결과 출력
        result.stream().forEach(question -> {
            System.out.println(question);
        });


    }


    @DisplayName("다중 항목 검색 - 2개 검색")
    @Test
    public void 다중_항목_검색1() {
        // 1. Pageable 설정: 0번째 페이지, 한 페이지당 10개의 항목, id 필드로 내림차순 정렬
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        // 2. Querydsl Q 클래스 설정: Question 엔티티에 대한 메타데이터 클래스
        QQuestion qQuestion = QQuestion.question;

        // 3. 검색 키워드 설정
        String keyword = "1";

        // 4. BooleanBuilder 생성 및 조건 추가
        BooleanBuilder builder = new BooleanBuilder();

        // subject 필드에 키워드가 포함된 항목 찾기
        BooleanExpression exSubject = qQuestion.subject.contains(keyword);

        // content 필드에 키워드가 포함된 항목 찾기
        BooleanExpression exContent = qQuestion.content.contains(keyword);

        // subject 또는 content 필드에 키워드가 포함된 항목 찾기
        BooleanExpression exAll = exSubject.or(exContent);

        // 조건을 빌더에 추가
        builder.and(exAll);

        // id가 0보다 큰 항목 찾기
        builder.and(qQuestion.id.gt(0L));

        // 5. Repository를 사용하여 검색 수행
        Page<Question> result = questionRepository.findAll(builder, pageable);

        // 6. 결과 출력
        result.stream().forEach(question -> {
            System.out.println(question);
        });

    }


    @DisplayName("다중 항목 검색 - 3개 검색")
    @Test
    public void 다중_항목_검색2() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        QQuestion qQuestion = QQuestion.question;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression exSubject = qQuestion.subject.contains(keyword);
        BooleanExpression exContent = qQuestion.content.contains(keyword);

        // keyword를 Long 타입으로 변환한 후 비교하기
        Long userId = Long.parseLong(keyword);
        BooleanExpression exUser = qQuestion.user.id.eq(userId);

        builder.and(exSubject.or(exContent).or(exUser));
        builder.and(qQuestion.id.gt(0L));

        Page<Question> result = questionRepository.findAll(builder, pageable);

        result.stream().forEach(question -> {
            System.out.println(question);
        });

    }


}