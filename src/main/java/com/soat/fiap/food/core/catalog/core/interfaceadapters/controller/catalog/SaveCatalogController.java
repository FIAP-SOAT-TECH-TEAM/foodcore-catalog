package com.soat.fiap.food.core.catalog.core.interfaceadapters.controller.catalog;

import com.soat.fiap.food.core.catalog.core.application.inputs.mappers.CatalogMapper;
import com.soat.fiap.food.core.catalog.core.application.usecases.catalog.CreateCatalogUseCase;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.presenter.CatalogPresenter;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.gateways.CatalogGateway;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.requests.CatalogRequest;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.responses.CatalogResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Controller: Salvar catálogo.
 */
@Slf4j
public class SaveCatalogController {

	/**
	 * Salva um catálogo.
	 *
	 * @param catalogRequest
	 *            Catálogo a ser salvo
	 * @param catalogDataSource
	 *            Origem de dados para o gateway
	 * @return Catálogo salvo com identificadores atualizados
	 */
	public static CatalogResponse saveCatalog(CatalogRequest catalogRequest, CatalogDataSource catalogDataSource) {

		var gateway = new CatalogGateway(catalogDataSource);

		var catalogInput = CatalogMapper.toInput(catalogRequest);

		var catalog = CreateCatalogUseCase.createCatalog(catalogInput, gateway);

		var savedCatalog = gateway.save(catalog);

		log.debug("Catalogo criado com sucesso: {}", savedCatalog.getId());

		return CatalogPresenter.toCatalogResponse(savedCatalog);
	}
}
