package zhaw.voter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zhaw.voter.model.Option;
import zhaw.voter.service.OptionService;

import java.util.List;

@Tag(name = "Option API", description = "Endpoints for managing options")
@RestController
@RequestMapping("/api/option")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "Get all options", description = "Fetches a list of all options in the system.")
    @GetMapping("/all")
    public ResponseEntity<List<Option>> getAllOptions() {
        return ResponseEntity.ok(optionService.getAllOptions());
    }

    @Operation(summary = "Create a new option", description = "Creates a new option with the given text.")
    @PostMapping("/create")
    public ResponseEntity<Option> createOption(
            @RequestBody @Parameter(description = "Text of the option to create") String text) {
        return ResponseEntity.ok(optionService.createOption(text));
    }

    @Operation(summary = "Get an option by ID", description = "Fetches the option associated with the given ID.")
    @GetMapping("/{optionId}")
    public ResponseEntity<Option> getOption(
            @PathVariable @Parameter(description = "ID of the option to retrieve") long optionId) {
        return ResponseEntity.ok(optionService.findOption(optionId));
    }

    @Operation(summary = "Update an option", description = "Updates the details of an option with the given ID.")
    @PutMapping("/{optionId}")
    public ResponseEntity<Option> updateOption(
            @PathVariable @Parameter(description = "ID of the option to update") long optionId,
            @RequestBody @Parameter(description = "Updated option object") Option updatedOption) {
        return ResponseEntity.ok(optionService.updateOption(optionId, updatedOption));
    }

    @Operation(summary = "Delete an option", description = "Deletes the option with the specified ID.")
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(
            @PathVariable @Parameter(description = "ID of the option to delete") long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all option IDs", description = "Fetches a list of IDs for all options.")
    @GetMapping("/option-ids")
    public ResponseEntity<List<Long>> getAllOptionIds() {
        return ResponseEntity.ok(optionService.getAllOptionIds());
    }

    @Operation(summary = "Add a vote to an option", description = "Adds a vote to an option by their respective IDs.")
    @PostMapping("/{optionId}/add-vote/{voteId}")
    public ResponseEntity<Option> addVote(
            @PathVariable @Parameter(description = "ID of the option") long optionId,
            @PathVariable @Parameter(description = "ID of the vote to add") long voteId) {
        return ResponseEntity.ok(optionService.addVote(optionId, voteId));
    }

    @Operation(summary = "Remove a vote from an option", description = "Removes a vote from an option by their respective IDs.")
    @DeleteMapping("/{optionId}/remove-vote/{voteId}")
    public ResponseEntity<Void> removeVote(
            @PathVariable @Parameter(description = "ID of the option") long optionId,
            @PathVariable @Parameter(description = "ID of the vote to remove") long voteId) {
        optionService.removeVote(optionId, voteId);
        return ResponseEntity.noContent().build();
    }
}
