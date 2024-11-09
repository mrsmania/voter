package guh.hug.voter.service;

import guh.hug.voter.model.Option;
import guh.hug.voter.model.Question;
import guh.hug.voter.model.Poll;
import guh.hug.voter.repository.PollRepository;
import guh.hug.voter.repository.QuestionRepository;
import guh.hug.voter.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class PollService {

    private static final String EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    public Poll createPoll(String hostUserEmail) {
        validateEmail(hostUserEmail);


        Poll poll = new Poll();
        poll.setHostUserEmail(hostUserEmail);
        poll.setActive(false);
        String token;
        do {
            token = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (pollRepository.existsByToken(token));
        poll.setToken(token);
        return pollRepository.save(poll);
    }

    public Poll savePoll(Poll poll) {
        validatePoll(poll);

        Optional<Poll> existingPollOpt = pollRepository.findById(poll.getId());
        if (existingPollOpt.isPresent()) {
            Poll existingPoll = existingPollOpt.get();

            // Clear the existing list, then add all new questions
            existingPoll.setActive(poll.getActive());

            existingPoll.getQuestions().clear();
            existingPoll.getQuestions().addAll(poll.getQuestions());
            existingPoll.setActive(poll.getActive());

            //log poll.isActive() to see if it is true or false
            System.out.println(poll.getActive());

            for (Question question : existingPoll.getQuestions()) {
                optionRepository.saveAll(question.getOptions());
                questionRepository.save(question);
            }
            return pollRepository.save(existingPoll);
        } else {
            throw new RuntimeException("Poll with ID " + poll.getId() + " does not exist.");
        }
    }


    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public Poll getPollByToken(String token) {
        Optional<Poll> optionalPoll = pollRepository.findByToken(token);
        if (optionalPoll.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poll not found.");
        }
        Poll poll = optionalPoll.get();
        if (!poll.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poll is inactive.");
        }
        return poll;
    }



    public Poll findPollByTokenAndPasswordAndEmail(String token, String password, String email) {
        if (token == null || token.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token is required.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required.");
        }
        try {
            return pollRepository.findByTokenAndPasswordAndHostUserEmail(token, password, email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found or access denied"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found or access denied");
        }
    }

    private void validatePoll(Poll poll) {
        if (poll.getQuestions().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The poll must have at least one question.");
        }

        for (Question question : poll.getQuestions()) {
            if (question.getText() == null || question.getText().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot have a question with empty text.");
            }

            if (question.getOptions().size() < 2) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each question must have at least two options.");
            }

            for (Option option : question.getOptions()) {
                if (option.getText() == null || option.getText().trim().isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each option must have text.");
                }
            }
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address is required.");
        }

        if (!isValidEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address.");
        }
    }

    private boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
