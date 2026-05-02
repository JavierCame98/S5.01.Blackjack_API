package It_Academy.blackjack_api.application.useCase.player.command;

public record UpdatePlayerNameCommand (Long playerId, String newName) {
}
