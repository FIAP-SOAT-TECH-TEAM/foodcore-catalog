package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.nimbusds.jose.shaded.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForCreatedItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Listener para eventos de pedido criado no módulo de catálogo via Azure
 * Service Bus.
 * <p>
 * Este listener consome mensagens do tópico
 * {@link ServiceBusConfig#ORDER_CREATED_TOPIC} e da subscription
 * {@link ServiceBusConfig#CATALOG_ORDER_CREATED_TOPIC_SUBSCRIPTION},
 * processando eventos {@link OrderCreatedEventDto} para atualizar o estoque dos
 * produtos de acordo com os itens do pedido.
 */
@Component @Slf4j
public class OrderCreatedListener {

	private final Gson gson = new Gson();

	/**
	 * Construtor do listener de eventos de pedido criado.
	 *
	 * @param catalogDataSource
	 *            Fonte de dados do catálogo
	 * @param connectionString
	 *            Connection string do Azure Service Bus
	 */
	public OrderCreatedListener(CatalogDataSource catalogDataSource,
			@Value("${azsvcbus.connection-string}") String connectionString) {

		try (ServiceBusProcessorClient processor = new ServiceBusClientBuilder().connectionString(connectionString)
				.processor()
				.topicName(ServiceBusConfig.ORDER_CREATED_TOPIC)
				.subscriptionName(ServiceBusConfig.CATALOG_ORDER_CREATED_TOPIC_SUBSCRIPTION)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					OrderCreatedEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							OrderCreatedEventDto.class);
					handle(event, catalogDataSource);
				})
				.processError(context -> log.error("Erro no listener de pedido criado", context.getException()))
				.buildProcessorClient()) {

			processor.start();

		} catch (Exception e) {
			log.error("Falha ao iniciar OrderCreatedListener", e);
		}
	}

	/**
	 * Processa o evento de pedido criado, atualizando o estoque de produtos.
	 *
	 * @param event
	 *            Evento de pedido criado
	 * @param catalogDataSource
	 *            Fonte de dados do catálogo
	 */
	private void handle(OrderCreatedEventDto event, CatalogDataSource catalogDataSource) {
		log.info("Módulo Catálogo: Notificado de criação de pedido: {}", event.getId());

		UpdateProductStockForCreatedItemsController.updateProductStockForCreatedItems(event, catalogDataSource);

		log.info("Quantidade em estoque atualizada para: {} produtos.", event.getItems().size());
	}
}
