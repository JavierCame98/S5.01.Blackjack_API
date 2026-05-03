package It_Academy.blackjack_api.application.exception;

public class GameAlreadyFinishedException extends RuntimeException {
    public GameAlreadyFinishedException(String id) {
        super("La partida " + id + " ya ha finalizado");
    }
}
