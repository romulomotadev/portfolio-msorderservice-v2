package github.romulomotadev.msorderservice.service;

import github.romulomotadev.msorderservice.dto.KeycloakTokenResponse;
import github.romulomotadev.msorderservice.response.KeycloakClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Data
public class TokenService {

    private final KeycloakClient keycloakClient;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public String getAccessToken() {

        MultiValueMap<String, String> form =
                new LinkedMultiValueMap<>();

        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        KeycloakTokenResponse response =
                keycloakClient.getToken(form);

        /*Imprimi payload do jwt, para inspencionar o campo iss*/
/*        String accessToken = response.getAccessToken();

        String[] parts = accessToken.split("\\.");

        String payload = new String(
                java.util.Base64.getUrlDecoder().decode(parts[1]),
                java.nio.charset.StandardCharsets.UTF_8
        );

        System.out.println("TOKEN PAYLOAD: " + payload);

        return accessToken;*/

        return response.getAccessToken();
    }
}
