package It_Academy.blackjack_api.application.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String id) {
        super("Jugador no encontrado con id: " + id);
    }
}
