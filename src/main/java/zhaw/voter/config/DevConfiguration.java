package zhaw.voter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import zhaw.voter.model.Poll;
import zhaw.voter.model.Option;
import zhaw.voter.model.Question;
import zhaw.voter.model.Vote;
import zhaw.voter.repository.PollRepository;
import zhaw.voter.util.HasLogger;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("dev")
public class DevConfiguration implements HasLogger {

    private final PollRepository pollRepository;

    public DevConfiguration(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
        getLogger().info("*****YOU ARE IN DEV PROFILE*****");
    }

    @PostConstruct
    public void init() {
        Poll poll = new Poll();
        poll.setHostUserEmail("devuser@voter.test");
        poll.setActive(true);
        poll.setToken("AAAAAA");
        poll.setPassword("111111");

        Option option1Q1 = new Option();
        option1Q1.setText("Rot");
        Option option2Q1 = new Option();
        option2Q1.setText("Gelb");
        Option option3Q1 = new Option();
        option3Q1.setText("Blau");
        List<Option> optionsQ1 = new ArrayList<>();
        optionsQ1.add(option1Q1);
        optionsQ1.add(option2Q1);
        optionsQ1.add(option3Q1);

        Vote vote1 = new Vote();
        vote1.setOption(option1Q1);
        Vote vote2 = new Vote();
        vote2.setOption(option1Q1);
        Vote vote3 = new Vote();
        vote3.setOption(option1Q1);
        List<Vote> votesQ1 = new ArrayList<>();
        votesQ1.add(vote1);
        votesQ1.add(vote2);
        votesQ1.add(vote3);
        option3Q1.setVotes(votesQ1);

        Option option1Q2 = new Option();
        option1Q2.setText("Lord of the Rings");
        Option option2Q2 = new Option();
        option2Q2.setText("The Godfather");
        Option option3Q2 = new Option();
        option3Q2.setText("The Dark Knight");
        Option option4Q2 = new Option();
        option4Q2.setText("The Matrix");
        List<Option> optionsQ2 = new ArrayList<>();
        optionsQ2.add(option1Q2);
        optionsQ2.add(option2Q2);
        optionsQ2.add(option3Q2);
        optionsQ2.add(option4Q2);

        Question question = new Question();
        question.setText("Lieblingsfarbe?");
        question.setMultipleChoice(false);
        question.setPoll(poll);
        question.setOptions(optionsQ1);

        Question question2 = new Question();
        question2.setText("Best movie trilogy of all times?");
        question2.setMultipleChoice(false);
        question2.setPoll(poll);
        question2.setOptions(optionsQ2);

        List<Question> questions = new ArrayList<>();
        questions.add(question);
        questions.add(question2);
        poll.setQuestions(questions);

        pollRepository.save(poll);
    }
}