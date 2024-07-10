package com.example.demo.service;

import com.example.demo.dto.QuestionReqDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class QuestionServiceTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void createQuestion() throws NotFoundException {
        Optional<User> userOptional = userRepository.findByUsername("user3");
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();

        QuestionReqDto dto = QuestionReqDto.builder()
                .subject("new sub")
                .content("new content")
                .writer(String.valueOf(user))
                .createdAt(LocalDateTime.now())
                .build();

        System.out.println(questionService.register(dto));
    }

}