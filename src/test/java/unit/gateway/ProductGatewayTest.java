package unit.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.domain.model.Product;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.ProductDTO;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers.ProductDTOMapper;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.ProductGateway;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.ProductDataSource;

import unit.fixtures.CatalogFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("ProductGateway - Testes Unitários")
class ProductGatewayTest {

	@Mock
	private ProductDataSource productDataSource;

	@Test @DisplayName("Deve retornar lista de produtos quando encontrados por IDs")
	void shouldReturnListOfProductsWhenFound() {
		// Arrange
		var gateway = new ProductGateway(productDataSource);
		List<Long> productIds = List.of(1L, 2L);

		ProductDTO dto1 = CatalogFixture.createProductDTO(1L,
				ProductDTOMapper.toDTO(CatalogFixture.createValidProduct()));
		ProductDTO dto2 = CatalogFixture.createProductDTO(2L,
				ProductDTOMapper.toDTO(CatalogFixture.createBeverageProduct()));

		when(productDataSource.findByProductIds(anyList())).thenReturn(Optional.of(List.of(dto1, dto2)));

		// Act
		var result = gateway.findByProductIds(productIds);

		// Assert
		assertThat(result).isPresent();
		assertThat(result.get()).hasSize(2);
		assertThat(result.get().getFirst()).isInstanceOf(Product.class);
		verify(productDataSource).findByProductIds(productIds);
	}

	@Test @DisplayName("Deve retornar Optional.empty quando nenhum produto for encontrado")
	void shouldReturnEmptyWhenNoProductsFound() {
		// Arrange
		var gateway = new ProductGateway(productDataSource);
		List<Long> productIds = List.of(999L);

		when(productDataSource.findByProductIds(anyList())).thenReturn(Optional.empty());

		// Act
		var result = gateway.findByProductIds(productIds);

		// Assert
		assertThat(result).isEmpty();
		verify(productDataSource).findByProductIds(productIds);
	}

	@Test @DisplayName("Deve chamar o data source sem lançar exceção")
	void shouldCallDataSourceWithoutException() {
		// Arrange
		var gateway = new ProductGateway(productDataSource);
		List<Long> productIds = List.of(1L);

		when(productDataSource.findByProductIds(anyList())).thenReturn(Optional.of(List.of()));

		// Act & Assert
		assertThatNoException().isThrownBy(() -> gateway.findByProductIds(productIds));
		verify(productDataSource).findByProductIds(productIds);
	}

	@Test @DisplayName("Deve mapear corretamente ProductDTO para Product")
	void shouldMapDTOToDomainCorrectly() {
		// Arrange
		var gateway = new ProductGateway(productDataSource);

		var product = CatalogFixture.createValidProduct();
		ProductDTO dto = ProductDTOMapper.toDTO(product);

		when(productDataSource.findByProductIds(anyList())).thenReturn(Optional.of(List.of(dto)));

		// Act
		var result = gateway.findByProductIds(List.of(1L));

		// Assert
		assertThat(result).isPresent();
		var mapped = result.get().getFirst();

		assertThat(mapped.getId()).isEqualTo(dto.id());
		assertThat(mapped.getDetails().name()).isEqualTo(dto.details().name());
		assertThat(mapped.getPrice()).isEqualTo(dto.price());
		assertThat(mapped.getDisplayOrder()).isEqualTo(dto.displayOrder());
		assertThat(mapped.isActive()).isEqualTo(dto.active());

		verify(productDataSource).findByProductIds(List.of(1L));
	}
}
