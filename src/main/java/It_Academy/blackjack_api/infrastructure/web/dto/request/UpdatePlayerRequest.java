package It_Academy.blackjack_api.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePlayerRequest(
        @NotBlank
        String newName
) {
}
