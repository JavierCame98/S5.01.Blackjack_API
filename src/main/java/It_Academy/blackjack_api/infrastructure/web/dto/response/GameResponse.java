package It_Academy.blackjack_api.infrastructure.web.dto.response;

import java.util.List;

public record GameResponse(String   id,
                           String   playerId,
                           HandInfo playerHand,
                           HandInfo dealerHand,
                           String   status,
                           int      remainingCards
) {
    public record HandInfo(
            List<CardInfo> cards,
            int value,
            boolean isBlackjack,
            boolean isBusted
    ) {
    }

    public record CardInfo(
            String rank,
            String suit,
            int value
    ) {}
}