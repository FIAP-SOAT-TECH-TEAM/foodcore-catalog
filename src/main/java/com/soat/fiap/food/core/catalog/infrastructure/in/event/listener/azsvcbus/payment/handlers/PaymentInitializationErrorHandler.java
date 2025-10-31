package com.soat.fiap.food.core.catalog.infrastructure.in.event.listener.azsvcbus.payment.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product.UpdateProductStockForReversalItemsController;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockDebitEventDto;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por processar eventos de erro na inicialização de
 * pagamento.
 *
 * <p>
 * Quando ocorre falha na inicialização de pagamento, este handler realiza
 * reversão de estoque e publica eventos relacionados.
 * </p>
 */
@Slf4j @Service @RequiredArgsConstructor
public class PaymentInitializationErrorHandler {

	private final CatalogDataSource catalogDataSource;
	private final EventPublisherSource eventPublisherSource;

	/**
	 * Processa o evento de erro na inicialização de pagamento.
	 *
	 * @param event
	 *            Evento de criação de pedido com falha de pagamento
	 */
	@Transactional
	public void handle(StockDebitEventDto event) {
		log.info("Evento de erro na inicialização do pagamento recebido: {}", event.orderId);

		UpdateProductStockForReversalItemsController.updateProductStockForReversalItems(event, catalogDataSource,
				eventPublisherSource);

		log.info("Status do pedido atualizado após erro na inicialização do pagamento: {}", event.orderId);
	}
}
