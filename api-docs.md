# 📄 Documentação da API

**Oficina API** — v1.0

Documentação interativa da API Oficina usando Swagger UI

## 🌐 Servidores

- `http://localhost:8001` — Generated server url

## 🔀 Endpoints

### `GET /api/pecas/{id}`

- **operationId:** `getById`
- **tags:** `peca-controller`

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
- **tags:** `peca-controller`

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
- **tags:** `peca-controller`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |
| `quantidade` | query | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `PecaDto` |


### `PUT /api/os/{id}/iniciar-servico`

- **operationId:** `iniciarServico`
- **tags:** `ordem-de-servico-controller`

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
- **tags:** `ordem-de-servico-controller`

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
- **tags:** `ordem-de-servico-controller`

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
- **tags:** `ordem-de-servico-controller`

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
- **tags:** `ordem-de-servico-controller`

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


### `GET /api/funcionarios/cpf/{cpf}`

- **operationId:** `getByCpf`
- **tags:** `funcionario-controller`

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
- **tags:** `funcionario-controller`

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
- **tags:** `funcionario-controller`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | - |


### `GET /api/clientes/cpf/{cpf}`

- **operationId:** `getClienteByCpf`
- **tags:** `cliente-controller`

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
- **tags:** `cliente-controller`

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
- **tags:** `cliente-controller`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | - |


### `GET /api/carros/{id}`

- **operationId:** `getById_1`
- **tags:** `carro-controller`

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
- **tags:** `carro-controller`

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
- **tags:** `carro-controller`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | - |


### `GET /api/agendamento/{id}`

- **operationId:** `getById_2`
- **tags:** `agendamento-controller`

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
- **tags:** `agendamento-controller`

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
- **tags:** `agendamento-controller`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | integer | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | - |


### `POST /auth/login`

- **operationId:** `login`
- **tags:** `authentication-controller`
**Request Body**

- `application/json` → ref → `LoginRequestDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `LoginResponseDto` |


### `POST /auth/changepassword`

- **operationId:** `mudarSenha`
- **tags:** `authentication-controller`
**Request Body**

- `application/json` → ref → `ChangePasswordDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → string |


### `POST /api/ponto/saida`

- **operationId:** `baterPontoSaida`
- **tags:** `ponto-controller`
**Request Body**

- `application/json` → ref → `PontoRequestDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `SuccessResponseDto` |


### `POST /api/ponto/entrada`

- **operationId:** `baterPontoEntrada`
- **tags:** `ponto-controller`
**Request Body**

- `application/json` → ref → `PontoRequestDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `SuccessResponseDto` |


### `GET /api/pecas`

- **operationId:** `getAll`
- **tags:** `peca-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `PecaDto`> |


### `POST /api/pecas`

- **operationId:** `create`
- **tags:** `peca-controller`
**Request Body**

- `application/json` → ref → `PecaDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `PecaDto` |


### `GET /api/os`

- **operationId:** `getAll_1`
- **tags:** `ordem-de-servico-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `OrdemDeServicoDto`> |


### `POST /api/os`

- **operationId:** `create_1`
- **tags:** `ordem-de-servico-controller`
**Request Body**

- `application/json` → ref → `OrdemDeServicoCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


### `GET /api/funcionarios`

- **operationId:** `getAll_2`
- **tags:** `funcionario-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<-> |


### `POST /api/funcionarios`

- **operationId:** `create_2`
- **tags:** `funcionario-controller`
**Request Body**

- `application/json` → ref → `FuncionarioCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `FuncionarioDto` |


### `POST /api/financeiro/pagar-salarios`

- **operationId:** `pagarSalarios`
- **tags:** `financeiro-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → string |


### `GET /api/clientes`

- **operationId:** `getAll_3`
- **tags:** `cliente-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `ClienteDto`> |


### `POST /api/clientes`

- **operationId:** `create_3`
- **tags:** `cliente-controller`
**Request Body**

- `application/json` → ref → `ClienteCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `ClienteDto` |


### `GET /api/carros`

- **operationId:** `getAll_4`
- **tags:** `carro-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `CarroDto`> |


### `POST /api/carros`

- **operationId:** `create_4`
- **tags:** `carro-controller`
**Request Body**

- `application/json` → ref → `CarroCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `CarroDto` |


### `GET /api/agendamento`

- **operationId:** `getAll_5`
- **tags:** `agendamento-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `AgendamentoDto`> |


### `POST /api/agendamento`

- **operationId:** `create_5`
- **tags:** `agendamento-controller`
**Request Body**

- `application/json` → ref → `AgendamentoCreateDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `AgendamentoDto` |


### `POST /api/agendamento/mecanico/{cpf}/puxar-proximo`

- **operationId:** `puxarProximoAgendamento`
- **tags:** `agendamento-controller`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `cpf` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `AgendamentoDto` |


### `GET /docs/download-md`

- **operationId:** `downloadDocs`
- **tags:** `docs-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → string (binary) |


### `GET /auth/me`

- **operationId:** `me`
- **tags:** `authentication-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → object |


### `GET /api/ponto/resumo/{cpf}/{ano}/{mes}`

- **operationId:** `getResumoMensal`
- **tags:** `ponto-controller`

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
- **tags:** `ponto-controller`

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
- **tags:** `ponto-controller`

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


### `GET /api/os/{id}`

- **operationId:** `getById_3`
- **tags:** `ordem-de-servico-controller`

**Parâmetros**

| Nome | In | Tipo | Obrigatório |
|---|---|---|---|
| `id` | path | string | sim |

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `OrdemDeServicoDto` |


### `GET /api/financeiro/relatorio/mensal/{ano}/{mes}`

- **operationId:** `getRelatorioMensal_1`
- **tags:** `financeiro-controller`

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
- **tags:** `financeiro-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → array<ref → `RegistroFinanceiroDto`> |


### `GET /api/financeiro/holerite/{id}/{ano}/{mes}`

- **operationId:** `getHolerite`
- **tags:** `financeiro-controller`

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
- **tags:** `financeiro-controller`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → object |


## 🧩 Schemas

### PecaDto

| Propriedade | Tipo |
|---|---|
| `id_produto` | string |
| `nome` | string |
| `preco` | number |
| `quantidade` | integer (int32) |
| `fornecedor` | string |


### CarroDto

| Propriedade | Tipo |
|---|---|
| `id_carro` | string |
| `chassi` | string |
| `placa` | string |
| `fabricante` | string |
| `modelo` | string |
| `dono` | ref → `ClienteDto` |


### ClienteDto

| Propriedade | Tipo |
|---|---|
| `id_cliente` | string |
| `cpf` | string |
| `nome` | string |
| `telefone` | string |
| `endereco` | string |
| `email` | string |


### FuncionarioDto

| Propriedade | Tipo |
|---|---|
| `id_usuario` | string |
| `cpf` | string |
| `nome` | string |
| `cargo` | string |
| `telefone` | string |
| `endereco` | string |
| `email` | string |


### OrdemDeServicoDto

| Propriedade | Tipo |
|---|---|
| `numero_os` | string |
| `defeito_relatado` | string |
| `data_abertura` | string (date-time) |
| `data_fechamento` | string (date-time) |
| `status` | string |
| `cliente` | ref → `ClienteDto` |
| `carro` | ref → `CarroDto` |
| `mecanico` | ref → `FuncionarioDto` |
| `pecasUtilizadas` | array<ref → `PecaUtilizadaDto`> |
| `valorTotal` | number |


### PecaUtilizadaDto

| Propriedade | Tipo |
|---|---|
| `id` | integer (int64) |
| `nomePeca` | string |
| `quantidade_utilizada` | integer (int32) |
| `preco_no_momento_do_uso` | number |
| `subtotal` | number |


### AgendamentoDto

| Propriedade | Tipo |
|---|---|
| `id` | integer (int64) |
| `data_hora` | string (date-time) |
| `tipo_servico` | string |
| `cliente` | ref → `ClienteDto` |
| `carro` | ref → `CarroDto` |
| `mecanico` | ref → `FuncionarioDto` |


### LoginRequestDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |
| `senha` | string |


### LoginResponseDto

| Propriedade | Tipo |
|---|---|
| `token` | string |


### ChangePasswordDto

| Propriedade | Tipo |
|---|---|
| `senhaAtual` | string |
| `novaSenha` | string |


### PontoRequestDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |


### SuccessResponseDto

| Propriedade | Tipo |
|---|---|
| `timestamp` | string (date-time) |
| `message` | string |


### OrdemDeServicoCreateDto

| Propriedade | Tipo |
|---|---|
| `agendamentoId` | integer (int64) |
| `defeitoRelatado` | string |


### FuncionarioCreateDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |
| `nome` | string |
| `senha` | string |
| `cargo` | string |
| `telefone` | string |
| `endereco` | string |
| `email` | string |


### ClienteCreateDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |
| `nome` | string |
| `telefone` | string |
| `endereco` | string |
| `email` | string |


### CarroCreateDto

| Propriedade | Tipo |
|---|---|
| `chassi` | string |
| `placa` | string |
| `fabricante` | string |
| `modelo` | string |
| `cpfDono` | string |


### AgendamentoCreateDto

| Propriedade | Tipo |
|---|---|
| `data` | string (date-time) |
| `tipo_servico` | string |
| `id_cliente` | string |
| `id_carro` | string |
| `id_mecanico` | string |


### ResumoPontoDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |
| `ano` | integer (int32) |
| `mes` | integer (int32) |
| `diasTrabalhados` | integer (int64) |
| `horasTrabalhadas` | integer (int64) |
| `horasExtras` | integer (int64) |


### PontoDto

| Propriedade | Tipo |
|---|---|
| `entrada` | string (date-time) |
| `saida` | string (date-time) |
| `horasTrabalhadas` | integer (int64) |


### RelatorioMensalDto

| Propriedade | Tipo |
|---|---|
| `ano` | integer (int32) |
| `mes` | integer (int32) |
| `funcionarios` | array<ref → `ResumoPontoDto`> |
| `totalDiasTrabalhados` | integer (int64) |
| `totalHorasTrabalhadas` | integer (int64) |
| `totalHorasExtras` | integer (int64) |
| `custoTotalSalarios` | number |
| `custoHorasExtras` | number |
| `custoFinalFolha` | number |


### RegistroFinanceiroDto

| Propriedade | Tipo |
|---|---|
| `id` | integer (int64) |
| `descricao` | string |
| `valor` | number |
| `tipo` | string |
| `data` | string (date-time) |


### HoleriteDto

| Propriedade | Tipo |
|---|---|
| `funcionario` | string |
| `cpf` | string |
| `ano` | integer (int32) |
| `mes` | integer (int32) |
| `salarioBase` | number |
| `diasTrabalhados` | integer (int64) |
| `horasTrabalhadas` | integer (int64) |
| `horasExtras` | integer (int64) |
| `valorHorasExtras` | number |
| `salarioLiquido` | number |


