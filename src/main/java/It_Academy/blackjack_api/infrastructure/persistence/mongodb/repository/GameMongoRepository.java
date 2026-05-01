package It_Academy.blackjack_api.infrastructure.persistence.mongodb.repository;

import It_Academy.blackjack_api.infrastructure.persistence.mongodb.documents.GameDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


//conecta java con el motor de d.b de MongoDb, heredas métodos de ReactiveMon
public interface GameMongoRepository extends ReactiveMongoRepository <GameDocument, String> {
}
