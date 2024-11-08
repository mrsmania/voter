package guh.hug.voter.controller;


import guh.hug.voter.model.Question;
import guh.hug.voter.model.Poll;
import guh.hug.voter.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api/poll")
public class PollController {

    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private PollService pollService;

    @GetMapping("/all")
    public Iterable<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }



    @GetMapping( "/{token}/get")
    public ResponseEntity<?> getPollByToken(@PathVariable String token) {
        try {
            Poll poll = pollService.getPollByToken(token);
            return ResponseEntity.ok(poll);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createPoll(@RequestParam String hostUserEmail) {
        try {
            if (hostUserEmail == null || hostUserEmail.isEmpty()) {
                return ResponseEntity.badRequest().body("Host username must not be empty.");
            }
            if (!isValidEmail(hostUserEmail)) {
                return ResponseEntity.badRequest().body("Invalid email address.");
            }
            Poll poll = pollService.createPoll(hostUserEmail);
            return ResponseEntity.ok(poll);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

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

    @PostMapping("/{token}/activate")
    public ResponseEntity<?> activatePoll(@PathVariable String token) {
        try {
            Poll updatedPoll = pollService.activatePoll(token);
            return ResponseEntity.ok(updatedPoll);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{token}/deactivate")
    public ResponseEntity<?> deactivatePoll(@PathVariable String token) {
        try {
            Poll updatedPoll = pollService.deactivatePoll(token);
            return ResponseEntity.ok(updatedPoll);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
