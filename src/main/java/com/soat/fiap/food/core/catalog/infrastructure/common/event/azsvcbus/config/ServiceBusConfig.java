package com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

/**
 * Classe de configuração do Azure Service Bus.
 * <p>
 */
@Configuration
public class ServiceBusConfig {

	@Value("${azsvcbus.connection-string}")
	private String connectionString;

	/** Nome do tópico para eventos de estoque debitado. */
	public static final String STOCK_DEBIT_QUEUE = "stock.debit.queue";

	/** Nome do tópico para eventos de pedido criado. */
	public static final String ORDER_CREATED_TOPIC = "order.created.topic";

	/** Nome da subscription para eventos de pedido criado. */
	public static final String CATALOG_ORDER_CREATED_TOPIC_SUBSCRIPTION = "catalog.order.created.topic.subscription";

	/** Nome do tópico para eventos de pedido cancelado. */
	public static final String ORDER_CANCELED_TOPIC = "order.canceled.topic";

	/** Fila para eventos de estoque estornado. */
	public static final String STOCK_REVERSAL_QUEUE = "stock.reversal.queue";

	/** Nome da subscription para eventos de pedido cancelado. */
	public static final String CATALOG_ORDER_CANCELED_TOPIC_SUBSCRIPTION = "catalog.order.canceled.topic.subscription";

	@Bean
	public ServiceBusClientBuilder serviceBusClientBuilder() {
		return new ServiceBusClientBuilder().connectionString(connectionString);
	}

	@Bean
	public ServiceBusSenderClient stockReversalSender(ServiceBusClientBuilder builder) {
		return builder.sender().topicName(STOCK_REVERSAL_QUEUE).buildClient();
	}

	@Bean
	public ServiceBusSenderClient stockDebitSender(ServiceBusClientBuilder builder) {
		return builder.sender().topicName(STOCK_DEBIT_QUEUE).buildClient();
	}
}
