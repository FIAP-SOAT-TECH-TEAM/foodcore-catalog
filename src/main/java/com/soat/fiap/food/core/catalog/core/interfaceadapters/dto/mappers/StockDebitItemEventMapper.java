package com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitItemEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.StockDebitItemEventDto;

/**
 * Classe utilitária responsável por mapear {@link StockDebitItemEvent} para o
 * DTO {@link StockDebitItemEventDto}, utilizado para transporte de dados de um
 * item debitado do estoque.
 */
public class StockDebitItemEventMapper {

	/**
	 * Converte um {@link StockDebitItemEvent} em um {@link StockDebitItemEventDto}.
	 *
	 * @param event
	 *            Evento de item de débito de estoque.
	 * @return DTO com os dados do item debitado.
	 */
	public static StockDebitItemEventDto toDto(StockDebitItemEvent event) {
		StockDebitItemEventDto dto = new StockDebitItemEventDto();
		dto.productId = event.getProductId();
		dto.name = event.getName();
		dto.quantity = event.getQuantity();
		dto.unitPrice = event.getUnitPrice();
		dto.subtotal = event.getSubtotal();
		dto.observations = event.getObservations();
		return dto;
	}

	/**
	 * Converte uma lista de {@link StockDebitItemEvent} em uma lista de
	 * {@link StockDebitItemEventDto}.
	 *
	 * @param events
	 *            Lista de eventos de itens debitados do estoque.
	 * @return Lista de DTOs com os dados dos itens debitados.
	 */
	public static List<StockDebitItemEventDto> toDtoList(List<StockDebitItemEvent> events) {
		return events.stream().map(StockDebitItemEventMapper::toDto).collect(Collectors.toList());
	}
}
