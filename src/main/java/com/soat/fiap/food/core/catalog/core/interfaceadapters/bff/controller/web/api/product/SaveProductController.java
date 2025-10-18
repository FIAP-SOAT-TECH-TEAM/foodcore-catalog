package com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.controller.web.api.product;

import com.soat.fiap.food.core.catalog.core.application.inputs.mappers.ProductMapper;
import com.soat.fiap.food.core.catalog.core.application.usecases.product.AddProductToCategoryUseCase;
import com.soat.fiap.food.core.catalog.core.application.usecases.product.UpdateProductImageInCategoryUseCase;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.presenter.web.api.ProductPresenter;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.CatalogGateway;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.requests.ProductRequest;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.responses.ProductResponse;
import com.soat.fiap.food.core.shared.core.interfaceadapters.dto.FileUploadDTO;
import com.soat.fiap.food.core.shared.core.interfaceadapters.gateways.ImageStorageGateway;
import com.soat.fiap.food.core.shared.infrastructure.common.source.ImageDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Salvar produto.
 */
@Slf4j
public class SaveProductController {

	/**
	 * Salva um produto em uma categoria específica de um catálogo.
	 *
	 * @param catalogId
	 *            ID do catálogo
	 * @param productRequest
	 *            Produto a ser salvo
	 * @param imageFile
	 *            Arquivo de imagem para o produto
	 * @param catalogDataSource
	 *            Origem de dados para o gateway de catálogo
	 * @param imageDataSource
	 *            Origem de dados para o gateway de imagens
	 * @return Produto salvo com identificadores atualizados
	 */
	public static ProductResponse saveProduct(Long catalogId, ProductRequest productRequest, FileUploadDTO imageFile,
			CatalogDataSource catalogDataSource, ImageDataSource imageDataSource) {

		var gateway = new CatalogGateway(catalogDataSource);
		var imageStorageGateway = new ImageStorageGateway(imageDataSource);

		var productInput = ProductMapper.toInput(productRequest);

		var catalog = AddProductToCategoryUseCase.addProductToCategory(catalogId, productInput, gateway);

		var savedCatalog = gateway.save(catalog);

		var savedProduct = savedCatalog.getLastProductOfCategory(productRequest.getCategoryId());

		if (imageFile != null) {
			var updatedCatalog = UpdateProductImageInCategoryUseCase.updateProductImageInCategory(catalogId,
					productRequest.getCategoryId(), savedProduct.getId(), imageFile, gateway, imageStorageGateway);
			savedCatalog = gateway.save(updatedCatalog);
			savedProduct = savedCatalog.getLastProductOfCategory(productRequest.getCategoryId());
		}

		log.debug("Produto criado com sucesso: {}", savedProduct.getId());

		return ProductPresenter.toProductResponse(savedProduct);
	}
}
