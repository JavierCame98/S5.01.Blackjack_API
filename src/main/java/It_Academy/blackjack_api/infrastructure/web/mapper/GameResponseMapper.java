package It_Academy.blackjack_api.infrastructure.web.mapper;

import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.game.Hand;
import It_Academy.blackjack_api.infrastructure.web.dto.response.GameResponse;
import org.springframework.stereotype.Component;

@Component
public class GameResponseMapper {

    public GameResponse toResponse (Game game){
        return new GameResponse(
                game.getId().value(),
                game.getPlayerId().toString(),
                toHandInfo(game.getPlayerHand()),
                toHandInfo(game.getDealerHand()),
                game.getStatus().name(),
                game.getDeck().remainingCards()
        );
    }

    private GameResponse.HandInfo toHandInfo(Hand hand) {
        return new GameResponse.HandInfo(
                hand.getCards().stream()
                        .map(c -> new GameResponse.CardInfo(
                                c.rank().name(),
                                c.suit().name(),
                                c.rank().getValue()))
                        .toList(),
                hand.getValue(),
                hand.isBlackjack(),
                hand.isBusted()
        );
    }
}
