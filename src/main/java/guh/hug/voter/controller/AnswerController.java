package guh.hug.voter.controller;


import guh.hug.voter.model.Answer;
import guh.hug.voter.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public Answer saveAnswer(@RequestBody Answer answer) {
        return answerService.saveAnswer(answer);
    }
}