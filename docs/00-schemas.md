# 🧩 Schemas

## PecaDto

| Propriedade | Tipo |
|---|---|
| `id_produto` | string |
| `nome` | string |
| `preco` | number |
| `quantidade` | integer (int32) |
| `fornecedor` | string |

## CarroDto

| Propriedade | Tipo |
|---|---|
| `id_carro` | string |
| `chassi` | string |
| `placa` | string |
| `fabricante` | string |
| `modelo` | string |
| `dono` | ref → `ClienteDto` |

## ClienteDto

| Propriedade | Tipo |
|---|---|
| `id_cliente` | string |
| `cpf` | string |
| `nome` | string |
| `telefone` | string |
| `endereco` | string |
| `email` | string |

## FuncionarioDto

| Propriedade | Tipo |
|---|---|
| `id_usuario` | string |
| `cpf` | string |
| `nome` | string |
| `cargo` | string |
| `telefone` | string |
| `endereco` | string |
| `email` | string |

## OrdemDeServicoDto

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

## PecaUtilizadaDto

| Propriedade | Tipo |
|---|---|
| `id` | integer (int64) |
| `nomePeca` | string |
| `quantidade_utilizada` | integer (int32) |
| `preco_no_momento_do_uso` | number |
| `subtotal` | number |

## AgendamentoDto

| Propriedade | Tipo |
|---|---|
| `id` | integer (int64) |
| `data_hora` | string (date-time) |
| `tipo_servico` | string |
| `cliente` | ref → `ClienteDto` |
| `carro` | ref → `CarroDto` |
| `mecanico` | ref → `FuncionarioDto` |

## LoginRequestDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |
| `senha` | string |

## LoginResponseDto

| Propriedade | Tipo |
|---|---|
| `token` | string |

## ChangePasswordDto

| Propriedade | Tipo |
|---|---|
| `senhaAtual` | string |
| `novaSenha` | string |

## PontoRequestDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |

## SuccessResponseDto

| Propriedade | Tipo |
|---|---|
| `timestamp` | string (date-time) |
| `message` | string |

## OrdemDeServicoCreateDto

| Propriedade | Tipo |
|---|---|
| `agendamentoId` | integer (int64) |
| `defeitoRelatado` | string |

## FuncionarioCreateDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |
| `nome` | string |
| `senha` | string |
| `cargo` | string |
| `telefone` | string |
| `endereco` | string |
| `email` | string |

## ClienteCreateDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |
| `nome` | string |
| `telefone` | string |
| `endereco` | string |
| `email` | string |

## CarroCreateDto

| Propriedade | Tipo |
|---|---|
| `chassi` | string |
| `placa` | string |
| `fabricante` | string |
| `modelo` | string |
| `cpfDono` | string |

## AgendamentoCreateDto

| Propriedade | Tipo |
|---|---|
| `data` | string (date-time) |
| `tipo_servico` | string |
| `id_cliente` | string |
| `id_carro` | string |
| `id_mecanico` | string |

## ResumoPontoDto

| Propriedade | Tipo |
|---|---|
| `cpf` | string |
| `ano` | integer (int32) |
| `mes` | integer (int32) |
| `diasTrabalhados` | integer (int64) |
| `horasTrabalhadas` | integer (int64) |
| `horasExtras` | integer (int64) |

## PontoDto

| Propriedade | Tipo |
|---|---|
| `entrada` | string (date-time) |
| `saida` | string (date-time) |
| `horasTrabalhadas` | integer (int64) |

## RelatorioMensalDto

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

## RegistroFinanceiroDto

| Propriedade | Tipo |
|---|---|
| `id` | integer (int64) |
| `descricao` | string |
| `valor` | number |
| `tipo` | string |
| `data` | string (date-time) |

## HoleriteDto

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

