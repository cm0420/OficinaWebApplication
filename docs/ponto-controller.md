# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: ponto-controller

### `POST /api/ponto/saida`

- **operationId:** `baterPontoSaida`
**Request Body**

- `application/json` → ref → `PontoRequestDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `SuccessResponseDto` |


### `POST /api/ponto/entrada`

- **operationId:** `baterPontoEntrada`
**Request Body**

- `application/json` → ref → `PontoRequestDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `SuccessResponseDto` |


### `GET /api/ponto/resumo/{cpf}/{ano}/{mes}`

- **operationId:** `getResumoMensal`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |
| `ano` | path | integer | sim |
| `mes` | path | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `ResumoPontoDto` |


### `GET /api/ponto/relatorio/{ano}/{mes}`

- **operationId:** `getRelatorioMensal`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `ano` | path | integer | sim |
| `mes` | path | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `ResumoPontoDto`> |


### `GET /api/ponto/funcionario/{cpf}/{ano}/{mes}`

- **operationId:** `getPontosDoFuncionario`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |
| `ano` | path | integer | sim |
| `mes` | path | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `PontoDto`> |


