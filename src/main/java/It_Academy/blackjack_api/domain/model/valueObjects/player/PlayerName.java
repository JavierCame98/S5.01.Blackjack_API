package It_Academy.blackjack_api.domain.model.valueObjects.player;


public record PlayerName (String value){

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 60;

    public PlayerName {
        if(value == null || value.isBlank()){
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
        }
        String trimmed = value.trim();
        if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "El nombre debe tener entre %d y %d caracteres".formatted(MIN_LENGTH, MAX_LENGTH));
        }
        value = trimmed;
    }

    public static PlayerName of(String value){
        return new PlayerName(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
