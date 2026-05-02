package It_Academy.blackjack_api.infrastructure.web.dto.response;

public record PlayerRankingResponse(int    position,
                                    Long   id,
                                    String name,
                                    int    gamesPlayed,
                                    int    gamesWon,
                                    double winRate) {}
