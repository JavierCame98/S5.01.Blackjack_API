package It_Academy.blackjack_api.domain.model.valueObjects.player;

public record PlayerId (Long value) {

    public PlayerId {
        if(value == null || value <= 0){
            throw new IllegalArgumentException("PlayerId debe ser un número positivo");
        }
    }

    public static PlayerId of(Long value){
        return new PlayerId(value);
    }

    public static PlayerId of(String value){
        return new PlayerId(Long.valueOf(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
