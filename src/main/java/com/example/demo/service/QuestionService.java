package com.example.demo.service;


import com.example.demo.dto.QuestionReqDto;
import com.example.demo.dto.QuestionRespDto;
import com.example.demo.entity.QQuestion;
import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.example.demo.helper.PageRequestDto;
import com.example.demo.helper.PageResponseDto;
import com.example.demo.repository.QuestionRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;


    public Question register(QuestionReqDto dto) throws NotFoundException {
        User user = userService.findByUsername(dto.getWriter());

        // User 객체를 toEntity 메서드에 전달하여 Question 객체 생성
        Question question = QuestionReqDto.toEntity(dto, user);

        return questionRepository.save(question);
    }


    public PageResponseDto<QuestionRespDto> getList(PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.getPageable(Sort.by("id").descending());

        BooleanBuilder booleanBuilder = getSearch(pageRequestDto);

        Page<Question> result = questionRepository.findAll(booleanBuilder, pageable);
        Page<QuestionRespDto> dtoPage = result.map(QuestionRespDto::toDto);

        return new PageResponseDto<>(dtoPage);
    }

    private BooleanBuilder getSearch(PageRequestDto pageRequestDto) {
        String type = pageRequestDto.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QQuestion qQuestion = QQuestion.question;
        String keyword = pageRequestDto.getKeyword();
        BooleanExpression expression = qQuestion.id.gt(0L);  // id > 0 조건만
        booleanBuilder.and(expression);

        // 검색 조건 없는 경우
        if (type == null || type.trim().isEmpty()) {
            return booleanBuilder;
        }

        // 검색 조건이 있는 경우
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qQuestion.subject.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qQuestion.content.contains(keyword));
        }

        if(type.contains("w")){
            conditionBuilder.or(qQuestion.user.username.contains(keyword));
        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }




    public QuestionRespDto read(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new com.example.demo.exception.NotFoundException("Question id is not found"));

        return QuestionRespDto.toDto(question);
    }


    public void modify(Long id, QuestionReqDto dto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new com.example.demo.exception.NotFoundException("Question id is not found"));

        question.changeSubject(dto.getSubject());
        question.changeContent(dto.getContent());

        questionRepository.save(question);
    }


    public void remove(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new com.example.demo.exception.NotFoundException("Question id is not found"));

        questionRepository.deleteById(id);
    }



}
