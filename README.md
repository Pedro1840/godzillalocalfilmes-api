# Documentação da API do Godzilla Local Filmes

>[!IMPORTANT]
>Disponibizei um arquivo pdf com o nome '**respostas_teste_dev_java.pdf**' na raiz do projeto, com as respostas das atividades restantes referente ao teste que me passaram (Disponibilizei também o pdf do teste que me passaram).

Bem-vindo à documentação da API do Godzilla Local Filmes.

Esta API é um aplicativo Spring Boot criado com Java 17, projetado para gerenciar um serviço local de aluguel de filmes.

A API permite que os usuários se registrem, façam login, aluguem filmes e que os administradores gerenciem usuários e filmes.

>[!NOTE]
> Agradeço a oportunidade, mesmo com meu trabalho atualmente, consegui tempo e esforço durantes as horas vagas para desenvolver e distrubuir a aplicação da melhor forma para os Devs. Obrigado equipe A5Solutions! :)

## 1. Pré-requisitos

Antes de executar o aplicativo, certifique-se de ter o seguinte instalado em seu sistema:

- Java Development Kit (JDK) 17: Baixe o JDK 17
- PostgreSQL
- Maven
- Um IDE (por exemplo, IntelliJ IDEA, Eclipse) ou um editor de texto para edição de código.

- Postman ou qualquer outra ferramenta de teste de API (opcional, mas recomendado).

### 1.1. Configuração do Banco

Estou deixando disponivel o **.env** em */resources* para a utilização do banco, certifique-se que os dados do arquivo **(env)** corresponda as do seu banco **PostgreSQL**.

>[!IMPORTANT]
> Crie o nome do banco exatamente igual ao DB_NAME que esta no arquivo (.env) pois o sistema não cria automáticamente o banco de dados.

## 2. Primeiros passos

### 2.1. Clone o repositório

Clone o repositório do projeto para sua máquina local:

``` bash
https://github.com/Pedro1840/godzillalocalfilmes-api.git
```

### 2.2. Construir o Projeto

Navegue até o diretório do projeto e construa o projeto usando o Maven:

```bash
cd godzillalocalfilmes
mvn clean install
```

Este comando compilará o projeto e baixará todas as dependências necessárias.

### 2.3. Execute o aplicativo

Inicie o aplicativo Spring Boot:

```bash
mvn spring-boot:run
```

Por padrão, o aplicativo será executado na porta 8080. Certifique-se de que essa porta esteja disponível ou modifique o arquivo application.properties para alterar a porta.

## 3. Endpoints da API

Abaixo está uma explicação detalhada dos endpoints da API, suas funcionalidades e como usá-los.

### 3.1. Endpoints de autenticação

Esses endpoints permitem que os usuários se registrem e efetuem login no sistema.

### 3.1.1. Registre um novo usuário

- URL: /usuarios/registrar
- Método: POST
- Descrição: Registra um novo usuário com a função de ROLE_USER.

Request Body:

```json
{
  "nome": "User Name",
  "email": "user@example.com",
  "senha": "password123"
}
```

- Respostas:

  - 200 OK: Usuário registrado com sucesso.
  - 400 Solicitação inválida: E-mail já em uso.

### 3.1.2. Login do usuário

- URL: /usuarios/usuario

- Método: POST

- Descrição: Autentica um usuário e fornece um token JWT.

Request Body:

```json
{
  "email": "user@example.com",
  "senha": "password123"
}
```

- Respostas:
  - 200 OK: Retorna uma resposta de autenticação contendo o token.

```json
{
"auth": true,
"usuario": {
    "id": 1,
    "email": "user@example.com",
    "nome": "User Name"
},
"token": "jwt-token-string"
}
```

- 401 Unauthorized: Senha incorreta.

- 404 Not Found: Usuário não encontrado.

## 3.2. Endpoints do usuário

Esses endpoints são acessíveis a usuários autenticados com ROLE_USER.

### 3.2.1. Alugar um filme

- URL: /godzilla
- Método: POST
- Descrição: Permite que um usuário alugue um filme.
- Cabeçalhos:
  - Authorization: Bearer {jwt-token}
- Parâmetros:
  - clienteId: (Long) ID do usuário autenticado.
  - filmeId: (Long) ID do filme a ser alugado.
- Respostas:
  - 200 OK: Filme alugado com sucesso.
  - 403 Proibido: Se o clienteId não corresponder ao usuário autenticado.
  - 403 Proibido: Se o usuário já tiver um filme alugado ou o filme não estiver disponível.

### 3.2.2. Retornar um filme

- URL: /godzilla/devolver
- Método: POST
- Descrição: Permite que um usuário devolva um filme alugado.
- Cabeçalhos:
  - Authorization: Bearer {jwt-token}
- Parâmetros:
  - clienteId: (Long) ID do usuário autenticado.
- Respostas:
  - 200 OK: Filme retornado com sucesso.
  - 403 Proibido: Se o clienteId não corresponder ao usuário autenticado.
  - 403 Proibido: Se o usuário não tiver nenhum filme alugado.

## 3.3. Endpoints de administração

Esses endpoints são restritos a usuários com ROLE_ADMIN.

### 3.3.1. Criar um novo usuário

- URL: /admin/clientes

- Método: POST

- Descrição: Permite que um administrador crie um novo usuário.

- Cabeçalhos:
  - Authorization: Bearer {jwt-token}

Corpo da solicitação:

```json
{
"nome": "Novo usuário",
"email": "newuser@example.com",
"senha": "password123"
}
```

Respostas:

- 200 OK: Usuário criado com sucesso.
- 400 Solicitação inválida: E-mail já em uso.

### 3.3.2. Atribuir função a um usuário

- URL: /admin/clientes/atribuir-role
- Método: POST
- Descrição: Atribui uma função a um usuário.
- Cabeçalhos:
  - Authorization: Bearer {jwt-token}
- Parâmetros:
  - clienteId: (Long) ID do usuário.
  - role: (String) Nome da função (por exemplo, ROLE_ADMIN).

- Respostas:
  - 200 OK: Função atribuída com sucesso.
  - 404 Não encontrado: Usuário não encontrado.
  - 500 Erro interno do servidor: Função não encontrada.

### 3.3.3. Remover função de um usuário

- URL: /admin/clientes/remover-role
- Método: POST
- Descrição: Remove uma função de um usuário.
- Cabeçalhos:
  - Authorization: Bearer {jwt-token}
- Parâmetros:
  - clienteId: (Long) ID do usuário.
  - role: (String) Nome da função (por exemplo, ROLE_ADMIN).

- Respostas:
  - 200 OK: Função removida com sucesso.
  - 404 Não encontrado: Usuário não encontrado.
  - 500 Erro interno do servidor: Função não encontrada.

### 3.3.4. Excluir um usuário

- URL: /admin/clientes/delete/{id}
- Método: DELETE
- Descrição: Exclui um usuário do sistema.
- Cabeçalhos:
  - Authorization: Bearer {jwt-token}
- Parâmetros:
  - id: (Variável de caminho) ID do usuário.

- Respostas:
  - 200 OK: Usuário excluído com sucesso.
  - 404 Não encontrado: Usuário não encontrado.

## 3.4. Pontos finais do filme

### 3.4.1. Listar todos os filmes

- URL: /localdora/godzilla/todos
- Método: GET
- Descrição: Recupera uma lista de todos os filmes.

- Respostas:
  - 200 OK: Retorna uma lista de filmes.

### 3.4.2. Pesquisar filmes por título

- URL: /localdora/godzilla
- Método: GET
- Descrição: Pesquisa filmes que contenham o título especificado.
- Parâmetros:
  - titulo: (String) Título ou parte do título.
- Respostas:
  - 200 OK: Retorna uma lista de filmes correspondentes.

### 3.4.3. Adicionar um novo filme (somente administrador)

- URL: /localdora/godzilla
- Método: POST
- Descrição: Adiciona um novo filme ao catálogo.
- Cabeçalhos:
  - Authorization: Bearer {jwt-token}

Response Body

```json
{
  "titulo": "Movie Title",
  "diretor": "Director Name",
  "estoque": 10,
  "ano": 2021
}
```

- Resposta:
  - 200 OK: Filme adicionado com sucesso.
  - 403 Forbidden: O usuário não é um administrador.

### 3.5. Pontos de extremidade de aluguel

Os pontos de extremidade de aluguel são abordados na seção Pontos de extremidade do usuário, especificamente:

- Alugar um filme: /godzilla (POST)
- Retornar um filme: /godzilla/devolver (POST)

## 4. Segurança e Funções

A API usa JWT (JSON Web Tokens) para autenticação e autorização.

- Funções:
  - ROLE_USER: Função padrão atribuída a novos usuários.
  - ROLE_ADMIN: Função administrativa com permissões elevadas.
  - Aquisição de Token:
    - Os usuários obtêm um token efetuando login via /usuarios/usuario.

    - O token deve ser incluído no cabeçalho Authorization para endpoints protegidos.

  - Endpoints protegidos:
    - Endpoints anotados com @PreAuthorize("hasRole('ADMIN')") requerem o ROLE_ADMIN.

    - Endpoints sem requisitos de função específicos são acessíveis a usuários autenticados.

## 5. Testando a API - (Bônus)

### 5.1. Usando o Postman

- Registrar um novo usuário:

  - Envie uma solicitação POST para /usuarios/registrar com os detalhes do usuário no corpo.

- Login:

  - Envie uma solicitação POST para /usuarios/usuario com o e-mail e a senha do usuário.
  - Copie o token da resposta.

- Acessar endpoints protegidos:

Para endpoints que exigem autenticação, inclua o cabeçalho Authorization:

```json
Authorization: Bearer {jwt-token}
```

- Ações do Admin:
  - Faça login como um usuário admin para executar ações específicas do administrador.
  - Certifique-se de que o usuário admin tenha o ROLE_ADMIN.

### 5.2. Exemplo de fluxo de trabalho

#### Registrar cliente

```bash
curl -X POST http://localhost:8080/usuarios/registrar \
-H "Content-Type: application/json" \
-d '{"nome":"John Doe", "email":"john@example.com", "senha":"password123"}'
```

#### 5.3. Logar Cliente

```bash
curl -X POST http://localhost:8080/usuarios/usuario \
-H "Content-Type: application/json" \
-d '{"email":"john@example.com", "senha":"password123"}'
```

- Armazene o token da resposta.

#### 5.4. Listar todos filmes

```bash
curl -X POST http://localhost:8080/localdora/godzilla/todos \
-H "Content-Type: application/json" \
-d '{"email":"john@example.com", "senha":"password123"}'
```

#### 5.5. Listar Filmes de acordo com o parametro passado

```bash
curl -X POST http://localhost:8080/localdora/godzilla?titulo= \
-H "Content-Type: application/json" \
-d '{"email":"john@example.com", "senha":"password123"}'
```

#### 5.6. Alugar um Filme

```bash
curl -X POST http://localhost:8080/godzilla \
-H "Authorization: Bearer {jwt-token}" \
-d "clienteId=1&filmeId=2"
```

#### 5.7. Devolver o Filme alugado

```bash
curl -X POST http://localhost:8080/godzilla/devolver \
-H "Authorization: Bearer {jwt-token}" \
-d "clienteId=1"
```

### 5.8 Deletar Cliente

```bash
curl -X POST http://localhost:8080/localdora/godzilla/{id} \
-H "Authorization: Bearer {jwt-token-admin}" \
```

### 5.9 Atribuir Privilegio / Remover Privilegio a um cliente

```bash
curl -X POST http://localhost:8080/admin/clientes/atribuir-role?clienteId=2&role=ROLE_USER \
-H "Authorization: Bearer {jwt-token-admin}" \
```

```bash
curl -X POST http://localhost:8080/admin/clientes/remover-role?clienteId=2&role=ROLE_ADMIN \
-H "Authorization: Bearer {jwt-token-admin}" \
```

- Opções criados automaticamente são (ROLE_USER, ROLE_ADMIN)

## 6. Conclusão

Esta API fornece um sistema robusto para gerenciar aluguéis de filmes, registros de usuários e funções administrativas. Seguindo as instruções fornecidas no PDF, você deve conseguir executar o aplicativo e interagir com todos os endpoints.

Para quaisquer problemas ou assistência adicional, consulte a base de código ou entre em contato comigo.
