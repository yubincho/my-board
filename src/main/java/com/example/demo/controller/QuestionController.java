package com.example.demo.controller;


import com.example.demo.dto.QuestionReqDto;
import com.example.demo.dto.QuestionRespDto;
import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helper.PageRequestDto;
import com.example.demo.helper.PageResponseDto;
import com.example.demo.service.ImageService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@RequiredArgsConstructor
@Controller("questionController")
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final ImageService imageService;


    @GetMapping("/list")
    public String list(PageRequestDto pageRequestDto, Model model) {
        PageResponseDto<QuestionRespDto> questionList = questionService.getList(pageRequestDto);
        System.out.println("[questionList]" + questionList);
        model.addAttribute("questionList", questionList);
        model.addAttribute("pageRequestDto", pageRequestDto);
        return "question_list";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerQna(QuestionReqDto dto, RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) throws IOException {
        try {
            Question question = questionService.register(dto);
            System.out.println("[question]" + question);
            imageService.saveImage(file, question, true); // 첫 번째 이미지를 썸네일로 저장
            redirectAttributes.addFlashAttribute("msg", "Question registered successfully!");
        } catch (NotFoundException | javassist.NotFoundException e) {
            redirectAttributes.addFlashAttribute("msg", "Error occurred while registering questio");
        }
        return "redirect:/question/list";
    }


    @GetMapping("/read/{id}")
    public String read(@PathVariable Long id, @ModelAttribute("pageRequestDto") PageRequestDto pageRequestDto, Model model) {
        QuestionRespDto dto = questionService.read(id);

        model.addAttribute("dto", dto);
        model.addAttribute("pageRequestDto", pageRequestDto);
        return "read";
    }

    @GetMapping("/modify/{id}")
    public String moveToModifyPage(@PathVariable Long id, @ModelAttribute("pageRequestDto") PageRequestDto pageRequestDto, Model model) {
        QuestionRespDto dto = questionService.read(id);

        model.addAttribute("dto", dto);
        model.addAttribute("pageRequestDto", pageRequestDto);
        return "modify";
    }


    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @ModelAttribute QuestionReqDto dto, @ModelAttribute("pageRequestDto") PageRequestDto pageRequestDto,
                         Model model, RedirectAttributes redirectAttributes) {
        questionService.modify(id, dto);
        redirectAttributes.addFlashAttribute("msg", "Question is modified successfully!.");
        redirectAttributes.addAttribute("page", pageRequestDto.getPage());
        redirectAttributes.addAttribute("id", id);
        return "redirect:/question/read/{id}";
    }


    @PostMapping("/delete/{id}")
    public String remove(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        questionService.remove(id);
        redirectAttributes.addFlashAttribute("msg", "Question is deleted successfully!");
        return "redirect:/question/list";
    }

}
