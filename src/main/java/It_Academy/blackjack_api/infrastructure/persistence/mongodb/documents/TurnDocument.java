package It_Academy.blackjack_api.infrastructure.persistence.mongodb.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TurnDocument {
    private String owner;
    private String type;
    private CardDocument card;
    private LocalDateTime localDateTime;
}
