package com.soat.fiap.food.core.catalog.infrastructure.common.source;

import java.util.List;
import java.util.Optional;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.CatalogDTO;

/**
 * DataSource para persistência do agregado Catálogo.
 */
public interface CatalogDataSource {

	/**
	 * Salva o agregado Catálogo.
	 *
	 * @param catalogDTO
	 *            DTO do Catálogo a ser salvo
	 * @return DTO do Catálogo salvo com identificadores atualizados
	 */
	CatalogDTO save(CatalogDTO catalogDTO);

	/**
	 * Busca um catálogo por ID.
	 *
	 * @param id
	 *            ID do catálogo
	 * @return Optional contendo o catálogo ou vazio se não encontrado
	 */
	Optional<CatalogDTO> findById(Long id);

	/**
	 * Busca um catálogo por nome.
	 *
	 * @param name
	 *            Nome do catalogo
	 * @return Optional contendo o catálogo ou vazio se não encontrado
	 */
	Optional<CatalogDTO> findByName(String name);

	/**
	 * Lista todos os catálogos.
	 *
	 * @return Lista de catálogos
	 */
	List<CatalogDTO> findAll();

	/**
	 * Verifica se existe um catalogo com um determinado ID.
	 *
	 * @param id
	 *            ID do catálogo
	 * @return true se existir um catalogo com um determinado ID, false caso
	 *         contrário
	 */
	boolean existsById(Long id);

	/**
	 * Verifica se existe um catalogo com um determinado nome.
	 *
	 * @param name
	 *            Nome do catálogo
	 * @return true se existir um catalogo com um determinado nome, false caso
	 *         contrário
	 */
	boolean existsByName(String name);

	/**
	 * Verifica se existe outro catálogo com o mesmo nome, mas com ID diferente.
	 *
	 * @param name
	 *            Nome do catálogo
	 * @param id
	 *            ID do catálogo a ser desconsiderado na verificação
	 * @return true se existir outro catálogo com o mesmo nome e ID diferente, false
	 *         caso contrário
	 */
	boolean existsByNameAndIdNot(String name, Long id);

	/**
	 * Verifica se existe pelo menos uma categoria associada ao catálogo com o ID
	 * informado.
	 *
	 * @param catalogId
	 *            ID do catálogo
	 * @return true se houver ao menos uma categoria associada, false caso contrário
	 */
	boolean existsCategoryByCatalogId(Long catalogId);

	/**
	 * Verifica se um produto existe pelo id.
	 *
	 * @param id
	 *            ID do catálogo a ser removido
	 */
	void delete(Long id);

	/**
	 * Retorna um catalogo pelo ID do produto
	 *
	 * @param productId
	 *            ID do produto
	 */
	Optional<CatalogDTO> findByProductId(Long productId);
}
