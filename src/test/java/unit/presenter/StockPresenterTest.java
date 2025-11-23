package unit.presenter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.soat.fiap.food.core.catalog.core.interfaceadapters.bff.presenter.web.api.StockPresenter;

import unit.fixtures.CatalogFixture;

@DisplayName("StockPresenter - Testes Unitários")
class StockPresenterTest {

	@Test @DisplayName("Deve converter Stock para StockResponse")
	void shouldConvertStockToResponse() {
		// Arrange
		var stock = CatalogFixture.createValidStock();

		// Act
		var response = StockPresenter.toStockResponse(stock);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(stock.getId());
		assertThat(response.getQuantity()).isEqualTo(stock.getQuantity());
	}

	@Test @DisplayName("Deve converter Stock com datas preenchidas")
	void shouldConvertStockWithAuditDates() {
		// Arrange
		var stock = CatalogFixture.createValidStock();

		// Act
		var response = StockPresenter.toStockResponse(stock);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getCreatedAt()).isNotNull();
		assertThat(response.getUpdatedAt()).isNotNull();
	}

	@Test @DisplayName("Deve executar conversão sem exceções")
	void shouldConvertWithoutExceptions() {
		// Arrange
		var stock = CatalogFixture.createValidStock();

		// Act
		var response = StockPresenter.toStockResponse(stock);

		// Assert
		assertThat(response).isNotNull();
		assertThat(response.getQuantity()).isGreaterThan(0);
	}
}
