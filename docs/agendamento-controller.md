# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: agendamento-controller

### `GET /api/agendamento/{id}`

- **operationId:** `getById_2`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `AgendamentoDto` |


### `PUT /api/agendamento/{id}`

- **operationId:** `update_3`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | integer | sim |

**Request Body**

- `application/json` → ref → `AgendamentoDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `AgendamentoDto` |


### `DELETE /api/agendamento/{id}`

- **operationId:** `delete_2`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | - |


### `GET /api/agendamento`

- **operationId:** `getAll_5`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `AgendamentoDto`> |


### `POST /api/agendamento`

- **operationId:** `create_5`
**Request Body**

- `application/json` → ref → `AgendamentoCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `AgendamentoDto` |


### `POST /api/agendamento/mecanico/{cpf}/puxar-proximo`

- **operationId:** `puxarProximoAgendamento`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `AgendamentoDto` |


