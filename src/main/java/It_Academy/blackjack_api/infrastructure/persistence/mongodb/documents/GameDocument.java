package It_Academy.blackjack_api.infrastructure.persistence.mongodb.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//esta es la clase que podremos mappear para documento y el builder lo usamos en el mapper tmb, solo agregados tienen @Document
@Document(collection = "games")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GameDocument {


    @Id
    private String id;

    private String playerId;
    private HandDocument playerHand;
    private HandDocument dealerHand;
    private DeckDocument deck;
    private String status;
    private List<TurnDocument>turns;


}
