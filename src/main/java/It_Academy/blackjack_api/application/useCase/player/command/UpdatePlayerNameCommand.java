package It_Academy.blackjack_api.application.useCase.player.command;

public record UpdatePlayerNameCommand (String playerId, String newName) {
}
