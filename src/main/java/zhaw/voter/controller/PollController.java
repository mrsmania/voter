package zhaw.voter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.*;
import zhaw.voter.model.Poll;
import zhaw.voter.service.PollService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Poll API", description = "Endpoints for managing polls")
@RestController
@RequestMapping("/api/poll")
public class PollController {

    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @Operation(summary = "Get all polls", description = "Fetches a list of all polls in the system.")
    @GetMapping("/all")
    public ResponseEntity<List<Poll>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @Operation(summary = "Create a new poll", description = "Creates a new poll with the provided host user email.")
    @PostMapping("/create")
    public ResponseEntity<Poll> createPoll(
            @RequestParam @Parameter(description = "Email of the poll host") String hostUserEmail) {
        return ResponseEntity.ok(pollService.createPoll(hostUserEmail));
    }

    @Operation(summary = "Get a poll by token", description = "Fetches the poll associated with the given token.")
    @GetMapping("/{token}")
    public ResponseEntity<Poll> getPollByToken(
            @PathVariable @Parameter(description = "Token of the poll") String token) {
        return ResponseEntity.ok(pollService.getPollByToken(token));
    }

    @Operation(summary = "Save a poll", description = "Saves or updates the details of a poll.")
    @PostMapping("/save")
    public ResponseEntity<Poll> savePoll(
            @RequestBody @Parameter(description = "Poll object to save or update") Poll poll) {
        return ResponseEntity.ok(pollService.savePoll(poll));
    }

    @Operation(summary = "Delete a poll", description = "Deletes a poll by its unique identifier.")
    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(
            @PathVariable @Parameter(description = "ID of the poll to delete") long pollId) {
        pollService.deletePoll(pollId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all poll IDs", description = "Fetches a list of IDs for all polls.")
    @GetMapping("/poll-ids")
    public ResponseEntity<List<Long>> getAllPollIds() {
        return ResponseEntity.ok(pollService.getAllPollIds());
    }

    @Operation(summary = "Get all poll tokens", description = "Fetches a list of tokens for all polls.")
    @GetMapping("/tokens")
    public ResponseEntity<List<String>> getAllTokens() {
        return ResponseEntity.ok(pollService.getAllTokens());
    }

    @Operation(summary = "Add a question to a poll", description = "Adds an existing question to a specified poll.")
    @PostMapping("/{pollId}/add-question/{questionId}")
    public ResponseEntity<Poll> addQuestion(
            @PathVariable @Parameter(description = "ID of the poll") long pollId,
            @PathVariable @Parameter(description = "ID of the question to add") long questionId) {
        return ResponseEntity.ok(pollService.addQuestion(pollId, questionId));
    }

    @Operation(summary = "Remove a question from a poll", description = "Removes an existing question from a specified poll.")
    @DeleteMapping("/{pollId}/remove-question/{questionId}")
    public ResponseEntity<Void> removeQuestion(
            @PathVariable @Parameter(description = "ID of the poll") long pollId,
            @PathVariable @Parameter(description = "ID of the question to remove") long questionId) {
        pollService.removeQuestion(pollId, questionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Export poll results as CSV", description = "Generates a CSV file with the results of a poll identified by its token.")
    @GetMapping("/{token}/export")
    public ResponseEntity<byte[]> exportPollResults(
            @PathVariable @Parameter(description = "Token of the poll to export results for") String token) {
        byte[] csvBytes = pollService.generatePollResultsCSV(token).getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("poll-results.csv").build());
        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }

    @Operation(summary = "Get a poll with authentication", description = "Fetches a poll using its token, password, and email.")
    @GetMapping
    public ResponseEntity<Poll> getPoll(
            @RequestParam @Parameter(description = "Token of the poll") String token,
            @RequestParam @Parameter(description = "Password for the poll") String password,
            @RequestParam @Parameter(description = "Email of the poll host") String email) {
        return ResponseEntity.ok(pollService.findPollByTokenAndPasswordAndEmail(token, password, email));
    }
}
