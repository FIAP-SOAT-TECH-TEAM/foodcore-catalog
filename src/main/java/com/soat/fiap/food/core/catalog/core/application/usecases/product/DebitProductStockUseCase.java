package com.soat.fiap.food.core.catalog.core.application.usecases.product;

import com.soat.fiap.food.core.catalog.core.application.inputs.ProductStockUpdateInput;
import com.soat.fiap.food.core.catalog.core.domain.exceptions.CatalogNotFoundException;
import com.soat.fiap.food.core.catalog.core.domain.exceptions.ProductNotFoundException;
import com.soat.fiap.food.core.catalog.core.domain.model.Catalog;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.CatalogGateway;

import lombok.extern.slf4j.Slf4j;

/**
 * Caso de uso: Debitar quantidade em estoque de um produto.
 *
 * Este caso de uso remove do estoque a quantidade de um produto,
 * geralmente após a efetivação de um pedido.
 */
@Slf4j
public class DebitProductStockUseCase {

	/**
	 * Debita a quantidade em estoque de um produto.
	 *
	 * @param productStockItemInput
	 *            item do pedido
	 * @param gateway
	 *            Gateway de catalogo para comunicação com o mundo exterior
	 * @return Catalogo com o estoque de produto atualizado
	 */
	public static Catalog debitProductStock(ProductStockUpdateInput.ProductStockItemInput productStockItemInput,
											CatalogGateway gateway) {
		if (productStockItemInput == null) {
			throw new ProductNotFoundException(
					"Item de produto é nulo. Não é possível efetuar o debitar quantidade em estoque.");
		}

		var catalog = gateway.findByProductId(productStockItemInput.productId());
		if (catalog.isEmpty()) {
			throw new CatalogNotFoundException(
					"Catálogo do produto não encontrado. Não é possível debitar quantidade em estoque.");
		}

		var currentProductQuantity = catalog.get().getProductStockQuantity(productStockItemInput.productId());
		var newProductQuantity = currentProductQuantity - productStockItemInput.quantity();

		log.info("Iniciando debito de quantidade em estoque: ProductId {}, atual: {}, nova: {}",
				productStockItemInput.productId(), currentProductQuantity, newProductQuantity);

		catalog.get().updateProductStockQuantity(productStockItemInput.productId(), newProductQuantity);

		return catalog.get();
	}
}
