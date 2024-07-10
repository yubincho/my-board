package com.example.demo.controller;


import com.example.demo.dto.QuestionReqDto;
import com.example.demo.dto.QuestionRespDto;
import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.helper.PageRequestDto;
import com.example.demo.helper.PageResponseDto;
import com.example.demo.service.QuestionService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequiredArgsConstructor
@Controller("questionController")
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;


    @GetMapping("/list")
    public String list(PageRequestDto pageRequestDto, Model model) {
        PageResponseDto<QuestionRespDto> questionList = questionService.getList(pageRequestDto);
        model.addAttribute("questionList", questionList);
        model.addAttribute("pageRequestDto", pageRequestDto);
        return "question_list";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerQna(QuestionReqDto dto, RedirectAttributes redirectAttributes) {
        try {
            questionService.register(dto);
            redirectAttributes.addFlashAttribute("msg", "Question registered successfully!");
        } catch (NotFoundException | javassist.NotFoundException e) {
            redirectAttributes.addFlashAttribute("msg", "User not found");
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
