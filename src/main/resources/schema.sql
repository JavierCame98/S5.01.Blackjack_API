-- Players table (MySQL via R2DBC)
-- Stats are updated each time a game finishes via GameFinishedEvent.
CREATE TABLE IF NOT EXISTS players (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    games_played INT        NOT NULL DEFAULT 0,
    games_won    INT        NOT NULL DEFAULT 0,
    games_lost   INT        NOT NULL DEFAULT 0,
    games_tied   INT        NOT NULL DEFAULT 0,
    win_rate     DOUBLE     NOT NULL DEFAULT 0.0,
    created_at   TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_player_name (name)
);
