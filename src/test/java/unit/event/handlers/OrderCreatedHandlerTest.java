package unit.event.handlers;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.controller.product.UpdateProductStockForCreatedItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order.handlers.OrderCreatedHandler;

import unit.fixtures.EventFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("OrderCreatedHandler - Testes Unitários")
class OrderCreatedHandlerTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Mock
	private EventPublisherSource eventPublisherSource;

	private OrderCreatedHandler handler;

	@BeforeEach
	void setUp() {
		handler = new OrderCreatedHandler(catalogDataSource, eventPublisherSource);
	}

	@Test @DisplayName("Deve processar evento OrderCreated com sucesso")
	void shouldHandleOrderCreatedEventSuccessfully() {
		// Arrange
		OrderCreatedEventDto event = EventFixture.createOrderCreatedEventDto(1L, "ORDER-1", "CREATED", "USER-1",
				BigDecimal.valueOf(150.00),
				List.of(EventFixture.createOrderItemCreatedEventDto(1L, 10L, 2, BigDecimal.valueOf(50.00))));

		try (MockedStatic<UpdateProductStockForCreatedItemsController> mockedStatic = mockStatic(
				UpdateProductStockForCreatedItemsController.class)) {

			// Act
			assertThatNoException().isThrownBy(() -> handler.handle(event));

			// Assert
			mockedStatic.verify(() -> UpdateProductStockForCreatedItemsController
					.updateProductStockForCreatedItems(eq(event), eq(catalogDataSource), eq(eventPublisherSource)),
					times(1));
		}
	}

	@Test @DisplayName("Deve processar evento com valores customizados")
	void shouldHandleOrderCreatedEventWithCustomValues() {
		// Arrange
		OrderCreatedEventDto event = EventFixture.createOrderCreatedEventDto(2L, "ORDER-XYZ", "CREATED", "USER-ABC",
				BigDecimal.valueOf(999.99),
				List.of(EventFixture.createOrderItemCreatedEventDto(2L, 50L, 5, BigDecimal.valueOf(199.99))));

		try (MockedStatic<UpdateProductStockForCreatedItemsController> mockedStatic = mockStatic(
				UpdateProductStockForCreatedItemsController.class)) {

			// Act
			assertThatNoException().isThrownBy(() -> handler.handle(event));

			// Assert
			mockedStatic.verify(() -> UpdateProductStockForCreatedItemsController
					.updateProductStockForCreatedItems(eq(event), eq(catalogDataSource), eq(eventPublisherSource)),
					times(1));
		}
	}

	@Test @DisplayName("Deve processar múltiplos eventos OrderCreated")
	void shouldHandleMultipleOrderCreatedEvents() {
		// Arrange
		OrderCreatedEventDto event1 = EventFixture.createOrderCreatedEventDto(3L, "ORDER-3", "CREATED", "USER-3",
				BigDecimal.valueOf(200.00),
				List.of(EventFixture.createOrderItemCreatedEventDto(3L, 77L, 3, BigDecimal.valueOf(70.00))));

		OrderCreatedEventDto event2 = EventFixture.createOrderCreatedEventDto(4L, "ORDER-4", "CREATED", "USER-4",
				BigDecimal.valueOf(300.00),
				List.of(EventFixture.createOrderItemCreatedEventDto(4L, 88L, 6, BigDecimal.valueOf(50.00))));

		try (MockedStatic<UpdateProductStockForCreatedItemsController> mockedStatic = mockStatic(
				UpdateProductStockForCreatedItemsController.class)) {

			// Act
			handler.handle(event1);
			handler.handle(event2);

			// Assert
			mockedStatic.verify(() -> UpdateProductStockForCreatedItemsController
					.updateProductStockForCreatedItems(eq(event1), eq(catalogDataSource), eq(eventPublisherSource)),
					times(1));

			mockedStatic.verify(() -> UpdateProductStockForCreatedItemsController
					.updateProductStockForCreatedItems(eq(event2), eq(catalogDataSource), eq(eventPublisherSource)),
					times(1));
		}
	}
}
