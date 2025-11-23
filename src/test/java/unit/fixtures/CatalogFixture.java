package unit.fixtures;

import java.math.BigDecimal;
import java.util.List;

import com.soat.fiap.food.core.catalog.core.application.inputs.CatalogInput;
import com.soat.fiap.food.core.catalog.core.application.inputs.CategoryInput;
import com.soat.fiap.food.core.catalog.core.domain.model.Catalog;
import com.soat.fiap.food.core.catalog.core.domain.model.Category;
import com.soat.fiap.food.core.catalog.core.domain.model.Product;
import com.soat.fiap.food.core.catalog.core.domain.model.Stock;
import com.soat.fiap.food.core.catalog.core.domain.vo.Details;
import com.soat.fiap.food.core.catalog.core.domain.vo.ImageUrl;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.CatalogDTO;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.CategoryDTO;
import com.soat.fiap.food.core.catalog.core.interfaceadapters.dto.ProductDTO;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.requests.CategoryRequest;
import com.soat.fiap.food.core.catalog.infrastructure.in.web.api.dto.requests.ProductRequest;

/**
 * Fixture utilitária para criação de objetos relacionados ao catálogo,
 * categorias e produtos para uso exclusivo em testes unitários.
 * <p>
 * Fornece instâncias de domínio, inputs, requests e DTOs preenchidos com dados
 * válidos ou cenários específicos de teste.
 */
public class CatalogFixture {

	/**
	 * Cria um catálogo válido padrão.
	 *
	 * @return catálogo preenchido
	 */
	public static Catalog createValidCatalog() {
		return new Catalog("Catálogo Principal");
	}

	/**
	 * Cria um catálogo válido com nome customizado.
	 *
	 * @param name
	 *            nome do catálogo
	 * @return catálogo criado
	 */
	public static Catalog createValidCatalog(String name) {
		return new Catalog(name);
	}

	/**
	 * Cria um catálogo contendo duas categorias válidas.
	 *
	 * @return catálogo com categorias
	 */
	public static Catalog createCatalogWithCategories() {
		var catalog = new Catalog("Catálogo com Categorias");

		var category1 = createValidCategory();
		category1.setId(1L);
		catalog.addCategory(category1);

		var category2 = createBeverageCategory();
		category2.setId(2L);
		catalog.addCategory(category2);

		return catalog;
	}

	/**
	 * Cria um catálogo contendo uma categoria e um produto.
	 *
	 * @return catálogo com produtos
	 */
	public static Catalog createCatalogWithProducts() {
		var catalog = new Catalog("Catálogo com Produtos");

		var category = createValidCategory();
		category.setId(1L);
		catalog.addCategory(category);

		var product = createValidProduct();
		product.setId(1L);
		catalog.addProductToCategory(1L, product);

		return catalog;
	}

	/**
	 * Cria um catálogo vazio sem categorias.
	 *
	 * @return catálogo vazio
	 */
	public static Catalog createEmptyCatalog() {
		return new Catalog("Catálogo Vazio");
	}

	/**
	 * Cria um catálogo contendo uma categoria inativa.
	 *
	 * @return catálogo com categoria inativa
	 */
	public static Catalog createCatalogWithInactiveCategory() {
		var catalog = new Catalog("Catálogo com Categoria Inativa");

		var category = createInactiveCategory();
		category.setId(1L);
		catalog.addCategory(category);

		return catalog;
	}

	/**
	 * Cria um catálogo completo contendo categorias e múltiplos produtos.
	 *
	 * @return catálogo completo
	 */
	public static Catalog createCatalogWithMultipleProducts() {
		var catalog = new Catalog("Catálogo Completo");

		var category1 = createValidCategory();
		category1.setId(1L);
		catalog.addCategory(category1);

		var category2 = createBeverageCategory();
		category2.setId(2L);
		catalog.addCategory(category2);

		var product1 = createValidProduct();
		product1.setId(1L);
		catalog.addProductToCategory(1L, product1);

		var product2 = createBeverageProduct();
		product2.setId(2L);
		catalog.addProductToCategory(2L, product2);

		var product3 = createExpensiveProduct();
		product3.setId(3L);
		catalog.addProductToCategory(1L, product3);

		return catalog;
	}

	/**
	 * Cria uma categoria válida padrão.
	 *
	 * @return categoria válida
	 */
	public static Category createValidCategory() {
		return new Category(new Details("Lanches", "Deliciosos lanches artesanais"),
				new ImageUrl("https://example.com/images/lanches.jpg"), 1, true);
	}

	/**
	 * Cria uma categoria do tipo bebidas.
	 *
	 * @return categoria de bebidas
	 */
	public static Category createBeverageCategory() {
		return new Category(new Details("Bebidas", "Bebidas refrescantes e sucos naturais"),
				new ImageUrl("https://example.com/images/bebidas.jpg"), 2, true);
	}

	/**
	 * Cria uma categoria inativa.
	 *
	 * @return categoria inativa
	 */
	public static Category createInactiveCategory() {
		return new Category(new Details("Categoria Inativa", "Categoria desabilitada para testes"),
				new ImageUrl("https://example.com/images/inactive.jpg"), 4, false);
	}

	/**
	 * Cria um produto válido associado a uma categoria.
	 *
	 * @return produto válido
	 */
	public static Product createValidProduct() {
		Product product = new Product(
				new Details("Big Mac",
						"Delicioso hambúrguer com dois hambúrgueres, alface, queijo, molho especial, cebola e picles"),
				new BigDecimal("25.90"), new ImageUrl("https://example.com/images/bigmac.jpg"), 10);

		Category category = createValidCategory();
		category.setId(1L);
		product.setCategory(category);

		var stock = new Stock(10);
		product.setStock(stock);

		return product;
	}

	/**
	 * Cria um produto do tipo bebida.
	 *
	 * @return produto de bebida
	 */
	public static Product createBeverageProduct() {
		Product product = new Product(new Details("Coca-Cola", "Refrigerante Coca-Cola 350ml gelado"),
				new BigDecimal("8.50"), new ImageUrl("https://example.com/images/coca.jpg"), 20);

		Category category = createBeverageCategory();
		category.setId(2L);
		product.setCategory(category);

		var stock = new Stock(20);
		product.setStock(stock);

		return product;
	}

	/**
	 * Cria um produto de valor elevado.
	 *
	 * @return produto premium
	 */
	public static Product createExpensiveProduct() {
		Product product = new Product(new Details("Combo Premium", "Combo gourmet com bebida especial"),
				new BigDecimal("45.00"), new ImageUrl("https://example.com/images/combo-premium.jpg"), 3);

		Category category = createValidCategory();
		category.setId(1L);
		product.setCategory(category);

		var stock = new Stock(3);
		product.setStock(stock);

		return product;
	}

	/**
	 * Cria um produto válido sem imagem cadastrada.
	 *
	 * @return produto sem imagem
	 */
	public static Product createProductWithoutImage() {
		Product product = new Product(new Details("Hambúrguer Simples", "Hambúrguer tradicional"),
				new BigDecimal("15.90"), null, 8);

		Category category = createValidCategory();
		category.setId(1L);
		product.setCategory(category);

		return product;
	}

	/**
	 * Cria um input válido de catálogo.
	 *
	 * @return input de catálogo
	 */
	public static CatalogInput createValidCatalogInput() {
		return new CatalogInput("Novo Catálogo");
	}

	/**
	 * Cria um input que simula conflito de nome do catálogo.
	 *
	 * @return input com nome duplicado
	 */
	public static CatalogInput createConflictingCatalogInput() {
		return new CatalogInput("Catálogo Principal");
	}

	/**
	 * Cria um {@link CategoryInput} válido.
	 *
	 * @param id
	 *            ID
	 * @param name
	 *            nome
	 * @param description
	 *            descrição
	 * @param active
	 *            ativo/inativo
	 * @param displayOrder
	 *            ordem
	 * @return input criado
	 */
	public static CategoryInput createValidCategoryInput(Long id, String name, String description, boolean active,
			int displayOrder) {
		return new CategoryInput(id, name, description, active, displayOrder);
	}

	/**
	 * Cria um {@link CategoryRequest} válido.
	 */
	public static CategoryRequest createValidCategoryRequest(Long id, String name, String description, boolean active,
			int displayOrder) {
		return new CategoryRequest(id, name, description, active, displayOrder);
	}

	/**
	 * Cria um {@link ProductRequest} válido.
	 */
	public static ProductRequest createValidProductRequest(Long categoryId, String name, String description,
			BigDecimal price, int stockQuantity, int displayOrder) {
		return new ProductRequest(categoryId, name, description, price, stockQuantity, displayOrder);
	}

	/**
	 * Clona um {@link ProductDTO} com novo ID.
	 */
	public static ProductDTO createProductDTO(Long id, ProductDTO original) {
		return new ProductDTO(id, original.details(), original.imageUrl(), original.price(), original.stock(),
				original.category(), original.displayOrder(), original.active(), original.createdAt(),
				original.updatedAt());
	}

	/**
	 * Clona um {@link CategoryDTO} com novo ID e lista de produtos.
	 */
	public static CategoryDTO createCategoryDTO(Long id, CategoryDTO original, List<ProductDTO> products) {
		return new CategoryDTO(id, original.details(), original.imageUrl(), original.displayOrder(), original.active(),
				products != null ? products : original.products(), original.createdAt(), original.updatedAt());
	}

	/**
	 * Cria um {@link CatalogDTO} com categorias personalizadas.
	 */
	public static CatalogDTO createCatalogDTO(CatalogDTO original, List<CategoryDTO> categories) {
		return new CatalogDTO(original.id(), original.name(), categories, original.createdAt(), original.updatedAt());
	}

	/**
	 * Cria um {@link Details} válido.
	 */
	public static Details createDetails(String name, String description) {
		return new Details(name, description);
	}

	/**
	 * Cria um {@link ImageUrl} válido.
	 */
	public static ImageUrl createImageUrl(String url) {
		return new ImageUrl(url);
	}

	/**
	 * Cria um estoque válido com ID atribuído.
	 */
	public static Stock createValidStock() {
		var stock = new Stock(10);
		stock.setId(1L);
		return stock;
	}
}
