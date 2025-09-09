# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: funcionario-controller

### `GET /api/funcionarios/cpf/{cpf}`

- **operationId:** `getByCpf`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `FuncionarioDto` |


### `PUT /api/funcionarios/cpf/{cpf}`

- **operationId:** `update_1`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Request Body**

- `application/json` → ref → `FuncionarioDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `FuncionarioDto` |


### `DELETE /api/funcionarios/cpf/{cpf}`

- **operationId:** `delete`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | - |


### `GET /api/funcionarios`

- **operationId:** `getAll_2`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<-> |


### `POST /api/funcionarios`

- **operationId:** `create_2`
**Request Body**

- `application/json` → ref → `FuncionarioCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `FuncionarioDto` |


