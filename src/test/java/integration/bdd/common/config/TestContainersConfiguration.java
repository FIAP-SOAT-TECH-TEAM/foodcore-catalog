package integration.bdd.common.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Configuração base para testes de integração que utilizam múltiplos serviços
 * emulados via Testcontainers.
 *
 * <p>
 * Esta classe centraliza todos os contêineres necessários para testes de
 * integração, fornecendo um ambiente completamente isolado, estável e
 * reproduzível.
 * </p>
 *
 * <p>
 * <b>Serviços integrados:</b>
 * </p>
 * <ul>
 * <li><b>PostgreSQL</b> — banco principal usado pelos serviços testados.</li>
 * </ul>
 *
 * <p>
 * <b>Recursos relacionados:</b>
 * </p>
 * <ul>
 * <li><a href="https://testcontainers.com/modules/postgresql/">Testcontainers
 * PostgreSQL</a></li>
 * </ul>
 *
 * @see <a href=
 *      "https://docs.spring.io/spring-boot/reference/testing/testcontainers.html">
 *      Spring Boot — Testcontainers Integration</a>
 */
@Testcontainers
public abstract class TestContainersConfiguration {

	@Container @ServiceConnection
	private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16-alpine");
}
