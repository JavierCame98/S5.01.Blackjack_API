package It_Academy.blackjack_api.application.usecase.game;

import It_Academy.blackjack_api.application.exception.GameAlreadyFinishedException;
import It_Academy.blackjack_api.application.exception.GameNotFoundException;
import It_Academy.blackjack_api.application.useCase.game.PlayGameUseCase;
import It_Academy.blackjack_api.application.useCase.game.command.PlayGameCommand;
import It_Academy.blackjack_api.domain.event.DomainEventPublisher;
import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Rank;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Suit;
import It_Academy.blackjack_api.domain.model.valueObjects.game.*;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.repository.GameRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlayGameUseCase")
class PlayGameUseCaseTest {

    @Mock private GameRepository       gameRepository;
    @Mock private DomainEventPublisher eventPublisher;

    @InjectMocks private PlayGameUseCase playGameUseCase;

    // ── Helpers ───────────────────────────────────────────────────────────────

    // Card(Suit, Rank) — orden correcto del record
    private Game gameInPlayingStatus() {
        Hand playerHand = Hand.empty()
                .addCard(new Card(Suit.HEARTS,   Rank.FIVE))
                .addCard(new Card(Suit.DIAMONDS, Rank.SIX));    // total: 11

        Hand dealerHand = Hand.empty()
                .addCard(new Card(Suit.CLUBS, Rank.SEVEN));     // total: 7

        return Game.restore(
                GameId.of("game-1"),
                PlayerId.of(1L),
                playerHand,
                dealerHand,
                Deck.of(DeckCount.ONE),
                GameStatus.PLAYING,
                List.of()
        );
    }

    private Game gameAlreadyFinished() {
        Hand playerHand = Hand.empty()
                .addCard(new Card(Suit.SPADES, Rank.ACE))
                .addCard(new Card(Suit.HEARTS, Rank.KING));     // Blackjack

        Hand dealerHand = Hand.empty()
                .addCard(new Card(Suit.CLUBS, Rank.SEVEN));

        return Game.restore(
                GameId.of("game-2"),
                PlayerId.of(1L),
                playerHand,
                dealerHand,
                Deck.of(DeckCount.ONE),
                GameStatus.PLAYER_WIN,                          // ya finalizada
                List.of()
        );
    }

    // ── Tests HIT ─────────────────────────────────────────────────────────────

    @Test
    @DisplayName("HIT debe añadir una carta a la mano del jugador")
    void execute_hit_shouldAddCardToPlayerHand() {
        Game game         = gameInPlayingStatus();
        int  cardsBefore  = game.getPlayerHand().size();

        when(gameRepository.findById(GameId.of("game-1")))
                .thenReturn(Mono.just(game));
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(playGameUseCase.execute(new PlayGameCommand("game-1", "HIT")))
                .assertNext(result ->
                        assertThat(result.getPlayerHand().size())
                                .isEqualTo(cardsBefore + 1))
                .verifyComplete();

        verify(gameRepository).save(any(Game.class));
        verify(eventPublisher, never()).publish(any());  // partida no ha terminado aún
    }

    // ── Tests STAND ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("STAND debe finalizar la partida con un status definido")
    void execute_stand_shouldFinishGame() {
        Game game = gameInPlayingStatus();

        when(gameRepository.findById(GameId.of("game-1")))
                .thenReturn(Mono.just(game));
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(playGameUseCase.execute(new PlayGameCommand("game-1", "STAND")))
                .assertNext(result -> {
                    assertThat(result.getStatus()).isNotEqualTo(GameStatus.PLAYING);
                    assertThat(result.getStatus())
                            .isIn(GameStatus.PLAYER_WIN, GameStatus.DEALER_WIN, GameStatus.TIE);
                })
                .verifyComplete();

        verify(eventPublisher).publish(any());          // partida terminó → evento publicado
        verify(gameRepository).save(any(Game.class));
    }

    // ── Tests de error ────────────────────────────────────────────────────────

    @Test
    @DisplayName("jugar una partida ya finalizada debe lanzar GameAlreadyFinishedException")
    void execute_whenGameAlreadyFinished_shouldThrowException() {
        Game finished = gameAlreadyFinished();

        when(gameRepository.findById(GameId.of("game-2")))
                .thenReturn(Mono.just(finished));

        StepVerifier.create(playGameUseCase.execute(new PlayGameCommand("game-2", "HIT")))
                .expectError(GameAlreadyFinishedException.class)
                .verify();

        verify(gameRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    @DisplayName("acción inválida debe lanzar IllegalArgumentException")
    void execute_withInvalidAction_shouldThrowIllegalArgumentException() {
        // parseTurnType() lanza la excepción antes de llegar al repositorio
        StepVerifier.create(playGameUseCase.execute(new PlayGameCommand("game-1", "INVALID")))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(gameRepository, never()).findById(any());
    }

    @Test
    @DisplayName("partida no encontrada debe propagarse como GameNotFoundException")
    void execute_whenGameNotFound_shouldPropagateGameNotFoundException() {
        when(gameRepository.findById(GameId.of("not-found")))
                .thenReturn(Mono.error(new GameNotFoundException("not-found")));

        StepVerifier.create(playGameUseCase.execute(new PlayGameCommand("not-found", "HIT")))
                .expectError(GameNotFoundException.class)
                .verify();
    }
}
