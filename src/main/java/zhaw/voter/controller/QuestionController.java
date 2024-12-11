package zhaw.voter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zhaw.voter.dto.QuestionDTO;
import zhaw.voter.model.Question;
import zhaw.voter.service.QuestionService;

import java.io.IOException;
import java.util.List;

@Tag(name = "Question API", description = "Endpoints for managing questions")
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "Get all questions", description = "Fetches a list of all questions in the system.")
    @GetMapping("/all")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @Operation(summary = "Create a question", description = "Creates a new question with the provided text and multiple choice flag.")
    @PostMapping("/create")
    public ResponseEntity<Question> createQuestion(
            @RequestBody @Parameter(description = "Text of the question") String text,
            @RequestParam @Parameter(description = "Flag to indicate if multiple choice is allowed") boolean multipleChoice) {
        return ResponseEntity.ok(questionService.createQuestion(text, multipleChoice));
    }

    @Operation(summary = "Get a question by ID", description = "Fetches details of a question by its unique identifier.")
    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestion(
            @PathVariable @Parameter(description = "ID of the question to retrieve") long questionId) {
        return ResponseEntity.ok(questionService.findQuestion(questionId));
    }

    @Operation(summary = "Update a question", description = "Updates an existing question by its unique identifier.")
    @PutMapping("/{questionId}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable @Parameter(description = "ID of the question to update") long questionId,
            @RequestBody @Parameter(description = "Updated question details") Question updatedQuestion) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, updatedQuestion));
    }

    @Operation(summary = "Delete a question", description = "Deletes a question by its unique identifier.")
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable @Parameter(description = "ID of the question to delete") long questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all question IDs", description = "Fetches a list of IDs for all questions.")
    @GetMapping("/question-ids")
    public ResponseEntity<List<Long>> getAllQuestionIds() {
        return ResponseEntity.ok(questionService.getAllQuestionIds());
    }

    @Operation(summary = "Add an option to a question", description = "Adds an existing option to a specified question.")
    @PostMapping("/{questionId}/add-option/{optionId}")
    public ResponseEntity<Question> addOption(
            @PathVariable @Parameter(description = "ID of the question") long questionId,
            @PathVariable @Parameter(description = "ID of the option to add") long optionId) {
        return ResponseEntity.ok(questionService.addOption(questionId, optionId));
    }

    @Operation(summary = "Remove an option from a question", description = "Removes an existing option from a specified question.")
    @DeleteMapping("/{questionId}/remove-option/{optionId}")
    public ResponseEntity<Void> removeOption(
            @PathVariable @Parameter(description = "ID of the question") long questionId,
            @PathVariable @Parameter(description = "ID of the option to remove") long optionId) {
        questionService.removeOption(questionId, optionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve total number of votes for this question", description = "Fetches the total number of votes for this question.")
    @GetMapping("/{id}/total-votes")
    public ResponseEntity<Integer> getTotalVotes(
            @PathVariable @Parameter(description = "ID of the question") long id) {
        return ResponseEntity.ok(questionService.getTotalVotesByQuestionId(id));
    }

    @Operation(summary = "Upload questions via file", description = "Uploads and parses a file to create multiple questions at once.")
    @PostMapping("/upload-questions")
    public ResponseEntity<List<QuestionDTO>> uploadQuestions(
            @RequestParam("file") @Parameter(description = "File containing questions in the specified format") MultipartFile file) throws IOException {
        return ResponseEntity.ok(questionService.verifyAndParseQuestions(file));
    }
}
