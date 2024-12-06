package zhaw.voter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import zhaw.voter.model.Vote;
import zhaw.voter.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Vote API", description = "Endpoints for managing votes")
@RestController
@RequestMapping("/api/vote")
public class VoteController {

    private final VoteService voteService;
    private final SimpMessagingTemplate messagingTemplate;

    public VoteController(VoteService voteService, SimpMessagingTemplate messagingTemplate) {
        this.voteService = voteService;
        this.messagingTemplate = messagingTemplate;
    }

    @Operation(summary = "Retrieve all votes", description = "Fetches a list of all votes stored in the system")
    @GetMapping("/all")
    public ResponseEntity<List<Vote>> getAllVotes() {
        return ResponseEntity.ok(voteService.getAllVotes());
    }

    @Operation(summary = "Create a new vote", description = "Creates a vote associated with a specific user email")
    @PostMapping("/create")
    public ResponseEntity<Vote> createVote(
            @Parameter(description = "Email address of the user") @RequestBody String userEmail) {
        return ResponseEntity.ok(voteService.createVote(userEmail));
    }

    @Operation(summary = "Retrieve a specific vote", description = "Fetches a vote by its ID")
    @GetMapping("/{voteId}")
    public ResponseEntity<Vote> getVote(
            @Parameter(description = "ID of the vote to retrieve") @PathVariable long voteId) {
        return ResponseEntity.ok(voteService.findVote(voteId));
    }

    @Operation(summary = "Update a vote", description = "Updates an existing vote by its ID")
    @PutMapping("/{voteId}")
    public ResponseEntity<Vote> updateVote(
            @Parameter(description = "ID of the vote to update") @PathVariable long voteId,
            @Parameter(description = "Updated vote details") @RequestBody Vote updatedVote) {
        return ResponseEntity.ok(voteService.updateVote(voteId, updatedVote));
    }

    @Operation(summary = "Delete a vote", description = "Deletes a vote by its ID")
    @DeleteMapping("/{voteId}")
    public ResponseEntity<Void> deleteVote(
            @Parameter(description = "ID of the vote to delete") @PathVariable long voteId) {
        voteService.deleteVote(voteId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve all vote IDs", description = "Fetches a list of all vote IDs in the system")
    @GetMapping("/vote-ids")
    public ResponseEntity<List<Long>> getAllVoteIds() {
        return ResponseEntity.ok(voteService.getAllVoteIds());
    }

    @Operation(summary = "Toggle a vote", description = "Toggles a vote for a user and option")
    @PostMapping
    public ResponseEntity<Map<String, String>> toggleVote(
            @Parameter(description = "Email address of the user") @RequestParam String userEmail,
            @Parameter(description = "ID of the option to toggle the vote for") @RequestParam Long optionId) {
        voteService.toggleVote(userEmail, optionId);
        messagingTemplate.convertAndSend("/topic/votes", "Vote updated");
        return ResponseEntity.ok(Map.of("message", "Vote toggled"));
    }

    @Operation(summary = "Get user votes for a poll", description = "Fetches all votes for a specific user and poll")
    @GetMapping("/votes")
    public ResponseEntity<?> getVotesByUserAndPollId(
            @Parameter(description = "Email address of the user") @RequestParam String userEmail,
            @Parameter(description = "ID of the poll") @RequestParam Long pollId) {
        return ResponseEntity.ok(voteService.votesByUserEmailAndPollId(userEmail, pollId));
    }

    @Operation(summary = "Get vote counts for a poll", description = "Fetches updated vote counts for all options in a poll")
    @GetMapping("/counts")
    public ResponseEntity<?> getUpdatedVoteCounts(
            @Parameter(description = "ID of the poll") @RequestParam Long pollId) {
        return ResponseEntity.ok(voteService.getUpdatedVoteCounts(pollId));
    }
}
