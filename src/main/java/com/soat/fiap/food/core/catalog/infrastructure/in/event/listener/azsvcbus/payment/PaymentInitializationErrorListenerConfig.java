package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.payment;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.SubQueue;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForReversalItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listener responsável por processar eventos de erro na inicialização de
 * pagamento.
 *
 * @see <a href="https://learn.microsoft.com/pt-br/java/api/overview/azure/messaging-servicebus-readme?view=azure-java-stable#create-a-dead-letter-queue-receiver">Create a dead-letter queue Receiver - Azure Service Bus</a>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class PaymentInitializationErrorListenerConfig {

	private final Gson gson;

	@Bean
	public ServiceBusProcessorClient paymentInitializationErrorServiceBusProcessorClient(
			EventPublisherSource eventPublisherSource,
			CatalogDataSource catalogDataSource,
			@Value("${azsvcbus.connection-string}") String connectionString) {

		return new ServiceBusClientBuilder().connectionString(connectionString)
				.processor()
				.topicName(ServiceBusConfig.ORDER_CREATED_TOPIC)
				.subscriptionName(ServiceBusConfig.PAYMENT_ORDER_CREATED_TOPIC_SUBSCRIPTION)
				.subQueue(SubQueue.DEAD_LETTER_QUEUE)
				.processMessage(context -> {
					OrderCreatedEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							OrderCreatedEventDto.class);
					handle(event, catalogDataSource, eventPublisherSource);
				})
				.processError(context -> log.error("Erro ao processar erro de inicialização de pagamento",
						context.getException()))
				.buildProcessorClient();
	}

	private void handle(OrderCreatedEventDto event, CatalogDataSource catalogDataSource, EventPublisherSource eventPublisherSource) {

		log.info("Evento de erro na inicialização do pagamento recebido: {}", event.getId());

		UpdateProductStockForReversalItemsController.updateProductStockForReversalItems(event, catalogDataSource, eventPublisherSource);

		log.info("Status do pedido atualizado após erro na inicialização do pagamento: {}", event.getId());
	}
}
