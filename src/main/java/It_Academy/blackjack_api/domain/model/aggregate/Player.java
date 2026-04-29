package It_Academy.blackjack_api.domain.model.aggregate;

import It_Academy.blackjack_api.domain.model.valueObjects.game.GameStatus;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerName;

public class Player {

    private PlayerId id;
    private PlayerName name;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesTied;
    private double winRate;

    private Player(){}

    public static Player create(PlayerName name) {
        Player player = new Player();
        player.name = name;
        player.gamesPlayed = 0;
        player.gamesWon = 0;
        player.gamesLost = 0;
        player.gamesTied = 0;
        player.winRate = 0;
        return player;

    }

    // métodos para actualizar las estadísticas del player
    public void recordWin(){
        gamesPlayed++;
        gamesWon++;
        recalculateWinRate();
    }

    public void recordLost(){
        gamesPlayed++;
        gamesLost++;
        recalculateWinRate();
    }

    public void recordTied(){
        gamesPlayed++;
        gamesTied++;
        recalculateWinRate();
    }

    //siempre que se juegue una partida le metemos el winRate
    public void recalculateWinRate(){
        winRate = gamesPlayed == 0 ? 0.0 :(double) gamesWon / gamesPlayed *100;
    }


    //cuando el Game termina llamamos a este método que se actualiza solo (cuando se lanza el Event) es el Observer
    public void recordGameResult(GameStatus status){
        switch (status){
            case PLAYER_WIN -> recordWin();
            case DEALER_WIN -> recordLost();
            case TIE -> recordTied();
            default -> throw new IllegalArgumentException(
                    "Estado no válido para registrar resultado: " + status);
        }
    }

    public void updateName(PlayerName newName) {
        this.name = newName;
    }

    public PlayerId   getId()          { return id; }
    public PlayerName getName()        { return name; }
    public int        getGamesPlayed() { return gamesPlayed; }
    public int        getGamesWon()    { return gamesWon; }
    public int        getGamesLost()   { return gamesLost; }
    public int        getGamesTied()   { return gamesTied; }
    public double     getWinRate()     { return winRate; }

    public void setId(PlayerId id) { this.id = id; }
}
