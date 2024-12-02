package zhaw.voter.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import zhaw.voter.model.Option;
import zhaw.voter.model.Vote;
import zhaw.voter.repository.OptionRepository;
import zhaw.voter.repository.VoteRepository;
import zhaw.voter.util.InputValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final VoteService voteService;
    private final VoteRepository voteRepository;

    public OptionService(OptionRepository optionRepository, VoteService voteService, VoteRepository voteRepository) {
        this.optionRepository = optionRepository;
        this.voteService = voteService;
        this.voteRepository = voteRepository;
    }

    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    public Option createOption(String text) {
        Option option = new Option();
        option.setText(text);
        return optionRepository.save(option);
    }

    public Option updateOption(long optionId, Option updatedOption) {
        InputValidator.validateInput(updatedOption.getText(), "Option text cannot be empty");
        Option existingOption = optionRepository.findById(optionId).orElseThrow(() -> new EntityNotFoundException("Option with id " + optionId + " not found"));
        existingOption.setText(updatedOption.getText());
        return optionRepository.save(existingOption);
    }

    public Option findOption(long optionId) {
        return optionRepository.findById(optionId).orElseThrow(() -> new EntityNotFoundException("Option with id " + optionId + " not found"));
    }

    public void deleteOption(long optionId) {
        try {
            optionRepository.deleteById(optionId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Option with id " + optionId + " not found");
        }
    }

    public List<Long> getAllOptionIds() {
        List<Option> options = optionRepository.findAll();
        if (options.isEmpty()) {
            throw new EntityNotFoundException("No options found");
        }
        return options.stream().map(Option::getId).collect(Collectors.toList());
    }

    public Option addVote(long optionId, long voteId) {
        Option option = findOption(optionId);
        Vote vote = voteService.findVote(voteId);
        validateOptionContainsVote(option, vote);
        option.addVote(vote);
        voteRepository.save(vote);
        return optionRepository.save(option);
    }

    public void removeVote(long optionId, long voteId) {
        Option option = findOption(optionId);
        Vote vote = voteService.findVote(voteId);
        validateOptionContainsVote(option, vote);
        option.removeVote(vote);
        optionRepository.save(option);
        voteRepository.save(vote);
    }

    private void validateOptionContainsVote(Option option, Vote vote) {
        if (!option.getVotes().contains(vote)) {
            throw new IllegalArgumentException("Vote with id " + vote.getId() + " is not part of Option with id " + option.getId());
        }
    }
}
