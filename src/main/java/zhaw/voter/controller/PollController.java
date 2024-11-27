package zhaw.voter.controller;

import org.springframework.http.*;
import zhaw.voter.model.Poll;
import zhaw.voter.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poll")
public class PollController {

    @Autowired
    PollService pollService;

    @GetMapping("/all")
    public ResponseEntity<List<Poll>> getAllPolls() {
        List<Poll> polls = pollService.getAllPolls();
        return ResponseEntity.ok(polls);
    }

    @GetMapping("/tokens")
    public ResponseEntity<List<String>> getAllTokens() {
        List<String> tokens = pollService.getAllTokens();
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/{token}")
    public ResponseEntity<Poll> getPollByToken(@PathVariable String token) {
        Poll poll = pollService.getPollByToken(token);
        return ResponseEntity.ok(poll);
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

    @PostMapping("/save")
    public ResponseEntity<Poll> savePoll(@RequestBody Poll poll) {
        Poll savedPoll = pollService.savePoll(poll);
        return ResponseEntity.ok(savedPoll);
    }

    @PostMapping("/create")
    public ResponseEntity<Poll> createPoll(@RequestParam String hostUserEmail) {
        Poll poll = pollService.createPoll(hostUserEmail);
        return ResponseEntity.ok(poll);
    }

    @GetMapping
    public ResponseEntity<Poll> getPoll(@RequestParam String token, @RequestParam String password, @RequestParam String email) {
        Poll poll = pollService.findPollByTokenAndPasswordAndEmail(token, password, email);
        return ResponseEntity.ok(poll);
    }

}
