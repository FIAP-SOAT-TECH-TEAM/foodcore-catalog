package unit.controller.category;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.controller.category.DeleteCategoryController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers.CatalogDTOMapper;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

import unit.fixtures.CatalogFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("DeleteCategoryController - Testes Unitários")
class DeleteCategoryControllerTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Test @DisplayName("Deve excluir categoria com sucesso")
	void shouldDeleteCategorySuccessfully() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithCategories();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));
		when(catalogDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		var catalogId = 1L;
		var categoryId = 1L;

		// Act & Assert
		assertThatCode(() -> DeleteCategoryController.deleteCategory(catalogId, categoryId, catalogDataSource))
				.doesNotThrowAnyException();

		verify(catalogDataSource, times(1)).findById(catalogId);
		verify(catalogDataSource, times(1)).save(any());
	}

	@Test @DisplayName("Deve executar fluxo completo sem lançar exceções")
	void shouldExecuteFullFlowWithoutExceptions() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithCategories();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));
		when(catalogDataSource.save(any())).thenReturn(catalogDto);

		// Act & Assert
		assertThatCode(() -> DeleteCategoryController.deleteCategory(2L, 1L, catalogDataSource))
				.doesNotThrowAnyException();

		verify(catalogDataSource, atLeastOnce()).findById(any());
		verify(catalogDataSource, atLeastOnce()).save(any());
	}

	@Test @DisplayName("Deve chamar gateway e salvar catálogo após remoção")
	void shouldCallGatewayAndSaveCatalog() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithCategories();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));
		when(catalogDataSource.save(any())).thenReturn(catalogDto);

		// Act
		DeleteCategoryController.deleteCategory(10L, 2L, catalogDataSource);

		// Assert
		verify(catalogDataSource).findById(10L);
		verify(catalogDataSource).save(any());
	}
}
