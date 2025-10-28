package com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config;

import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração do Azure Service Bus.
 * <p>
 */
@Configuration
public class ServiceBusConfig {

	/** Nome do tópico para eventos de pedido criado. */
	public static final String ORDER_CREATED_TOPIC = "order.created.topic";

	/** Nome da subscription para eventos de pedido criado. */
	public static final String CATALOG_ORDER_CREATED_TOPIC_SUBSCRIPTION = "catalog.order.created.topic.subscription";

	/** Fila para eventos de pedido cancelado. */
	public static final String ORDER_CANCELED_QUEUE = "order.canceled.queue";
}
