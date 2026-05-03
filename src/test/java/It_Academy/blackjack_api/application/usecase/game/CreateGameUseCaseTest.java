package It_Academy.blackjack_api.application.usecase.game;

import It_Academy.blackjack_api.application.useCase.game.CreateGameUseCase;
import It_Academy.blackjack_api.application.useCase.game.command.CreateGameCommand;
import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Rank;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Suit;
import It_Academy.blackjack_api.domain.model.valueObjects.game.DeckCount;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameId;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameStatus;
import It_Academy.blackjack_api.domain.model.valueObjects.game.Hand;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerName;
import It_Academy.blackjack_api.domain.repository.GameRepository;
import It_Academy.blackjack_api.domain.repository.PlayerRepository;
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
@DisplayName("CreateGameUseCase")
class CreateGameUseCaseTest {

    @Mock private GameRepository   gameRepository;
    @Mock private PlayerRepository playerRepository;

    @InjectMocks private CreateGameUseCase createGameUseCase;

    // ── Helper ────────────────────────────────────────────────────────────────

    private Player playerWithId(String name, Long id) {
        return Player.restore(
                PlayerId.of(id),
                PlayerName.of(name),
                0, 0, 0, 0, 0.0
        );
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("cuando el jugador ya existe debe reutilizarlo y crear la partida")
    void execute_whenPlayerExists_shouldReusePlayerAndCreateGame() {
        // Arrange
        CreateGameCommand command  = new CreateGameCommand("Alice", 1);
        Player            existing = playerWithId("Alice", 1L);

        when(playerRepository.findByName(PlayerName.of("Alice")))
                .thenReturn(Mono.just(existing));
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // Act & Assert
        StepVerifier.create(createGameUseCase.execute(command))
                .assertNext(game -> {
                    assertThat(game.getPlayerId()).isEqualTo(existing.getId());
                    assertThat(game.getStatus()).isNotNull();
                    assertThat(game.getPlayerHand().size()).isEqualTo(2);
                    assertThat(game.getDealerHand().size()).isEqualTo(1);
                })
                .verifyComplete();

        verify(playerRepository).findByName(PlayerName.of("Alice"));
        verify(playerRepository, never()).save(any());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    @DisplayName("cuando el jugador no existe debe crearlo y luego crear la partida")
    void execute_whenPlayerNotExists_shouldCreatePlayerThenGame() {
        // Arrange
        CreateGameCommand command   = new CreateGameCommand("Bob", 1);
        Player            newPlayer = playerWithId("Bob", 2L);

        when(playerRepository.findByName(PlayerName.of("Bob")))
                .thenReturn(Mono.empty());
        when(playerRepository.save(any(Player.class)))
                .thenReturn(Mono.just(newPlayer));
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // Act & Assert
        StepVerifier.create(createGameUseCase.execute(command))
                .assertNext(game -> {
                    assertThat(game.getPlayerId()).isEqualTo(newPlayer.getId());
                    assertThat(game.getPlayerHand().size()).isEqualTo(2);
                    assertThat(game.getDealerHand().size()).isEqualTo(1);
                })
                .verifyComplete();

        verify(playerRepository).findByName(PlayerName.of("Bob"));
        verify(playerRepository).save(any(Player.class));
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    @DisplayName("el status inicial debe ser PLAYING o PLAYER_WIN si hay blackjack")
    void execute_initialStatus_shouldBePlayingOrPlayerWin() {
        // Arrange
        CreateGameCommand command = new CreateGameCommand("Carol", 1);
        Player            player  = playerWithId("Carol", 3L);

        when(playerRepository.findByName(PlayerName.of("Carol")))
                .thenReturn(Mono.just(player));
        when(gameRepository.save(any(Game.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        // Act & Assert
        StepVerifier.create(createGameUseCase.execute(command))
                .assertNext(game ->
                        assertThat(game.getStatus())
                                .isIn(GameStatus.PLAYING, GameStatus.PLAYER_WIN))
                .verifyComplete();
    }
}
