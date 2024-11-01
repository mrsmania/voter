package guh.hug.voter.controller;


import guh.hug.voter.model.Question;
import guh.hug.voter.model.Poll;
import guh.hug.voter.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/poll")
public class PollController {

    @Autowired
    private PollService pollService;

    @GetMapping("/all")
    public Iterable<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }

    @PostMapping("/create")
    public Poll createPoll(@RequestParam String hostUsername) {
        if (hostUsername == null || hostUsername.isEmpty()) {
            throw new IllegalArgumentException("Host username must not be empty.");
        }
        return pollService.createPoll(hostUsername);
    }

    @PostMapping("/{token}/addQuestion")
    public ResponseEntity<?> addQuestion(@PathVariable String token, @RequestBody Question question) {
        try {
            if (question == null) {
                return ResponseEntity.badRequest().body("Question must not be null.");
            }
            if (question.getText() == null || question.getText().isEmpty()) {
                return ResponseEntity.badRequest().body("Question text must not be empty.");
            }
            if (question.getOptions() == null || question.getOptions().size() <= 1) {
                return ResponseEntity.badRequest().body("A question must have at least 2 options.");
            }
            Poll poll = pollService.addQuestion(token, question);
            return ResponseEntity.ok(poll);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{token}/removeQuestion/{questionId}")
    public ResponseEntity<?> removeQuestion(@PathVariable String token, @PathVariable Long questionId) {
        try {
            Poll updatedPoll = pollService.removeQuestion(token, questionId);
            return ResponseEntity.ok(updatedPoll);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
