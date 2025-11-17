package unit.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.application.usecases.product.PublishStockReversalEventUseCase;
import com.soat.fiap.food.core.catalog.core.domain.events.StockReversalEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.EventPublisherGateway;

@ExtendWith(MockitoExtension.class) @DisplayName("PublishStockReversalEventUseCase - Testes Unitários")
class PublishStockReversalEventUseCaseTest {

	@Mock
	private EventPublisherGateway eventPublisherGateway;

	@Test @DisplayName("Deve publicar evento de estorno de estoque com sucesso")
	void shouldPublishStockReversalEventSuccessfully() {
		// Arrange
		Long orderId = 123L;

		// Act & Assert
		assertDoesNotThrow(
				() -> PublishStockReversalEventUseCase.publishStockReversalEvent(orderId, eventPublisherGateway));

		verify(eventPublisherGateway).publishStockReversalEvent(any());
	}

	@Test @DisplayName("Deve chamar o gateway uma única vez")
	void shouldCallGatewayOnlyOnce() {
		// Arrange
		Long orderId = 123L;

		// Act
		PublishStockReversalEventUseCase.publishStockReversalEvent(orderId, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway, times(1)).publishStockReversalEvent(any());
	}

	@Test @DisplayName("Deve processar diferentes IDs de pedido")
	void shouldProcessDifferentOrderIds() {
		// Arrange
		Long orderId1 = 100L;
		Long orderId2 = 200L;

		// Act
		PublishStockReversalEventUseCase.publishStockReversalEvent(orderId1, eventPublisherGateway);
		PublishStockReversalEventUseCase.publishStockReversalEvent(orderId2, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway, times(2)).publishStockReversalEvent(any());
	}

	@Test @DisplayName("Deve publicar evento com ID correto do pedido")
	void shouldPublishEventWithCorrectOrderId() {
		// Arrange
		Long orderId = 999L;
		ArgumentCaptor<StockReversalEvent> eventCaptor = ArgumentCaptor.forClass(StockReversalEvent.class);

		// Act
		PublishStockReversalEventUseCase.publishStockReversalEvent(orderId, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishStockReversalEvent(eventCaptor.capture());
		StockReversalEvent capturedEvent = eventCaptor.getValue();

		assertThat(capturedEvent.getOrderId()).isEqualTo(orderId);
	}

	@Test @DisplayName("Deve manter integridade do dado orderId")
	void shouldMaintainOrderIdIntegrity() {
		// Arrange
		Long originalOrderId = 555L;
		ArgumentCaptor<StockReversalEvent> eventCaptor = ArgumentCaptor.forClass(StockReversalEvent.class);

		// Act
		PublishStockReversalEventUseCase.publishStockReversalEvent(originalOrderId, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishStockReversalEvent(eventCaptor.capture());
		StockReversalEvent capturedEvent = eventCaptor.getValue();

		assertThat(capturedEvent.getOrderId()).isEqualTo(originalOrderId);
	}
}
