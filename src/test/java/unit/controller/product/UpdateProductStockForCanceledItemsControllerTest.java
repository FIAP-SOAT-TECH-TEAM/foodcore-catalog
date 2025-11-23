package unit.controller.product;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.application.usecases.product.CreditProductStockUseCase;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForCanceledItemsController;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

import unit.fixtures.CatalogFixture;
import unit.fixtures.EventFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("UpdateProductStockForCanceledItemsController - Testes Unitários")
class UpdateProductStockForCanceledItemsControllerTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Test @DisplayName("Deve atualizar estoque dos produtos cancelados com sucesso")
	void shouldUpdateStockSuccessfully() {

		// Arrange
		var item1 = EventFixture.createOrderItemCanceledEventDto(1L, 2L, 3, BigDecimal.valueOf(2L));
		var item2 = EventFixture.createOrderItemCanceledEventDto(2L, 3L, 4, BigDecimal.valueOf(10L));
		var event = EventFixture.createOrderCanceledEventDto(3L, List.of(item1, item2));

		var updatedCatalog = CatalogFixture.createCatalogWithProducts();

		try (MockedStatic<CreditProductStockUseCase> creditMock = mockStatic(CreditProductStockUseCase.class)) {

			creditMock.when(() -> CreditProductStockUseCase.creditProductStock(any(), any()))
					.thenReturn(updatedCatalog);
			when(catalogDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

			// Act & Assert
			assertDoesNotThrow(() -> UpdateProductStockForCanceledItemsController
					.updateProductStockForCanceledItems(event, catalogDataSource));

			verify(catalogDataSource, times(2)).save(any());
		}
	}

	@Test @DisplayName("Deve executar atualização sem lançar exceção quando lista está vazia")
	void shouldRunWithoutExceptionWhenListIsEmpty() {

		// Arrange
		var event = EventFixture.createOrderCanceledEventDto(4L, List.of());

		// Act & Assert
		assertDoesNotThrow(() -> UpdateProductStockForCanceledItemsController.updateProductStockForCanceledItems(event,
				catalogDataSource));

		verify(catalogDataSource, times(0)).save(any());
	}

	@Test @DisplayName("Deve salvar catálogo uma vez por item processado")
	void shouldSaveCatalogOncePerItemProcessed() {

		// Arrange
		var item1 = EventFixture.createOrderItemCanceledEventDto(10L, 5L, 3, BigDecimal.valueOf(15L));
		var event = EventFixture.createOrderCanceledEventDto(5L, List.of(item1));

		var catalog = CatalogFixture.createValidCatalog();

		try (MockedStatic<CreditProductStockUseCase> creditMock = mockStatic(CreditProductStockUseCase.class)) {

			creditMock.when(() -> CreditProductStockUseCase.creditProductStock(any(), any())).thenReturn(catalog);
			when(catalogDataSource.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

			// Act
			UpdateProductStockForCanceledItemsController.updateProductStockForCanceledItems(event, catalogDataSource);

			// Assert
			verify(catalogDataSource, times(1)).save(any());
		}
	}
}
