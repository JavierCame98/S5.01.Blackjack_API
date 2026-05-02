package It_Academy.blackjack_api.infrastructure.web.dto.response;

public record PlayerResponse (Long   id,
                              String name,
                              int    gamesPlayed,
                              int    gamesWon,
                              int    gamesLost,
                              int    gamesTied,
                              double winRate
) {}
