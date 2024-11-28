package zhaw.voter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import zhaw.voter.model.Poll;
import zhaw.voter.model.Option;
import zhaw.voter.model.Question;
import zhaw.voter.model.Vote;
import zhaw.voter.repository.PollRepository;
import zhaw.voter.service.PollService;
import zhaw.voter.service.QuestionService;
import zhaw.voter.util.HasLogger;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("dev")
public class DevConfiguration implements HasLogger {

    private final PollRepository pollRepository;

    @Autowired
    private PollService pollService;
    @Autowired
    private QuestionService questionService;

    public DevConfiguration(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
        getLogger().info("*****YOU ARE IN DEV PROFILE*****");
    }

    @PostConstruct
    public void init() {
        Poll poll = new Poll("devuser@voter.test", true, "AAAAAA", "111111");

        Question question1 = new Question("Whats your favourite color?", false);
        Question question2 = new Question("Best movie trilogy of all times?", false);

        Option option1Q1 = new Option("Red");
        Option option2Q1 = new Option("Yellow");
        Option option3Q1 = new Option("Blue");
        Option option1Q2 = new Option("Lord of the Rings");
        Option option2Q2 = new Option("The Godfather");
        Option option3Q2 = new Option("The Dark Knight");
        Option option4Q2 = new Option("The Matrix");

        Vote vote1 = new Vote("test@test.ch");
        Vote vote2 = new Vote("earth@plantes.ch");
        Vote vote3 = new Vote("tree@plants.ch");
        Vote vote4 = new Vote("hello@words.ch");
        Vote vote5 = new Vote("goodbye@words.ch");
        Vote vote6 = new Vote("whatsup@words.ch");
        Vote vote7 = new Vote("hi@words.ch");

        option3Q1.addVote(vote1);
        option3Q1.addVote(vote2);
        option3Q1.addVote(vote3);
        option2Q1.addVote(vote4);
        option1Q1.addVote(vote5);
        option1Q1.addVote(vote6);
        option3Q1.addVote(vote7);

        question1.addOption(option1Q1);
        question1.addOption(option2Q1);
        question1.addOption(option3Q1);
        question2.addOption(option1Q2);
        question2.addOption(option2Q2);
        question2.addOption(option3Q2);
        question2.addOption(option4Q2);

        poll.addQuestion(question1);
        poll.addQuestion(question2);

        pollRepository.save(poll);
    }
}