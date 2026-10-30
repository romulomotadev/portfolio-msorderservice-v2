package github.romulomotadev.msorderservice.config;

import feign.RequestInterceptor;
import github.romulomotadev.msorderservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;


@RequiredArgsConstructor
public class FeignSecurityConfig {

    // Serviço responsável por solicitar e retornar
    // o Access Token do orders-service no Keycloak.
    private final TokenService tokenService;

    /**
     * Intercepta a requisição Feign antes de ela ser enviada
     * para outro microsserviço.
     *
     * Obtém um Access Token através do TokenService e adiciona
     * esse token ao header Authorization no formato Bearer.
     *
     * Resultado:
     * Authorization: Bearer <access_token>
     */
    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {

            // Solicita ao TokenService um Access Token válido.
            String accessToken =
                    tokenService.getAccessToken();

            // Adiciona o token ao header HTTP da requisição Feign.
            requestTemplate.header(
                    "Authorization",
                    "Bearer " + accessToken
            );
        };
    }
}
