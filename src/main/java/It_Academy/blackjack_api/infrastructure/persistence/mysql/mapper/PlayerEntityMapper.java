package It_Academy.blackjack_api.infrastructure.persistence.mysql.mapper;

import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerName;
import It_Academy.blackjack_api.infrastructure.persistence.mysql.entity.PlayerEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerEntityMapper {

    public PlayerEntity toEntity(Player player) {
        return PlayerEntity.builder()
                .id(player.getId() != null ? player.getId().value() : null)
                .name(player.getName().value())
                .gamesPlayed(player.getGamesPlayed())
                .gamesWon(player.getGamesWon())
                .gamesLost(player.getGamesLost())
                .gamesTied(player.getGamesTied())
                .winRate(player.getWinRate())
                .build();
    }

    public Player toDomain(PlayerEntity entity) {
        Player player = Player.restore(
                PlayerId.of(entity.getId()),
                PlayerName.of(entity.getName()),
                entity.getGamesPlayed(),
                entity.getGamesWon(),
                entity.getGamesLost(),
                entity.getGamesTied(),
                entity.getWinRate()
        );
        return player;
    }
}
