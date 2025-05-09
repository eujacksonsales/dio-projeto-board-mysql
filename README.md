# Projeto Board Kanban Console

Este projeto é um sistema de gerenciamento de boards Kanban, com manipulação de cards, colunas e boards, utilizando **Spring Boot**, **JPA/Hibernate**, **Flyway** para versionamento do banco de dados e um **menu interativo via console**.

---

## Funcionalidades

- **Criação, seleção e exclusão de boards**
- **Gerenciamento de cards**: criar, mover, cancelar, bloquear e desbloquear cards
- **Estrutura de colunas**: cada board possui colunas do tipo INICIAL, FINAL, CANCELAMENTO, etc.
- **Persistência em banco de dados MySQL**
- **Controle de versões do banco via Flyway**
- **Respeito às boas práticas de carregamento de entidades JPA (Lazy/Eager/Join Fetch)**

---

## Tecnologias Utilizadas

- Java 21+
- Spring Boot 3.x
- Spring Data JPA
- Hibernate
- MySQL
- Flyway
- Maven

---

## Principais Soluções Aplicadas

### 1. Versionamento do Banco com Flyway

- Migrations SQL criam as tabelas e constraints.
- O banco de dados deve ser criado manualmente antes de rodar a aplicação.
- O Flyway gerencia apenas o schema (tabelas, índices, etc).

### 2. Estratégia de Geração de IDs

- Usado `@GeneratedValue(strategy = GenerationType.IDENTITY)` para compatibilidade com MySQL (evitando problemas com SEQUENCE).

### 3. Evitar LazyInitializationException

- Utilizado métodos customizados nos repositórios com `JOIN FETCH` para buscar entidades com suas coleções necessárias já carregadas.
- Exemplo:
  ```java
  @Query("SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.colunas WHERE b.id = :id")
  Optional<Board> findByIdWithColunas(@Param("id") Long id);

  @Query("SELECT c FROM Coluna c LEFT JOIN FETCH c.cards WHERE c.id = :id")
  Optional<Coluna> findByIdWithCards(@Param("id") Long id);


### 4. Limitação de MultipleBagFetchException

- Evitado o uso de múltiplos `JOIN FETCH` em coleções do tipo `List`/`Set` na mesma query, seguindo a limitação do Hibernate.
- Carregamento das coleções feito em etapas: primeiro as colunas, depois os cards de cada coluna.

### 5. Menu Interativo via Console

- Implementado com `CommandLineRunner` para iniciar o menu após o boot do Spring.
- O menu principal permite criar, selecionar, excluir boards e gerenciar cards.
- Cada operação busca os dados atualizados do banco antes de manipular, garantindo consistência e evitando erros de sessão fechada.

### 6. Boas Práticas de Engenharia de Software

- Separação clara entre camadas (`model`, `repository`, `service`, `menu`).
- Uso de injeção de dependência do Spring.
- Métodos de serviço e menu bem definidos para cada ação do usuário.