package com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product;

import com.soat.fiap.food.core.catalog.core.application.inputs.ProductStockUpdateInput;
import com.soat.fiap.food.core.catalog.core.application.inputs.mappers.ProductStockUpdateMapper;
import com.soat.fiap.food.core.catalog.core.application.usecases.product.CreditProductStockUseCase;
import com.soat.fiap.food.core.catalog.core.application.usecases.product.PublishStockReversalEventUseCase;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.CatalogGateway;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.EventPublisherGateway;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.EventPublisherSource;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Atualizar quantidade em estoque de produtos, de acordo com a
 * quantidade solicitada em um pedido, em cenários de estorno.
 */
@Slf4j
public class UpdateProductStockForReversalItemsController {
	/**
	 * Atualiza quantidade em estoque de produtos, de acordo com a
	 * quantidade solicitada em um pedido, em cenários de estorno.
	 *
	 * @param orderCreatedEvent
	 *            evento de criação de pedido
	 * @param catalogDataSource
	 *            Origem de dados para o gateway]
	 * @param eventPublisherSource
	 *            Origem de publicação de eventos
	 */
	public static void updateProductStockForReversalItems(OrderCreatedEventDto orderCreatedEvent,
														  CatalogDataSource catalogDataSource, EventPublisherSource eventPublisherSource) {
		var catalogGateway = new CatalogGateway(catalogDataSource);
		var eventPublisherGateway = new EventPublisherGateway(eventPublisherSource);
		var productStockUpdateInput = ProductStockUpdateMapper.toInput(orderCreatedEvent.getItems());

		for (ProductStockUpdateInput.ProductStockItemInput productStockItemInput : productStockUpdateInput.items()) {

			var catalog = CreditProductStockUseCase.creditProductStock(productStockItemInput,
					catalogGateway);

			catalogGateway.save(catalog);

			log.info("Atualização de quantidade em estoque realizada com sucesso! Motivo: Estorno de Pedido, ProductId {}",
					productStockItemInput.productId());
		}

		PublishStockReversalEventUseCase.publishStockReversalEvent(orderCreatedEvent.getId(), eventPublisherGateway);
	}
}
