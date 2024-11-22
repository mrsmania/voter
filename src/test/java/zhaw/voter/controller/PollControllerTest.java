package zhaw.voter.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import zhaw.voter.dto.QuestionDTO;
import zhaw.voter.model.Poll;
import zhaw.voter.service.PollService;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class PollControllerTest {

    private PollController pollController;
    private PollService pollService;

    @BeforeEach
    void setUp() {
        pollService = mock(PollService.class);
        pollController = new PollController();
        pollController.pollService = pollService; // Inject the mock service
    }

    @Test
    void getAllPolls_shouldReturnPolls() {
        List<Poll> polls = Arrays.asList(new Poll(), new Poll());
        when(pollService.getAllPolls()).thenReturn(polls);

        ResponseEntity<?> response = pollController.getAllPolls();

        assertEquals(OK, response.getStatusCode());
        assertEquals(polls, response.getBody());
    }

    @Test
    void getAllPolls_shouldHandleException() {
        when(pollService.getAllPolls()).thenThrow(new ResponseStatusException(BAD_REQUEST, "Error"));

        ResponseEntity<?> response = pollController.getAllPolls();

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("message"));
    }

    @Test
    void getAllTokens_shouldReturnTokens() {
        List<String> tokens = Arrays.asList("token1", "token2");
        when(pollService.getAllTokens()).thenReturn(tokens);

        ResponseEntity<?> response = pollController.getAllTokens();

        assertEquals(OK, response.getStatusCode());
        assertEquals(tokens, response.getBody());
    }

    @Test
    void getPollByToken_shouldReturnPoll() {
        String token = "testToken";
        Poll poll = new Poll();
        when(pollService.getPollByToken(token)).thenReturn(poll);

        ResponseEntity<?> response = pollController.getPollByToken(token);

        assertEquals(OK, response.getStatusCode());
        assertEquals(poll, response.getBody());
    }

    @Test
    void getPollByToken_shouldHandleException() {
        String token = "invalidToken";
        when(pollService.getPollByToken(token)).thenThrow(new ResponseStatusException(NOT_FOUND, "Poll not found"));

        ResponseEntity<?> response = pollController.getPollByToken(token);

        assertEquals(NOT_FOUND, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("message"));
    }

    @Test
    void exportPollResults_shouldReturnCsv() {
        String token = "testToken";
        String csvData = "id,question\n1,Sample Question";
        when(pollService.generatePollResultsCSV(token)).thenReturn(csvData);

        ResponseEntity<byte[]> response = pollController.exportPollResults(token);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void uploadQuestions_shouldParseQuestions() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "questions.csv", "text/csv", "sample data".getBytes());
        List<QuestionDTO> questions = Collections.singletonList(new QuestionDTO());
        when(pollService.verifyAndParseQuestions(file)).thenReturn(questions);

        ResponseEntity<?> response = pollController.uploadQuestions(file);

        assertEquals(OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void uploadQuestions_shouldHandleInvalidFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "questions.csv", "text/csv", "invalid data".getBytes());
        when(pollService.verifyAndParseQuestions(file)).thenThrow(new ResponseStatusException(BAD_REQUEST, "Invalid file"));

        ResponseEntity<?> response = pollController.uploadQuestions(file);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("message"));
    }

    @Test
    void savePoll_shouldSavePoll() {
        Poll poll = new Poll();
        Poll savedPoll = new Poll();
        when(pollService.savePoll(poll)).thenReturn(savedPoll);

        ResponseEntity<?> response = pollController.savePoll(poll);

        assertEquals(OK, response.getStatusCode());
        assertEquals(savedPoll, response.getBody());
    }

    @Test
    void createPoll_shouldCreatePoll() {
        String email = "host@example.com";
        Poll poll = new Poll();
        when(pollService.createPoll(email)).thenReturn(poll);

        ResponseEntity<?> response = pollController.createPoll(email);

        assertEquals(OK, response.getStatusCode());
        assertEquals(poll, response.getBody());
    }

    @Test
    void getPoll_shouldFindPoll() {
        String token = "token";
        String password = "password";
        String email = "email";
        Poll poll = new Poll();
        when(pollService.findPollByTokenAndPasswordAndEmail(token, password, email)).thenReturn(poll);

        ResponseEntity<?> response = pollController.getPoll(token, password, email);

        assertEquals(OK, response.getStatusCode());
        assertEquals(poll, response.getBody());
    }
}
