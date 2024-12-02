package zhaw.voter.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import zhaw.voter.model.Vote;
import zhaw.voter.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;
    private final SimpMessagingTemplate messagingTemplate;

    public VoteController(VoteService voteService, SimpMessagingTemplate messagingTemplate) {
        this.voteService = voteService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Vote>> getAllVotes() {
        return ResponseEntity.ok(voteService.getAllVotes());
    }

    @PostMapping("/create")
    public ResponseEntity<Vote> createVote(@RequestBody String userEmail) {
        return ResponseEntity.ok(voteService.createVote(userEmail));
    }

    @GetMapping("/{voteId}")
    public ResponseEntity<Vote> getVote(@PathVariable long voteId) {
        return ResponseEntity.ok(voteService.findVote(voteId));
    }

    @PutMapping("/{voteId}")
    public ResponseEntity<Vote> updateVote(@PathVariable long voteId, @RequestBody Vote updatedVote) {
        return ResponseEntity.ok(voteService.updateVote(voteId, updatedVote));
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<Void> deleteVote(@PathVariable long voteId) {
        voteService.deleteVote(voteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vote-ids")
    public ResponseEntity<List<Long>> getAllVoteIds() {
        return ResponseEntity.ok(voteService.getAllVoteIds());
    }

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
