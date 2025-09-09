# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: ordem-de-servico-controller

### `PUT /api/os/{id}/iniciar-servico`

- **operationId:** `iniciarServico`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


### `PUT /api/os/{id}/iniciar-inspecao`

- **operationId:** `iniciarInspecao`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


### `PUT /api/os/{id}/finalizar-servico`

- **operationId:** `finalizarServico`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


### `PUT /api/os/{id}/cancelar`

- **operationId:** `cancelarOS`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


### `PUT /api/os/{id}/adicionar-peca`

- **operationId:** `adicionarPeca`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |
| `pecaId` | query | string | sim |
| `quantidade` | query | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


### `GET /api/os`

- **operationId:** `getAll_1`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `OrdemDeServicoDto`> |


### `POST /api/os`

- **operationId:** `create_1`
**Request Body**

- `application/json` → ref → `OrdemDeServicoCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


### `GET /api/os/{id}`

- **operationId:** `getById_3`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


