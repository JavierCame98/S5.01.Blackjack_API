package It_Academy.blackjack_api.infrastructure.web.controller;

import It_Academy.blackjack_api.application.useCase.ranking.GetRankingUseCase;
import It_Academy.blackjack_api.infrastructure.web.dto.response.PlayerRankingResponse;
import It_Academy.blackjack_api.infrastructure.web.mapper.PlayerRankingResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "Clasificación de jugadores por winRate")
public class RankingController {

    private final GetRankingUseCase getRankingUseCase;
    private final PlayerRankingResponseMapper mapper;

    @GetMapping("/ranking")
    @Operation(summary = "Ver ranking", description = "Devuelve la clasificación de todos los jugadores ordenada por tasa de victorias")
    @ApiResponse(responseCode = "200", description = "Ranking obtenido correctamente")
    public Flux<PlayerRankingResponse> getRanking(){
        return getRankingUseCase.execute()
                .index()
                .map(tuple -> mapper.toRankingResponse((int) (tuple.getT1() + 1),
                tuple.getT2()));
    }
}
