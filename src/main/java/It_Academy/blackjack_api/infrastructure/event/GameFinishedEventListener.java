package It_Academy.blackjack_api.infrastructure.event;

import It_Academy.blackjack_api.domain.event.GameFinishedEvent;
import It_Academy.blackjack_api.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameFinishedEventListener {

    private final PlayerRepository playerRepository;

    @EventListener
    public void handle(GameFinishedEvent event) {
        playerRepository.findById(event.playerId())
                .flatMap(player -> {
                    player.recordGameResult(event.finalStatus());
                    return playerRepository.save(player);
                })
                .subscribe();
    }

}
