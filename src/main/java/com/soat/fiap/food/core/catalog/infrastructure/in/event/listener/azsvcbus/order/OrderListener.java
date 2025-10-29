package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order;

import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Component
public class OrderListener {

	private final ServiceBusProcessorClient catalogOrderCreatedTopicServiceBusProcessorClient;
	private final ServiceBusProcessorClient orderCanceledServiceBusProcessorClient;

	@PostConstruct
	public void run() {
		catalogOrderCreatedTopicServiceBusProcessorClient.start();
		orderCanceledServiceBusProcessorClient.start();
	}

}
