package zhaw.voter.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import zhaw.voter.dto.VoteCountDTO;
import zhaw.voter.dto.VoteDTO;
import zhaw.voter.model.Option;
import zhaw.voter.model.Vote;
import zhaw.voter.repository.OptionRepository;
import zhaw.voter.repository.VoteRepository;
import zhaw.voter.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import zhaw.voter.util.InputValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository, OptionRepository optionRepository) {
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
    }

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Vote createVote(String userEmail) {
        Vote vote = new Vote();
        vote.setUserEmail(userEmail);
        return voteRepository.save(vote);
    }

    public Vote updateVote(long voteId, Vote updatedVote) {
        InputValidator.validateInput(updatedVote.getUserEmail(), "User Email cannot be empty");
        Vote existingVote = voteRepository.findById(voteId).orElseThrow(() -> new EntityNotFoundException("Vote with id " + voteId + " not found"));
        existingVote.setUserEmail(updatedVote.getUserEmail());
        return voteRepository.save(existingVote);
    }

    public Vote findVote(Long voteId) {
        return voteRepository.findById(voteId).orElseThrow(() -> new EntityNotFoundException("Vote with id " + voteId + " not found"));
    }

    public void deleteVote(long voteId) {
        try {
            voteRepository.deleteById(voteId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Vote with id " + voteId + " not found");
        }
    }

    public List<Long> getAllVoteIds() {
        List<Vote> votes = voteRepository.findAll();
        if (votes.isEmpty()) {
            throw new EntityNotFoundException("No votes found");
        }
        return votes.stream().map(Vote::getId).collect(Collectors.toList());
    }

    @Transactional
    public void toggleVote(String userEmail, Long optionId) {
        EmailValidator.validate(userEmail);
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option not found"));
        Vote existingVote = voteRepository.findByUserEmailAndOptionId(userEmail, optionId);
        //if question is not multiple choice, delete all votes of the user
        if (!option.getQuestion().getMultipleChoice()) {
            List<Option> options = optionRepository.findByQuestionId(option.getQuestion().getId());
            options.forEach(opt -> {
                Vote vote = voteRepository.findByUserEmailAndOptionId(userEmail, opt.getId());
                if (vote != null) {
                    voteRepository.delete(vote);
                }
            });
        }
        if (existingVote != null) {
            voteRepository.delete(existingVote);
        } else {
            voteRepository.save(new Vote(userEmail, option));
        }
    }

    public List<VoteDTO> votesByUserEmailAndPollId(String userEmail, Long pollId) {
        EmailValidator.validate(userEmail);
        return voteRepository.findAllByUserEmailAndPollId(userEmail, pollId);
    }

    public List<VoteCountDTO> getUpdatedVoteCounts(Long pollId) {
        return optionRepository.findByPollId(pollId)
                .stream()
                .map(option -> new VoteCountDTO(option.getId(), option.getVotes().size()))
                .collect(Collectors.toList());
    }
}
