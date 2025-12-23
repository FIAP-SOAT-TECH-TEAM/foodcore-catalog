package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.controller.product.UpdateProductStockForCreatedItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por processar eventos de pedidos criados.
 *
 * <p>
 * Quando um novo pedido é criado, este handler atualiza o estoque de produtos
 * afetados, garantindo consistência do catálogo.
 * </p>
 */
@Slf4j @Service @RequiredArgsConstructor
public class OrderCreatedHandler {

	private final CatalogDataSource catalogDataSource;
	private final EventPublisherSource eventPublisherSource;

	/**
	 * Processa o evento de pedido criado.
	 *
	 * @param event
	 *            Evento de pedido criado
	 */
	@Transactional
	public void handle(OrderCreatedEventDto event) {
		log.info("Evento de pedido criado recebido: {}", event.getId());

		UpdateProductStockForCreatedItemsController.updateProductStockForCreatedItems(event, catalogDataSource,
				eventPublisherSource);

		log.info("Processamento concluído para {} itens.", event.getItems().size());
	}
}
