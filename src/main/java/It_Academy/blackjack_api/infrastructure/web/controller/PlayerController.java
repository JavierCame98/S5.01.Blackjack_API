package It_Academy.blackjack_api.infrastructure.web.controller;

import It_Academy.blackjack_api.application.useCase.player.UpdatePlayerNameUseCase;
import It_Academy.blackjack_api.application.useCase.player.command.UpdatePlayerNameCommand;
import It_Academy.blackjack_api.infrastructure.web.dto.request.UpdatePlayerRequest;
import It_Academy.blackjack_api.infrastructure.web.dto.response.PlayerResponse;
import It_Academy.blackjack_api.infrastructure.web.mapper.PlayerResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final UpdatePlayerNameUseCase updatePlayerNameUseCase;
    private final PlayerResponseMapper mapper;


    @PutMapping("/player/{playerId}")
    @Operation(summary = "Cambiar nombre del jugador",
            description = "Actualiza el nombre de un jugador existente")
    @ApiResponse(responseCode = "200", description = "Nombre actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Nombre no válido")
    @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    public Mono<PlayerResponse> updatePlayerName (@PathVariable String playerId,
                                                  @Valid @RequestBody UpdatePlayerRequest request){
        UpdatePlayerNameCommand command = new UpdatePlayerNameCommand(playerId, request.newName());
        return updatePlayerNameUseCase.execute(command)
                .map(mapper::toResponse);

    }
}
