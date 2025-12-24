package com.soat.fiap.food.core.catalog.core.interfaceadapters.controller.product;

import com.soat.fiap.food.core.catalog.core.application.usecases.product.GetProductByIdUseCase;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.CatalogGateway;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.presenter.ProductPresenter;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.responses.ProductResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Buscar um produto pelo seu ID.
 */
@Slf4j
public class GetProductByIdController {

	/**
	 * Busca um produto por ID dentro de uma categoria de um catálogo.
	 *
	 * @param catalogId
	 *            ID do catálogo
	 * @param categoryId
	 *            ID da categoria
	 * @param productId
	 *            ID do produto
	 * @param catalogDataSource
	 *            Origem de dados para o gateway
	 * @return o produto encontrado
	 */
	public static ProductResponse getProductById(Long catalogId, Long categoryId, Long productId,
			CatalogDataSource catalogDataSource) {
		log.debug("Buscando produto de id: {} na categoria de id: {} no catalogo de id: {}", productId, categoryId,
				catalogId);

		var gateway = new CatalogGateway(catalogDataSource);

		var existingProduct = GetProductByIdUseCase.getProductById(catalogId, categoryId, productId, gateway);

		return ProductPresenter.toProductResponse(existingProduct);
	}
}
