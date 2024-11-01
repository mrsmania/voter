package guh.hug.voter.repository;

import guh.hug.voter.model.Voting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingRepository extends JpaRepository<Voting, Long> {
    Voting findByToken(String token);
}
