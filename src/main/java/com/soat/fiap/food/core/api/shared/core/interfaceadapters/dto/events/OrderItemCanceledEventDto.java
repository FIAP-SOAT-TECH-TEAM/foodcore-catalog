package com.soat.fiap.food.core.api.shared.core.interfaceadapters.dto.events;

import java.math.BigDecimal;

import com.soat.fiap.food.core.api.order.core.domain.events.OrderItemCanceledEvent;

import lombok.Data;

/**
 * DTO utilizado para representar dados do evento de domínio
 * {@link OrderItemCanceledEvent}. Serve como objeto de transferência entre o
 * domínio e o mundo externo (DataSource).
 */
@Data
public class OrderItemCanceledEventDto {
	public Long id;
	public Long productId;
	public String name;
	public Integer quantity;
	public BigDecimal unitPrice;
	public BigDecimal subtotal;
	public String observations;
}
