package guh.hug.voter.service;

import guh.hug.voter.model.Option;
import guh.hug.voter.model.Vote;
import guh.hug.voter.repository.OptionRepository;
import guh.hug.voter.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Transactional
    public void addVote(Long optionId, String username) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));
        Vote vote = new Vote(username, option);
        voteRepository.save(vote);
    }

    public void deleteVote(Long voteId) {
        voteRepository.deleteById(voteId);
    }

    public void updateVote(Long voteId, Long optionId, String username) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));
        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new RuntimeException("Vote not found"));
        vote.setUsername(username);
        vote.setOption(option);
        voteRepository.save(vote);
    }
}
