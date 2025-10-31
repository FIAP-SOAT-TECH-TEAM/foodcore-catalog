package com.soat.fiap.food.core.catalog.infrastructure.out.event.publisher.azsvcbus;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockReversalEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implementação do {@link EventPublisherSource} usando Azure Service Bus.
 * <p>
 * Esta classe envia eventos de domínio para filas do Azure Service Bus
 * correspondentes. Cada método publica um tipo de evento específico.
 * </p>
 */
@Slf4j @Component
public class AzSvcBusEventPublisher implements EventPublisherSource {

	private final ServiceBusSenderClient stockReversalSender;
	private final Gson gson;

	/**
	 * Construtor que inicializa os clients do Azure Service Bus usando a connection
	 * string.
	 *
	 * @param connectionString
	 *            Connection string do Azure Service Bus, lida do application.yaml
	 */
	public AzSvcBusEventPublisher(@Value("${azsvcbus.connection-string}") String connectionString, Gson gson) {

		this.stockReversalSender = new ServiceBusClientBuilder().connectionString(connectionString)
				.sender()
				.queueName(ServiceBusConfig.STOCK_REVERSAL_QUEUE)
				.buildClient();

		this.gson = gson;
	}

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
			log.error("Erro ao publicar evento de estorno de estoque aprovado", ex);
		}
	}

	/**
	 * Fecha todos os clients do Azure Service Bus ao destruir o bean.
	 */
	@PreDestroy
	public void close() {
		log.info("Fechando clients do Azure Service Bus...");
		if (stockReversalSender != null)
			stockReversalSender.close();
	}
}
