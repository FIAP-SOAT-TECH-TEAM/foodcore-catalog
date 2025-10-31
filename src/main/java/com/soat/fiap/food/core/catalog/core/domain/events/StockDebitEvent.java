package com.soat.fiap.food.core.catalog.core.domain.events;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Evento de domínio (DDD) emitido quando um estoque é debitado
 */
@Getter @AllArgsConstructor
public class StockDebitEvent {
	public Long orderId;
	public String orderNumber;
	public String userId;
	public BigDecimal totalAmount;
	public List<StockDebitItemEvent> items;

	/**
	 * Cria um evento de produto criado
	 *
	 * @param orderId
	 *            Id do pedido que originou o débito de estoque
	 * @param orderNumber
	 *            Número do pedido que originou o débito de estoque
	 * @param userId
	 *            Id do usuário que originou o débito de estoque
	 * @param totalAmount
	 *            Total do pedido que originou o débito de estoque
	 * @param items
	 *            Lista de itens do pedido que originou o débito de estoque
	 * @return Nova instância do evento
	 */
	public static StockDebitEvent of(Long orderId, String orderNumber, String userId, BigDecimal totalAmount,
			List<StockDebitItemEvent> items) {
		return new StockDebitEvent(orderId, orderNumber, userId, totalAmount, items);
	}
}
