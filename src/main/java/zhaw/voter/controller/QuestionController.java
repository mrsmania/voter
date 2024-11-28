package zhaw.voter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zhaw.voter.dto.QuestionDTO;
import zhaw.voter.model.Question;
import zhaw.voter.service.QuestionService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @PostMapping("/create")
    public ResponseEntity<Question> createQuestion(@RequestBody String text, @RequestParam boolean multipleChoice) {
        Question question = questionService.createQuestion(text, multipleChoice);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestion (@PathVariable long questionId) {
        Question question = questionService.findQuestion(questionId);
        return ResponseEntity.ok(question);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<Question> updateQuestion(@PathVariable long questionId, @RequestBody Question updatedQuestion) {
        Question question = questionService.updateQuestion(questionId, updatedQuestion);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/question-ids")
    public ResponseEntity<List<Long>> getAllQuestionIds() {
        List<Long> questionIds = questionService.getAllQuestionIds();
        return ResponseEntity.ok(questionIds);
    }

    @PostMapping("/upload-questions")
    public ResponseEntity<List<QuestionDTO>> uploadQuestions(@RequestParam("file") MultipartFile file) throws IOException {
        List<QuestionDTO> questions = questionService.verifyAndParseQuestions(file);
        return ResponseEntity.ok(questions);
    }
}