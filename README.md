
# ⚔️ Desafio POO em Java: CRUD de Personagens do Universo Naruto

## 📖 Descrição
Projeto desenvolvido como parte de um desafio de Programação Orientada a Objetos (POO). Consiste em:
- CRUD completo de personagens do universo Naruto, com implementação de uma interface `Ninja` e das classes `NinjaDeTaijutsu`, `NinjaDeNinjutsu` e `NinjaDeGenjutsu`, cada uma com seus próprios métodos `usarJutsu()` e `desviar()`.
- CRUD de usuários com criptografia de senha e autenticação via JWT.
---

## 📌 Tecnologias Utilizadas (ordenado)

- Spring Boot
- Docker & Docker Compose
- Flyway (migrations de banco)
- H2 Database (teste)
- Jakarta Bean Validation
- Java 17
- PostgreSQL (via Docker)
- Spring Security com JWT
---
## 📊 Cobertura de Testes

A cobertura inclui:

- **Testes de integração** com JUnit 5 & MockMvc nos controllers de Usuário, Personagem, Login e Ação.
- **Testes unitários** com Mockito nos serviços e validadores.
<p align="center">
    <img src="https://github.com/user-attachments/assets/fbe27a35-5a75-40db-a15d-687de39cfd0c" alt="Cobertura de Testes" width="300"/>
  </p>

---

## 🎯 Funcionalidades

### CRUD de Personagens
- [x] Criar personagem  
- [x] Listar personagens com paginação e ordenação  
- [x] Filtrar por nome, idade, idade mínima, idade máxima, jutsu, aldeia e chakra  
- [x] Buscar personagem por ID  
- [x] Atualizar personagem  
- [x] Excluir personagem  

### CRUD de Usuários
- [x] Registrar usuário  
- [x] Listar usuários com paginação  
- [x] Buscar usuário por ID  
- [x] Atualizar usuário  
- [x] Excluir usuário  

### Autenticação & Autorização
- [x] Login de usuários com JWT  

### Ações de Ninja
- [x] `GET /api/v1/personagens/acoes/{id}/atacar`  
- [x] `GET /api/v1/personagens/acoes/{id}/defender`

---

---

## 🐳 Como Clonar e Rodar o Projeto

```bash
git clone https://github.com/DannyCMMarques/Crud-Desafio-Ninja.git
cd demo

docker-compose build
docker-compose up
```

## 🛠️ Documentação Swagger
1. Acesse: `http://localhost:8080/swagger-ui.html`
2. As únicas rotas sem token são:
   - `POST /api/v1/auth/register` (criar usuário)
   - `POST /api/v1/auth/login` (obter token)
3. Copie o token retornado e clique em **Authorize** no Swagger para liberar os demais endpoints.
<p align="center">
  <img src="https://github.com/user-attachments/assets/ec8e3c62-0745-4232-9836-b5fbee3778b0" alt="Cobertura de Testes" width="500"/>

</p>

---

## 🔥 Exemplos de Uso

### 1. Criar Personagem


**Response**:

```json
{
  "id": 1,
  "nome": "Naruto",
  "idade": 33,
  "aldeia": "Konoha",
  "jutsus": [
    { "id": 1, "tipo": "Ninjutsu" }
  ],
  "chakra": 1000
}
```

---

### 2. Listar Personagens (Paginação)


**Response**:

```json
{
  "content": [
    { "id": 2, "nome": "Sasuke", ... },
    { "id": 1, "nome": "Naruto", ... }
  ],
  "pageable": { "pageNumber": 0, "pageSize": 10 },
  "totalElements": 2,
  ...
}
```

---

### 3. Criar Usuário

```bash
{
  "nome": "Danny",
  "email": "danny@email.com",
  "senha": "minhaSenha123"
}
```

**Response**:

```json
{
  "id": 7,
  "nome": "Danny",
  "email": "danny@email.com",
  "senha": null
}
```

**Header de Location**:
```
Location: http://localhost:8080/api/v1/usuarios/7
```
---

### 4. Ações de Ninja

#### Defender

```bash
GET /api/v1/personagens/acoes/1/defender
Authorization: Bearer <TOKEN>
```

```json
{ "message": "Naruto está desviando de um ataque utilizando Ninjutsu!" }
```

#### Atacar

```bash
GET /api/v1/personagens/acoes/1/atacar
Authorization: Bearer <TOKEN>
```

```json
{ "message": "Naruto está usando um jutsu de Ninjutsu!" }
```

---

### 5. Login

**Response**:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "sub": "danny@email.com",
  "role": "USER",
  "createdAt": "Tue Apr 22 14:37:16 UTC 2025",
  "exp": "2025-04-23T14:37:16.000+00:00",
  "iat": 1745332636
}
```




