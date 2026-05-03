package It_Academy.blackjack_api.application.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String id) {
        super("Partida no encontrada con id: " + id);
    }
}
