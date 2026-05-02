package It_Academy.blackjack_api.infrastructure.web.mapper;

import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.infrastructure.web.dto.response.PlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class PlayerResponseMapper {

    public PlayerResponse toResponse (Player player){
        return new PlayerResponse(
                player.getId().value(),
                player.getName().toString(),
                player.getGamesPlayed(),
                player.getGamesWon(),
                player.getGamesLost(),
                player.getGamesTied(),
                player.getWinRate()
        );
    }
}
