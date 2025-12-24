package unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.catalog.core.domain.vo.Details;

import unit.fixtures.CatalogFixture;

@DisplayName("Details - Testes Unitários")
class DetailsTest {

	@Test @DisplayName("Deve criar Details válido")
	void shouldCreateValidDetails() {
		// Arrange & Act
		Details details = CatalogFixture.createDetails("Hambúrguer", "Um delicioso hambúrguer artesanal");

		// Assert
		assertNotNull(details);
		assertEquals("Hambúrguer", details.name());
		assertEquals("Um delicioso hambúrguer artesanal", details.description());
	}

	@Test @DisplayName("Deve lançar exceção para nome nulo")
	void shouldThrowExceptionForNullName() {
		// Arrange & Act
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			CatalogFixture.createDetails(null, "Descrição válida");
		});

		// Assert
		assertEquals("Nome não pode ser nulo", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para descrição nula")
	void shouldThrowExceptionForNullDescription() {
		// Arrange & Act
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			CatalogFixture.createDetails("Produto", null);
		});

		// Assert
		assertEquals("Descrição não pode ser nula", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para nome vazio")
	void shouldThrowExceptionForEmptyName() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			CatalogFixture.createDetails("   ", "Descrição válida");
		});

		// Assert
		assertEquals("Nome não pode estar vazio", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção para descrição vazia")
	void shouldThrowExceptionForEmptyDescription() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			CatalogFixture.createDetails("Produto", "   ");
		});

		// Assert
		assertEquals("Descrição não pode estar vazia", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção quando nome excede 100 caracteres")
	void shouldThrowExceptionWhenNameExceedsMaxLength() {
		// Arrange
		String longName = "A".repeat(101);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			CatalogFixture.createDetails(longName, "Descrição válida");
		});

		// Assert
		assertEquals("Nome deve ter no máximo 100 caracteres", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção quando descrição excede 1000 caracteres")
	void shouldThrowExceptionWhenDescriptionExceedsMaxLength() {
		// Arrange
		String longDescription = "A".repeat(1001);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			CatalogFixture.createDetails("Produto", longDescription);
		});

		// Assert
		assertEquals("Descrição deve ter no máximo 1000 caracteres", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção quando nome e descrição são iguais")
	void shouldThrowExceptionWhenNameEqualsDescription() {
		// Arrange
		String same = "Texto igual";

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			CatalogFixture.createDetails(same, same);
		});

		// Assert
		assertEquals("Nome e descrição não podem ser iguais", exception.getMessage());
	}

	@Test @DisplayName("Deve criar Details com espaços removidos")
	void shouldTrimValues() {
		// Arrange & Act
		Details details = CatalogFixture.createDetails("   Produto   ", "   Uma descrição válida   ");

		// Assert
		assertEquals("Produto", details.name());
		assertEquals("Uma descrição válida", details.description());
	}
}
