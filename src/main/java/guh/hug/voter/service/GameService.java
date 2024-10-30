package guh.hug.voter.service;

import guh.hug.voter.model.Game;
import guh.hug.voter.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    public Game createNewGame(String hostUsername) {
        String token = generateUniqueToken();
        Game game = new Game();
        game.setHostUsername(hostUsername);
        game.setToken(token);
        game.setStatus("WAITING");
        return gameRepository.save(game);
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = RandomStringUtils.randomAlphanumeric(6);
        } while (gameRepository.findByToken(token).isPresent());
        return token;
    }

    public Optional<Game> findByToken(String token) {
        return gameRepository.findByToken(token);
    }
}
