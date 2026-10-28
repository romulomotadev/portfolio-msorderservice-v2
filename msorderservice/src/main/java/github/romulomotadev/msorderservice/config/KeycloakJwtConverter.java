package github.romulomotadev.msorderservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

//Conversor que recebe um Jwt e devolve uma coleção de GrantedAuthority
@Component
public class KeycloakJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    /* primeiro jwt - token JWT que o Spring Security recebeu e decodificou
       segundo jwt - classe relacionada às propriedades/configurações do Resource Server,
         não representa o JWT recebido na requisição*/
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        // acessando realm_access
        Map<String, Object> realmAccess =
                jwt.getClaim("realm_access");

        // capturando os roles
        Collection<String> roles =
                (Collection<String>) realmAccess.get("roles");

        return roles.stream()
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();
    }
}
