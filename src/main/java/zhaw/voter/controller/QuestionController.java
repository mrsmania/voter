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
        return ResponseEntity.ok(questionService.createQuestion(text, multipleChoice));
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestion (@PathVariable long questionId) {
        return ResponseEntity.ok(questionService.findQuestion(questionId));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<Question> updateQuestion(@PathVariable long questionId, @RequestBody Question updatedQuestion) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, updatedQuestion));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/question-ids")
    public ResponseEntity<List<Long>> getAllQuestionIds() {
        return ResponseEntity.ok(questionService.getAllQuestionIds());
    }

    @PostMapping("/{questionId}/add-option/{optionId}")
    public ResponseEntity<Question> addOption(@PathVariable long questionId, @PathVariable long optionId) {
        return ResponseEntity.ok(questionService.addOption(questionId, optionId));
    }

    @DeleteMapping("/{questionId/remove-option/{optionId}")
    public ResponseEntity<Void> removeOption(@PathVariable long questionId, @PathVariable long optionId) {
        questionService.removeOption(questionId, optionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-questions")
    public ResponseEntity<List<QuestionDTO>> uploadQuestions(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(questionService.verifyAndParseQuestions(file));
    }
}