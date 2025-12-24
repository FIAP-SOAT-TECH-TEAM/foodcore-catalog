package integration.bdd.addProductToCategory.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.requests.ProductRequest;

import integration.bdd.common.config.CucumberSpringConfiguration;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

/**
 * Steps BDD responsáveis por validar a adição de um novo produto em uma
 * categoria específica de um catálogo.
 *
 * Executa chamadas reais à API para criação de produtos.
 */
public class AdicionarProdutoCategoriaSteps extends CucumberSpringConfiguration {

	private ProductRequest productRequest;
	private Response response;

	/**
	 * Recebe os dados necessários para criar um novo produto.
	 *
	 * @param dataTable
	 *            tabela contendo os atributos do produto.
	 */
	@Dado("que um produto possua as seguintes informações:")
	public void queUmProdutoPossuaAsSeguintesInformacoes(DataTable dataTable) {
		var values = dataTable.asMap(String.class, String.class);

		productRequest = new ProductRequest(Long.valueOf(values.get("categoryId")), values.get("name"),
				values.get("description"), new BigDecimal(values.get("price")),
				Integer.valueOf(values.get("stockQuantity")), Integer.valueOf(values.get("displayOrder")));
	}

	/**
	 * Envia a requisição de criação de um novo produto.
	 */
	@Quando("o administrador solicitar a criação do produto")
	public void oAdministradorSolicitarACriacaoDoProduto() {
		var catalogId = 1L;

		response = given().header("Content-Type", "multipart/form-data")
				.header("Auth-Role", "ADMIN")
				.multiPart("data", productRequest, "application/json")
				.when()
				.post("/" + catalogId + "/categories/products")
				.then()
				.extract()
				.response();
	}

	/**
	 * Verifica se o produto foi criado com sucesso.
	 */
	@Então("o produto deve ser criado com sucesso")
	public void oProdutoDeveSerCriadoComSucesso() {
		response.then().statusCode(201);
		response.then().body("id", notNullValue());
	}

	/**
	 * Verifica se o produto foi associado à categoria correta.
	 *
	 * @param price
	 *            Preço do produto.
	 */
	@Então("o produto deve ter o preço {bigdecimal}")
	public void oProdutoDeveTerOPreco(BigDecimal price) {
		var rawPriceObtained = response.jsonPath().getString("price");
		var priceObtained = new BigDecimal(rawPriceObtained);
		assertThat(priceObtained).isEqualByComparingTo(price);
	}

	/**
	 * Verifica se o produto está ativo.
	 *
	 */
	@Então("o produto deve estar ativo")
	public void oProdutoDeveEstarAtivo() {
		response.then().body("active", is(true));
	}

	/**
	 * Verifica se o produto está inativo.
	 *
	 */
	@Então("o produto deve estar inativo")
	public void oProdutoDeveEstarInativo() {
		response.then().body("active", is(false));
	}

	/**
	 * Verifica se a resposta possui o nome correto do produto.
	 *
	 * @param nome
	 *            nome esperado do produto.
	 */
	@Então("o retorno deve conter o nome {string}")
	public void oRetornoDeveConterONome(String nome) {
		response.then().body("name", equalTo(nome));
	}
}
