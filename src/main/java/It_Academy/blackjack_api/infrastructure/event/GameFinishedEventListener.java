package It_Academy.blackjack_api.infrastructure.event;

import It_Academy.blackjack_api.domain.event.GameFinishedEvent;
import It_Academy.blackjack_api.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static reactor.netty.http.HttpConnectionLiveness.log;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameFinishedEventListener {

    private final PlayerRepository playerRepository;


    @EventListener
    @Async
    public void handle(GameFinishedEvent event) {
        playerRepository.findById(event.playerId())
                .flatMap(player -> {
                    player.recordGameResult(event.finalStatus());
                    return playerRepository.save(player);
                })
                .subscribe(
                updated -> log.debug("Stats actualizadas para jugador: {}",
                        updated.getId().value()),
                error   -> log.error("Error actualizando stats del jugador {}: {}",
                        event.playerId().value(), error.getMessage())
        );
    }
}

