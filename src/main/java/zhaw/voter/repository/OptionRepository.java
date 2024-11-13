package zhaw.voter.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zhaw.voter.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    @Query("SELECT o FROM Option o WHERE o.question.poll.id = :pollId")
    List<Option> findByPollId(@Param("pollId") Long pollId);

    @Query("SELECT o FROM Option o WHERE o.question.id = :questionId")
    List<Option> findByQuestionId(@Param("questionId") Long questionId);
}