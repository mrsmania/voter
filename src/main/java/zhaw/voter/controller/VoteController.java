package zhaw.voter.controller;

import zhaw.voter.model.Vote;
import zhaw.voter.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping
    public ResponseEntity<?> toggleVote(@RequestParam String userEmail, @RequestParam Long optionId) {
        try {
            Vote vote = voteService.toggleVote(userEmail, optionId);
            if (vote == null) {
                return ResponseEntity.ok(Map.of("message", "Vote removed"));
            }
            return ResponseEntity.ok(vote);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.requireNonNull(e.getReason())));
        }
    }

    @GetMapping("/votes")
    public ResponseEntity<?> getVotesByUserAndPollId(@RequestParam String userEmail, @RequestParam Long pollId) {

        try {
            return ResponseEntity.ok(voteService.votesByUserEmailAndPollId(userEmail, pollId));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", Objects.requireNonNull(e.getReason())));
        }
    }

}
