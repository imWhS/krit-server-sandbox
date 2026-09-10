package imwhs.krit_server_sandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl") // 작업자 정보를 제공받을 빈 설정 후 JPA Auditing 활성화
@SpringBootApplication
public class KritServerSandboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(KritServerSandboxApplication.class, args);
	}

}
