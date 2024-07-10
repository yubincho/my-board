package com.example.demo.dto;


import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuestionReqDto {

    private String subject;
    private String content;
    private String writer;  // 사용자 이름으로 받기

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // ISO 8601 형식 지정
    private LocalDateTime createdAt;


    public static Question toEntity(QuestionReqDto dto, User user) {
        return Question.builder()
                .subject(dto.getSubject())
                .content(dto.getContent())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
    }


}

