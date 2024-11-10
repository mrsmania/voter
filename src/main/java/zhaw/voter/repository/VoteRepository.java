package zhaw.voter.repository;

import zhaw.voter.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByUserEmailAndOptionId(String userEmail, Long optionId);
}