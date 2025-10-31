package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForCreatedItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listener responsável por processar eventos relacionados a pedidos criados
 * recebidos pelo Azure Service Bus.
 *
 * <p>
 * Este componente realiza o tratamento necessário no módulo de catálogo quando
 * um novo pedido é criado.
 * </p>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class OrderCreatedListenerConfig {

	private final Gson gson;

	/**
	 * Cria o processador responsável por consumir mensagens de criação de pedido.
	 *
	 * @param catalogDataSource
	 *            Fonte de dados do catálogo
	 * @param connectionString
	 *            String de conexão do Azure Service Bus
	 * @return Cliente processador configurado
	 */
	@Bean
	public ServiceBusProcessorClient catalogOrderCreatedTopicServiceBusProcessorClient(
			CatalogDataSource catalogDataSource, @Value("${azsvcbus.connection-string}") String connectionString) {

		return new ServiceBusClientBuilder().connectionString(connectionString)
				.processor()
				.topicName(ServiceBusConfig.ORDER_CREATED_TOPIC)
				.subscriptionName(ServiceBusConfig.CATALOG_ORDER_CREATED_TOPIC_SUBSCRIPTION)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					OrderCreatedEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							OrderCreatedEventDto.class);
					handle(event, catalogDataSource);
				})
				.processError(context -> log.error("Erro ao processar evento de pedido criado", context.getException()))
				.buildProcessorClient();
	}

	/**
	 * Executa o tratamento do evento recebido.
	 *
	 * @param event
	 *            Evento de pedido criado
	 * @param catalogDataSource
	 *            Fonte de dados do catálogo
	 */
	private void handle(OrderCreatedEventDto event, CatalogDataSource catalogDataSource) {
		log.info("Evento de pedido criado recebido: {}", event.getId());

		UpdateProductStockForCreatedItemsController.updateProductStockForCreatedItems(event, catalogDataSource);

		log.info("Processamento concluído para {} itens.", event.getItems().size());
	}
}
