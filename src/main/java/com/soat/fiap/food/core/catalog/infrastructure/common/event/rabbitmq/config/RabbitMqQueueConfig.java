package com.soat.fiap.food.core.catalog.infrastructure.common.event.rabbitmq.config;

import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuração do RabbitMQ para o sistema.
 * <p>
 * Define as filas e exchanges utilizadas pela aplicação e fornece os beans
 * necessários para integração com o RabbitMQ.
 * </p>
 */
@Configuration
public class RabbitMqQueueConfig {

	/** Fila para eventos de pedido criado, no módulo de catálogo. */
	public static final String ORDER_CATALOG_CREATED_QUEUE = "order.catalog.created.queue";

	/** Fila para eventos de pedido cancelado. */
	public static final String ORDER_CANCELED_QUEUE = "order.canceled.queue";

}
