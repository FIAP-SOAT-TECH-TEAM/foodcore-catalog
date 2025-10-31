package com.soat.fiap.food.core.catalog.core.application.inputs;

import java.math.BigDecimal;
import java.util.List;

/**
 * Representa um DTO de entrada da aplicação (Application Layer) contendo os
 * dados necessários para publicar o evento de domínio de débito de estoque.
 * <p>
 * Esse input é utilizado no caso de uso responsável por publicar o evento de
 * domínio {@code StockDebitEvent}.
 *
 * @param orderId
 *            Id do pedido que originou o débito de estoque
 * @param orderNumber
 *            Número do pedido que originou o débito de estoque
 * @param userId
 *            Id do usuário que originou o débito de estoque
 * @param totalAmount
 *            Total do pedido que originou o débito de estoque
 * @param items
 *            Lista de itens debitados do estoque
 */
public record StockDebitEventInput(Long orderId, String orderNumber, String userId, BigDecimal totalAmount,
		List<StockDebitItemEventInput> items) {

	/**
	 * Construtor compacto que inicializa um {@code StockDebitEventInput} com todos
	 * os campos necessários.
	 *
	 * @param orderId
	 *            Id do pedido que originou o débito de estoque
	 * @param orderNumber
	 *            Número do pedido que originou o débito de estoque
	 * @param userId
	 *            Id do usuário que originou o débito de estoque
	 * @param totalAmount
	 *            Total do pedido que originou o débito de estoque
	 * @param items
	 *            Lista de itens debitados do estoque
	 */
	public StockDebitEventInput {
	}
}
