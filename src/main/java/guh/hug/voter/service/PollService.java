package guh.hug.voter.service;

import guh.hug.voter.model.Question;
import guh.hug.voter.model.Poll;
import guh.hug.voter.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    public Poll createPoll(String hostUserEmail) {
        Poll poll = new Poll();
        poll.setHostUserEmail(hostUserEmail);
        poll.setActive(false);

        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public Poll getPollByToken(String token) {
        Optional<Poll> optionalPoll = pollRepository.findByToken(token);
        if (optionalPoll.isEmpty()) {
            throw new RuntimeException("Poll not found for token: " + token);
        }

        //turn optional into poll
        Poll poll = optionalPoll.get();

        if (!poll.isActive()) {
            throw new RuntimeException("Poll with token " + token + " is not active.");
        }

        return poll;
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

    public Poll deleteAllQuestions(String token) {
        Poll poll = pollRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Poll not found for token: " + token));
        if (poll.isActive()) {
            throw new RuntimeException("Poll is active. Cannot remove question.");
        }
        poll.getQuestions().clear();
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
