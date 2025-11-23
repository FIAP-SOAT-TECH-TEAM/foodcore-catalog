package unit.presenter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.presenter.web.api.CategoryPresenter;

import unit.fixtures.CatalogFixture;

@DisplayName("CategoryPresenter - Testes Unitários")
class CategoryPresenterTest {

	@Test @DisplayName("Deve converter Category para CategoryResponse")
	void shouldConvertCategoryToResponse() {
		// Arrange
		var category = CatalogFixture.createValidCategory();

		// Act
		var response = CategoryPresenter.toCategoryResponse(category);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(category.getId());
		assertThat(response.getName()).isEqualTo(category.getName());
		assertThat(response.getDescription()).isEqualTo(category.getDescription());
	}

	@Test @DisplayName("Deve converter lista de categorias para lista de responses")
	void shouldConvertCategoryListToResponseList() {
		// Arrange
		var category = CatalogFixture.createValidCategory();
		var categories = List.of(category);

		// Act
		var responseList = CategoryPresenter.toListCategoryResponse(categories);

		// Assert
		assertThat(responseList).isNotNull();
		assertThat(responseList).hasSize(1);
		assertThat(responseList.getFirst().getId()).isEqualTo(category.getId());
	}

	@Test @DisplayName("Deve executar conversão sem exceções")
	void shouldConvertWithoutExceptions() {
		// Arrange
		var category = CatalogFixture.createValidCategory();

		// Act
		var response = CategoryPresenter.toCategoryResponse(category);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getName()).isNotBlank();
	}
}
