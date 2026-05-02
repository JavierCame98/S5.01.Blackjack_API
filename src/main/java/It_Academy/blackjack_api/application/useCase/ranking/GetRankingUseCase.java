package It_Academy.blackjack_api.application.useCase.ranking;

import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class GetRankingUseCase {

    private final PlayerRepository playerRepository;

    public Flux<Player> exectue (){
        return playerRepository.findAllOrderByWinRatesDesc();
    }
}
