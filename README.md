# 🍔 FoodCore Catalog

<div align="center">
  
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-catalog&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-catalog)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-catalog&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-catalog)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-catalog&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-catalog)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-catalog&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-catalog)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-catalog&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-catalog)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=FIAP-SOAT-TECH-TEAM_foodcore-catalog&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=FIAP-SOAT-TECH-TEAM_foodcore-catalog)

</div>


Microsserviço responsável pelo gerenciamento de catálogo de produtos e categorias do sistema FoodCore. Desenvolvido como parte do curso de Arquitetura de Software da FIAP (Tech Challenge).

<div align="center">
  <a href="#visao-geral">Visão Geral</a> •
  <a href="#arquitetura">Arquitetura</a> •
  <a href="#infra">Infraestrutura</a> •
  <a href="#tecnologias">Tecnologias</a> •
  <a href="#debitos-tecnicos">Débitos Técnicos</a> •
  <a href="#instalacao-e-uso">Instalação e Uso</a> •
  <a href="#apis">APIs</a> •
  <a href="#contribuicao">Contribuição</a>
</div><br>

> 📽️ Vídeo de demonstração da arquitetura: [https://www.youtube.com/watch?v=XgUpOKJjqak](https://www.youtube.com/watch?v=XgUpOKJjqak)<br>

---

<h2 id="visao-geral">📋 Visão Geral</h2>

O **FoodCore Catalog** é o microsserviço responsável por:

- **Gerenciamento de Produtos**: CRUD completo de produtos
- **Gerenciamento de Categorias**: Lanches, bebidas, sobremesas, acompanhamentos
- **Upload de Imagens**: Armazenamento de imagens de produtos no Azure Blob
- **Consulta de Catálogo**: APIs para listagem e busca de produtos

### Principais Recursos

| Recurso | Descrição |
|---------|-----------|
| **Produtos** | Criar, editar, listar e remover produtos |
| **Categorias** | Organização por tipo de produto |
| **Imagens** | Upload para Azure Blob Storage |
| **Busca** | Filtros por categoria, nome, preço |

---

<h2 id="arquitetura">🧱 Arquitetura</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### 🎯 Princípios Adotados

- **Clean Architecture**: Domínio independente de frameworks
- **DDD**: Bounded context de catálogo isolado
- **CQRS Light**: Separação de comandos e consultas

---

### ⚙️ Camadas da Arquitetura

| Camada | Componentes |
|--------|-------------|
| **Domínio** | `Product`, `Category`, `Details`, `ImageUrl` |
| **Aplicação** | Use Cases de produtos e categorias |
| **Interface** | Controllers REST, Presenters, Gateways |
| **Infraestrutura** | PostgreSQL, Azure Blob Storage |

---

### 🏗️ Microsserviços do Ecossistema

| Microsserviço | Responsabilidade | Repositório |
|---------------|------------------|-------------|
| **foodcore-auth** | Autenticação (Azure Function + Cognito) | [foodcore-auth](https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-auth) |
| **foodcore-order** | Gerenciamento de pedidos | [foodcore-order](https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-order) |
| **foodcore-payment** | Processamento de pagamentos | [foodcore-payment](https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-payment) |
| **foodcore-catalog** | Catálogo de produtos (este repositório) | [foodcore-catalog](https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-catalog) |

</details>

---

<h2 id="infra">🌐 Infraestrutura</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### Recursos Kubernetes

| Recurso | Descrição |
|---------|-----------|
| **Deployment** | Pods com health probes, limites de recursos |
| **Service** | Exposição interna no cluster |
| **Ingress** | Roteamento: `/api/catalog/*` |
| **ConfigMap** | Configurações não sensíveis |
| **Secrets** | Credenciais (Database, Azure Blob) |
| **HPA** | Escalabilidade automática |

### Integrações

| Serviço | Tipo | Descrição |
|---------|------|-----------|
| **PostgreSQL** | Síncrona | Persistência de dados |
| **Azure Blob Storage** | Síncrona | Armazenamento de imagens |
| **Azure Service Bus** | Assíncrona | Eventos de catálogo |

</details>

---

<h2 id="tecnologias">🔧 Tecnologias</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### Backend
- **Java 21**: Linguagem principal
- **Spring Boot 3.4**: Framework base
- **Spring Data JPA**: Persistência
- **MapStruct / Lombok**: Produtividade

### Banco de Dados
- **PostgreSQL**: Banco relacional
- **Liquibase**: Migrations

### Storage
- **Azure Blob Storage**: Imagens de produtos

### Qualidade
- **SonarCloud**: Análise estática
- **JUnit 5 + Mockito**: Testes unitários
- **Cucumber**: Testes BDD

</details>

---

<h2 id="debitos-tecnicos">⚠️ Débitos Técnicos</h2>

<details>
<summary>Expandir para mais detalhes</summary>

### 🔴 Alta Prioridade

| Débito | Descrição | Impacto |
|--------|-----------|---------|
| **Azure Function de Imagens** | Criar Azure Function para atualização de imagens - remover essa responsabilidade do microsserviço | Separação de responsabilidades |
| **Separar Estoque** | Extrair gerenciamento de estoque para microsserviço dedicado (mantido simples por ora) | Futuro: escalabilidade de estoque |
| **Workload Identity** | Usar Workload Identity para Pods (atual: Azure Key Vault Provider) | Segurança |
| **OpenTelemetry** | Migrar de Zipkin/Micrometer para OpenTelemetry | Padronização |

<h2 id="limitacoes-quota">Limitações de Quota (Azure for Students)</h2>

> A assinatura **Azure for Students** impõe as seguintes restrições:
>
> - **Região**: Brazil South não está disponível. Utilizamos **South Central US** como alternativa
>
> - **Quota de VMs**: Apenas **2 instâncias** do SKU utilizado para o node pool do AKS, tendo um impacto direto na escalabilidade do cluster. Quando o limite é atingido, novos nós não podem ser criados e dão erro no provisionamento de workloads.
>
> ### Erro no CD dos Microsserviços
>
> Durante o deploy dos microsserviços, Pods podem ficar com status **Pending** e o seguinte erro pode aparecer:
>
> <img src=".github/images/error.jpeg" alt="Error" />
>
> **Causa**: O cluster atingiu o limite máximo de VMs permitido pela quota e não há recursos computacionais (CPU/memória) disponíveis nos nós existentes.
>
> **Solução**: Aguardar a liberação de recursos de outros pods e reexecutar CI + CD.

</details>

---

<h2 id="instalacao-e-uso">🚀 Instalação e Uso</h2>

### Pré-requisitos
- Java 21
- Docker e Docker Compose
- Gradle

### Desenvolvimento Local

```bash
# Clonar repositório
git clone https://github.com/FIAP-SOAT-TECH-TEAM/foodcore-catalog.git
cd foodcore-catalog

# Subir dependências
docker-compose -f docker/docker-compose.yml up -d

# Executar aplicação
./gradlew bootRun --args='--spring.profiles.active=local'

# Executar testes
./gradlew test
```

---

<h2 id="apis">📡 APIs</h2>

### Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/catalog/products` | Listar produtos |
| `GET` | `/api/catalog/products/{id}` | Buscar produto por ID |
| `POST` | `/api/catalog/products` | Criar produto |
| `PUT` | `/api/catalog/products/{id}` | Atualizar produto |
| `DELETE` | `/api/catalog/products/{id}` | Remover produto |
| `GET` | `/api/catalog/categories` | Listar categorias |
| `POST` | `/api/catalog/products/{id}/image` | Upload de imagem |

### Documentação
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

---

<h2 id="contribuicao">🤝 Contribuição</h2>

### Fluxo de Deploy

1. Abra um Pull Request
2. Pipeline CI executa testes e análise
3. Após aprovação, merge para `main`
4. Pipeline CD faz deploy no AKS

### Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

---

<div align="center">
  <strong>FIAP - Pós-graduação em Arquitetura de Software</strong><br>
  Tech Challenge
</div>
