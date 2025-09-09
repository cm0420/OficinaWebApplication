# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: cliente-controller

### `GET /api/clientes/cpf/{cpf}`

- **operationId:** `getClienteByCpf`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `ClienteDto` |


### `PUT /api/clientes/cpf/{cpf}`

- **operationId:** `updateCliente`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Request Body**

- `application/json` → ref → `ClienteDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `ClienteDto` |


### `DELETE /api/clientes/cpf/{cpf}`

- **operationId:** `deleteCliente`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | - |


### `GET /api/clientes`

- **operationId:** `getAll_3`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `ClienteDto`> |


### `POST /api/clientes`

- **operationId:** `create_3`
**Request Body**

- `application/json` → ref → `ClienteCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `ClienteDto` |


