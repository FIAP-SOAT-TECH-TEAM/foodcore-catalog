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

import com.soat.fiap.food.core.catalog.core.application.inputs.StockDebitEventInput;
import com.soat.fiap.food.core.catalog.core.application.usecases.product.DebitProductStockUseCase;
import com.soat.fiap.food.core.catalog.core.application.usecases.product.PublishStockDebitEventUseCase;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.controller.product.UpdateProductStockForCreatedItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;

import unit.fixtures.CatalogFixture;
import unit.fixtures.EventFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("UpdateProductStockForCreatedItemsController - Testes Unitários")
class UpdateProductStockForCreatedItemsControllerTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Mock
	private EventPublisherSource eventPublisherSource;

	@Test @DisplayName("Deve debitar estoque e publicar evento com sucesso")
	void shouldDebitStockAndPublishEventSuccessfully() {

		// Arrange
		var item1 = EventFixture.createOrderCreatedItemDto(1L, "X-Salada", 2, BigDecimal.valueOf(7.5));
		var item2 = EventFixture.createOrderCreatedItemDto(2L, "X-Bacon", 3, BigDecimal.valueOf(10));

		var event = new OrderCreatedEventDto();
		event.setItems(List.of(item1, item2));

		var updatedCatalog = CatalogFixture.createCatalogWithProducts();

		try (MockedStatic<DebitProductStockUseCase> debitMock = mockStatic(DebitProductStockUseCase.class);
				MockedStatic<PublishStockDebitEventUseCase> publishMock = mockStatic(
						PublishStockDebitEventUseCase.class)) {

			debitMock.when(() -> DebitProductStockUseCase.debitProductStock(any(), any())).thenReturn(updatedCatalog);

			when(catalogDataSource.save(any())).thenAnswer(inv -> inv.getArgument(0));

			// Act & Assert
			assertDoesNotThrow(() -> UpdateProductStockForCreatedItemsController
					.updateProductStockForCreatedItems(event, catalogDataSource, eventPublisherSource));

			verify(catalogDataSource, times(2)).save(any());
			publishMock.verify(
					() -> PublishStockDebitEventUseCase.publishStockDebitEvent(any(StockDebitEventInput.class), any()),
					times(1));
		}
	}

	@Test @DisplayName("Deve executar sem exceção quando lista de itens está vazia e publicar evento")
	void shouldRunWithoutExceptionWhenListIsEmpty() {

		// Arrange
		var event = new OrderCreatedEventDto();
		event.setItems(List.of());

		try (MockedStatic<PublishStockDebitEventUseCase> publishMock = mockStatic(
				PublishStockDebitEventUseCase.class)) {

			// Act & Assert
			assertDoesNotThrow(() -> UpdateProductStockForCreatedItemsController
					.updateProductStockForCreatedItems(event, catalogDataSource, eventPublisherSource));

			verify(catalogDataSource, times(0)).save(any());
			publishMock.verify(
					() -> PublishStockDebitEventUseCase.publishStockDebitEvent(any(StockDebitEventInput.class), any()),
					times(1));
		}
	}

	@Test @DisplayName("Deve salvar catálogo uma vez por item processado")
	void shouldSaveCatalogOncePerItemProcessed() {

		// Arrange
		var item = EventFixture.createOrderCreatedItemDto(9L, "Produto-9", 4, BigDecimal.valueOf(20));

		var event = new OrderCreatedEventDto();
		event.setItems(List.of(item));

		var catalog = CatalogFixture.createValidCatalog();

		try (MockedStatic<DebitProductStockUseCase> debitMock = mockStatic(DebitProductStockUseCase.class);
				MockedStatic<PublishStockDebitEventUseCase> publishMock = mockStatic(
						PublishStockDebitEventUseCase.class)) {

			debitMock.when(() -> DebitProductStockUseCase.debitProductStock(any(), any())).thenReturn(catalog);

			when(catalogDataSource.save(any())).thenAnswer(inv -> inv.getArgument(0));

			// Act
			UpdateProductStockForCreatedItemsController.updateProductStockForCreatedItems(event, catalogDataSource,
					eventPublisherSource);

			// Assert
			verify(catalogDataSource, times(1)).save(any());
			publishMock.verify(
					() -> PublishStockDebitEventUseCase.publishStockDebitEvent(any(StockDebitEventInput.class), any()),
					times(1));
		}
	}
}
