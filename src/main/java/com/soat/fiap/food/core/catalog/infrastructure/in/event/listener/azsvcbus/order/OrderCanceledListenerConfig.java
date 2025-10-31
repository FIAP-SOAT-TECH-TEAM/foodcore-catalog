package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCanceledEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order.handlers.OrderCanceledHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	private final OrderCanceledHandler orderCanceledHandler;

	@Bean
	public ServiceBusProcessorClient catalogOrderCanceledTopicServiceBusProcessorClient(
			ServiceBusClientBuilder builder) {

		return builder.processor()
				.topicName(ServiceBusConfig.ORDER_CANCELED_TOPIC)
				.subscriptionName(ServiceBusConfig.CATALOG_ORDER_CANCELED_TOPIC_SUBSCRIPTION)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					OrderCanceledEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							OrderCanceledEventDto.class);
					orderCanceledHandler.handle(event);
				})
				.processError(
						context -> log.error("Erro ao processar evento de pedido cancelado", context.getException()))
				.buildProcessorClient();
	}
}
