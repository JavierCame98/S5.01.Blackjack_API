package It_Academy.blackjack_api.infrastructure.web.controller;

import It_Academy.blackjack_api.application.exception.GameNotFoundException;
import It_Academy.blackjack_api.application.useCase.game.CreateGameUseCase;
import It_Academy.blackjack_api.application.useCase.game.DeleteGameUseCase;
import It_Academy.blackjack_api.application.useCase.game.GetGameByIdUseCase;
import It_Academy.blackjack_api.application.useCase.game.PlayGameUseCase;
import It_Academy.blackjack_api.application.useCase.game.command.CreateGameCommand;
import It_Academy.blackjack_api.application.useCase.game.command.PlayGameCommand;
import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Rank;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Suit;
import It_Academy.blackjack_api.domain.model.valueObjects.game.*;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.infrastructure.web.dto.response.GameResponse;
import It_Academy.blackjack_api.infrastructure.web.exception.GlobalExceptionHandler;
import It_Academy.blackjack_api.infrastructure.web.mapper.GameResponseMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(GameController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("GameController")
class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean private CreateGameUseCase  createGameUseCase;
    @MockitoBean private GetGameByIdUseCase getGameByIdUseCase;
    @MockitoBean private PlayGameUseCase    playGameUseCase;
    @MockitoBean private DeleteGameUseCase  deleteGameUseCase;
    @MockitoBean private GameResponseMapper responseMapper;      // nombre del campo en el controller

    // ── Helper ────────────────────────────────────────────────────────────────

    // Card(Suit, Rank) — orden correcto del record
    private Game buildGame(String gameId, GameStatus status) {
        Hand playerHand = Hand.empty()
                .addCard(new Card(Suit.SPADES, Rank.ACE))
                .addCard(new Card(Suit.HEARTS, Rank.KING));
        Hand dealerHand = Hand.empty()
                .addCard(new Card(Suit.CLUBS, Rank.SEVEN));

        return Game.restore(
                GameId.of(gameId),
                PlayerId.of(1L),
                playerHand,
                dealerHand,
                Deck.of(DeckCount.ONE),
                status,
                List.of()
        );
    }

    private GameResponse buildResponse(String gameId, String status) {
        GameResponse.CardInfo  card  = new GameResponse.CardInfo("ACE", "SPADES", 11);
        GameResponse.HandInfo  pHand = new GameResponse.HandInfo(List.of(card), 21, true, false);
        GameResponse.HandInfo  dHand = new GameResponse.HandInfo(List.of(), 7, false, false);
        return new GameResponse(gameId, "1", pHand, dHand, status, 49);
    }

    // ── POST /game/new ────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /game/new debe retornar 201")
    void createGame_shouldReturn201() {
        Game         game = buildGame("abc123", GameStatus.PLAYING);
        GameResponse resp = buildResponse("abc123", "PLAYING");

        when(createGameUseCase.execute(any(CreateGameCommand.class)))
                .thenReturn(Mono.just(game));
        when(responseMapper.toResponse(game)).thenReturn(resp);

        webTestClient.post().uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        { "playerName": "Alice", "deckCount": 1 }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("abc123")
                .jsonPath("$.status").isEqualTo("PLAYING");
    }

    @Test
    @DisplayName("POST /game/new con nombre vacío debe retornar 400")
    void createGame_withBlankName_shouldReturn400() {
        webTestClient.post().uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        { "playerName": "", "deckCount": 1 }
                        """)
                .exchange()
                .expectStatus().isBadRequest();

        verify(createGameUseCase, never()).execute(any());
    }

    // ── GET /game/{id} ────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /game/{id} debe retornar 200")
    void getGame_shouldReturn200() {
        Game         game = buildGame("abc123", GameStatus.PLAYING);
        GameResponse resp = buildResponse("abc123", "PLAYING");

        when(getGameByIdUseCase.execute("abc123")).thenReturn(Mono.just(game));
        when(responseMapper.toResponse(game)).thenReturn(resp);

        webTestClient.get().uri("/game/abc123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("abc123");
    }

    @Test
    @DisplayName("GET /game/{id} con id inexistente debe retornar 404")
    void getGame_whenNotFound_shouldReturn404() {
        when(getGameByIdUseCase.execute("not-found"))
                .thenReturn(Mono.error(new GameNotFoundException("not-found")));

        webTestClient.get().uri("/game/not-found")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(404)
                .jsonPath("$.message").exists();
    }

    // ── POST /game/{id}/play ──────────────────────────────────────────────────

    @Test
    @DisplayName("POST /game/{id}/play con HIT debe retornar 200")
    void play_hit_shouldReturn200() {
        Game         game = buildGame("abc123", GameStatus.PLAYING);
        GameResponse resp = buildResponse("abc123", "PLAYING");

        when(playGameUseCase.execute(any(PlayGameCommand.class)))
                .thenReturn(Mono.just(game));
        when(responseMapper.toResponse(game)).thenReturn(resp);

        webTestClient.post().uri("/game/abc123/play")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        { "action": "HIT" }
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("PLAYING");
    }

    @Test
    @DisplayName("POST /game/{id}/play con STAND debe retornar 200 con partida finalizada")
    void play_stand_shouldReturn200WithFinishedGame() {
        Game         game = buildGame("abc123", GameStatus.PLAYER_WIN);
        GameResponse resp = buildResponse("abc123", "PLAYER_WIN");

        when(playGameUseCase.execute(any(PlayGameCommand.class)))
                .thenReturn(Mono.just(game));
        when(responseMapper.toResponse(game)).thenReturn(resp);

        webTestClient.post().uri("/game/abc123/play")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        { "action": "STAND" }
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("PLAYER_WIN");
    }

    @Test
    @DisplayName("POST /game/{id}/play con acción vacía debe retornar 400")
    void play_withBlankAction_shouldReturn400() {
        webTestClient.post().uri("/game/abc123/play")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        { "action": "" }
                        """)
                .exchange()
                .expectStatus().isBadRequest();

        verify(playGameUseCase, never()).execute(any());
    }

    // ── DELETE /game/{id}/delete ──────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /game/{id}/delete debe retornar 204")
    void deleteGame_shouldReturn204() {
        when(deleteGameUseCase.execute("abc123")).thenReturn(Mono.empty());

        webTestClient.delete().uri("/game/abc123/delete")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("DELETE /game/{id}/delete con id inexistente debe retornar 404")
    void deleteGame_whenNotFound_shouldReturn404() {
        when(deleteGameUseCase.execute("not-found"))
                .thenReturn(Mono.error(new GameNotFoundException("not-found")));

        webTestClient.delete().uri("/game/not-found/delete")
                .exchange()
                .expectStatus().isNotFound();
    }
}
