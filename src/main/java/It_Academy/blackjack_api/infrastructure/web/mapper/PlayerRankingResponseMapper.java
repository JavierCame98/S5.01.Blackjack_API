package It_Academy.blackjack_api.infrastructure.web.mapper;

import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.infrastructure.web.dto.response.PlayerRankingResponse;
import org.springframework.stereotype.Component;

@Component
public class PlayerRankingResponseMapper {

    public PlayerRankingResponse toRankingResponse (int position, Player player){
        return new PlayerRankingResponse(
                position,
                player.getId().value(),
                player.getName().toString(),
                player.getGamesPlayed(),
                player.getGamesWon(),
                player.getWinRate()
        );
    }

}
