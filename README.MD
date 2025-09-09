# 🚗 Oficina API

[![API Prod](https://img.shields.io/badge/API-Prod-green)](https://apioficina.flipafile.com)
[![Swagger UI](https://img.shields.io/badge/Swagger-UI-blue)](https://apioficina.flipafile.com/swagger-ui.html)
[![OpenAPI JSON](https://img.shields.io/badge/OpenAPI-JSON-orange)](https://apioficina.flipafile.com/v3/api-docs)
[![Docs MD](https://img.shields.io/badge/Docs-Markdown-lightgrey)](https://apioficina.flipafile.com/docs/download-md)

Backend em **Spring Boot (JDK 24)** para gestão de oficina mecânica.  
A aplicação está conteinerizada via **Dockerfile** e usa **PostgreSQL** como banco de dados.

---

## 🌐 Deploy

- **Produção:** [https://apioficina.flipafile.com](https://apioficina.flipafile.com)  
- **Swagger UI:** [https://apioficina.flipafile.com/swagger-ui.html](https://apioficina.flipafile.com/swagger-ui.html)  
- **OpenAPI JSON:** [https://apioficina.flipafile.com/v3/api-docs](https://apioficina.flipafile.com/v3/api-docs)  
- **Documentação Markdown:**  
  - [api-docs.md](https://apioficina.flipafile.com/docs/download-md)  
  - [docs.zip](https://apioficina.flipafile.com/docs/download-md-all)

---

## 📦 Tecnologias

- [Spring Boot 3.x](https://spring.io/projects/spring-boot)
- [JDK 24](https://jdk.java.net/24/)
- [PostgreSQL](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)
- [Spring Security + JWT] para autenticação
- [OpenAPI / Swagger UI](https://swagger.io/tools/swagger-ui/) para documentação

## ▶️ Como rodar

### Pré-requisitos

- Docker e Docker Compose instalados
- Porta `8001` disponível (API)
- Porta `5432` disponível (Postgres)

### Passos

```bash
# clone o projeto
git clone https://github.com/cm0420/OficinaWebApplication.git
cd OficinaWebApplication/oficina

# build da imagem
docker build -t oficina:latest .

# rodar container da app + postgres (exemplo)
docker run --rm -p 8001:8001   -e DB_URL=jdbc:postgresql://host.docker.internal:5432/postgres   -e DB_USER=postgres   -e DB_PASSWORD=postgres   oficina:latest
```

> ⚠️ ajuste `DB_URL`, `DB_USER`, `DB_PASSWORD` conforme seu ambiente.

---

## 📖 Documentação

- **Swagger UI (interativo):**  
  [APIURL/swagger-ui.html](APIURL/swagger-ui.html)

- **OpenAPI JSON:**  
  [APIURL/v3/api-docs](APIURL/v3/api-docs)

- **Markdown gerado automaticamente:**  
  O sistema gera arquivos `.md` no diretório [`/docs`](./docs), incluindo:

  - [00-schemas.md](./docs/00-schemas.md)
  - [agendamento-controller.md](./docs/agendamento-controller.md)
  - [authentication-controller.md](./docs/authentication-controller.md)
  - [carro-controller.md](./docs/carro-controller.md)
  - [cliente-controller.md](./docs/cliente-controller.md)
  - [docs-controller.md](./docs/docs-controller.md)
  - [financeiro-controller.md](./docs/financeiro-controller.md)
  - [funcionario-controller.md](./docs/funcionario-controller.md)
  - [ordem-de-servico-controller.md](./docs/ordem-de-servico-controller.md)
  - [peca-controller.md](./docs/peca-controller.md)
  - [ponto-controller.md](./docs/ponto-controller.md)

Esses arquivos podem ser baixados também em:

- `APIURL/docs/download-md` → único `.md` agregado
- `APIURL/docs/download-md-all` → `.zip` com todos os arquivos separados

---

## 📂 Estrutura de pastas

```
src/main/java/miguel/projetos/oficina/
 ├── config/          # Configurações de segurança, etc.
 ├── controller/      # Controllers REST
 ├── dto/             # DTOs usados nos endpoints
 ├── entity/          # Entidades JPA
 ├── repository/      # Repositórios Spring Data JPA
 ├── service/         # Regras de negócio
 └── docs/            # Geração de documentação OpenAPI
```

---

## 📜 Licença

Este projeto é de uso acadêmico/profissional. Ajuste a licença conforme necessário.
