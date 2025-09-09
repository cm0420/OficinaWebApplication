# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: financeiro-controller

### `POST /api/financeiro/pagar-salarios`

- **operationId:** `pagarSalarios`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → string |


### `GET /api/financeiro/relatorio/mensal/{ano}/{mes}`

- **operationId:** `getRelatorioMensal_1`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `ano` | path | integer | sim |
| `mes` | path | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `RelatorioMensalDto` |


### `GET /api/financeiro/registros`

- **operationId:** `getRegistros`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `RegistroFinanceiroDto`> |


### `GET /api/financeiro/holerite/{id}/{ano}/{mes}`

- **operationId:** `getHolerite`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |
| `ano` | path | integer | sim |
| `mes` | path | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `HoleriteDto` |


### `GET /api/financeiro/balanco`

- **operationId:** `getBalanco`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → object |


