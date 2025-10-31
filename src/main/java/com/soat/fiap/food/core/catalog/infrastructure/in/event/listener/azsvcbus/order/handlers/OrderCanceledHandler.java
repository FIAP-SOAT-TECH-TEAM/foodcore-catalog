package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.order.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForCanceledItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCanceledEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por processar eventos de pedidos cancelados.
 *
 * <p>
 * Quando um pedido é cancelado, este handler atualiza o estoque de produtos
 * afetados, garantindo consistência do catálogo.
 * </p>
 */
@Slf4j @Service @RequiredArgsConstructor
public class OrderCanceledHandler {

	private final CatalogDataSource catalogDataSource;

	/**
	 * Processa o evento de pedido cancelado.
	 *
	 * @param event
	 *            Evento de pedido cancelado
	 */
	@Transactional
	public void handle(OrderCanceledEventDto event) {
		log.info("Evento de pedido cancelado recebido: {}", event.getId());

		UpdateProductStockForCanceledItemsController.updateProductStockForCanceledItems(event, catalogDataSource);

		log.info("Processamento concluído para {} itens.", event.getItems().size());
	}
}
