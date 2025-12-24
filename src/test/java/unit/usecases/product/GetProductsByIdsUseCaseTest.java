package unit.usecases.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.application.usecases.product.GetProductsByIdsUseCase;
import com.soat.fiap.food.core.catalog.core.domain.exceptions.ProductNotFoundException;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.ProductGateway;

import unit.fixtures.CatalogFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("GetProductsByIdsUseCase - Testes Unitários")
public class GetProductsByIdsUseCaseTest {

	@Mock
	private ProductGateway productGateway;

	@Test @DisplayName("Deve retornar a lista de produtos quando todos os IDs são encontrados")
	void shouldReturnListOfProductsWhenAllIdsAreFound() {
		// Arrange
		var product1 = CatalogFixture.createValidProduct();
		product1.setId(1L);
		var product2 = CatalogFixture.createBeverageProduct();
		product2.setId(2L);
		var productIds = List.of(1L, 2L);
		var expectedProducts = List.of(product1, product2);

		when(productGateway.findByProductIds(productIds)).thenReturn(Optional.of(expectedProducts));

		// Act
		var result = GetProductsByIdsUseCase.getProductsByIds(productIds, productGateway);

		// Assert
		assertThat(result).isNotNull().hasSize(2);
		assertThat(result.get(0).getId()).isEqualTo(1L);
		assertThat(result.get(1).getId()).isEqualTo(2L);
		verify(productGateway).findByProductIds(productIds);
	}

	@Test @DisplayName("Deve lançar exceção quando a lista de produtos encontrados está vazia")
	void shouldThrowExceptionWhenListOfFoundProductsIsEmpty() {
		// Arrange
		var productIds = List.of(999L, 998L);

		when(productGateway.findByProductIds(productIds)).thenReturn(Optional.empty());

		// Act & Assert
		assertThatThrownBy(() -> GetProductsByIdsUseCase.getProductsByIds(productIds, productGateway))
				.isInstanceOf(ProductNotFoundException.class)
				.hasMessageContaining("Nenhum produto encontrado para a lista de IDs de produtos fornecida");

		verify(productGateway).findByProductIds(productIds);
	}

	@Test @DisplayName("Deve lançar exceção quando o Optional está vazio")
	void shouldThrowExceptionWhenOptionalIsEmpty() {
		// Arrange
		var productIds = List.of(999L, 998L);

		when(productGateway.findByProductIds(productIds)).thenReturn(Optional.of(Collections.emptyList()));

		// Act & Assert
		assertThatThrownBy(() -> GetProductsByIdsUseCase.getProductsByIds(productIds, productGateway))
				.isInstanceOf(ProductNotFoundException.class)
				.hasMessageContaining("Nenhum produto encontrado para a lista de IDs de produtos fornecida");

		verify(productGateway).findByProductIds(productIds);
	}

	@Test @DisplayName("Deve lançar exceção quando um ou mais IDs de produtos não são encontrados")
	void shouldThrowExceptionWhenOneOrMoreProductIdsAreNotFound() {
		// Arrange
		var product1 = CatalogFixture.createValidProduct();
		product1.setId(1L);
		var requestedIds = List.of(1L, 999L, 2L);
		var foundProducts = List.of(product1, CatalogFixture.createBeverageProduct());
		foundProducts.get(1).setId(2L);

		when(productGateway.findByProductIds(requestedIds)).thenReturn(Optional.of(foundProducts));

		// Act & Assert
		assertThatThrownBy(() -> GetProductsByIdsUseCase.getProductsByIds(requestedIds, productGateway))
				.isInstanceOf(ProductNotFoundException.class)
				.hasMessageContaining("Produto não encontrado com id: 999");

		verify(productGateway).findByProductIds(requestedIds);
	}

	@Test @DisplayName("Deve retornar lista vazia de produtos encontrados sem lançar exceção")
	void shouldHandleEmptyListFromGateway() {
		// Arrange
		var productIds = List.of(1L, 2L);
		when(productGateway.findByProductIds(productIds)).thenReturn(Optional.of(Collections.emptyList()));

		// Act & Assert
		assertThatThrownBy(() -> GetProductsByIdsUseCase.getProductsByIds(productIds, productGateway))
				.isInstanceOf(ProductNotFoundException.class);

		verify(productGateway).findByProductIds(productIds);
	}

	@Test @DisplayName("Deve lançar exceção para lista de IDs nula")
	void shouldThrowExceptionForNullIdList() {
		// Arrange
		List<Long> productIds = null;

		// Act & Assert
		assertThatThrownBy(() -> GetProductsByIdsUseCase.getProductsByIds(productIds, productGateway))
				.isInstanceOf(ProductNotFoundException.class);
	}
}
