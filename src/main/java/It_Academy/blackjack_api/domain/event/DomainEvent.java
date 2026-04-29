package It_Academy.blackjack_api.domain.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime ocurredAt();
}
