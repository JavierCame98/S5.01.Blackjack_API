package It_Academy.blackjack_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateGameRequest(
        @NotBlank
        String playerName,

        @Min(1)@Max(10)
        int deckCount

) {
    public CreateGameRequest {
        if (deckCount == 0) deckCount = 1;
    }
}
