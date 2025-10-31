package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.SubQueue;
import com.google.gson.Gson;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockDebitEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.event.azsvcbus.config.ServiceBusConfig;
import com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.payment.handlers.PaymentInitializationErrorHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Listener responsável por processar eventos de erro na inicialização de
 * pagamento.
 *
 * @see <a href=
 *      "https://learn.microsoft.com/pt-br/java/api/overview/azure/messaging-servicebus-readme?view=azure-java-stable#create-a-dead-letter-queue-receiver">Create
 *      a dead-letter queue Receiver - Azure Service Bus</a>
 */
@Configuration @Slf4j @RequiredArgsConstructor @Transactional
public class PaymentInitializationErrorListenerConfig {

	private final Gson gson;
	private final PaymentInitializationErrorHandler paymentInitializationErrorHandler;

	@Bean
	public ServiceBusProcessorClient paymentInitializationErrorServiceBusProcessorClient(
			ServiceBusClientBuilder builder) {

		return builder.processor()
				.queueName(ServiceBusConfig.STOCK_DEBIT_QUEUE)
				.subQueue(SubQueue.DEAD_LETTER_QUEUE)
				.processMessage(context -> {
					StockDebitEventDto event = gson.fromJson(context.getMessage().getBody().toString(),
							StockDebitEventDto.class);
					paymentInitializationErrorHandler.handle(event);
				})
				.processError(context -> log.error("Erro ao processar erro da inicialização de pagamento",
						context.getException()))
				.buildProcessorClient();
	}
}
