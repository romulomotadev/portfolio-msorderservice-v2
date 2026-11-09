package github.romulomotadev.msorderservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/* Configuração necessaria:
msorderservice possui uma responsabilidade que os outros microsserviços não têm: ele precisa obter um
token do Keycloak para realizar chamadas autenticadas pelo Feign.
Por isso o (properties = {"keycloak.client-secret=test-secret"})
 */
@SpringBootTest(properties = {
		"keycloak.client-secret=test-secret"
})
class MsorderserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
