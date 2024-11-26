package zhaw.voter.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import zhaw.voter.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping
    public ResponseEntity<Map<String, String>> toggleVote(@RequestParam String userEmail, @RequestParam Long optionId) {
        voteService.toggleVote(userEmail, optionId);
        messagingTemplate.convertAndSend("/topic/votes", "Vote updated");
        return ResponseEntity.ok(Map.of("message", "Vote toggled"));
    }

    @GetMapping("/votes")
    public ResponseEntity<?> getVotesByUserAndPollId(@RequestParam String userEmail, @RequestParam Long pollId) {
        return ResponseEntity.ok(voteService.votesByUserEmailAndPollId(userEmail, pollId));
    }

    @GetMapping("/counts")
    public ResponseEntity<?> getUpdatedVoteCounts(@RequestParam Long pollId) {
        return ResponseEntity.ok(voteService.getUpdatedVoteCounts(pollId));
    }
}
