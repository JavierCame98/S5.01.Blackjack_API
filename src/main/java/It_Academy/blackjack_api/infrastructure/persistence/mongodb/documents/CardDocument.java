package It_Academy.blackjack_api.infrastructure.persistence.mongodb.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CardDocument {
    private String suit;
    private String rank;
}
