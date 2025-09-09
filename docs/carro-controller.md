# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: carro-controller

### `GET /api/carros/{id}`

- **operationId:** `getById_1`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `CarroDto` |


### `PUT /api/carros/{id}`

- **operationId:** `update_2`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Request Body**

- `application/json` → ref → `CarroDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `CarroDto` |


### `DELETE /api/carros/{id}`

- **operationId:** `delete_1`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | - |


### `GET /api/carros`

- **operationId:** `getAll_4`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `CarroDto`> |


### `POST /api/carros`

- **operationId:** `create_4`
**Request Body**

- `application/json` → ref → `CarroCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `CarroDto` |


