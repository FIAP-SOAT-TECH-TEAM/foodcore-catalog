package com.soat.fiap.food.core.catalog.core.application.usecases.product;

import com.soat.fiap.food.core.catalog.core.application.inputs.StockDebitEventInput;
import com.soat.fiap.food.core.catalog.core.application.inputs.mappers.StockDebitEventMapper;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.EventPublisherGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: Publicar evento de débito de estoque.
 */
@Slf4j
public class PublishStockDebitEventUseCase {

	/**
	 * Publica o evento {@link StockDebitEvent} para o estoque fornecido.
	 *
	 * @param stockDebitEventInput
	 *            Dados do evento
	 * @param gateway
	 *            Gateway responsável por publicar o evento
	 */
	public static void publishStockDebitEvent(StockDebitEventInput stockDebitEventInput,
			EventPublisherGateway gateway) {
		var debitStockEvent = StockDebitEventMapper.toEvent(stockDebitEventInput);
		gateway.publishStockDebitEvent(debitStockEvent);
		log.info("Evento de débito de estoque publicado: Pedido {}", stockDebitEventInput.orderId());
	}
}
