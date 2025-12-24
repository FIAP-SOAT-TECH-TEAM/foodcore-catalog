package com.soat.fiap.food.core.catalog.infrastructure.out.event.publisher.azsvcbus;
import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockDebitEventDto;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockReversalEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementação do {@link EventPublisherSource} usando Azure Service Bus.
 * <p>
 * Esta classe envia eventos de domínio para filas do Azure Service Bus
 * correspondentes. Cada método publica um tipo de evento específico.
 * </p>
 */
@Slf4j @Component @AllArgsConstructor
public class AzSvcBusEventPublisher implements EventPublisherSource {

	private final ServiceBusSenderClient stockReversalSender;
	private final ServiceBusSenderClient stockDebitSender;
	private final Gson gson;

	/**
	 * Publica um evento de estorno de estoque na fila correspondente do Azure
	 * Service Bus.
	 *
	 * @param event
	 *            Evento de estorno de estoque
	 */
	@Override
	public void publishStockReversalEvent(StockReversalEventDto event) {
		try {
			stockReversalSender.sendMessage(new ServiceBusMessage(gson.toJson(event)));
			log.info("Evento de estorno de estoque publicado com sucesso: {}", event);
		} catch (Exception ex) {
			log.error("Erro ao publicar evento de estorno de estoque", ex);
		}
	}

	/**
	 * Publica um evento de débito de estoque na fila correspondente do Azure
	 * Service Bus.
	 *
	 * @param event
	 *            Evento de débito de estoque
	 */
	@Override
	public void publishStockDebitEvent(StockDebitEventDto event) {
		try {
			stockDebitSender.sendMessage(new ServiceBusMessage(gson.toJson(event)));
			log.info("Evento de débito de estoque publicado com sucesso: {}", event);
		} catch (Exception ex) {
			log.error("Erro ao publicar evento de débito de estoque", ex);
		}
	}
}
