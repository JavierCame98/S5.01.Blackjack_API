# Blackjack API

API reactiva de Blackjack construida con **Spring Boot 3 + WebFlux**, persistencia dual en **MongoDB** (partidas) y **MySQL** (jugadores), y arquitectura hexagonal.

---

## Reglas del juego

- La baraja estándar tiene 52 cartas (configurable entre 1 y 8 barajas).
- Al crear una partida el jugador recibe **2 cartas** y el dealer **1 carta**.
- Los Ases valen **11 o 1** (el valor que más convenga al jugador).
- J, Q, K valen **10**.
- **HIT**: el jugador pide una carta. Si supera 21 → `DEALER_WIN`.
- **STAND**: el jugador se planta. El dealer roba hasta alcanzar al menos 17 y se determina el ganador.
- Blackjack inicial (As + figura con valor 10) → `PLAYER_WIN` inmediato.
- Al finalizar la partida las estadísticas del jugador se actualizan automáticamente en MySQL.

---

## Requisitos

| Herramienta | Versión mínima |
|---|---|
| Java | 21 |
| Maven | 3.9 |
| Docker & Docker Compose | 24 |

---

## Levantar el entorno

```bash
docker-compose up -d
```

Levanta MongoDB (puerto 27017) y MySQL (puerto 3306) con los datos de conexión preconfigurados.

---

## Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La API arranca en `http://localhost:8080`.

---

## Swagger UI

Documentación interactiva disponible en:

```
http://localhost:8080/swagger-ui.html
```

Especificación OpenAPI (JSON):

```
http://localhost:8080/v3/api-docs
```

---

## Endpoints

| Método | URL | Body | Respuesta |
|--------|-----|------|-----------|
| `POST` | `/game/new` | `CreateGameRequest` | `201 GameResponse` |
| `GET` | `/game/{id}` | — | `200 GameResponse` |
| `POST` | `/game/{id}/play` | `PlayGameRequest` | `200 GameResponse` |
| `DELETE` | `/game/{id}/delete` | — | `204 No Content` |
| `PUT` | `/player/{playerId}` | `UpdatePlayerRequest` | `200 PlayerResponse` |
| `GET` | `/ranking` | — | `200 PlayerRankingResponse[]` |

---

## Ejemplos JSON

### POST `/game/new` — Crear partida

**Request:**
```json
{
  "playerName": "Alice",
  "deckCount": 1
}
```

**Response `201`:**
```json
{
  "id": "683f2a1c4e0000b800c3d591",
  "playerId": "1",
  "playerHand": {
    "cards": [
      { "rank": "ACE",  "suit": "SPADES", "value": 11 },
      { "rank": "KING", "suit": "HEARTS", "value": 10 }
    ],
    "value": 21,
    "isBlackjack": true,
    "isBusted": false
  },
  "dealerHand": {
    "cards": [
      { "rank": "SEVEN", "suit": "CLUBS", "value": 7 }
    ],
    "value": 7,
    "isBlackjack": false,
    "isBusted": false
  },
  "status": "PLAYER_WIN",
  "remainingCards": 49
}
```

---

### POST `/game/{id}/play` — Realizar jugada

**Request (HIT):**
```json
{ "action": "HIT" }
```

**Request (STAND):**
```json
{ "action": "STAND" }
```

**Response `200`:** mismo esquema que `GameResponse` con el estado actualizado.

---

### PUT `/player/{playerId}` — Cambiar nombre

**Request:**
```json
{ "newName": "Bob" }
```

**Response `200`:**
```json
{
  "id": 1,
  "name": "Bob",
  "gamesPlayed": 5,
  "gamesWon": 3,
  "gamesLost": 1,
  "gamesTied": 1,
  "winRate": 0.6
}
```

---

### GET `/ranking` — Clasificación

**Response `200`:**
```json
[
  {
    "position": 1,
    "id": 1,
    "name": "Alice",
    "gamesPlayed": 10,
    "gamesWon": 8,
    "winRate": 0.8
  },
  {
    "position": 2,
    "id": 2,
    "name": "Bob",
    "gamesPlayed": 5,
    "gamesWon": 3,
    "winRate": 0.6
  }
]
```

---

## Respuestas de error

Todos los errores devuelven JSON estructurado:

```json
{
  "timestamp": "2026-05-03T19:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Game not found with id: abc123"
}
```

| Excepción | Código HTTP |
|---|---|
| `GameNotFoundException` | `404 Not Found` |
| `PlayerNotFoundException` | `404 Not Found` |
| `GameAlreadyFinishedException` | `422 Unprocessable Entity` |
| `IllegalArgumentException` | `400 Bad Request` |
| Validación (`@Valid`) | `400 Bad Request` |
| Genérica | `500 Internal Server Error` |
