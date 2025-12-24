package unit.usecases.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.core.application.inputs.StockDebitEventInput;
import com.soat.fiap.food.core.catalog.core.application.usecases.product.PublishStockDebitEventUseCase;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.EventPublisherGateway;

import unit.fixtures.EventFixture;

@ExtendWith(MockitoExtension.class) @DisplayName("PublishStockDebitEventUseCase - Testes Unitários")
class PublishStockDebitEventUseCaseTest {

	@Mock
	private EventPublisherGateway eventPublisherGateway;

	@Test @DisplayName("Deve publicar evento de débito de estoque com sucesso")
	void shouldPublishStockDebitEventSuccessfully() {

		// Arrange
		var item = EventFixture.createStockDebitItemEventDto(10L, "Produto Teste", 2, new BigDecimal("20.0"));

		StockDebitEventInput input = EventFixture.createStockDebitEventInput(99L, "ORDER-001", "user-123",
				new BigDecimal("40.0"), List.of(item));

		// Act & Assert
		assertDoesNotThrow(() -> PublishStockDebitEventUseCase.publishStockDebitEvent(input, eventPublisherGateway));

		verify(eventPublisherGateway).publishStockDebitEvent(any());
	}

	@Test @DisplayName("Deve chamar o gateway uma única vez")
	void shouldCallGatewayOnlyOnce() {

		// Arrange
		StockDebitEventInput input = EventFixture.createStockDebitEventInput(50L, "ORDER-55", "user-test",
				new BigDecimal("99.9"), List.of());

		// Act
		PublishStockDebitEventUseCase.publishStockDebitEvent(input, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway, times(1)).publishStockDebitEvent(any());
	}

	@Test @DisplayName("Deve processar diferentes eventos de débito de estoque")
	void shouldProcessDifferentInputs() {

		// Arrange
		var input1 = EventFixture.createStockDebitEventInput(1L, "A1", "u1", new BigDecimal("10"), List.of());
		var input2 = EventFixture.createStockDebitEventInput(2L, "A2", "u2", new BigDecimal("20"), List.of());

		// Act
		PublishStockDebitEventUseCase.publishStockDebitEvent(input1, eventPublisherGateway);
		PublishStockDebitEventUseCase.publishStockDebitEvent(input2, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway, times(2)).publishStockDebitEvent(any());
	}

	@Test @DisplayName("Deve publicar evento com orderId correto")
	void shouldPublishEventWithCorrectOrderId() {

		// Arrange
		Long orderId = 777L;

		StockDebitEventInput input = EventFixture.createStockDebitEventInput(orderId, "ORD-XYZ", "user-x",
				new BigDecimal("55.0"), List.of());

		ArgumentCaptor<StockDebitEvent> eventCaptor = ArgumentCaptor.forClass(StockDebitEvent.class);

		// Act
		PublishStockDebitEventUseCase.publishStockDebitEvent(input, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishStockDebitEvent(eventCaptor.capture());

		StockDebitEvent capturedEvent = eventCaptor.getValue();
		assertThat(capturedEvent.getOrderId()).isEqualTo(orderId);
	}

	@Test @DisplayName("Deve manter integridade dos dados do evento")
	void shouldMaintainEventDataIntegrity() {

		// Arrange
		var item = EventFixture.createStockDebitItemEventDto(10L, "Teste", 4, new BigDecimal("3.0"));

		StockDebitEventInput input = EventFixture.createStockDebitEventInput(333L, "ORD-333", "user-abc",
				new BigDecimal("12.0"), List.of(item));

		ArgumentCaptor<StockDebitEvent> eventCaptor = ArgumentCaptor.forClass(StockDebitEvent.class);

		// Act
		PublishStockDebitEventUseCase.publishStockDebitEvent(input, eventPublisherGateway);

		// Assert
		verify(eventPublisherGateway).publishStockDebitEvent(eventCaptor.capture());

		StockDebitEvent event = eventCaptor.getValue();

		assertThat(event.getOrderId()).isEqualTo(333L);
		assertThat(event.getOrderNumber()).isEqualTo("ORD-333");
		assertThat(event.getUserId()).isEqualTo("user-abc");
		assertThat(event.getTotalAmount()).isEqualTo(new BigDecimal("12.0"));
		assertThat(event.getItems()).hasSize(1);
	}
}
