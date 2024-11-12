package zhaw.voter.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
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


    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<?> toggleVote(@RequestParam String userEmail, @RequestParam Long optionId) {
        try {
            Vote vote = voteService.toggleVote(userEmail, optionId);
            messagingTemplate.convertAndSend("/topic/votes", "Vote updated");
            return ResponseEntity.ok(Map.of("message", "Vote toggled"));
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

    @GetMapping("/counts")
    public ResponseEntity<?> getUpdatedVoteCounts(@RequestParam Long pollId) {
        return ResponseEntity.ok(voteService.getUpdatedVoteCounts(pollId));
    }
}
