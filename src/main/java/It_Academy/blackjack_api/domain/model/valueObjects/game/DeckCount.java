package It_Academy.blackjack_api.domain.model.valueObjects.game;

public enum DeckCount {
    ONE(1), TWO(2), FOUR(4), SIX(6), EIGHT(8);

    private final int value;
    DeckCount(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DeckCount of(int n) {
        return switch (n) {
            case 1 -> ONE;
            case 2 -> TWO;
            case 4 -> FOUR;
            case 6 -> SIX;
            case 8 -> EIGHT;
            default -> throw new IllegalArgumentException(
                    "Número de barajas no válido: " + n + ". Valores permitidos: 1, 2, 4, 6, 8");
        };
    }
}
