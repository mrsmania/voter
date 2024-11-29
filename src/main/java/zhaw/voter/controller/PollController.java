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
        return ResponseEntity.ok(pollService.createPoll(hostUserEmail));
    }

    @GetMapping("/{token}")
    public ResponseEntity<Poll> getPollByToken(@PathVariable String token) {
        return ResponseEntity.ok(pollService.getPollByToken(token));
    }

    @PostMapping("/save")
    public ResponseEntity<Poll> savePoll(@RequestBody Poll poll) {
        return ResponseEntity.ok(pollService.savePoll(poll));
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable long pollId) {
        pollService.deletePoll(pollId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/poll-ids")
    public ResponseEntity<List<Long>> getAllPollIds() {
        return ResponseEntity.ok(pollService.getAllPollIds());
    }

    @GetMapping("/tokens")
    public ResponseEntity<List<String>> getAllTokens() {
        return ResponseEntity.ok(pollService.getAllTokens());
    }

    @PostMapping("/{pollId}/add-question/{questionId}")
    public ResponseEntity<Poll> addQuestion(@PathVariable long pollId, @PathVariable long questionId) {
        return ResponseEntity.ok(pollService.addQuestion(pollId, questionId));
    }

    @DeleteMapping("/{pollId}/remove-question/{questionId}")
    public ResponseEntity<Void> removeQuestion(@PathVariable long pollId, @PathVariable long questionId) {
        pollService.removeQuestion(pollId, questionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{token}/export")
    public ResponseEntity<byte[]> exportPollResults(@PathVariable String token) {
        byte[] csvBytes = pollService.generatePollResultsCSV(token).getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("poll-results.csv").build());
        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }

    @GetMapping
    public ResponseEntity<Poll> getPoll(@RequestParam String token, @RequestParam String password, @RequestParam String email) {
        return ResponseEntity.ok(pollService.findPollByTokenAndPasswordAndEmail(token, password, email));
    }
}
