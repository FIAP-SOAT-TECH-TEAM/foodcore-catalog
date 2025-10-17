package com.soat.fiap.food.core.catalog.core.domain.exceptions;

import com.soat.fiap.food.core.catalog.shared.core.domain.exceptions.BusinessException;

/**
 * Exceção lançada quando uma regra de negócio referente a entidade produto é
 * violada
 */
public class ProductException extends BusinessException {

	public ProductException(String message) {
		super(message);
	}

	public ProductException(String message, Throwable cause) {
		super(message, cause);
	}
}
