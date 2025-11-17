package unit.gateway;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockDebitEventDto;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockReversalEventDto;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.EventPublisherGateway;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;

import unit.fixtures.EventFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("EventPublisherGateway (Catalog) - Testes Unitários")
class EventPublisherGatewayTest {

	@Mock
	private EventPublisherSource eventPublisherSource;

	@Test @DisplayName("Deve publicar StockReversalEvent com sucesso")
	void shouldPublishStockReversalEventSuccessfully() {
		// Arrange
		var gateway = new EventPublisherGateway(eventPublisherSource);
		var event = EventFixture.createStockReversalEvent(10L);

		// Act & Assert
		assertThatNoException().isThrownBy(() -> gateway.publishStockReversalEvent(event));

		verify(eventPublisherSource).publishStockReversalEvent(any(StockReversalEventDto.class));
	}

	@Test @DisplayName("Deve publicar StockDebitEvent com sucesso")
	void shouldPublishStockDebitEventSuccessfully() {
		// Arrange
		var gateway = new EventPublisherGateway(eventPublisherSource);
		var item = EventFixture.createStockDebitItemEvent(1L, "Produto A", 2, BigDecimal.valueOf(10.00),
				BigDecimal.valueOf(20.00), "obs");
		var event = EventFixture.createStockDebitEvent(55L, "ORD-55", "user55", BigDecimal.valueOf(250.00),
				List.of(item));

		// Act & Assert
		assertThatNoException().isThrownBy(() -> gateway.publishStockDebitEvent(event));

		verify(eventPublisherSource).publishStockDebitEvent(any(StockDebitEventDto.class));
	}

	@Test @DisplayName("Deve delegar corretamente múltiplos eventos para EventPublisherSource")
	void shouldDelegateCorrectlyForMultipleEvents() {
		// Arrange
		var gateway = new EventPublisherGateway(eventPublisherSource);

		var reversalEvent = EventFixture.createStockReversalEvent(500L);

		var item1 = EventFixture.createStockDebitItemEvent(1L, "Item 1", 1, BigDecimal.valueOf(30),
				BigDecimal.valueOf(30), null);
		var item2 = EventFixture.createStockDebitItemEvent(2L, "Item 2", 3, BigDecimal.valueOf(5),
				BigDecimal.valueOf(15), "extra");

		var debitEvent = EventFixture.createStockDebitEvent(900L, "ORD-900", "user900", BigDecimal.valueOf(45.00),
				List.of(item1, item2));

		// Act
		gateway.publishStockReversalEvent(reversalEvent);
		gateway.publishStockDebitEvent(debitEvent);

		// Assert
		verify(eventPublisherSource).publishStockReversalEvent(any(StockReversalEventDto.class));
		verify(eventPublisherSource).publishStockDebitEvent(any(StockDebitEventDto.class));
	}
}
