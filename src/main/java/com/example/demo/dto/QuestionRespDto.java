package com.example.demo.dto;

import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class QuestionRespDto {
    private Long id;
    private String subject;
    private String content;
    private User writer;  // 나중에 사용자 이름으로 받기

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // ISO 8601 형식 지정
    private LocalDateTime createdAt;

    public static QuestionRespDto toDto(Question question) {
        return QuestionRespDto.builder()
                .id(question.getId())
                .subject(question.getSubject())
                .content(question.getContent())
                .writer(question.getUser())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
