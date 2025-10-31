package com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers;

import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockDebitEventDto;

/**
 * Classe utilitária responsável por mapear {@link StockDebitEvent} para o DTO
 * {@link StockDebitEventDto}, utilizado para transporte de dados do evento de
 * débito de estoque.
 */
public class StockDebitEventMapper {

	/**
	 * Converte um {@link StockDebitEvent} em um {@link StockDebitEventDto}.
	 *
	 * @param event
	 *            Evento de débito de estoque.
	 * @return DTO com os dados do estoque debitado.
	 */
	public static StockDebitEventDto toDto(StockDebitEvent event) {
		StockDebitEventDto dto = new StockDebitEventDto();
		dto.setOrderId(event.getOrderId());
		dto.setOrderNumber(event.getOrderNumber());
		dto.setUserId(event.getUserId());
		dto.setTotalAmount(event.getTotalAmount());
		dto.setItems(StockDebitItemEventMapper.toDtoList(event.getItems()));
		return dto;
	}
}
