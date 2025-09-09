# Oficina API — v1.0

Documentação interativa da API Oficina usando Swagger UI

## Controller: authentication-controller

### `POST /auth/login`

- **operationId:** `login`
**Request Body**

- `application/json` → ref → `LoginRequestDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → ref → `LoginResponseDto` |


### `POST /auth/changepassword`

- **operationId:** `mudarSenha`
**Request Body**

- `application/json` → ref → `ChangePasswordDto`

**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → string |


### `GET /auth/me`

- **operationId:** `me`
**Responses**

| Código | Schema |
|---|---|
| 200 | `*/*` → object |


