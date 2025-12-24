package com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers;

import com.soat.fiap.food.core.catalog.core.domain.events.StockReversalEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockReversalEventDto;

/**
 * Classe utilitária responsável por mapear {@link StockReversalEvent} para o
 * DTO {@link StockReversalEventDto}, utilizado para transporte de dados do
 * evento de estorno de estoque.
 */
public class StockReversalEventMapper {

	/**
	 * Converte um {@link StockReversalEvent} em um {@link StockReversalEventDto}.
	 *
	 * @param event
	 *            Evento de estorno de estoque.
	 * @return DTO com os dados do estoque estornado.
	 */
	public static StockReversalEventDto toDto(StockReversalEvent event) {
		StockReversalEventDto dto = new StockReversalEventDto();
		dto.setOrderId(event.getOrderId());
		return dto;
	}
}
