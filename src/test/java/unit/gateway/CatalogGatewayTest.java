package unit.gateway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.domain.model.Catalog;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.CatalogDTO;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers.CatalogDTOMapper;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.CatalogGateway;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

import unit.fixtures.CatalogFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("CatalogGateway - Testes Unitários")
class CatalogGatewayTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Test @DisplayName("Deve salvar um catálogo e retornar domínio mapeado")
	void shouldSaveCatalogCorrectly() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);
		Catalog catalog = CatalogFixture.createValidCatalog();
		CatalogDTO dto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.save(any(CatalogDTO.class))).thenReturn(dto);

		// Act
		var result = gateway.save(catalog);

		// Assert
		assertThat(result).isInstanceOf(Catalog.class);
		assertThat(result.getName()).isEqualTo(dto.name());

		verify(catalogDataSource).save(any(CatalogDTO.class));
	}

	@Test @DisplayName("Deve retornar catálogo ao buscar por ID")
	void shouldReturnCatalogWhenFound() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);
		var catalog = CatalogFixture.createCatalogWithCategories();
		CatalogDTO dto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findById(1L)).thenReturn(Optional.of(dto));

		// Act
		var result = gateway.findById(1L);

		// Assert
		assertThat(result).isPresent();
		assertThat(result.get()).isInstanceOf(Catalog.class);
		assertThat(result.get().getName()).isEqualTo(dto.name());

		verify(catalogDataSource).findById(1L);
	}

	@Test @DisplayName("Deve retornar Optional.empty quando ID não encontrado")
	void shouldReturnEmptyWhenCatalogNotFound() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		when(catalogDataSource.findById(anyLong())).thenReturn(Optional.empty());

		// Act
		var result = gateway.findById(999L);

		// Assert
		assertThat(result).isEmpty();

		verify(catalogDataSource).findById(999L);
	}

	@Test @DisplayName("Deve retornar lista de catálogos no método findAll")
	void shouldReturnAllCatalogs() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		CatalogDTO dto1 = CatalogDTOMapper.toDTO(CatalogFixture.createValidCatalog());
		CatalogDTO dto2 = CatalogDTOMapper.toDTO(CatalogFixture.createCatalogWithCategories());

		when(catalogDataSource.findAll()).thenReturn(List.of(dto1, dto2));

		// Act
		var result = gateway.findAll();

		// Assert
		assertThat(result).hasSize(2);
		assertThat(result.getFirst()).isInstanceOf(Catalog.class);

		verify(catalogDataSource).findAll();
	}

	@Test @DisplayName("Deve retornar true quando existsById encontrar catálogo")
	void shouldReturnTrueWhenCatalogExistsById() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		when(catalogDataSource.existsById(1L)).thenReturn(true);

		// Act
		var result = gateway.existsById(1L);

		// Assert
		assertThat(result).isTrue();
		verify(catalogDataSource).existsById(1L);
	}

	@Test @DisplayName("Deve retornar false quando existsById não encontrar catálogo")
	void shouldReturnFalseWhenCatalogNotExistsById() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		when(catalogDataSource.existsById(999L)).thenReturn(false);

		// Act
		var result = gateway.existsById(999L);

		// Assert
		assertThat(result).isFalse();
		verify(catalogDataSource).existsById(999L);
	}

	@Test @DisplayName("Deve validar existsByName corretamente")
	void shouldCheckExistsByName() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		when(catalogDataSource.existsByName("Catálogo")).thenReturn(true);

		// Act
		var result = gateway.existsByName("Catálogo");

		// Assert
		assertThat(result).isTrue();
		verify(catalogDataSource).existsByName("Catálogo");
	}

	@Test @DisplayName("Deve validar existsByNameAndIdNot corretamente")
	void shouldCheckExistsByNameAndIdNot() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		when(catalogDataSource.existsByNameAndIdNot("Catálogo", 1L)).thenReturn(true);

		// Act
		var result = gateway.existsByNameAndIdNot("Catálogo", 1L);

		// Assert
		assertThat(result).isTrue();
		verify(catalogDataSource).existsByNameAndIdNot("Catálogo", 1L);
	}

	@Test @DisplayName("Deve validar existsCategoryByCatalogId corretamente")
	void shouldCheckExistsCategoryByCatalogId() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		when(catalogDataSource.existsCategoryByCatalogId(5L)).thenReturn(true);

		// Act
		var result = gateway.existsCategoryByCatalogId(5L);

		// Assert
		assertThat(result).isTrue();
		verify(catalogDataSource).existsCategoryByCatalogId(5L);
	}

	@Test @DisplayName("Deve chamar delete por ID sem lançar exceção")
	void shouldDeleteWithoutException() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		// Act & Assert
		assertThatNoException().isThrownBy(() -> gateway.delete(1L));
		verify(catalogDataSource).delete(1L);
	}

	@Test @DisplayName("Deve retornar catálogo ao buscar por ID do produto")
	void shouldFindByProductId() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		var catalog = CatalogFixture.createCatalogWithProducts();
		CatalogDTO dto = CatalogDTOMapper.toDTO(catalog);

		when(catalogDataSource.findByProductId(10L)).thenReturn(Optional.of(dto));

		// Act
		var result = gateway.findByProductId(10L);

		// Assert
		assertThat(result).isPresent();
		assertThat(result.get()).isInstanceOf(Catalog.class);

		verify(catalogDataSource).findByProductId(10L);
	}

	@Test @DisplayName("Deve retornar Optional.empty quando produto não pertence a nenhum catálogo")
	void shouldReturnEmptyWhenProductIdNotFound() {
		// Arrange
		var gateway = new CatalogGateway(catalogDataSource);

		when(catalogDataSource.findByProductId(anyLong())).thenReturn(Optional.empty());

		// Act
		var result = gateway.findByProductId(999L);

		// Assert
		assertThat(result).isEmpty();
		verify(catalogDataSource).findByProductId(999L);
	}

}
