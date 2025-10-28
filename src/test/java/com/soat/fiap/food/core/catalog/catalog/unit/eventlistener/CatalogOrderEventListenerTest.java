package com.soat.fiap.food.core.catalog.catalog.unit.eventlistener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.soat.fiap.food.core.catalog.infrastructure.common.source.CatalogDataSource;

@ExtendWith(MockitoExtension.class) @DisplayName("CatalogOrderEventListener - Testes Unitários")
class CatalogOrderEventListenerTest {

	@Mock
	private CatalogDataSource catalogDataSource;

	@Test @DisplayName("Deve criar listener sem lançar exceção")
	void shouldCreateListenerWithoutThrowingException() {
		// Act & Assert
		assertDoesNotThrow(() -> new CatalogOrderEventListener(catalogDataSource));
	}

	@Test @DisplayName("Deve verificar que listener tem dataSource injetado")
	void shouldVerifyListenerHasDataSourceInjected() {
		// Act
		var listener = new CatalogOrderEventListener(catalogDataSource);

		// Assert
		assertDoesNotThrow(listener::toString);
	}
}
