package zhaw.voter.repository;

import zhaw.voter.model.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PollRepository extends JpaRepository<Poll, Long> {
    Optional<Poll> findByToken(String token);
    boolean existsByToken(String token);
    Optional<Poll> findById(Long id);
    Optional<Poll> findByTokenAndPasswordAndHostUserEmail(String token, String password, String hostUserEmail);
}
