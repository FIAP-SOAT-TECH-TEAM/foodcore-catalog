package com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.repository.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.ProductDTO;
import com.soat.fiap.food.core.catalog.infrastructure.common.source.ProductDataSource;
import com.soat.fiap.food.core.catalog.infrastructure.out.persistence.postgres.mapper.ProductEntityMapper;

/**
 * Implementação concreta: DataSource para obtenção de Produto.
 */
@Component
public class PostgresProductDataSource implements ProductDataSource {

	private final SpringDataProductRepository springDataProductRepository;
	private final ProductEntityMapper productEntityMapper;

	public PostgresProductDataSource(SpringDataProductRepository springDataProductRepository,
			ProductEntityMapper productEntityMapper) {
		this.springDataProductRepository = springDataProductRepository;
		this.productEntityMapper = productEntityMapper;
	}

	/**
	 * Retorna uma lista de Produtos pelo seus IDs
	 *
	 * @param productIds
	 *            IDs do produtos
	 */
	@Override @Transactional(readOnly = true)
	public Optional<List<ProductDTO>> findByProductIds(List<Long> productIds) {
		return springDataProductRepository.findByIdIn(productIds).map(productEntityMapper::toDTOList);
	}

}
