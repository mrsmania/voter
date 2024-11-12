package zhaw.voter.service;

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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;

    private List<Vote> test;

    @Autowired
    public VoteService(VoteRepository voteRepository, OptionRepository optionRepository) {
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public Vote toggleVote(String userEmail, Long optionId) {
        EmailValidator.validate(userEmail);

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option not found"));

        Vote existingVote = voteRepository.findByUserEmailAndOptionId(userEmail, optionId);

        if (existingVote != null) {
            System.out.println("Removing vote by user: " + userEmail + " for optionId: " + optionId);
            voteRepository.delete(existingVote);
            return null;  // Return null or response indicating un-vote
        } else {
            // Log new vote creation
            System.out.println("Creating vote by user: " + userEmail + " for optionId: " + optionId);
            Vote vote = new Vote(userEmail, option);
            return voteRepository.save(vote);
        }
    }

    public List<VoteDTO> votesByUserEmailAndPollId(String userEmail, Long pollId) {
        EmailValidator.validate(userEmail);

        return voteRepository.findAllByUserEmailAndPollId(userEmail, pollId);
    }


    public List<VoteCountDTO> getUpdatedVoteCounts(Long pollId) {
        List<Option> options = optionRepository.findByPollId(pollId);
        return options.stream()
                .map(option -> new VoteCountDTO(option.getId(), option.getVotes().size()))
                .collect(Collectors.toList());
    }

}
