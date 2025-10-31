package com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways;

import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitEvent;
import com.soat.fiap.food.core.catalog.core.domain.events.StockReversalEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers.StockDebitEventMapper;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers.StockReversalEventMapper;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;

/**
 * Gateway para publicação de eventos de domínio.
 * <p>
 * Este gateway delega a publicação de eventos ao {@link EventPublisherSource},
 * fornecendo métodos específicos para cada tipo de evento de domínio.
 * </p>
 */
public class EventPublisherGateway {

	private final EventPublisherSource eventPublisherSource;

	public EventPublisherGateway(EventPublisherSource eventPublisherSource) {
		this.eventPublisherSource = eventPublisherSource;
	}

	/**
	 * Publica um evento de estorno de estoque.
	 *
	 * @param event
	 *            Evento contendo informações do estoque estornado.
	 */
	public void publishStockReversalEvent(StockReversalEvent event) {
		var eventDto = StockReversalEventMapper.toDto(event);

		eventPublisherSource.publishStockReversalEvent(eventDto);
	}

	/**
	 * Publica um evento de débito de estoque.
	 *
	 * @param event
	 *            Evento contendo informações do estoque debitado.
	 */
	public void publishStockDebitEvent(StockDebitEvent event) {
		var eventDto = StockDebitEventMapper.toDto(event);

		eventPublisherSource.publishStockDebitEvent(eventDto);
	}
}
