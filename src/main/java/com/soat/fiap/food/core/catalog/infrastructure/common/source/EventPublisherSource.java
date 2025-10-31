package com.soat.fiap.food.core.catalog.infrastructure.common.source;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockReversalEventDto;

/**
 * Interface para publicação de eventos no sistema.
 * <p>
 * Fornece métodos para publicar diferentes tipos de eventos. Implementações
 * dessa interface podem enviar os eventos para mecanismos de mensageria (como
 * RabbitMQ) ou outros sistemas de eventos.
 * </p>
 */
public interface EventPublisherSource {

	/**
	 * Publica um evento de estoque estornado.
	 *
	 * @param stockReversalEventDto
	 *            evento contendo informações do estoque estornado.
	 */
	void publishStockReversalEvent(StockReversalEventDto stockReversalEventDto);
}
