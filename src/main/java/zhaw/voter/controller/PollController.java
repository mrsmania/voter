package zhaw.voter.controller;

import org.springframework.http.*;
import zhaw.voter.model.Poll;
import zhaw.voter.service.PollService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poll")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Poll>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @PostMapping("/create")
    public ResponseEntity<Poll> createPoll(@RequestParam String hostUserEmail) {
        Poll poll = pollService.createPoll(hostUserEmail);
        return ResponseEntity.ok(poll);
    }

    @GetMapping("/{token}")
    public ResponseEntity<Poll> getPollByToken(@PathVariable String token) {
        Poll poll = pollService.getPollByToken(token);
        return ResponseEntity.ok(poll);
    }

    @PostMapping("/save")
    public ResponseEntity<Poll> savePoll(@RequestBody Poll poll) {
        Poll savedPoll = pollService.savePoll(poll);
        return ResponseEntity.ok(savedPoll);
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable long pollId) {
        pollService.deletePoll(pollId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/poll-ids")
    public ResponseEntity<List<Long>> getAllPollIds() {
        List<Long> pollIds = pollService.getAllPollIds();
        return ResponseEntity.ok(pollIds);
    }

    @GetMapping("/tokens")
    public ResponseEntity<List<String>> getAllTokens() {
        List<String> tokens = pollService.getAllTokens();
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/{pollId}/add-question/{questionId}")
    public ResponseEntity<Poll> addQuestion(@PathVariable long pollId, @PathVariable long questionId) {
        Poll poll = pollService.addQuestion(pollId, questionId);
        return ResponseEntity.ok(poll);
    }

    @DeleteMapping("/{pollId}/remove-question/{questionId}")
    public ResponseEntity<Void> removeQuestion(@PathVariable long pollId, @PathVariable long questionId) {
        pollService.removeQuestion(pollId, questionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{token}/export")
    public ResponseEntity<byte[]> exportPollResults(@PathVariable String token) {
        String csvData = pollService.generatePollResultsCSV(token);
        byte[] csvBytes = csvData.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("poll-results.csv").build());
        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }

    @GetMapping
    public ResponseEntity<Poll> getPoll(@RequestParam String token, @RequestParam String password, @RequestParam String email) {
        Poll poll = pollService.findPollByTokenAndPasswordAndEmail(token, password, email);
        return ResponseEntity.ok(poll);
    }
}
