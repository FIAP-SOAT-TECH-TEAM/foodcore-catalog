package unit.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.catalog.core.domain.vo.ImageUrl;

import unit.fixtures.CatalogFixture;

@DisplayName("ImageUrl - Testes Unitários")
class ImageUrlTest {

	@Test @DisplayName("Deve criar ImageUrl válida")
	void shouldCreateValidImageUrl() {
		// Arrange & Act
		ImageUrl imageUrl = CatalogFixture.createImageUrl("https://example.com/image.jpg");

		// Assert
		assertNotNull(imageUrl);
		assertEquals("https://example.com/image.jpg", imageUrl.imageUrl());
	}

	@Test @DisplayName("Deve lançar exceção para URL nula")
	void shouldThrowExceptionForNullUrl() {
		// Arrange & Act
		NullPointerException exception = assertThrows(NullPointerException.class, () -> {
			CatalogFixture.createImageUrl(null);
		});

		// Assert
		assertEquals("A url da imagem não pode ser nula", exception.getMessage());
	}

	@Test @DisplayName("Deve lançar exceção quando URL excede 500 caracteres")
	void shouldThrowExceptionWhenUrlExceedsMaxLength() {
		// Arrange
		String longUrl = "A".repeat(501);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			CatalogFixture.createImageUrl(longUrl);
		});

		// Assert
		assertEquals("Url da imagem deve ter no máximo 500 caracteres", exception.getMessage());
	}

	@Test @DisplayName("Deve permitir URL vazia")
	void shouldAllowEmptyUrl() {
		// Arrange & Act
		ImageUrl imageUrl = CatalogFixture.createImageUrl("");

		// Assert
		assertNotNull(imageUrl);
		assertEquals("", imageUrl.imageUrl());
	}
}
