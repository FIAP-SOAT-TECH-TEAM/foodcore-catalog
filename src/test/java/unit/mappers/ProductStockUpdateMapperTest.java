package unit.mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.catalog.core.application.inputs.ProductStockUpdateInput;
import com.soat.fiap.food.core.catalog.core.application.inputs.mappers.ProductStockUpdateMapper;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderItemCreatedEventDto;

import unit.fixtures.EventFixture;

@DisplayName("ProductStockUpdateMapper - Testes Unitários")
class ProductStockUpdateMapperTest {

	@Test @DisplayName("Deve mapear OrderItemCreatedEventDto para ProductStockUpdateInput com sucesso")
	void shouldMapOrderItemCreatedEventToInput() {

		// Arrange
		var events = List.of(EventFixture.createOrderItemCreatedEventDto(1L, 3L, 6, new BigDecimal("13.9")),
				EventFixture.createOrderItemCreatedEventDto(2L, 5L, 3, new BigDecimal("3.1")));

		// Act
		ProductStockUpdateInput result = ProductStockUpdateMapper.toInput(events);

		// Assert
		assertNotNull(result);
		assertThat(result.items()).hasSize(2);

		assertThat(result.items().get(0).productId()).isEqualTo(3L);
		assertThat(result.items().get(0).quantity()).isEqualTo(6);

		assertThat(result.items().get(1).productId()).isEqualTo(5L);
		assertThat(result.items().get(1).quantity()).isEqualTo(3);

		assertDoesNotThrow(() -> ProductStockUpdateMapper.toInput(events));
	}

	@Test @DisplayName("Deve mapear OrderItemCanceledEventDto para ProductStockUpdateInput com sucesso")
	void shouldMapOrderItemCanceledEventToInput() {

		// Arrange
		var events = List.of(EventFixture.createOrderItemCanceledEventDto(10L, 1L, 5, new BigDecimal("7.5")),
				EventFixture.createOrderItemCanceledEventDto(20L, 4L, 8, new BigDecimal("10.2")));

		// Act
		ProductStockUpdateInput result = ProductStockUpdateMapper.toInputFromCanceled(events);

		// Assert
		assertNotNull(result);
		assertThat(result.items()).hasSize(2);

		assertThat(result.items().get(0).productId()).isEqualTo(1L);
		assertThat(result.items().get(0).quantity()).isEqualTo(5);

		assertThat(result.items().get(1).productId()).isEqualTo(4L);
		assertThat(result.items().get(1).quantity()).isEqualTo(8);

		assertDoesNotThrow(() -> ProductStockUpdateMapper.toInputFromCanceled(events));
	}

	@Test @DisplayName("Deve mapear StockDebitItemEventDto para ProductStockUpdateInput com sucesso")
	void shouldMapStockDebitEventToInput() {

		// Arrange
		var events = List.of(EventFixture.createStockDebitItemEventDto(7L, "X-Burguer", 3, new BigDecimal("2.1")),
				EventFixture.createStockDebitItemEventDto(9L, "X-Frango", 8, new BigDecimal("8.4")));

		// Act
		ProductStockUpdateInput result = ProductStockUpdateMapper.toInputFromStockDebit(events);

		// Assert
		assertNotNull(result);
		assertThat(result.items()).hasSize(2);

		assertThat(result.items().get(0).productId()).isEqualTo(7L);
		assertThat(result.items().get(0).quantity()).isEqualTo(3);

		assertThat(result.items().get(1).productId()).isEqualTo(9L);
		assertThat(result.items().get(1).quantity()).isEqualTo(8);

		assertDoesNotThrow(() -> ProductStockUpdateMapper.toInputFromStockDebit(events));
	}

	@Test @DisplayName("Deve mapear lista vazia sem lançar exceção")
	void shouldMapEmptyListWithoutException() {

		// Arrange
		var events = List.<OrderItemCreatedEventDto>of();

		// Act
		var result = assertDoesNotThrow(() -> ProductStockUpdateMapper.toInput(events));

		// Assert
		assertNotNull(result);
		assertThat(result.items()).isEmpty();

		assertDoesNotThrow(() -> ProductStockUpdateMapper.toInput(events));
	}
}
