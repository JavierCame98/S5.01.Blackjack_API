package It_Academy.blackjack_api.domain.model.aggregate;

import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;
import It_Academy.blackjack_api.domain.model.valueObjects.game.*;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.model.valueObjects.turn.Turn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private GameId id;
    private PlayerId playerId;
    private Hand playerHand;
    private Hand dealerHand;
    private Deck deck;
    private GameStatus gameStatus;
    private List<Turn> turns;

    private Game(){};

    public static Game create(PlayerId playerId, DeckCount deckCount){
        Game game = new Game();
        game.playerId = playerId;
        game.deck = Deck.of(deckCount);
        game.playerHand = Hand.empty();
        game.dealerHand = Hand.empty();
        game.gameStatus = GameStatus.PLAYING;
        game.turns = new ArrayList<>();

        game.playerHand = game.playerHand.addCard(game.deck.draw());
        game.playerHand = game.playerHand.addCard(game.deck.draw());

        if(game.playerHand.isBalcjack()){
            game.gameStatus = GameStatus.PLAYER_WIN;
        }

        return game;
    }

    public void hit(){
        validateIsPlaying();
        Card card = deck.draw();
        playerHand = playerHand.addCard(card);
        turns.add(Turn.playerHit(card));

        if(playerHand.isBusted()){
            gameStatus = GameStatus.DEALER_WIN;
        }
    }

    public void stand() {
        validateIsPlaying();

        turns.add(Turn.playerStand());
        resolverDealerTurn();
        resolverWinner();

    }
     //meto aquí la lógica interna para resolver los turnos y no tenerlo todo en stand
    private void resolverDealerTurn(){
        while(dealerHand.getValue() < 17){
            Card card = deck.draw();
            dealerHand = dealerHand.addCard(card);
            turns.add(Turn.dealerHit(card));
        }
    }

    private void resolverWinner(){
        int playerValue = playerHand.getValue();
        int dealerValue = dealerHand.getValue();

        if(dealerHand.isBusted() || playerValue > dealerValue){
            gameStatus = GameStatus.PLAYER_WIN;
        } else if(playerValue < dealerValue){
            gameStatus = GameStatus.DEALER_WIN;
        } else {
            gameStatus = GameStatus.TIE;
        }
    }

    //método Guard para asegurar que se puede seguir jugando
    private void validateIsPlaying(){
        if(gameStatus.isFinished()){
            throw new IllegalArgumentException("El juego ha terminado con este estado:" + gameStatus);
        }
    }

    public GameId        getId()          { return id; }
    public PlayerId      getPlayerId()    { return playerId; }
    public Hand          getPlayerHand()  { return playerHand; }
    public Hand          getDealerHand()  { return dealerHand; }
    public Deck          getDeck()        { return deck; }
    public GameStatus    getStatus()      { return gameStatus; }
    public List<Turn>    getTurnHistory() { return Collections.unmodifiableList(turns); }


    public void setId(GameId id) { this.id = id; }
}

