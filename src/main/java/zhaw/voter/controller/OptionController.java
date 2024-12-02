package zhaw.voter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zhaw.voter.model.Option;
import zhaw.voter.service.OptionService;

import java.util.List;

@RestController
@RequestMapping("/api/option")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Option>> getAllOptions() {
        return ResponseEntity.ok(optionService.getAllOptions());
    }

    @PostMapping("/create")
    public ResponseEntity<Option> createOption(@RequestBody String text) {
        return ResponseEntity.ok(optionService.createOption(text));
    }

    @GetMapping("/{optionId}")
    public ResponseEntity<Option> getOption(@PathVariable long optionId) {
        return ResponseEntity.ok(optionService.findOption(optionId));
    }

    @PutMapping("/{optionId}")
    public ResponseEntity<Option> updateOption(@PathVariable long optionId, @RequestBody Option updatedOption) {
        return ResponseEntity.ok(optionService.updateOption(optionId, updatedOption));
    }

    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/option-ids")
    public ResponseEntity<List<Long>> getAllOptionIds() {
        return ResponseEntity.ok(optionService.getAllOptionIds());
    }

    @PostMapping("/{optionId}/add-vote/{voteId}")
    public ResponseEntity<Option> addVote(@PathVariable long optionId, @PathVariable long voteId) {
        return ResponseEntity.ok(optionService.addVote(optionId, voteId));
    }

    @DeleteMapping("/{optionId}/remove-vote/{voteId}")
    public ResponseEntity<Void> removeVote(@PathVariable long optionId, @PathVariable long voteId) {
        optionService.removeVote(optionId, voteId);
        return ResponseEntity.noContent().build();
    }
}
