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
import com.soat.fiap.food.core.catalog.core.application.usecases.product.PublishStockReversalEventUseCase;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForReversalItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockDebitEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;

import unit.fixtures.CatalogFixture;
import unit.fixtures.EventFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("UpdateProductStockForReversalItemsController - Testes Unitários")
class UpdateProductStockForReversalItemsControllerTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Mock
	private EventPublisherSource eventPublisherSource;

	@Test @DisplayName("Deve atualizar estoque e publicar evento de reversão com sucesso")
	void shouldUpdateStockAndPublishReversalEventSuccessfully() {

		// Arrange
		var item1 = EventFixture.createStockDebitItemEventDto(1L, "X-Salada", 2, BigDecimal.valueOf(7.5));
		var item2 = EventFixture.createStockDebitItemEventDto(2L, "X-Bacon", 3, BigDecimal.valueOf(10));

		var event = new StockDebitEventDto();
		event.orderId = 10L;
		event.items = List.of(item1, item2);

		var updatedCatalog = CatalogFixture.createCatalogWithProducts();

		try (MockedStatic<CreditProductStockUseCase> creditMock = mockStatic(CreditProductStockUseCase.class);
				MockedStatic<PublishStockReversalEventUseCase> reversalMock = mockStatic(
						PublishStockReversalEventUseCase.class)) {

			creditMock.when(() -> CreditProductStockUseCase.creditProductStock(any(), any()))
					.thenReturn(updatedCatalog);

			when(catalogDataSource.save(any())).thenAnswer(inv -> inv.getArgument(0));

			// Act & Assert
			assertDoesNotThrow(() -> UpdateProductStockForReversalItemsController
					.updateProductStockForReversalItems(event, catalogDataSource, eventPublisherSource));

			verify(catalogDataSource, times(2)).save(any());
			reversalMock.verify(() -> PublishStockReversalEventUseCase.publishStockReversalEvent(eq(10L), any()),
					times(1));
		}
	}

	@Test @DisplayName("Deve executar sem exceção quando lista de itens está vazia e publicar evento")
	void shouldRunWithoutExceptionWhenListIsEmpty() {

		// Arrange
		var event = new StockDebitEventDto();
		event.orderId = 200L;
		event.items = List.of();

		try (MockedStatic<PublishStockReversalEventUseCase> reversalMock = mockStatic(
				PublishStockReversalEventUseCase.class)) {

			// Act & Assert
			assertDoesNotThrow(() -> UpdateProductStockForReversalItemsController
					.updateProductStockForReversalItems(event, catalogDataSource, eventPublisherSource));

			verify(catalogDataSource, times(0)).save(any());
			reversalMock.verify(() -> PublishStockReversalEventUseCase.publishStockReversalEvent(eq(200L), any()),
					times(1));
		}
	}

	@Test @DisplayName("Deve salvar catálogo uma vez por item processado")
	void shouldSaveCatalogOncePerItemProcessed() {

		// Arrange
		var item = EventFixture.createStockDebitItemEventDto(5L, "Produto-5", 4, BigDecimal.valueOf(20));
		var event = new StockDebitEventDto();
		event.orderId = 300L;
		event.items = List.of(item);

		var catalog = CatalogFixture.createValidCatalog();

		try (MockedStatic<CreditProductStockUseCase> creditMock = mockStatic(CreditProductStockUseCase.class);
				MockedStatic<PublishStockReversalEventUseCase> reversalMock = mockStatic(
						PublishStockReversalEventUseCase.class)) {

			creditMock.when(() -> CreditProductStockUseCase.creditProductStock(any(), any())).thenReturn(catalog);

			when(catalogDataSource.save(any())).thenAnswer(inv -> inv.getArgument(0));

			// Act
			UpdateProductStockForReversalItemsController.updateProductStockForReversalItems(event, catalogDataSource,
					eventPublisherSource);

			// Assert
			verify(catalogDataSource, times(1)).save(any());
			reversalMock.verify(() -> PublishStockReversalEventUseCase.publishStockReversalEvent(eq(300L), any()),
					times(1));
		}
	}
}
