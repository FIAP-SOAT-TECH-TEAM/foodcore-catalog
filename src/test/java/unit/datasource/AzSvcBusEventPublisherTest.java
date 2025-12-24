package unit.datasource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockDebitEventDto;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockReversalEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.out.event.publisher.azsvcbus.AzSvcBusEventPublisher;

/**
 * Testes unitários para {@link AzSvcBusEventPublisher}.
 */
@ExtendWith(MockitoExtension.class) @DisplayName("AzSvcBusEventPublisher (Stock) - Testes Unitários")
class AzSvcBusEventPublisherTest {

	@Mock
	private ServiceBusSenderClient stockReversalSender;

	@Mock
	private ServiceBusSenderClient stockDebitSender;

	@Mock
	private Gson gson;

	private AzSvcBusEventPublisher eventPublisher;

	@BeforeEach
	void setUp() {
		eventPublisher = new AzSvcBusEventPublisher(stockReversalSender, stockDebitSender, gson);
	}

	@Test @DisplayName("Deve publicar evento de estorno de estoque com sucesso")
	void shouldPublishStockReversalEventSuccessfully() {
		// Arrange
		var event = new StockReversalEventDto();
		event.setOrderId(10L);

		when(gson.toJson(event)).thenReturn("{\"orderId\":10}");

		// Act & Assert
		assertThatNoException().isThrownBy(() -> eventPublisher.publishStockReversalEvent(event));

		// Assert
		verify(stockReversalSender).sendMessage(any(ServiceBusMessage.class));
	}

	@Test @DisplayName("Deve publicar evento de débito de estoque com sucesso")
	void shouldPublishStockDebitEventSuccessfully() {
		// Arrange
		var event = new StockDebitEventDto();
		event.orderId = 20L;
		event.orderNumber = "ORD-123";
		event.userId = "USR-999";

		when(gson.toJson(event)).thenReturn("{\"orderId\":20,\"orderNumber\":\"ORD-123\",\"userId\":\"USR-999\"}");

		// Act & Assert
		assertThatNoException().isThrownBy(() -> eventPublisher.publishStockDebitEvent(event));

		// Assert
		verify(stockDebitSender).sendMessage(any(ServiceBusMessage.class));
	}

	@Test @DisplayName("Deve publicar múltiplos eventos com sucesso")
	void shouldPublishMultipleEventsSuccessfully() {
		// Arrange
		var reversal = new StockReversalEventDto();
		reversal.setOrderId(30L);

		var debit = new StockDebitEventDto();
		debit.orderId = 40L;

		when(gson.toJson(reversal)).thenReturn("{\"orderId\":30}");
		when(gson.toJson(debit)).thenReturn("{\"orderId\":40}");

		// Act & Assert
		assertThatNoException().isThrownBy(() -> {
			eventPublisher.publishStockReversalEvent(reversal);
			eventPublisher.publishStockDebitEvent(debit);
		});

		// Assert
		verify(stockReversalSender).sendMessage(any(ServiceBusMessage.class));
		verify(stockDebitSender).sendMessage(any(ServiceBusMessage.class));
	}
}
