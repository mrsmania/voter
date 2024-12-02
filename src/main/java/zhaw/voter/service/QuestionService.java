package zhaw.voter.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zhaw.voter.dto.QuestionDTO;
import zhaw.voter.model.Option;
import zhaw.voter.model.Question;
import zhaw.voter.repository.OptionRepository;
import zhaw.voter.util.InputValidator;
import zhaw.voter.repository.QuestionRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final OptionService optionService;
    private final OptionRepository optionRepository;

    public QuestionService(QuestionRepository questionRepository, OptionService optionService, OptionRepository optionRepository) {
        this.questionRepository = questionRepository;
        this.optionService = optionService;
        this.optionRepository = optionRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question createQuestion(String text, boolean multipleChoice) {
        InputValidator.validateInput(text, "Question text cannot be empty");
        Question question = new Question();
        question.setText(text);
        question.setMultipleChoice(multipleChoice);
        return questionRepository.save(question);
    }

    public void deleteQuestion(long questionId) {
        try {
            questionRepository.deleteById(questionId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Question with id " + questionId + " not found");
        }
    }

    public List<QuestionDTO> verifyAndParseQuestions(MultipartFile file) throws IOException {
        List<QuestionDTO> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length < 3) {
                    throw new IllegalArgumentException("Each question must have at least two options");
                }
                QuestionDTO question = new QuestionDTO();
                question.setText(values[0]);
                question.setOptions(Arrays.asList(values).subList(1, values.length));
                questions.add(question);
            }
        }
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("The file contains no questions");
        }
        return questions;
    }

    public List<Long> getAllQuestionIds() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            throw new EntityNotFoundException("No questions found");
        }
        return questions.stream().map(Question::getId).collect(Collectors.toList());
    }

    public Question findQuestion(long questionId) {
        return questionRepository.findById(questionId).orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
    }

    public Question updateQuestion(long questionId, Question updatedQuestion) {
        InputValidator.validateInput(updatedQuestion.getText(), "Question text cannot be empty");
        Question existingQuestion = questionRepository.findById(questionId).orElseThrow(() ->
                new EntityNotFoundException("Question with id " + questionId + " not found"));
        existingQuestion.setText(updatedQuestion.getText());
        existingQuestion.setMultipleChoice(updatedQuestion.getMultipleChoice());
        return questionRepository.save(existingQuestion);
    }

    public Question addOption(long questionId, long optionId) {
        Question question = findQuestion(questionId);
        Option option = optionService.findOption(optionId);
        validateQuestionContainsOption(question, option);
        question.addOption(option);
        optionRepository.save(option);
        return questionRepository.save(question);
    }

    public void removeOption(long questionId, long optionId) {
        Question question = findQuestion(questionId);
        Option option = optionService.findOption(optionId);
        validateQuestionContainsOption(question, option);
        question.removeOption(option);
        questionRepository.save(question);
        optionRepository.save(option);
    }

    public void validateQuestionContainsOption(Question question, Option option) {
        if (!question.getOptions().contains(option)) {
            throw new IllegalArgumentException("Option with id " + option.getId() + " is not part of Question with id " + question.getId());
        }
    }
}