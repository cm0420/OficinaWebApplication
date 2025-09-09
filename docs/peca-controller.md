# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: peca-controller

### `GET /api/pecas/{id}`

- **operationId:** `getById`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `PecaDto` |


### `PUT /api/pecas/{id}`

- **operationId:** `update`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Request Body**

- `application/json` → ref → `PecaDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `PecaDto` |


### `PUT /api/pecas/{id}/repor-estoque`

- **operationId:** `reporEstoque`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |
| `quantidade` | query | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `PecaDto` |


### `GET /api/pecas`

- **operationId:** `getAll`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `PecaDto`> |


### `POST /api/pecas`

- **operationId:** `create`
**Request Body**

- `application/json` → ref → `PecaDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `PecaDto` |


