package github.romulomotadev.msorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsorderserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsorderserviceApplication.class, args);
	}

}
