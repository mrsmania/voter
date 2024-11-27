package zhaw.voter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import zhaw.voter.dto.QuestionDTO;
import zhaw.voter.service.QuestionService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping("/upload-questions")
    public ResponseEntity<List<QuestionDTO>> uploadQuestions(@RequestParam("file") MultipartFile file) throws IOException {
        List<QuestionDTO> questions = questionService.verifyAndParseQuestions(file);
        return ResponseEntity.ok(questions);
    }
}