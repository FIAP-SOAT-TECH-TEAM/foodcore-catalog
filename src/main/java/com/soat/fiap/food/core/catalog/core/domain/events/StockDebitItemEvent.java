package com.soat.fiap.food.core.catalog.core.domain.events;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Evento de domínio (DDD) emitido quando um item de estoque é debitado
 */
@Data @AllArgsConstructor
public class StockDebitItemEvent {

	private Long productId;
	private String name;
	private Integer quantity;
	private BigDecimal unitPrice;
	private BigDecimal subtotal;
	private String observations;

	/**
	 * Cria um evento de item de estoque debitado
	 *
	 * @param productId
	 *            Id do produto que originou o débito de estoque
	 * @param name
	 *            Nome do produto que originou o débito de estoque
	 * @param quantity
	 *            Quantidade solicitada do item de pedido que originou o débito de
	 *            estoque
	 * @param unitPrice
	 *            Preço unitário do item de pedido que originou o débito de estoque
	 * @param subtotal
	 *            Subtotal do item de pedido que originou o débito de estoque
	 * @param observations
	 *            Observações do item de pedido que originou o débito de estoque
	 * @return Nova instância do evento
	 */
	public static StockDebitItemEvent of(Long productId, String name, Integer quantity, BigDecimal unitPrice,
			BigDecimal subtotal, String observations) {
		return new StockDebitItemEvent(productId, name, quantity, unitPrice, subtotal, observations);
	}
}
