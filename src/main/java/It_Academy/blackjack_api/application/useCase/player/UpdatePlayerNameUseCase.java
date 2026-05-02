package It_Academy.blackjack_api.application.useCase.player;

import It_Academy.blackjack_api.application.useCase.player.command.UpdatePlayerNameCommand;
import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerName;
import It_Academy.blackjack_api.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdatePlayerNameUseCase {

    PlayerRepository playerRepository;

    public Mono<Player> execute (UpdatePlayerNameCommand command){
        return playerRepository.findById(PlayerId.of(command.playerId()))
                .flatMap(player -> {
                    player.updateName(PlayerName.of(command.newName()));
                    return playerRepository.save(player);
                });
    }
}
