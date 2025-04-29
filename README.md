# ⚔️ Desafio POO em Java: CRUD e Batalhas de Personagens do Universo Naruto 

## 📖 Descrição

Projeto desenvolvido como parte de um desafio de Programação Orientada a Objetos (POO), utilizando o universo do anime Naruto para aplicar os conceitos de herança, polimorfismo, encapsulamento e interfaces.



## 📌 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security com JWT
- Jakarta Bean Validation
- Flyway (migrations de banco)
- H2 Database (para testes)
- PostgreSQL (via Docker)
- Docker & Docker Compose
- WebSocket
- STOMP (Simple Text Oriented Messaging Protocol)
- Spring Security com JWT
-  Application Event Listeners (Eventos customizados)

---

---

## ⚡ Desafio Naruto 1.0 ⚡
### Pontos Principais: 
- Apresentação dos personagens do anime Naruto para demonstrar como herança e interfaces podem ser usadas para organizar classes relacionadas.
- CRUD completo de personagens do universo Naruto, com inserção de jutsus.
- Implementação da interface `Ninja`, com as classes `NinjaDeTaijutsu`, `NinjaDeNinjutsu` e `NinjaDeGenjutsu`, cada uma com comportamentos específicos nos métodos `usarJutsu()` e `desviar()`.
- CRUD de usuários com criptografia de senha e autenticação via JWT.
- Criação do endpoint de ações dos ninjas para demonstrar o poder da herança: ao identificar o tipo de jutsu do personagem (Taijutsu, Ninjutsu ou Genjutsu), a aplicação instancia automaticamente a classe correspondente (`NinjaDeTaijutsu`, `NinjaDeNinjutsu` ou `NinjaDeGenjutsu`), permitindo que herdem os métodos da classe base e sobrescrevam (`override`) os métodos para comportamentos específicos de ataque.


  
### 🎯 Funcionalidades

#### CRUD de Personagens
- [x] Criar personagem  
- [x] Listar personagens com paginação e ordenação  
- [x] Filtrar por nome, idade, idade mínima, idade máxima, jutsu, aldeia e chakra  
- [x] Buscar personagem por ID  
- [x] Atualizar personagem  
- [x] Excluir personagem  

#### CRUD de Usuários
- [x] Registrar usuário  
- [x] Listar usuários com paginação  
- [x] Buscar usuário por ID  
- [x] Atualizar usuário  
- [x] Excluir usuário  

#### Autenticação & Autorização
- [x] Login de usuários com JWT  

#### Ações de Ninja ⚠️ 
- [x] `GET /api/v1/personagens/acoes/{id}/atacar`  
- [x] `GET /api/v1/personagens/acoes/{id}/defender`

##### ⚠️ Esta seção de ações está desatualizada. Consulte o Desafio 2.0 para ver a nova implementação de batalhas.
---




### 📑 Exemplos de Interação com a API (JSON)

#### 1. Criar Personagem


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

#### 2. Listar Personagens (Paginação)


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

#### 3. Criar Usuário

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

#### 4. Ações de Ninja

##### Defender

```bash
GET /api/v1/personagens/acoes/1/defender
Authorization: Bearer <TOKEN>
```

```json
{ "message": "Naruto está desviando de um ataque utilizando Ninjutsu!" }
```

##### Atacar

```bash
GET /api/v1/personagens/acoes/1/atacar
Authorization: Bearer <TOKEN>
```

```json
{ "message": "Naruto está usando um jutsu de Ninjutsu!" }
```

---

#### 5. Login

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
##### ⚠️ Esta seção de ações está desatualizada. Consulte o Desafio 2.0 para ver a nova implementação de batalhas.

## ⚡ Desafio Naruto 2.0 - A batalha  ⚡

### Pontos Principais: 
- Implementação de batalhas entre personagens, permitindo que ninjas compitam entre si em tempo real.
- Cada batalha registra ataques, defesas e o uso de jutsus, simulando confrontos emocionantes com base nos atributos dos personagens.

### ✨ Algumas das Alterações em Relação ao Projeto 1.0

#### 🔹 Reformulações na Entidade Personagem
- A entidade `Personagem` passou por importantes reformulações:
  - Os jutsus agora são organizados em um `Map<String, Jutsu>`, onde a chave é o nome do jutsu e o valor é sua instância.
  - O atributo `chakra` de todos os personagens foi fixado em **100**.
  - Foi adicionado o atributo `vida`, iniciado com valor **5** para todos.
  - Foi criada a nova coluna `especialidade`, baseada em um enum com os valores `'TAIJUTSU'`, `'NINJUTSU'` e `'GENJUTSU'`.

#### 🔹 Mudanças na Gestão de Jutsus
- Na versão 1.0, a criação dos jutsus era feita automaticamente no momento da criação de um personagem, caso o jutsu não existisse.
- Agora, na versão 2.0, todos os jutsus são pré-cadastrados no sistema, não sendo mais possível criá-los dinamicamente através dos personagens.  
  Essa decisão foi tomada para garantir maior controle sobre as regras de negócio, evitando que fossem criados jutsus personalizados que pudessem quebrar o equilíbrio do consumo de chakra ou gerar inconsistências no sistema.
- Além disso, os jutsus passaram a possuir novos atributos:
  - `dano`
  - `consumoDeChakra`
  - `categoria` (`'TAIJUTSU'`, `'NINJUTSU'` ou `'GENJUTSU'`)

#### 🔹 Relacionamento entre Personagem e Jutsus
- Antes, os personagens podiam escolher livremente seus jutsus.
- Agora, essa relação é pré-definida: ao escolher a especialidade do personagem, seus jutsus serão automaticamente vinculados apenas à categoria correspondente à especialidade.

#### 🔹 Nova Entidade: Batalha
- Foi criada a entidade `Batalha`, contendo:
  - `id`
  - `criadoEm`
  - `finalizadaEm`
  - `status` (enum: `'NAO_INICIADA'`, `'EM_ANDAMENTO'`, `'FINALIZADA'`)
  - Lista de `ParticipantesBatalha`, representando os dois competidores.

#### 🔹 Nova Entidade: ParticipantesBatalha
- Representa a participação de um personagem em uma batalha, contendo:
  - `id_usuario`
  - `id_personagem`
  - `id_batalha`
  - `player_order` (ordem de ação na batalha, usada nas próximas releases)
  - `vencedor` (indicador se este participante venceu a batalha)

### 📜 Regras de Negócio

#### Jutsus Disponíveis
- O usuário poderá selecionar, para atacar, apenas entre os **Jutsus Disponíveis**.
- Os **Jutsus Disponíveis** correspondem:
  - Aos jutsus que pertencem à especialidade do personagem (por exemplo, personagens de Taijutsu terão apenas jutsus de Taijutsu disponíveis).
- **Regra de Chakra**:
  - Antes de atacar, o sistema verifica se o personagem possui **chakra suficiente** para utilizar o jutsu escolhido.
  - O custo de chakra do jutsu será descontado do chakra atual do personagem.
  - Se não houver chakra suficiente, o personagem não poderá usar o jutsu.
- **Histórico de Jutsus Utilizados**:
  - Um jutsu só poderá ser usado uma vez por personagem durante a batalha.
  - Após ser utilizado, o jutsu ficará registrado e não poderá mais ser selecionado.

#### Ataque
- Antes de permitir um ataque, o sistema verifica se a batalha ainda está em andamento (não finalizada).
- Durante o ataque:
  - O front-end enviará:
    - O ID do personagem atacante,
    - O ID do personagem adversário,
    - E o ID do jutsu selecionado.
- Ao atacar:
  - Será gerada uma mensagem de combate visível para todos os participantes, contendo:
    `Houve um ataque: [nome do personagem],um ninja de [nome da sua classe], usa [nome do jutsu] e causa a perda de [valor de dano do jutsu] chakras no adversário",`
  - O chakra do atacante será reduzido de acordo com o custo do jutsu utilizado.


#### Defesa
- 4 segundos após o ataque, o personagem adversário tentará realizar automaticamente uma defesa.
- A defesa será decidida de forma dinâmica:
  - O sistema sorteará um número aleatório entre 0 e 100.
  - Se **(chakra atual do defensor ÷ 2) ≥ número sorteado**, a defesa será bem-sucedida.
    - Uma mensagem será exibida:  
      `"Defesa: [Nome do Personagem], um ninja de [Classe], desviou do ataque."`
  - Caso contrário:
    - O defensor sofrerá o dano do jutsu:
      - Será descontado o valor do dano no chakra atual.
      - Será também reduzida a vida proporcionalmente:  
        - Para cada 10 pontos de chakra perdidos, o personagem perde 0.5 de vida.


#### Condições de Fim de Jogo
- A batalha termina quando:
  - O chakra **e** a vida de um dos personagens chegarem a **0**.
- O personagem sobrevivente será declarado como vencedor.

### 🛠️ Por Baixo dos Panos

Para a implementação da lógica de batalha, optei por utilizar **WebSocket** com **STOMP**, ao invés de REST puro, visando oferecer uma experiência mais dinâmica e interativa.  
O objetivo principal foi permitir que, durante a batalha, **os dois participantes recebessem atualizações em tempo real** sobre os ataques, defesas, mensagens e status do jogo, sem a necessidade de ficar fazendo polling constante.

Essa abordagem torna possível:
- Enviar eventos de ataque e defesa assim que ocorrem;
- Exibir mensagens de diálogo para ambos os jogadores simultaneamente;
- Atualizar visualmente a interface de forma instantânea (ex: perda de chakra ou vida);


A escolha por WebSocket foi motivada pela necessidade de **sincronia entre as ações dos jogadores** e pela fluidez que esse tipo de aplicação exige, algo que requisições HTTP tradicionais não oferecem com a mesma eficiência.

Além disso, decidi **experimentar a utilização de eventos do próprio Java/Spring**, por meio da publicação de eventos (`ApplicationEventPublisher`) e da escuta desses eventos (`@EventListener`).  
Assim, ao receber uma mensagem via `@MessageMapping` no WebSocket (ex.: um ataque de um personagem), **não apenas uma ação direta é executada**, mas **vários eventos são disparados em sequência**, acionando métodos de forma desacoplada.



## 🐳 Como Clonar e Rodar o Projeto

```bash
git clone https://github.com/DannyCMMarques/Crud-Desafio-Ninja.git
cd demo

docker-compose build
docker-compose up
```

## 📊 Cobertura de Testes

A cobertura inclui:

- **Testes de integração** com JUnit 5 & MockMvc nos controllers de Usuário, Personagem, Login,participanteBatalha e Batalha.
- **Testes unitários** com Mockito.

<p align="center">
    <img src="https://github.com/user-attachments/assets/09a8aa90-2743-45ae-aa9e-4295c954aac1"/>
  </p>

---
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
# 🧪 Como Testar a Aplicação (WebSocket + STOMP)

## 1. Configurações Iniciais

- Realizar o cadastro de um usuário via Swagger.
- Realizar o login para obter o token de autenticação.
- Criar uma batalha.
- Criar dois registros de `ParticipantesBatalha`, associando:
  - O ID da batalha criada,
  - O nome dos usuários,
  - E o nome dos personagens.
- Todas essas ações podem ser feitas através do Swagger UI.

---

## 2. Sobre os Testes com WebSocket

Devido à utilização de **WebSocket com STOMP**, houve uma dificuldade para realizar testes tradicionais, pois ferramentas como Swagger ou Postman, de forma nativa, apenas testam WebSocket puro, não STOMP.

Após uma pesquisa, encontrei a solução no seguinte artigo:  
🔗 [Testing STOMP WebSocket with Postman - dev.to](https://dev.to/danielsc/testing-stomp-websocket-with-postman-218a)

**Para facilitar os testes**, os frames STOMP foram transformados em **binários codificados em Base64**.  
Basta utilizar esses binários no Postman conforme mostrado abaixo.

<p align="center">
  <img src="https://github.com/user-attachments/assets/6e1f9260-fc63-4b8b-a673-00071d99686a" width="400"/>
</p>

---

## 3. Instruções de Teste via Postman

- URL para conectar: `ws://localhost:8080/ws`
- Método: `WebSocket`
- Frame Base64 para conexão:

  <p align="center">
  <img src="https://github.com/user-attachments/assets/551b1711-d4dc-40a9-80fa-67acf8c959e1" width="400"/>
</p>

### 🔹 Conectar (Connect)
```plaintext
Q09OTkVDVA0KQXV0aG9yaXphdGlvbjpCZWFyZXIgZXlKaGJHY2lPaS4uLg0KYWNjZXB0LXZlcnNpb246MS4wLDEuMSwxLjINCmhlYXJ0LWJlYXQ6MzAwMDAsMzAwMDANCg0KAA== 
```
  <p align="center">
  <img src="https://github.com/user-attachments/assets/7bb8b4c9-5830-40e0-8946-0bf90c074866" width="400"/>
</p>

### 🔹 Iniciar Batalha
```plaintext

U0VORA0KZGVzdGluYXRpb246L2FwcC9iYXRhbGhhLzEvc3RhcnQNCg0KAA==
```
  <p align="center">
  <img src="https://github.com/user-attachments/assets/8c35e674-5548-4006-a731-c24db7369eb9" width="400"/>
</p>

### 🔹 Ataque 1 (Personagem 10 ataca 11 com Chidori)
```plaintext
U0VORA0KZGVzdGluYXRpb246L2FwcC9iYXRhbGhhLzEvYXRhcXVlLzEwDQpjb250ZW50LXR5cGU6YXBwbGljYXRpb24vanNvbg0KDQp7ImlkQXRhY2FudGUiOjEwLCJpZERlZmVuc29yIjoxMSwianV0c3VFc2NvbGhpZG8iOiJDaGlkb3JpIn0NCg0KAA==
```

  <p align="center">
  <img src="https://github.com/user-attachments/assets/d4bdd72d-eba1-498e-bafa-5207b4970716" width="400"/>
</p>


### 🔹 Ataque 2 (Personagem 11 ataca 10 com Rede Mental)
```plaintext
U0VORA0KZGVzdGluYXRpb246L2FwcC9iYXRhbGhhLzEvYXRhcXVlLzExDQpjb250ZW50LXR5cGU6YXBwbGljYXRpb24vanNvbg0KDQp7ImlkQXRhY2FudGUiOjExLCJpZERlZmVuc29yIjoxMCwianV0c3VFc2NvbGhpZG8iOiJSZWRlIE1lbnRhbCJ9DQoNCgA=

```
  <p align="center">
  <img src="https://github.com/user-attachments/assets/85c3762e-f252-4919-a249-be6486256379" width="400"/>
</p>

### 🔹 Ataque 3 (Personagem 10 usa Clones das Sombras)

```plaintext

U0VORA0KZGVzdGluYXRpb246L2FwcC9iYXRhbGhhLzEvYXRhcXVlLzEwDQpjb250ZW50LXR5cGU6YXBwbGljYXRpb24vanNvbg0KDQp7ImlkQXRhY2FudGUiOjEwLCJpZERlZmVuc29yIjoxMSwianV0c3VFc2NvbGhpZG8iOiJDbG9uZSBkYXMgU29tYnJhcyJ9DQoNCgA=
```
  <p align="center">
  <img src="https://github.com/user-attachments/assets/3da8b204-c6a3-4be6-bc5c-6944387cc11c" width="400"/>
</p>

### 🔹 Ataque 4 (Personagem 11 usa Hipnose Demoníaca)

```plaintext
U0VORA0KZGVzdGluYXRpb246L2FwcC9iYXRhbGhhLzEvYXRhcXVlLzExDQpjb250ZW50LXR5cGU6YXBwbGljYXRpb24vanNvbg0KDQp7ImlkQXRhY2FudGUiOjExLCJpZERlZmVuc29yIjoxMCwianV0c3VFc2NvbGhpZG8iOiJIaXBub3NlIERlbW9uw61hY2EifQ0KDQoA

```
 <p align="center">
  <img src="https://github.com/user-attachments/assets/210eb9aa-4a85-4aa9-ba29-2e2299654642" width="400"/>
</p>


### 🔹 Fim do Jogo 

 <p align="center">
  <img src="https://github.com/user-attachments/assets/88e6dcfb-2543-45b8-b3a4-1b818daab294" width="400" />
</p>







