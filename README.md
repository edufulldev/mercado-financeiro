sistema de investimentos e operações de compra e venda de ativos financeiros, como ações. Será um projeto de portfólio que permitirá praticar arquitetura de backend, 
persistência de dados, regras de negócio e mensageria, evoluindo gradualmente.

Projeto: InvestHub
InvestHub — Plataforma de Investimentos
Java 17 · Spring Boot · JPA · PostgreSQL · Docker


O sistema permitirá cadastrar investidores, consultar ativos financeiros, realizar ordens de compra e venda, acompanhar operações e manter um histórico das transações.Investidores
Cadastro e contas
Ativos
Ações e cotações simuladas
Ordens
Compra e venda
Carteira
Saldo e posições

2. Tecnologias
Tecnologia	Utilização
- Java 17	Linguagem da aplicação
- Spring Boot 3.x	Configuração e estrutura da API
- Spring Web	Endpoints REST
- Spring Data JPA / Hibernate	Persistência e mapeamento objeto-relacional
- PostgreSQL	Banco de dados
- Docker Compose	Executar o banco em container
- Maven	Dependências e build
- JUnit 5 e Mockito	Testes unitários
- RabbitMQ	Eventos assíncronos, em uma etapa posterior

Para este projeto, RabbitMQ é suficiente para demonstrar publicação e consumo de eventos, filas, processamento assíncrono e desacoplamento.
Kafka também seria válido, especialmente se quisermos estudar streaming de eventos, alto volume e retenção de mensagens. Não precisamos dos dois ao mesmo tempo.

3. Como vamos aplicar cada conceito
O importante é que cada padrão tenha uma função real na aplicação, em vez de apenas existir no código para cumprir requisito.

SOLID

Separar responsabilidades, depender de abstrações e permitir extensões sem alterar desnecessariamente o código existente.

Exemplo: OrderService não deve implementar diretamente todas as regras de compra, venda e persistência.

Singleton

Compartilhar uma única instância de um componente dentro do contexto Spring.

Exemplo: um serviço de consulta de cotações implementado como bean Spring. O próprio Spring já gerencia beans singleton por padrão; não precisaremos criar um Singleton manual sem necessidade.

Observer e Listener

Notificar componentes interessados quando uma operação relevante acontecer.

Exemplo: ao registrar uma ordem, publicar um evento de domínio. Um listener poderá registrar o histórico ou gerar uma notificação.

Strategy

Permitir diferentes algoritmos por meio de uma interface comum.

Exemplo: estratégias para validar ordens de compra e venda, ou calcular taxas de operação.

DAO e JPA

Isolar o acesso aos dados e mapear entidades Java para tabelas.

Exemplo: InvestorRepository, AssetRepository e OrderRepository, usando Spring Data JPA sobre PostgreSQL.

4. Arquitetura
uma estrutura em camadas, com separação clara entre API, regras de negócio e persistência.

Controller

Recebe requisições HTTP

Service / Use Cases

Executa regras de negócio e aplica Strategy

Domain + Events

Entidades, regras e eventos de domínio

Repository / DAO

Interfaces de acesso a dados com JPA

PostgreSQL no Docker

projeto Spring Boot com estas dependências:
- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Validation
- Spring Boot Actuator
- Spring Boot Test
