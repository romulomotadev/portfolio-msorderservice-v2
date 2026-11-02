package github.romulomotadev.mscloudgateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) //autenticada por Bearer Token

                //Definir quais requisições são autorizadas.
                .authorizeExchange(exchange -> exchange
                        .anyExchange().authenticated() //Qualquer requisição que passar pelo Gateway precisa estar autenticada.
                )

                //Este Gateway é um OAuth2 Resource Server e aceita Bearer Tokens no formato JWT.
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults())
                );

        return http.build();
    }
}
