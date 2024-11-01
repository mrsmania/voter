package guh.hug.voter.service;

import guh.hug.voter.model.Question;
import guh.hug.voter.model.Poll;
import guh.hug.voter.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    public Poll createPoll(String hostUsername) {
        Poll poll = new Poll();
        poll.setHostUsername(hostUsername);
        poll.setActive(false);
        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public Poll addQuestion(String token, Question question){
        Poll poll = pollRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Poll not found for token: " + token));
        if (poll.isActive()) {
            throw new RuntimeException("Poll is active. Cannot remove question.");
        }
        poll.addQuestion(question);
        return pollRepository.save(poll);
    }

    public Poll removeQuestion(String token, Long questionId) {
        Poll poll = pollRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Poll not found for token: " + token));
        if (poll.isActive()) {
            throw new RuntimeException("Poll is active. Cannot remove question.");
        }
        if(poll.getQuestions().size() <= 1){
            throw new RuntimeException("A poll must have at least 1 question.");
        }
        poll.getQuestions().removeIf(question -> question.getId().equals(questionId));
        return pollRepository.save(poll);
    }
    
    public Poll activatePoll(String token) {
        Poll poll = pollRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Poll not found for token: " + token));
        if(poll.getQuestions().isEmpty()){
            throw new RuntimeException("A poll must have at least 1 question.");
        }
        poll.setActive(true);
        return pollRepository.save(poll);
    }
    
    public Poll deactivatePoll(String token) {
        Poll poll = pollRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Poll not found for token: " + token));
        poll.setActive(false);
        return pollRepository.save(poll);
    }


}
