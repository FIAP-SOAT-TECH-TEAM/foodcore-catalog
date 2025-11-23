package unit.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.catalog.core.application.inputs.StockDebitItemEventInput;
import com.soat.fiap.food.core.catalog.core.application.inputs.mappers.StockDebitItemEventMapper;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitItemEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderItemCreatedEventDto;

import unit.fixtures.EventFixture;

@DisplayName("StockDebitItemEventMapper - Testes Unitários")
class StockDebitItemEventMapperTest {

	@Test @DisplayName("Deve mapear OrderItemCreatedEventDto para StockDebitItemEventInput com sucesso")
	void shouldMapOrderItemCreatedEventToInput() {

		// Arrange
		OrderItemCreatedEventDto dto = EventFixture.createOrderItemCreatedEventDto(1L, 10L, 3, new BigDecimal("5.50"));

		// Act
		StockDebitItemEventInput result = StockDebitItemEventMapper.toInput(dto);

		// Assert
		assertNotNull(result);
		assertThat(result.productId()).isEqualTo(10L);
		assertThat(result.name()).isEqualTo("Product-10");
		assertThat(result.quantity()).isEqualTo(3);
		assertThat(result.unitPrice()).isEqualTo("5.50");
		assertThat(result.subtotal()).isEqualTo("16.50");

		assertDoesNotThrow(() -> StockDebitItemEventMapper.toInput(dto));
	}

	@Test
	@DisplayName("Deve mapear lista de OrderItemCreatedEventDto para lista de StockDebitItemEventInput com sucesso")
	void shouldMapListOfEventsToInputList() {

		// Arrange
		var events = List.of(EventFixture.createOrderItemCreatedEventDto(1L, 3L, 2, new BigDecimal("4.0")),
				EventFixture.createOrderItemCreatedEventDto(2L, 7L, 5, new BigDecimal("2.2")));

		// Act
		List<StockDebitItemEventInput> result = StockDebitItemEventMapper.toInputList(events);

		// Assert
		assertNotNull(result);
		assertThat(result).hasSize(2);

		assertThat(result.get(0).productId()).isEqualTo(3L);
		assertThat(result.get(0).quantity()).isEqualTo(2);

		assertThat(result.get(1).productId()).isEqualTo(7L);
		assertThat(result.get(1).quantity()).isEqualTo(5);

		assertDoesNotThrow(() -> StockDebitItemEventMapper.toInputList(events));
	}

	@Test @DisplayName("Deve retornar lista vazia ao mapear lista vazia")
	void shouldReturnEmptyListWhenEventsEmpty() {

		// Arrange
		var emptyList = List.<OrderItemCreatedEventDto>of();

		// Act
		List<StockDebitItemEventInput> result = assertDoesNotThrow(
				() -> StockDebitItemEventMapper.toInputList(emptyList));

		// Assert
		assertNotNull(result);
		assertThat(result).isEmpty();
	}

	@Test @DisplayName("Deve retornar lista vazia ao mapear lista nula")
	void shouldReturnEmptyListWhenNullProvided() {

		// Act
		List<StockDebitItemEventInput> result = assertDoesNotThrow(() -> StockDebitItemEventMapper.toInputList(null));

		// Assert
		assertNotNull(result);
		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("Deve converter StockDebitItemEventInput para StockDebitItemEvent (via reflection por ser protected)")
	void shouldMapToItemEventUsingReflection() throws Exception {

		// Arrange
		var input = EventFixture.createStockDebitItemEventInput(5L, "X-Salada", 2, new BigDecimal("7.5"),
				new BigDecimal("15.0"), null);

		// Act
		StockDebitItemEvent event = StockDebitItemEventMapper.toItemEvent(input);

		// Assert
		assertNotNull(event);
		assertThat(event.getProductId()).isEqualTo(5L);
		assertThat(event.getName()).isEqualTo("X-Salada");
		assertThat(event.getQuantity()).isEqualTo(2);
		assertThat(event.getUnitPrice()).isEqualTo("7.5");
		assertThat(event.getSubtotal()).isEqualTo("15.0");
	}
}
