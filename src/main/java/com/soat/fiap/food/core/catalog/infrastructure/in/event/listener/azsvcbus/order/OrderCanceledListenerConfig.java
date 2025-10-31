package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForCanceledItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCanceledEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listener responsável por processar eventos relacionados a pedidos cancelados
 * recebidos pelo Azure Service Bus.
 *
 * <p>
 * Ao receber um evento, este listener realiza o tratamento necessário no módulo
 * de catálogo, conforme a lógica de negócio definida.
 * </p>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class OrderCanceledListenerConfig {

	private final Gson gson;

	/**
	 * Cria o processador responsável por consumir mensagens de cancelamento de
	 * pedido.
	 *
	 * @param catalogDataSource
	 *            Fonte de dados do catálogo
	 * @param connectionString
	 *            String de conexão do Azure Service Bus
	 * @return Cliente processador configurado
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
				.processError(
						context -> log.error("Erro ao processar evento de pedido cancelado", context.getException()))
				.buildProcessorClient();
	}

	/**
	 * Executa o tratamento do evento recebido.
	 *
	 * @param event
	 *            Evento de pedido cancelado
	 * @param catalogDataSource
	 *            Fonte de dados do catálogo
	 */
	private void handle(OrderCanceledEventDto event, CatalogDataSource catalogDataSource) {
		log.info("Evento de pedido cancelado recebido: {}", event.getId());

		UpdateProductStockForCanceledItemsController.updateProductStockForCanceledItems(event, catalogDataSource);

		log.info("Processamento concluído para {} itens.", event.getItems().size());
	}
}
