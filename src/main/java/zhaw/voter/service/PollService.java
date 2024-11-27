package zhaw.voter.service;

import org.springframework.web.multipart.MultipartFile;
import zhaw.voter.dto.QuestionDTO;
import zhaw.voter.model.Option;
import zhaw.voter.model.Question;
import zhaw.voter.model.Poll;
import zhaw.voter.repository.PollRepository;
import zhaw.voter.repository.QuestionRepository;
import zhaw.voter.repository.OptionRepository;
import zhaw.voter.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    public Poll initPoll(String hostUserEmail, boolean isActive, String password, String token) {
        EmailValidator.validate(hostUserEmail);
        Poll poll = new Poll();
        poll.setHostUserEmail(hostUserEmail);
        poll.setActive(isActive);
        poll.setPassword(password);
        poll.setToken(token);
        return poll;
    }

    public Poll createDemoPoll(String hostUserEmail, boolean isActive, String password, String token) {
        return initPoll(hostUserEmail, isActive, password, token);
    }

    public Poll createPoll(String hostUserEmail) {
        String password = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String token;
        do {
            token = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } while (pollRepository.existsByToken(token));
        Poll poll = initPoll(hostUserEmail, false, password, token);
        return pollRepository.save(poll);
    }

    public Poll savePoll(Poll poll) {
        validatePoll(poll);
        Optional<Poll> existingPollOpt = pollRepository.findById(poll.getId());
        if (existingPollOpt.isPresent()) {
            Poll existingPoll = existingPollOpt.get();
            existingPoll.getQuestions().clear();
            existingPoll.getQuestions().addAll(poll.getQuestions());
            existingPoll.setActive(poll.getActive());

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
        List<Poll> polls = pollRepository.findAll();
        if (polls.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No polls found.");
        }
        return polls;
    }

    public List<String> getAllTokens() {
        List<Poll> polls = pollRepository.findAll();
        if (polls.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No polls found.");
        }
        List<String> tokens = new ArrayList<>();
        for (Poll poll : polls) {
            tokens.add(poll.getToken());
        }
        return tokens;
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
            return pollRepository.findByTokenAndPasswordAndHostUserEmail(token, password, email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found or access denied"));
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
    public List<QuestionDTO> verifyAndParseQuestions(MultipartFile file) throws IOException {
        List<QuestionDTO> questions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 3) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each question must have at least two options");
                }
                QuestionDTO question = new QuestionDTO();
                question.setText(values[0]);
                question.setOptions(Arrays.asList(values).subList(1, values.length));
                questions.add(question);
            }
        }

        if (questions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file contains no questions");
        }

        return questions;
    }
}
