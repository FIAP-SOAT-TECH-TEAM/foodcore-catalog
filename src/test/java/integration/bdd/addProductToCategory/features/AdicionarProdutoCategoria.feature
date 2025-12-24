# language: pt
Funcionalidade: Adicionar produto em uma categoria de um dado catálogo
  Como ADMINISTRADOR da cozinha
  Quero adicionar novos produtos em uma categoria de um catálogo específico
  Para que possam ser vendidos

  Contexto:
    Dado que existam categorias de produtos

  Cenário: Adicionar produto em uma categoria de um catálogo com sucesso
    Dado que um produto possua as seguintes informações:
      | categoryId      | 1                     |
      | name            | Burguer Supremo       |
      | description     | Triplo Bacon Cheddar  |
      | price           | 55.95                 |
      | stockQuantity   | 5                     |
      | displayOrder    | 1                     |
    Quando o administrador solicitar a criação do produto
    Então o produto deve ser criado com sucesso
    E o produto deve ter o preço 55,95
    E o produto deve estar ativo
    E o retorno deve conter o nome "Burguer Supremo"

  Cenário: Adicionar produto sem estoque em uma categoria de um catálogo com sucesso
    Dado que um produto possua as seguintes informações:
      | categoryId      | 1                     |
      | name            | Burguer Supremo       |
      | description     | Triplo Bacon Cheddar  |
      | price           | 55.95                 |
      | stockQuantity   | 0                     |
      | displayOrder    | 1                     |
    Quando o administrador solicitar a criação do produto
    Então o produto deve ser criado com sucesso
    E o produto deve ter o preço 55,95
    E o produto deve estar inativo
    E o retorno deve conter o nome "Burguer Supremo"
