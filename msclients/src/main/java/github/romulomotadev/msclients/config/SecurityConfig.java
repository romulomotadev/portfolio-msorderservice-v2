package github.romulomotadev.msclients.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;


/* Regras que cada requisição HTTP deve seguir antes de chegar aos controllers */
@Configuration
@EnableWebSecurity // Habilita a integração da configuração do Spring Security com a aplicação web
@Data
public class SecurityConfig {

    //Injetando conversor JWT
    private final KeycloakJwtConverter keycloakJwtConverter;

    //Literalmente - set > Jwt > GrantedAuthorities > Converter
    private JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(
                keycloakJwtConverter
        );

        return converter;
    }


    @Bean
    // Cadeia de filtro de seguranca
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // regras do filtro de seguranca
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        //Autorizações por metodo http
                        /*Descrição ex. Se a requisição for GET e estiver em /clients/**, o usuário
                         precisa possuir a authority CLIENT_READ "No Keycloak"*/
                        .requestMatchers(HttpMethod.GET, "/clients/**")
                        .hasAuthority("CLIENT_READ")

                        .requestMatchers(HttpMethod.POST, "/clients/**")
                        .hasAuthority("CLIENT_WRITE")

                        .requestMatchers(HttpMethod.PUT, "/clients/**")
                        .hasAuthority("CLIENT_WRITE")

                        .requestMatchers(HttpMethod.DELETE, "/clients/**")
                        .hasAuthority("CLIENT_WRITE")

                        .anyRequest().authenticated() // Qualquer requisição precisa estar autenticada.
                )
                // Recebe um Access Token
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter())));

        return http.build();
    }
}
