package imwhs.krit_server_sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@SpringBootApplication
public class KritServerSandboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(KritServerSandboxApplication.class, args);
	}

}
