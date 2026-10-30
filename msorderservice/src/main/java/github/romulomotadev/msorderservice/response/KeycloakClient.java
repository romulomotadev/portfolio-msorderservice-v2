package github.romulomotadev.msorderservice.response;

import github.romulomotadev.msorderservice.dto.KeycloakTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Interface capaz de realizar chamadas HTTP.
@FeignClient(
        name = "keycloak-client",
        url = "${keycloak.url}"
)
public interface KeycloakClient {

    // Representar da requisição do token/client dos microsservicos
    @PostMapping(
            value = "/realms/${keycloak.realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE //representacao formato x-www-form-urlencoded
    )
    // Informações enviadas na requisicao grantType, clientId e clientSecret
    KeycloakTokenResponse getToken(
            @RequestBody MultiValueMap<String, String> form
    );

}
