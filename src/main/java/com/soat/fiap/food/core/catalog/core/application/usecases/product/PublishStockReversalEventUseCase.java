package com.soat.fiap.food.core.catalog.core.application.usecases.product;

import com.soat.fiap.food.core.catalog.core.domain.events.StockReversalEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.EventPublisherGateway;
import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: Publicar evento de estorno de estoque.
 */
@Slf4j
public class PublishStockReversalEventUseCase {

	/**
	 * Publica o evento {@link StockReversalEvent} para o estoque fornecido.
	 *
	 * @param orderId
	 *            ID do pedido que originou o estorno
	 * @param gateway
	 *            Gateway responsável por publicar o evento
	 */
	public static void publishStockReversalEvent(Long orderId, EventPublisherGateway gateway) {
		var reversalEvent = StockReversalEvent.of(orderId);
		gateway.publishOrderCreatedEvent(reversalEvent);
		log.info("Evento de estorno de estoque publicado: Pedido {}", orderId);
	}
}
