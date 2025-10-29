package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.nimbusds.jose.shaded.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForCanceledItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCanceledEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Listener para eventos de pedido cancelado no módulo de catálogo via Azure
 * Service Bus.
 * <p>
 * Este listener consome mensagens da fila
 * {@link ServiceBusConfig#ORDER_CANCELED_QUEUE}, processando eventos
 * {@link OrderCanceledEventDto} para restaurar o estoque dos produtos conforme
 * os itens do pedido cancelado.
 */
@Configuration @Slf4j
public class OrderCanceledListenerConfig {

	private final Gson gson = new Gson();

	/**
	 * Construtor do listener de eventos de pedido cancelado.
	 *
	 * @param catalogDataSource
	 *            Fonte de dados do catálogo
	 * @param connectionString
	 *            Connection string do Azure Service Bus
	 */
	@Bean
	public ServiceBusProcessorClient orderCanceledServiceBusProcessorClient(CatalogDataSource catalogDataSource,
			@Value("${azsvcbus.connection-string}") String connectionString) {
		return new ServiceBusClientBuilder().connectionString(connectionString)
				.processor()
				.queueName(ServiceBusConfig.ORDER_CANCELED_QUEUE)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					OrderCanceledEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							OrderCanceledEventDto.class);
					handle(event, catalogDataSource);
				})
				.processError(context -> log.error("Erro no listener de pedido cancelado", context.getException()))
				.buildProcessorClient();
	}

	/**
	 * Processa o evento de pedido cancelado, restaurando o estoque de produtos.
	 *
	 * @param event
	 *            Evento de pedido cancelado
	 * @param catalogDataSource
	 *            Fonte de dados do catálogo
	 */
	private void handle(OrderCanceledEventDto event, CatalogDataSource catalogDataSource) {
		log.info("Módulo Catálogo: Notificado de cancelamento de pedido: {}", event.getId());

		UpdateProductStockForCanceledItemsController.updateProductStockForCanceledItems(event, catalogDataSource);

		log.info("Quantidade em estoque atualizada para: {} produtos.", event.getItems().size());
	}
}
