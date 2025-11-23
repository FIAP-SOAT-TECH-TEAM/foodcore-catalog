package unit.controller.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.category.SaveCategoryController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.CatalogDTO;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers.CatalogDTOMapper;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.responses.CategoryResponse;
import com.soat.fiap.food.core.shared.core.interfaceadapters.dto.FileUploadDTO;
import com.soat.fiap.food.core.shared.infrastructure.common.source.ImageDataSource;

import unit.fixtures.CatalogFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("SaveCategoryController - Testes Unitários")
class SaveCategoryControllerTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Mock
	private ImageDataSource imageDataSource;

	@Test @DisplayName("Deve salvar categoria com sucesso sem imagem")
	void shouldSaveCategorySuccessfullyWithoutImage() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithCategories();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);
		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));
		when(catalogDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		var categoryRequest = CatalogFixture.createValidCategoryRequest(1L, "Brindes", "Brindes artesanais", true, 1);

		// Act
		var response = assertDoesNotThrow(
				() -> SaveCategoryController.saveCategory(categoryRequest, null, catalogDataSource, imageDataSource));

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("Brindes");

		verify(catalogDataSource, times(1)).save(any(CatalogDTO.class));
	}

	@Test @DisplayName("Deve executar fluxo sem exceções com imagem")
	void shouldExecuteFlowWithImage() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithCategories();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));
		when(catalogDataSource.save(any())).thenAnswer(invocation -> {
			CatalogDTO catalogDTO = invocation.getArgument(0);

			var category = CatalogFixture.createCategoryDTO(1L, catalogDTO.categories().getLast(), null);

			var categories = new ArrayList<>(catalogDTO.categories());

			categories.removeLast();
			categories.add(category);

			return CatalogFixture.createCatalogDTO(catalogDTO, categories);
		});

		when(imageDataSource.uploadImage(any(), any())).thenReturn("path/to/image.jpg");

		var fileUpload = new FileUploadDTO("image.jpg", new byte[]{1, 2, 3});

		var categoryRequest = CatalogFixture.createValidCategoryRequest(1L, "Lanches Kids", "Lanches para crianças",
				true, 2);

		// Act & Assert
		assertDoesNotThrow(() -> SaveCategoryController.saveCategory(categoryRequest, fileUpload, catalogDataSource,
				imageDataSource));

		verify(imageDataSource).uploadImage(any(), any());
		verify(catalogDataSource, atLeastOnce()).save(any());
	}

	@Test @DisplayName("Deve retornar resposta válida para categoria criada")
	void shouldReturnValidResponse() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithCategories();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);
		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));
		when(catalogDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		var request = CatalogFixture.createValidCategoryRequest(1L, "Sobremesas", "Doces e sobremesas", true, 3);

		// Act
		CategoryResponse response = SaveCategoryController.saveCategory(request, null, catalogDataSource,
				imageDataSource);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("Sobremesas");

		verify(catalogDataSource, times(1)).save(any());
	}
}
