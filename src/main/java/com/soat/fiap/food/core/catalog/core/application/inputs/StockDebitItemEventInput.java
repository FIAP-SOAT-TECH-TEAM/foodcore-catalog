package com.soat.fiap.food.core.catalog.core.application.inputs;

import java.math.BigDecimal;

/**
 * Representa um DTO de entrada da aplicação (Application Layer) contendo os
 * dados necessários para publicar o evento de domínio de débito de item de
 * estoque.
 * <p>
 * Esse input é utilizado no caso de uso responsável por publicar o evento de
 * domínio {@code StockDebitItemEvent}.
 *
 * @param productId
 *            Id do produto debitado no estoque
 * @param name
 *            Nome do produto debitado
 * @param quantity
 *            Quantidade debitada do estoque
 * @param unitPrice
 *            Valor unitário do produto debitado
 * @param subtotal
 *            Subtotal referente à quantidade debitada
 * @param observations
 *            Observações relacionadas ao item debitado
 */
public record StockDebitItemEventInput(Long productId, String name, Integer quantity, BigDecimal unitPrice,
		BigDecimal subtotal, String observations) {

	/**
	 * Construtor compacto que inicializa um {@code StockDebitItemEventInput} com
	 * todos os campos necessários.
	 *
	 * @param productId
	 *            Id do produto debitado
	 * @param name
	 *            Nome do produto debitado
	 * @param quantity
	 *            Quantidade debitada
	 * @param unitPrice
	 *            Valor unitário do produto debitado
	 * @param subtotal
	 *            Subtotal referente ao item debitado
	 * @param observations
	 *            Observações do item debitado
	 */
	public StockDebitItemEventInput {
	}
}
