package It_Academy.blackjack_api.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title       = "Blackjack API",
                version     = "1.0.0",
                description = "API reactiva de Blackjack con Spring WebFlux, MongoDB y MySQL"
        )
)
public class SwaggerConfig { }
