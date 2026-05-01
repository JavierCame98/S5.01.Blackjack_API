package It_Academy.blackjack_api.domain.event;

public interface DomainEventPublisher {
    void publish (DomainEvent event);
}
