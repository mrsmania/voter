package zhaw.voter.controller;


import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import zhaw.voter.dto.QuestionDTO;
import zhaw.voter.model.Poll;
import zhaw.voter.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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

    @GetMapping("/{token}/export")
    public ResponseEntity<byte[]> exportPollResults(@PathVariable String token) {
        try {
            String csvData = pollService.generatePollResultsCSV(token);
            byte[] csvBytes = csvData.getBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("poll-results.csv").build());

            return ResponseEntity.ok().headers(headers).body(csvBytes);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }

    @PostMapping("/upload-questions")
    public ResponseEntity<?> uploadQuestions(@RequestParam("file") MultipartFile file) {
        try {
            List<QuestionDTO> questions = pollService.verifyAndParseQuestions(file);
            return ResponseEntity.ok(questions);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.requireNonNull(e.getReason())));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid file format"));
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
