package It_Academy.blackjack_api.domain.model.valueObjects.game;

public record GameId (String value) {

    public GameId {
        if( value == null || value.isBlank()){
            throw new IllegalArgumentException("GameId no puede estar vacío");
        }
    }

    public static GameId of(String value){
        return new GameId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
