package guh.hug.voter.controller;


import guh.hug.voter.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;


    @PostMapping("/{optionId}")
    public ResponseEntity<?> voteOnOption(
            @PathVariable Long optionId,
            @RequestParam String username) {
        try {
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Username must not be empty.");
            }
            voteService.addVote(optionId, username);
            return ResponseEntity.ok("Vote recorded successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<?> deleteVote(@PathVariable Long voteId) {
        try {
            voteService.deleteVote(voteId);
            return ResponseEntity.ok("Vote deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{voteId}/update")
    public ResponseEntity<?> updateVote(
            @PathVariable Long voteId,
            @RequestParam String username,
            @RequestParam Long optionId) {
        try {
            if (username == null || username.isEmpty()) {
                return ResponseEntity.badRequest().body("Username must not be empty.");
            }
            voteService.updateVote(voteId, optionId, username);
            return ResponseEntity.ok("Vote updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}