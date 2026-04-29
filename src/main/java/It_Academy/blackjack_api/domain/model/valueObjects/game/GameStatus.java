package It_Academy.blackjack_api.domain.model.valueObjects.game;

public enum GameStatus {
    PLAYING (false),
    PLAYER_WIN (true),
    DEALER_WIN (true),
    TIE (true);

    private final boolean finished;


    GameStatus(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished(){
        return finished;
    }

    public boolean isPlaying(){
        return !finished;
    }
}
