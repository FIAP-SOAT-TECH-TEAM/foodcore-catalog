package unit.fixtures;

import java.math.BigDecimal;
import java.util.List;

import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitEvent;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitItemEvent;
import com.soat.fiap.food.core.catalog.core.domain.events.StockReversalEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderItemCreatedEventDto;

/**
 * Fixture utilitária para criação de objetos DTO relacionados a eventos de
 * pedido criado para uso em testes unitários.
 */
public class EventFixture {

	/**
	 * Cria um {@link OrderCreatedEventDto} completo com os valores fornecidos.
	 *
	 * @param id
	 *            identificador do pedido
	 * @param orderNumber
	 *            número do pedido
	 * @param status
	 *            descrição do status do pedido
	 * @param userId
	 *            identificador do usuário
	 * @param total
	 *            valor total do pedido
	 * @param items
	 *            lista de itens do pedido
	 * @return instancia preenchida de {@link OrderCreatedEventDto}
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
	 *            identificador do item
	 * @param productId
	 *            identificador do produto
	 * @param quantity
	 *            quantidade solicitada
	 * @param price
	 *            preço unitário do produto
	 * @return instancia preenchida de {@link OrderItemCreatedEventDto}
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
	 * Cria um {@link StockReversalEvent} para o ID de pedido informado.
	 *
	 * <p>
	 * Este método facilita a criação de eventos de reversão de estoque durante a
	 * execução de testes unitários.
	 * </p>
	 *
	 * @param orderId
	 *            identificador do pedido cujo estoque será revertido
	 * @return uma instância de {@link StockReversalEvent} criada com o ID informado
	 */
	public static StockReversalEvent createStockReversalEvent(Long orderId) {
		return StockReversalEvent.of(orderId);
	}

	/**
	 * Cria um {@link StockDebitEvent} totalmente preenchido para uso em testes.
	 *
	 * <p>
	 * Esse evento representa o débito de estoque causado pela criação de um pedido.
	 * </p>
	 *
	 * @param orderId
	 *            identificador único do pedido
	 * @param orderNumber
	 *            número legível/externo do pedido
	 * @param userId
	 *            identificador do usuário que realizou o pedido
	 * @param totalAmount
	 *            valor total do pedido após cálculo dos itens
	 * @param items
	 *            lista de itens debitados do estoque
	 * @return uma instância de {@link StockDebitEvent} contendo todos os dados
	 *         fornecidos
	 */
	public static StockDebitEvent createStockDebitEvent(Long orderId, String orderNumber, String userId,
			BigDecimal totalAmount, List<StockDebitItemEvent> items) {

		return StockDebitEvent.of(orderId, orderNumber, userId, totalAmount, items);
	}

	/**
	 * Cria um {@link StockDebitItemEvent} representando um item individual debitado
	 * do estoque durante a criação de um pedido.
	 *
	 * @param productId
	 *            identificador do produto
	 * @param name
	 *            nome do produto
	 * @param quantity
	 *            quantidade debitada do estoque
	 * @param unitPrice
	 *            preço unitário do produto
	 * @param subtotal
	 *            valor total calculado para este item (unitPrice * quantity)
	 * @param observations
	 *            observações adicionais opcionais relacionadas ao item
	 * @return uma instância de {@link StockDebitItemEvent} contendo todos os dados
	 *         informados
	 */
	public static StockDebitItemEvent createStockDebitItemEvent(Long productId, String name, Integer quantity,
			BigDecimal unitPrice, BigDecimal subtotal, String observations) {

		return StockDebitItemEvent.of(productId, name, quantity, unitPrice, subtotal, observations);
	}
}
