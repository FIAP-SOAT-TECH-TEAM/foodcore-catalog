package com.soat.fiap.food.core.catalog.core.application.inputs.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.soat.fiap.food.core.catalog.core.application.inputs.StockDebitItemEventInput;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitItemEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderItemCreatedEventDto;

/**
 * Classe utilitária responsável por mapear objetos entre eventos de domínio
 * relacionados a itens de pedido e o DTO de entrada
 * {@link StockDebitItemEventInput} da aplicação para publicação de eventos de
 * débito de item de estoque.
 */
public class StockDebitItemEventMapper {

	/**
	 * Converte um {@link OrderItemCreatedEventDto} (evento de criação de item de
	 * pedido) em um {@link StockDebitItemEventInput} (input da aplicação).
	 * <p>
	 * Usado para publicar o evento de domínio de débito de item de estoque.
	 *
	 * @param event
	 *            Evento de criação de item de pedido.
	 * @return Um objeto {@link StockDebitItemEventInput} representando os dados do
	 *         item debitado do estoque.
	 */
	public static StockDebitItemEventInput toInput(OrderItemCreatedEventDto event) {
		return new StockDebitItemEventInput(event.getProductId(), event.getName(), event.getQuantity(),
				event.getUnitPrice(), event.getSubtotal(), event.getObservations());
	}

	/**
	 * Converte uma lista de {@link OrderItemCreatedEventDto} (eventos de criação de
	 * itens de pedido) em uma lista de {@link StockDebitItemEventInput} (inputs da
	 * aplicação).
	 * <p>
	 * Útil para publicar eventos de débito de estoque que envolvem múltiplos itens.
	 *
	 * @param events
	 *            Lista de eventos de criação de itens de pedido.
	 * @return Uma lista de objetos {@link StockDebitItemEventInput} representando
	 *         os dados dos itens debitados do estoque.
	 */
	public static List<StockDebitItemEventInput> toInputList(List<OrderItemCreatedEventDto> events) {
		if (events == null || events.isEmpty()) {
			return List.of();
		}

		return events.stream().map(StockDebitItemEventMapper::toInput).collect(Collectors.toList());
	}

	/**
	 * Converte um {@link StockDebitItemEventInput} (input da aplicação) em um
	 * evento {@link StockDebitItemEvent} (evento de domínio).
	 *
	 * @param input
	 *            Input da aplicação representando o item debitado.
	 * @return Um evento {@link StockDebitItemEvent}.
	 */
	public static StockDebitItemEvent toItemEvent(StockDebitItemEventInput input) {
		return StockDebitItemEvent.of(input.productId(), input.name(), input.quantity(), input.unitPrice(),
				input.subtotal(), input.observations());
	}
}
