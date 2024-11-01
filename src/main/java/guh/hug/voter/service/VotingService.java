package guh.hug.voter.service;

import guh.hug.voter.model.Question;
import guh.hug.voter.model.Voting;
import guh.hug.voter.repository.VotingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class VotingService {

    @Autowired
    private VotingRepository votingRepository;

    public Voting createVoting(String hostUsername) {
        Voting voting = new Voting();
        voting.setHostUsername(hostUsername);
        voting.setActive(false);
        return votingRepository.save(voting);
    }

    public Voting addQuestion(String token, Question question){
if (question.getOptions() == null || question.getOptions().size() <= 1) {
            throw new IllegalArgumentException("A question must have at least 2 options.");
        }


        Voting voting = votingRepository.findByToken(token);
        if (voting == null) {
            throw new RuntimeException("No voting with this token");
        }
        voting.addQuestion(question);
        return votingRepository.save(voting);
    }

}
