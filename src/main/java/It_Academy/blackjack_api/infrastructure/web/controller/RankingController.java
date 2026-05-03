package It_Academy.blackjack_api.infrastructure.web.controller;

import It_Academy.blackjack_api.application.useCase.ranking.GetRankingUseCase;
import It_Academy.blackjack_api.infrastructure.web.dto.response.PlayerRankingResponse;
import It_Academy.blackjack_api.infrastructure.web.mapper.PlayerRankingResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final GetRankingUseCase getRankingUseCase;
    private final PlayerRankingResponseMapper mapper;


    public Flux<PlayerRankingResponse> getRanking(){
        return getRankingUseCase.exectue()
                .index()
                .map(tuple -> mapper.toRankingResponse ((int) (tuple.getT1() + 1),
                tuple.getT2()));
    }
}
