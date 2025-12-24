package com.soat.fiap.food.core.catalog.core.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Evento de domínio (DDD) emitido quando um estoque é estornado
 */
@Getter @AllArgsConstructor
public class StockReversalEvent {
	private final Long orderId;

	/**
	 * Cria um evento de produto criado
	 *
	 * @param orderId
	 *            Id do pedido que originou o estorno de estoque
	 * @return Nova instância do evento
	 */
	public static StockReversalEvent of(Long orderId) {
		return new StockReversalEvent(orderId);
	}
}
