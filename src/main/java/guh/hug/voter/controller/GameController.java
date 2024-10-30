package guh.hug.voter.controller;

import guh.hug.voter.model.Game;
import guh.hug.voter.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public Game createGame(@RequestParam String hostUsername) {
        return gameService.createNewGame(hostUsername);
    }

    @GetMapping("/{token}")
    public Game getGameByToken(@PathVariable String token) {
        return gameService.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }
    @PostMapping("/{token}/join")
    public String joinGame(@PathVariable String token, @RequestParam String playerName) {
        Game game = gameService.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        // Check if the game is in a joinable state
        if (!"WAITING".equals(game.getStatus())) {
            throw new RuntimeException("Game has already started");
        }

        // Add player logic here
        // For example, save to a list of players (can be added in Game entity as a relationship)

        return "Player " + playerName + " joined game " + token;
    }
}


