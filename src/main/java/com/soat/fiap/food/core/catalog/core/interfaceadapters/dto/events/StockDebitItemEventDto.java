package com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events;
import java.math.BigDecimal;

/**
 * DTO utilizado para representar dados do evento de domínio
 * StockDebitItemEvent. Serve como objeto de transferência entre o domínio e o
 * mundo externo (DataSource).
 */
public class StockDebitItemEventDto {
	public Long productId;
	public String name;
	public Integer quantity;
	public BigDecimal unitPrice;
	public BigDecimal subtotal;
	public String observations;
}
