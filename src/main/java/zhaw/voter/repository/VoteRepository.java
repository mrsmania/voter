package zhaw.voter.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zhaw.voter.dto.VoteDTO;
import zhaw.voter.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByUserEmailAndOptionId(String userEmail, Long optionId);
    @Query("SELECT new zhaw.voter.dto.VoteDTO(v.id, v.userEmail, v.option.id) " +
            "FROM Vote v " +
            "JOIN v.option o " +
            "JOIN o.question q " +
            "JOIN q.poll p " +
            "WHERE v.userEmail = :userEmail " +
            "AND p.id = :pollId")
    List<VoteDTO> findAllByUserEmailAndPollId(@Param("userEmail") String userEmail, @Param("pollId") Long pollId);
}