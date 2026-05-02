package It_Academy.blackjack_api.infrastructure.web.controller;

import It_Academy.blackjack_api.application.useCase.game.CreateGameUseCase;
import It_Academy.blackjack_api.application.useCase.game.DeleteGameUseCase;
import It_Academy.blackjack_api.application.useCase.game.GetGameByIdUseCase;
import It_Academy.blackjack_api.application.useCase.game.PlayGameUseCase;
import It_Academy.blackjack_api.application.useCase.game.command.CreateGameCommand;
import It_Academy.blackjack_api.application.useCase.game.command.PlayGameCommand;
import It_Academy.blackjack_api.infrastructure.web.dto.request.CreateGameRequest;
import It_Academy.blackjack_api.infrastructure.web.dto.request.PlayGameRequest;
import It_Academy.blackjack_api.infrastructure.web.dto.response.GameResponse;
import It_Academy.blackjack_api.infrastructure.web.mapper.GameResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Tag(name = "Game", description = "Gestión de partidas de Blackjack")
public class GameController {

    private final CreateGameUseCase createGameUseCase;
    private final GameResponseMapper responseMapper;
    private final GetGameByIdUseCase getGameByIdUseCase;
    private final PlayGameUseCase playGameUseCase;
    private final DeleteGameUseCase deleteGameUseCase;

    @PostMapping("/game/new")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear partida",
            description = "Crea una nueva partida de Blackjack para el jugador indicado")
    @ApiResponse(responseCode = "201", description = "Partida creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos")
    public Mono<GameResponse> createGame (@Valid @RequestBody CreateGameRequest request){
        CreateGameCommand command = new CreateGameCommand(
                request.playerName(), request.deckCount());

        return createGameUseCase.execute(command)
                .map(responseMapper::toResponse);
    }

    @GetMapping("/game/{id}")
    @Operation(summary = "Obtener partida",
            description = "Obtiene los detalles de una partida específica")
    @ApiResponse(responseCode = "200", description = "Partida encontrada")
    @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    public Mono<GameResponse> getById (@PathVariable String id){
        return getGameByIdUseCase.execute(id)
                .map(responseMapper::toResponse);
    }

    @PostMapping("/game/{id}/play")
    @Operation(summary = "Realizar jugada",
            description = "Ejecuta una jugada HIT (pedir carta) o STAND (plantarse)")
    @ApiResponse(responseCode = "200", description = "Jugada realizada correctamente")
    @ApiResponse(responseCode = "400", description = "Acción no válida")
    @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    public Mono<GameResponse> play (@PathVariable String id,
                                    @Valid @RequestBody PlayGameRequest request){
        PlayGameCommand command = new PlayGameCommand(id,request.action());
        return playGameUseCase.execute(command)
                .map(responseMapper::toResponse);
    }

    @DeleteMapping("/game/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar partida",
            description = "Elimina una partida existente")
    @ApiResponse(responseCode = "204", description = "Partida eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    public Mono<Void> deleteGame (@PathVariable String id){
        return deleteGameUseCase.execute(id);
    }




}
