package br.com.redes2.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da API. Roda na VM 2 (192.168.1.100).
 * Sobe o Tomcat na porta 8080 e conecta ao PostgreSQL local da VM 2.
 * O Nginx da VM 1 (192.168.1.10:80) encaminha as requisições para cá.
 */
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
