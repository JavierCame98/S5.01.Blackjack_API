package It_Academy.blackjack_api.infrastructure.persistence.mongodb.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DeckDocument {
    private List<CardDocument> cards;
}
