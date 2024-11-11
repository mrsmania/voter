package zhaw.voter.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zhaw.voter.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByUserEmailAndOptionId(String userEmail, Long optionId);
    @Query(value = "SELECT v.* FROM Vote v " +
            "JOIN Option o ON v.OPTION_ID = o.id " +
            "JOIN Question q ON o.QUESTION_ID = q.id " +
            "JOIN Poll p ON q.POLL_ID = p.id " +
            "WHERE v.USER_EMAIL = :userEmail " +
            "AND p.id = :pollId", nativeQuery = true)
    List<Vote> findAllByUserEmailAndPollId(@Param("userEmail") String userEmail, @Param("pollId") Long pollId);

}