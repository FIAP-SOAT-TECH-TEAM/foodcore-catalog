package unit.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.catalog.core.application.inputs.CategoryInput;
import com.soat.fiap.food.core.catalog.core.application.inputs.mappers.CategoryMapper;
import com.soat.fiap.food.core.catalog.core.domain.model.Category;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.requests.CategoryRequest;

import unit.fixtures.CatalogFixture;

@DisplayName("CategoryMapper - Testes Unitários")
class CategoryMapperTest {

	@Test @DisplayName("Deve mapear CategoryRequest para CategoryInput com sucesso")
	void shouldMapCategoryRequestToInput() {
		// Arrange
		CategoryRequest request = CatalogFixture.createValidCategoryRequest(1L, "Lanches",
				"Categoria de lanches diversos", true, 1);

		// Act
		CategoryInput result = CategoryMapper.toInput(request);

		// Assert
		assertNotNull(result);
		assertEquals(1L, result.catalogId());
		assertEquals("Lanches", result.name());
		assertEquals("Categoria de lanches diversos", result.description());
		assertTrue(result.active());
		assertEquals(1, result.displayOrder());
	}

	@Test @DisplayName("Deve mapear CategoryInput para Category domain com sucesso")
	void shouldMapCategoryInputToDomain() {
		// Arrange
		CategoryInput input = CatalogFixture.createValidCategoryInput(2L, "Bebidas", "Categoria de bebidas variadas",
				false, 2);

		// Act
		Category result = CategoryMapper.toDomain(input);

		// Assert
		assertNotNull(result);
		assertEquals("Bebidas", result.getDetails().name());
		assertEquals("Categoria de bebidas variadas", result.getDetails().description());
		assertFalse(result.isActive());
		assertEquals(2, result.getDisplayOrder());
		assertNotNull(result.getImageUrl());
		assertEquals("", result.getImageUrl().imageUrl());
	}

	@Test @DisplayName("Deve mapear CategoryRequest para CategoryInput")
	void shouldMapCategoryRequestToCategoryInput() {
		// Arrange
		var categoryRequest = CategoryRequest.builder().name("Lanches").description("Categoria de lanches").build();

		// Act
		var categoryInput = assertDoesNotThrow(() -> CategoryMapper.toInput(categoryRequest));

		// Assert
		assertThat(categoryInput).isNotNull();
		assertThat(categoryInput.name()).isEqualTo("Lanches");
		assertThat(categoryInput.description()).isEqualTo("Categoria de lanches");
	}

	@Test @DisplayName("Deve mapear CategoryRequest com valores nulos")
	void shouldMapCategoryRequestWithNullValues() {
		// Arrange
		var categoryRequest = CategoryRequest.builder().build();

		// Act
		var categoryInput = assertDoesNotThrow(() -> CategoryMapper.toInput(categoryRequest));

		// Assert
		assertThat(categoryInput).isNotNull();
	}

	@Test @DisplayName("Deve mapear CategoryRequest com nome em branco")
	void shouldMapCategoryRequestWithBlankName() {
		// Arrange
		var categoryRequest = CategoryRequest.builder().name("").description("").build();

		// Act
		var categoryInput = assertDoesNotThrow(() -> CategoryMapper.toInput(categoryRequest));

		// Assert
		assertThat(categoryInput).isNotNull();
		assertThat(categoryInput.name()).isEmpty();
		assertThat(categoryInput.description()).isEmpty();
	}
}
