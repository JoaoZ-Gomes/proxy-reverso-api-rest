# Documentação das Rotas da API REST

## Base URL
```
http://localhost:8080
```

## Endpoints de Transações

### 1. Listar Todas as Transações

**Método:** `GET`  
**Endpoint:** `/transacoes`  
**Descrição:** Retorna uma lista de todas as transações cadastradas.

**Request:**
```bash
curl -X GET http://localhost:8080/transacoes \
  -H "Content-Type: application/json"
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "descricao": "Salário",
    "valor": 3000.00,
    "data": "2026-05-24",
    "tipo": "RECEITA"
  },
  {
    "id": 2,
    "descricao": "Aluguel",
    "valor": 1500.00,
    "data": "2026-05-20",
    "tipo": "DESPESA"
  }
]
```

---

### 2. Criar Nova Transação

**Método:** `POST`  
**Endpoint:** `/transacoes`  
**Descrição:** Cria uma nova transação no banco de dados.

**Request:**
```bash
curl -X POST http://localhost:8080/transacoes \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Supermercado",
    "valor": 250.50,
    "data": "2026-05-24",
    "tipo": "DESPESA"
  }'
```

**Body (JSON):**
```json
{
  "descricao": "string (obrigatório)",
  "valor": "number (obrigatório)",
  "data": "string - formato YYYY-MM-DD (obrigatório)",
  "tipo": "string - RECEITA ou DESPESA (obrigatório)"
}
```

**Response (201 CREATED):**
```json
{
  "id": 3,
  "descricao": "Supermercado",
  "valor": 250.50,
  "data": "2026-05-24",
  "tipo": "DESPESA"
}
```

---

### 3. Buscar Transação por ID

**Método:** `GET`  
**Endpoint:** `/transacoes/{id}`  
**Descrição:** Retorna uma transação específica pelo seu ID.

**Request:**
```bash
curl -X GET http://localhost:8080/transacoes/1 \
  -H "Content-Type: application/json"
```

**URL Params:**
- `id` (number, obrigatório): ID da transação

**Response (200 OK):**
```json
{
  "id": 1,
  "descricao": "Salário",
  "valor": 3000.00,
  "data": "2026-05-24",
  "tipo": "RECEITA"
}
```

**Response (404 NOT FOUND):**
```
Quando o ID não existe, retorna status 404 sem body.
```

---

### 4. Atualizar Transação

**Método:** `PUT`  
**Endpoint:** `/transacoes/{id}`  
**Descrição:** Atualiza uma transação existente.

**Request:**
```bash
curl -X PUT http://localhost:8080/transacoes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Salário Mensal",
    "valor": 3500.00,
    "data": "2026-05-24",
    "tipo": "RECEITA"
  }'
```

**Body (JSON):**
```json
{
  "descricao": "string (obrigatório)",
  "valor": "number (obrigatório)",
  "data": "string - formato YYYY-MM-DD (obrigatório)",
  "tipo": "string - RECEITA ou DESPESA (obrigatório)"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "descricao": "Salário Mensal",
  "valor": 3500.00,
  "data": "2026-05-24",
  "tipo": "RECEITA"
}
```

**Response (404 NOT FOUND):**
```
Quando o ID não existe, retorna status 404 sem body.
```

---

### 5. Deletar Transação

**Método:** `DELETE`  
**Endpoint:** `/transacoes/{id}`  
**Descrição:** Deleta uma transação específica do banco de dados.

**Request:**
```bash
curl -X DELETE http://localhost:8080/transacoes/1 \
  -H "Content-Type: application/json"
```

**URL Params:**
- `id` (number, obrigatório): ID da transação

**Response (204 NO CONTENT):**
```
Sem corpo na resposta. Status 204 indica sucesso na deleção.
```

**Response (404 NOT FOUND):**
```
Quando o ID não existe, retorna status 404.
```

---

## Códigos HTTP Utilizados

| Código | Descrição |
|--------|-----------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado com sucesso |
| 204 | No Content - Deleção bem-sucedida |
| 400 | Bad Request - Dados inválidos |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro no servidor |

---

## Campos da Transação

| Campo | Tipo | Descrição | Obrigatório |
|-------|------|-----------|------------|
| id | Long | Identificador único (gerado automaticamente) | Não |
| descricao | String | Descrição da transação | Sim |
| valor | BigDecimal | Valor da transação | Sim |
| data | LocalDate | Data da transação (formato YYYY-MM-DD) | Sim |
| tipo | String | Tipo: "RECEITA" ou "DESPESA" | Sim |

---

## Exemplos de Uso Completo

### 1. Listar todas as transações
```bash
curl -X GET http://localhost:8080/transacoes
```

### 2. Criar uma transação
```bash
curl -X POST http://localhost:8080/transacoes \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Venda de Produto",
    "valor": 500.00,
    "data": "2026-05-25",
    "tipo": "RECEITA"
  }'
```

### 3. Buscar uma transação
```bash
curl -X GET http://localhost:8080/transacoes/1
```

### 4. Atualizar uma transação
```bash
curl -X PUT http://localhost:8080/transacoes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Venda de Produto - Atualizado",
    "valor": 550.00,
    "data": "2026-05-25",
    "tipo": "RECEITA"
  }'
```

### 5. Deletar uma transação
```bash
curl -X DELETE http://localhost:8080/transacoes/1
```
