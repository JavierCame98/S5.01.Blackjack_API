package It_Academy.blackjack_api.infrastructure.web.request;

import jakarta.validation.constraints.NotBlank;

public record PlayGameRequest(

        @NotBlank
        String action
) {
}
