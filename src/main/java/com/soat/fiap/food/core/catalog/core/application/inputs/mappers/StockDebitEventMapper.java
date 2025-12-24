package com.soat.fiap.food.core.catalog.core.application.inputs.mappers;

import java.util.List;

import com.soat.fiap.food.core.catalog.core.application.inputs.StockDebitEventInput;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitEvent;
import com.soat.fiap.food.core.catalog.core.domain.events.StockDebitItemEvent;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.events.OrderCreatedEventDto;

/**
 * Classe utilitária responsável por mapear objetos entre eventos de domínio
 * relacionados a pedidos e o DTO de entrada {@link StockDebitEventInput} da
 * aplicação para publicação de eventos de débito de estoque.
 */
public class StockDebitEventMapper {

	/**
	 * Converte um {@link OrderCreatedEventDto} (evento de criação de pedido) em um
	 * {@link StockDebitEventInput} (input da aplicação).
	 * <p>
	 * Usado para publicar o evento de domínio de débito de estoque.
	 *
	 * @param event
	 *            Evento de criação de pedido.
	 * @return Um objeto {@link StockDebitEventInput} representando os dados do
	 *         débito de estoque.
	 */
	public static StockDebitEventInput toInput(OrderCreatedEventDto event) {
		return new StockDebitEventInput(event.getId(), event.getOrderNumber(), event.getUserId(),
				event.getTotalAmount(), StockDebitItemEventMapper.toInputList(event.getItems()));
	}

	/**
	 * Converte um {@link StockDebitEventInput} (input da aplicação) em um evento
	 * {@link StockDebitEvent}, mapeando seus atributos e seus itens.
	 *
	 * @param input
	 *            O input da aplicação a ser convertido.
	 * @return Um evento {@link StockDebitEvent} com os dados do débito de estoque.
	 */
	public static StockDebitEvent toEvent(StockDebitEventInput input) {
		List<StockDebitItemEvent> itemEvents = input.items()
				.stream()
				.map(StockDebitItemEventMapper::toItemEvent)
				.toList();

		return StockDebitEvent.of(input.orderId(), input.orderNumber(), input.userId(), input.totalAmount(),
				itemEvents);
	}

}
