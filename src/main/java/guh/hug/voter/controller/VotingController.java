package guh.hug.voter.controller;


import guh.hug.voter.model.Question;
import guh.hug.voter.model.Voting;
import guh.hug.voter.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/voting")
public class VotingController {

    @Autowired
    private VotingService votingService;

    @PostMapping("/create")
    public Voting createVoting(@RequestParam String hostUsername) {
        if (hostUsername == null || hostUsername.isEmpty()) {
            throw new IllegalArgumentException("Host username must not be empty.");
        }

        return votingService.createVoting(hostUsername);
    }

    @PostMapping("/{token}/addQuestion")
    public ResponseEntity<?> addQuestion(@PathVariable String token, @RequestBody Question question) {
        if (question.getOptions() == null || question.getOptions().size() <= 1) {
            // Return a 400 Bad Request response with an error message
            return ResponseEntity.badRequest().body("A question must have at least 2 options.");
        }

        try {
            Voting voting = votingService.addQuestion(token, question);
            return ResponseEntity.ok(voting);
        } catch (RuntimeException e) {
            // Return a 404 or other relevant error if the voting token is invalid
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
