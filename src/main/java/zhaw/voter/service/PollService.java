package zhaw.voter.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import zhaw.voter.model.Option;
import zhaw.voter.model.Question;
import zhaw.voter.model.Poll;
import zhaw.voter.repository.PollRepository;
import zhaw.voter.repository.QuestionRepository;
import zhaw.voter.repository.OptionRepository;
import zhaw.voter.util.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@Service
public class PollService {

    private final PollRepository pollRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public PollService(PollRepository pollRepository, QuestionRepository questionRepository, OptionRepository optionRepository) {
        this.pollRepository = pollRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    public List<Poll> getAllPolls() {
        return extractAllPolls();
    }

    public Poll createPoll(String hostUserEmail) {
        EmailValidator.validate(hostUserEmail);
        Poll poll = new Poll();
        poll.setHostUserEmail(hostUserEmail);
        poll.setActive(false);
        poll.setPassword(generateRandomString());
        poll.setToken(generateUniqueToken());
        return pollRepository.save(poll);
    }

    public Poll getPollByToken(String token) {
        Poll poll = pollRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poll not found."));
        if (!poll.getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poll is inactive.");
        }
        return poll;
    }

    public Poll savePoll(Poll poll) {
        Poll existingPoll = pollRepository.findById(
                Optional.ofNullable(poll.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Poll id cannot be null"))
        ).orElseThrow(() -> new EntityNotFoundException("Poll with id " + poll.getId() + " not found"));
        validatePoll(existingPoll);
        existingPoll.getQuestions().clear();
        existingPoll.getQuestions().addAll(poll.getQuestions());
        existingPoll.setActive(poll.getActive());

        existingPoll.getQuestions().forEach(question -> {
            optionRepository.saveAll(question.getOptions());
            questionRepository.save(question);
        });
        return pollRepository.save(existingPoll);
    }

    public void deletePoll(long pollId) {
        try {
            pollRepository.deleteById(pollId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Poll with id " + pollId + " not found");
        }
    }

    public List<Long> getAllPollIds() {
        List<Poll> polls = extractAllPolls();
        List<Long> pollIds = new ArrayList<>();
        for (Poll poll : polls) {
            pollIds.add(poll.getId());
        }
        return pollIds;
    }

    public List<String> getAllTokens() {
        List<Poll> polls = extractAllPolls();
        List<String> tokens = new ArrayList<>();
        for (Poll poll : polls) {
            tokens.add(poll.getToken());
        }
        return tokens;
    }

    public Poll addQuestion(long pollId, long questionId) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new EntityNotFoundException("Poll with id " + pollId + " not found"));
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        poll.addQuestion(question);
        question.setPoll(poll);
        return pollRepository.save(poll);
    }

    public void removeQuestion(long pollId, long questionId) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new EntityNotFoundException("Poll with id " + pollId + " not found"));
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        if (!poll.getQuestions().contains(question)) {
            throw new IllegalArgumentException("Question with id " + questionId + " is not part of Poll with id " + pollId);
        }
        poll.getQuestions().remove(question);
        question.setPoll(null);
        pollRepository.save(poll);
        questionRepository.save(question);
    }

    public String generatePollResultsCSV(String token) {
        Poll poll = getPollByToken(token);
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Poll;Question;Option;Vote Count\n");
        for (Question question : poll.getQuestions()) {
            for (Option option : question.getOptions()) {
                csvBuilder.append(poll.getToken()).append(";").append(question.getText()).append(";").append(option.getText()).append(";").append(option.getVotes().size()).append("\n");
            }
        }
        try {
            FileWriter csvWriter = new FileWriter("src/main/resources/dump/exportedPolls/pollResults_" + System.currentTimeMillis() + ".csv");
            csvWriter.append(csvBuilder.toString());
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvBuilder.toString();
    }

    public Poll findPollByTokenAndPasswordAndEmail(String token, String password, String email) {
        validateInput(token, "Token is required");
        validateInput(password, "Password is required");
        validateInput(email, "Email is required");
        return pollRepository.findByTokenAndPasswordAndHostUserEmail(token, password, email).orElseThrow(() -> new EntityNotFoundException("Poll not found or access denied"));
    }

    private List<Poll> extractAllPolls() {
        List<Poll> polls = pollRepository.findAll();
        if (polls.isEmpty()) {
            throw new EntityNotFoundException("No polls found.");
        }
        return polls;
    }

    private void validatePoll(Poll poll) {
        if (poll.getQuestions().isEmpty()) {
            throw new IllegalArgumentException("The poll must have at least one question");
        }
        poll.getQuestions().forEach(question -> {
            validateInput(question.getText(), "Question text cannot be empty");
            if (question.getOptions().size() < 2) {
                throw new IllegalArgumentException("Each question must have at least two options");
            }
            question.getOptions().forEach(option -> validateInput(option.getText(), "Each option must have text"));
        });
    }

    private void validateInput(String input, String errorMessage) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private String generateRandomString() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = generateRandomString();
        } while (pollRepository.existsByToken(token));
        return token;
    }
}
