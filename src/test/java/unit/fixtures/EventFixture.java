package unit.fixtures;

import java.math.BigDecimal;
import java.util.List;

import com.soat.fiap.food.core.catalog.core.application.inputs.StockDebitEventInput;
import com.soat.fiap.food.core.catalog.core.application.inputs.StockDebitItemEventInput;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitEvent;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitItemEvent;
import com.soat.fiap.food.core.catalog.core.domain.events.StockReversalEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.*;

/**
 * Fixture utilitária responsável por criar objetos de eventos, DTOs e inputs
 * relacionados ao fluxo de pedidos e operações de estoque para uso em testes
 * unitários.
 * <p>
 * Fornece métodos estáticos que geram instâncias completas, consistentes e
 * prontas para uso em cenários de teste.
 */
public class EventFixture {

	/**
	 * Cria um {@link OrderCreatedEventDto} com os dados fornecidos.
	 *
	 * @param id
	 *            ID do pedido
	 * @param orderNumber
	 *            número externo do pedido
	 * @param status
	 *            descrição do status
	 * @param userId
	 *            ID do usuário
	 * @param total
	 *            valor total
	 * @param items
	 *            itens do pedido
	 * @return DTO preenchido
	 */
	public static OrderCreatedEventDto createOrderCreatedEventDto(Long id, String orderNumber, String status,
			String userId, BigDecimal total, List<OrderItemCreatedEventDto> items) {
		OrderCreatedEventDto dto = new OrderCreatedEventDto();
		dto.id = id;
		dto.orderNumber = orderNumber;
		dto.statusDescription = status;
		dto.userId = userId;
		dto.totalAmount = total;
		dto.items = items;
		return dto;
	}

	/**
	 * Cria um {@link OrderItemCreatedEventDto} representando um item do pedido.
	 *
	 * @param id
	 *            ID do item
	 * @param productId
	 *            ID do produto
	 * @param quantity
	 *            quantidade solicitada
	 * @param price
	 *            preço unitário
	 * @return DTO preenchido
	 */
	public static OrderItemCreatedEventDto createOrderItemCreatedEventDto(Long id, Long productId, Integer quantity,
			BigDecimal price) {
		OrderItemCreatedEventDto dto = new OrderItemCreatedEventDto();
		dto.id = id;
		dto.productId = productId;
		dto.quantity = quantity;
		dto.unitPrice = price;
		dto.subtotal = price.multiply(BigDecimal.valueOf(quantity));
		dto.name = "Product-" + productId;
		dto.observations = null;
		return dto;
	}

	/**
	 * Cria um {@link StockDebitItemEventDto} para representar um débito de item.
	 *
	 * @param productId
	 *            ID do produto
	 * @param name
	 *            nome do produto
	 * @param quantity
	 *            quantidade debitada
	 * @param unitPrice
	 *            valor unitário
	 * @return DTO preenchido
	 */
	public static StockDebitItemEventDto createStockDebitItemEventDto(Long productId, String name, Integer quantity,
			BigDecimal unitPrice) {
		StockDebitItemEventDto dto = new StockDebitItemEventDto();
		dto.productId = productId;
		dto.name = name != null ? name : "Product-" + productId;
		dto.quantity = quantity;
		dto.unitPrice = unitPrice;
		dto.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
		dto.observations = null;
		return dto;
	}

	/**
	 * Cria um {@link OrderItemCanceledEventDto} representando um item cancelado.
	 *
	 * @param id
	 *            ID do item
	 * @param productId
	 *            ID do produto
	 * @param quantity
	 *            quantidade cancelada
	 * @param unitPrice
	 *            preço unitário
	 * @return DTO preenchido
	 */
	public static OrderItemCanceledEventDto createOrderItemCanceledEventDto(Long id, Long productId, Integer quantity,
			BigDecimal unitPrice) {
		OrderItemCanceledEventDto dto = new OrderItemCanceledEventDto();
		dto.id = id;
		dto.productId = productId;
		dto.name = "Product-" + productId;
		dto.quantity = quantity;
		dto.unitPrice = unitPrice;
		dto.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
		dto.observations = null;
		return dto;
	}

	/**
	 * Cria um {@link OrderCanceledEventDto} com os itens cancelados.
	 *
	 * @param orderId
	 *            ID do pedido
	 * @param items
	 *            itens cancelados
	 * @return DTO preenchido
	 */
	public static OrderCanceledEventDto createOrderCanceledEventDto(Long orderId,
			List<OrderItemCanceledEventDto> items) {
		var event = new OrderCanceledEventDto();
		event.setId(orderId);
		event.setItems(items);
		return event;
	}

	/**
	 * Cria um {@link StockReversalEvent} para o pedido informado.
	 *
	 * @param orderId
	 *            ID do pedido
	 * @return evento criado
	 */
	public static StockReversalEvent createStockReversalEvent(Long orderId) {
		return StockReversalEvent.of(orderId);
	}

	/**
	 * Cria um {@link StockDebitEvent} completo.
	 *
	 * @param orderId
	 *            ID do pedido
	 * @param orderNumber
	 *            número do pedido
	 * @param userId
	 *            usuário criador
	 * @param totalAmount
	 *            valor total
	 * @param items
	 *            itens debitados
	 * @return evento de débito
	 */
	public static StockDebitEvent createStockDebitEvent(Long orderId, String orderNumber, String userId,
			BigDecimal totalAmount, List<StockDebitItemEvent> items) {
		return StockDebitEvent.of(orderId, orderNumber, userId, totalAmount, items);
	}

	/**
	 * Cria um {@link StockDebitItemEvent}.
	 *
	 * @param productId
	 *            ID do produto
	 * @param name
	 *            nome do produto
	 * @param quantity
	 *            quantidade
	 * @param unitPrice
	 *            valor unitário
	 * @param subtotal
	 *            subtotal
	 * @param observations
	 *            observações
	 * @return evento criado
	 */
	public static StockDebitItemEvent createStockDebitItemEvent(Long productId, String name, Integer quantity,
			BigDecimal unitPrice, BigDecimal subtotal, String observations) {
		return StockDebitItemEvent.of(productId, name, quantity, unitPrice, subtotal, observations);
	}

	/**
	 * Cria um {@link StockDebitEventInput} convertendo os itens DTO fornecidos.
	 *
	 * @param orderId
	 *            ID do pedido
	 * @param orderNumber
	 *            número do pedido
	 * @param userId
	 *            ID do usuário
	 * @param totalAmount
	 *            valor total
	 * @param itemsDto
	 *            itens DTO
	 * @return input preenchido
	 */
	public static StockDebitEventInput createStockDebitEventInput(Long orderId, String orderNumber, String userId,
			BigDecimal totalAmount, List<StockDebitItemEventDto> itemsDto) {

		List<StockDebitItemEventInput> itemsInput = itemsDto.stream()
				.map(i -> new StockDebitItemEventInput(i.productId, i.name, i.quantity, i.unitPrice, i.subtotal,
						i.observations))
				.toList();

		return new StockDebitEventInput(orderId, orderNumber, userId, totalAmount, itemsInput);
	}

	/**
	 * Cria um {@link StockDebitItemEventInput}.
	 *
	 * @param productId
	 *            ID do produto
	 * @param name
	 *            nome
	 * @param quantity
	 *            quantidade
	 * @param unitPrice
	 *            preço unitário
	 * @param subtotal
	 *            subtotal
	 * @param observations
	 *            observações
	 * @return input criado
	 */
	public static StockDebitItemEventInput createStockDebitItemEventInput(Long productId, String name, Integer quantity,
			BigDecimal unitPrice, BigDecimal subtotal, String observations) {
		return new StockDebitItemEventInput(productId, name, quantity, unitPrice, subtotal, observations);
	}

	/**
	 * Cria um {@link OrderItemCreatedEventDto} simplificado.
	 *
	 * @param productId
	 *            ID do produto
	 * @param name
	 *            nome
	 * @param quantity
	 *            quantidade
	 * @param unitPrice
	 *            valor unitário
	 * @return DTO criado
	 */
	public static OrderItemCreatedEventDto createOrderCreatedItemDto(Long productId, String name, Integer quantity,
			BigDecimal unitPrice) {
		OrderItemCreatedEventDto dto = new OrderItemCreatedEventDto();
		dto.id = productId;
		dto.productId = productId;
		dto.name = name != null ? name : "Product-" + productId;
		dto.quantity = quantity;
		dto.unitPrice = unitPrice;
		dto.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
		dto.observations = null;
		return dto;
	}

}
