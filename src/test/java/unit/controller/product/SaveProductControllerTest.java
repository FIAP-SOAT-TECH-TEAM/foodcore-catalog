package unit.controller.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.SaveProductController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.CatalogDTO;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers.CatalogDTOMapper;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.responses.ProductResponse;
import com.soat.fiap.food.core.shared.core.interfaceadapters.dto.FileUploadDTO;
import com.soat.fiap.food.core.shared.infrastructure.common.source.ImageDataSource;

import unit.fixtures.CatalogFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("SaveProductController – Testes Unitários")
class SaveProductControllerTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Mock
	private ImageDataSource imageDataSource;

	@Test @DisplayName("Deve salvar produto com sucesso sem imagem")
	void shouldSaveProductSuccessfullyWithoutImage() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithProducts();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));
		when(catalogDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		var productRequest = CatalogFixture.createValidProductRequest(1L, "X-Burger Kids", "Lanche infantil",
				BigDecimal.valueOf(12.50), 10, 1);

		// Act
		var response = assertDoesNotThrow(
				() -> SaveProductController.saveProduct(1L, productRequest, null, catalogDataSource, imageDataSource));

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("X-Burger Kids");
		verify(catalogDataSource, times(1)).save(any());
	}

	@Test @DisplayName("Deve executar fluxo com imagem sem lançar exceções")
	void shouldExecuteFlowWithImage() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithProducts();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));

		when(catalogDataSource.save(any())).thenAnswer(invocation -> {
			CatalogDTO catalogDTO = invocation.getArgument(0);

			var firstCategory = catalogDTO.categories().getFirst();
			var product = CatalogFixture.createProductDTO(1L, firstCategory.products().getLast());

			var products = new ArrayList<>(firstCategory.products());
			products.removeLast();
			products.add(product);

			var category = CatalogFixture.createCategoryDTO(1L, firstCategory, products);

			var categories = new ArrayList<>(catalogDTO.categories());
			categories.removeFirst();
			categories.addFirst(category);

			return CatalogFixture.createCatalogDTO(catalogDTO, categories);
		});

		when(imageDataSource.uploadImage(any(), any())).thenReturn("path/to/image.jpg");

		var fileUpload = new FileUploadDTO("product.jpg", new byte[]{1, 2, 3});

		var productRequest = CatalogFixture.createValidProductRequest(1L, "Milk Shake", "Bebida doce",
				BigDecimal.valueOf(20.00), 5, 1);

		// Act
		assertDoesNotThrow(() -> SaveProductController.saveProduct(1L, productRequest, fileUpload, catalogDataSource,
				imageDataSource));

		// Assert
		verify(imageDataSource).uploadImage(any(), any());
		verify(catalogDataSource, atLeastOnce()).save(any());
	}

	@Test @DisplayName("Deve retornar ProductResponse válido")
	void shouldReturnValidResponse() {
		// Arrange
		var catalog = CatalogFixture.createCatalogWithProducts();
		var catalogDto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findById(any())).thenReturn(Optional.of(catalogDto));
		when(catalogDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		var request = CatalogFixture.createValidProductRequest(1L, "Sobremesa Kids", "Doce infantil",
				BigDecimal.valueOf(9.99), 4, 1);

		// Act
		ProductResponse response = SaveProductController.saveProduct(1L, request, null, catalogDataSource,
				imageDataSource);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getName()).isEqualTo("Sobremesa Kids");
		verify(catalogDataSource, times(1)).save(any());
	}
}
