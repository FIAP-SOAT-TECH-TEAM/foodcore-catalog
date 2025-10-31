package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order.handlers.OrderCreatedHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener responsável por processar eventos relacionados a pedidos criados
 * recebidos pelo Azure Service Bus.
 *
 * <p>
 * Este componente realiza o tratamento necessário no módulo de catálogo quando
 * um novo pedido é criado.
 * </p>
 */
@Configuration @Slf4j @RequiredArgsConstructor
public class OrderCreatedListenerConfig {

	private final Gson gson;
	private final OrderCreatedHandler orderCreatedHandler;

	@Bean
	public ServiceBusProcessorClient catalogOrderCreatedTopicServiceBusProcessorClient(
			ServiceBusClientBuilder builder) {

		return builder.processor()
				.topicName(ServiceBusConfig.ORDER_CREATED_TOPIC)
				.subscriptionName(ServiceBusConfig.CATALOG_ORDER_CREATED_TOPIC_SUBSCRIPTION)
				.receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
				.processMessage(context -> {
					OrderCreatedEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							OrderCreatedEventDto.class);
					orderCreatedHandler.handle(event);
				})
				.processError(context -> log.error("Erro ao processar evento de pedido criado", context.getException()))
				.buildProcessorClient();
	}
}
