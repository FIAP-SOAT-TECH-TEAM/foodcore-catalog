package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Responsável por inicializar os processadores de eventos do módulo de
 * catálogo.
 *
 * <p>
 * Este componente garante que todos os listeners sejam iniciados
 * automaticamente após o carregamento do contexto da aplicação.
 * </p>
 */
@RequiredArgsConstructor @Component
public class OrderListener {

	private final ServiceBusProcessorClient catalogOrderCreatedTopicServiceBusProcessorClient;
	private final ServiceBusProcessorClient catalogOrderCanceledTopicServiceBusProcessorClient;

	/**
	 * Inicia os processadores configurados após a injeção de dependências.
	 */
	@PostConstruct
	public void run() {
		catalogOrderCreatedTopicServiceBusProcessorClient.start();
		catalogOrderCanceledTopicServiceBusProcessorClient.start();
	}
}
