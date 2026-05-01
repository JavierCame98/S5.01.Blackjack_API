package It_Academy.blackjack_api.infrastructure.persistence.mysql.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table ("players")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlayerEntity {

    @Id
    private Long id;

    private String name;

    @Column("games_played") private int    gamesPlayed;
    @Column("games_won")    private int    gamesWon;
    @Column("games_lost")   private int    gamesLost;
    @Column("games_tied")   private int    gamesTied;
    @Column("win_rate")     private double winRate;
}
