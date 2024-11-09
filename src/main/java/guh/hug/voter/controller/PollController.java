package guh.hug.voter.controller;


import guh.hug.voter.model.Question;
import guh.hug.voter.model.Poll;
import guh.hug.voter.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/api/poll")
public class PollController {


    @Autowired
    private PollService pollService;

    @GetMapping("/all")
    public Iterable<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }



    @GetMapping( "/{token}")
    public ResponseEntity<?> getPollByToken(@PathVariable String token) {
        try {
            Poll poll = pollService.getPollByToken(token);
            return ResponseEntity.ok(poll);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.requireNonNull(e.getReason())));
        }
    }


    @PostMapping("/save")
    public ResponseEntity<?> savePoll(@RequestBody Poll poll) {
        try {
            Poll savedPoll = pollService.savePoll(poll);
            return ResponseEntity.ok(savedPoll);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.requireNonNull(e.getReason())));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> createPoll(@RequestParam String hostUserEmail) {
        try {
            Poll poll = pollService.createPoll(hostUserEmail);
            return ResponseEntity.ok(poll);
        }
        catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.requireNonNull(e.getReason())));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @GetMapping
    public ResponseEntity<?> getPoll(@RequestParam String token, @RequestParam String password, @RequestParam String email) {
        try {
            Poll poll = pollService.findPollByTokenAndPasswordAndEmail(token, password, email);
            return ResponseEntity.ok(poll);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.requireNonNull(e.getReason())));
        }
    }

}
