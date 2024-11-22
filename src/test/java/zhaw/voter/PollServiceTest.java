package zhaw.voter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import zhaw.voter.model.Poll;
import zhaw.voter.repository.OptionRepository;
import zhaw.voter.repository.PollRepository;
import zhaw.voter.repository.QuestionRepository;
import zhaw.voter.service.PollService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PollServiceTest {

    @InjectMocks
    private PollService pollService;

    @Mock
    private PollRepository pollRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPoll_ValidEmail_ShouldCreatePoll() {
        String email = "test@example.com";
        Poll poll = new Poll();
        poll.setHostUserEmail(email);
        poll.setToken("ABC123"); // Beispiel-Token

        when(pollRepository.existsByToken(anyString())).thenReturn(false);
        when(pollRepository.save(any(Poll.class))).thenAnswer(invocation -> {
            Poll savedPoll = invocation.getArgument(0);
            savedPoll.setId(1L); // Setze eine ID, um den Speicherprozess zu simulieren
            return savedPoll;
        });

        Poll result = pollService.createPoll(email);

        assertNotNull(result);
        assertEquals(email, result.getHostUserEmail()); // Überprüfe, dass die Email korrekt gespeichert wurde
        assertNotNull(result.getToken()); // Überprüfe, dass ein Token generiert wurde
        verify(pollRepository, times(1)).save(any(Poll.class));
    }


    @Test
    void getAllPolls_NoPolls_ShouldThrowException() {
        when(pollRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pollService.getAllPolls();
        });

        assertEquals("404 NOT_FOUND \"No polls found.\"", exception.getMessage());
    }

    @Test
    void getPollByToken_InactivePoll_ShouldThrowException() {
        String token = "ABC123";
        Poll poll = new Poll();
        poll.setToken(token);
        poll.setActive(false);

        when(pollRepository.findByToken(token)).thenReturn(Optional.of(poll));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pollService.getPollByToken(token);
        });

        assertEquals("400 BAD_REQUEST \"Poll is inactive.\"", exception.getMessage());
    }

    @Test
    void savePoll_InvalidPoll_ShouldThrowException() {
        Poll poll = new Poll();
        poll.setId(1L);
        poll.setQuestions(new ArrayList<>());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pollService.savePoll(poll);
        });

        assertEquals("400 BAD_REQUEST \"The poll must have at least one question.\"", exception.getMessage());
    }

    @Test
    void findPollByTokenAndPasswordAndEmail_InvalidToken_ShouldThrowException() {
        when(pollRepository.findByTokenAndPasswordAndHostUserEmail(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            pollService.findPollByTokenAndPasswordAndEmail("ABC123", "password", "test@example.com");
        });

        assertEquals("404 NOT_FOUND \"Poll not found or access denied\"", exception.getMessage());
    }

    @Test
    void getAllPolls_WithPolls_ShouldReturnListOfPolls() {
        // Arrange
        Poll poll1 = new Poll();
        poll1.setToken("ABC123");
        Poll poll2 = new Poll();
        poll2.setToken("XYZ789");
        when(pollRepository.findAll()).thenReturn(List.of(poll1, poll2));

        // Act
        List<Poll> polls = pollService.getAllPolls();

        // Assert
        assertNotNull(polls);
        assertEquals(2, polls.size());
        assertEquals("ABC123", polls.get(0).getToken());
        assertEquals("XYZ789", polls.get(1).getToken());
        verify(pollRepository, times(1)).findAll();
    }
    @Test
    void getPollByToken_ActivePoll_ShouldReturnPoll() {
        // Arrange
        String token = "ABC123";
        Poll poll = new Poll();
        poll.setToken(token);
        poll.setActive(true);
        when(pollRepository.findByToken(token)).thenReturn(Optional.of(poll));

        // Act
        Poll result = pollService.getPollByToken(token);

        // Assert
        assertNotNull(result);
        assertEquals(token, result.getToken());
        assertTrue(result.getActive());
        verify(pollRepository, times(1)).findByToken(token);
    }
}