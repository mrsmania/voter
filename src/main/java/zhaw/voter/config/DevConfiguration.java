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
import zhaw.voter.util.HasLogger;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Configuration
@Profile("dev")
public class DevConfiguration implements HasLogger {

    @Autowired
    private final PollRepository pollRepository;

    public DevConfiguration(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
        getLogger().info("*****YOU ARE IN DEV PROFILE*****");
    }

    @PostConstruct
    public void init() {
        List<Poll> polls = createDevPolls();
        polls.forEach(this::generateRandomVotesForPoll);
        pollRepository.saveAll(polls);
    }

    private List<Poll> createDevPolls() {
        Poll poll1 = initPoll("devuser1@voter.test", "AAAAAA", "111111");
        Poll poll2 = initPoll("devuser1@voter.test", "000000", "111111");

        Question question1 = initQuestion("Favourite color?", false, poll1);
        initOption("Red", question1);
        initOption("Yellow", question1);
        initOption("Blue", question1);

        Question question2 = initQuestion("Favourite movie trilogy?", false, poll1);
        initOption("Lord of the Rings", question2);
        initOption("The Godfather", question2);
        initOption("The Dark Knight", question2);
        initOption("The Matrix", question2);

        Question question3 = initQuestion("Favourite drink?", false, poll1);
        initOption("Beer", question3);
        initOption("Water", question3);
        initOption("Wine", question3);
        initOption("Coffee", question3);
        initOption("Tea", question3);
        initOption("Lemonade", question3);
        initOption("Coca Cola", question3);
        initOption("Coca Cola Zero", question3);
        initOption("Whiskey", question3);
        initOption("Gin", question3);
        initOption("Rum", question3);
        initOption("Tequila", question3);

        Question question4 = initQuestion("After-work beer?", true, poll2);
        initOption("03.01.2025", question4);
        initOption("04.01.2025", question4);
        initOption("10.01.2025", question4);
        initOption("11.01.2025", question4);
        initOption("17.01.2025", question4);
        initOption("18.01.2025", question4);
        initOption("24.01.2025", question4);
        initOption("25.01.2025", question4);
        initOption("31.01.2025", question4);
        initOption("01.02.2025", question4);

        return Arrays.asList(poll1, poll2);
    }

    private void generateRandomVotesForPoll(Poll poll) {
        poll.getQuestions().forEach(question -> {
            List<Option> options = question.getOptions();
            int totalVotes = new Random().nextInt(91) + 10; //
            for (int i = 0; i < totalVotes; i++) {
                Option randomOption = options.get(new Random().nextInt(options.size()));
                initVote(generateRandomEmail(), randomOption);
            }
        });
    }

    private String generateRandomEmail() {
        String[] domains = {"ilikezhaw.ch", "pollster.com", "voter.dev", "random.li", "mailinator.com"};
        String name = UUID.randomUUID().toString().substring(0, 8);
        String domain = domains[new Random().nextInt(domains.length)];
        return name + "@" + domain;
    }

    private Poll initPoll(String email, String token, String password) {
        Poll poll = new Poll();
        poll.setHostUserEmail(email);
        poll.setToken(token);
        poll.setActive(true);
        poll.setPassword(password);
        return poll;
    }

    private Question initQuestion(String text, Boolean multipleChoice, Poll poll) {
        Question question = new Question();
        question.setText(text);
        question.setMultipleChoice(multipleChoice);
        poll.setQuestion(question);
        return question;
    }

    private void initOption(String text, Question question) {
        Option option = new Option();
        option.setText(text);
        question.setOption(option);
    }

    private void initVote(String email, Option option) {
        Vote vote = new Vote();
        vote.setUserEmail(email);
        option.addVote(vote);
    }
}