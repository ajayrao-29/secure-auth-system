<<<<<<< HEAD
# Secure Authentication & Authorization System

Spring Boot backend implementing JWT authentication, refresh tokens, role-based access control, rate limiting, input validation, BCrypt password hashing, and account lockout.

## Endpoints

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh-token`
- `GET /api/user/profile`
- `GET /api/admin/dashboard`

## Sample Requests

### Register

```json
{
  "username": "john_admin",
  "email": "john@example.com",
  "password": "StrongPass1!"
}
```

### Login

```json
{
  "username": "john_admin",
  "password": "StrongPass1!"
}
```

### Refresh Token

```json
{
  "refreshToken": "2d9d2f2d-1234-4567-89ab-1234567890ab.56d1ecfe-2234-5566-87ab-1234567890ab"
}
```

## Sample Success Response

```json
{
  "accessToken": "<jwt-access-token>",
  "refreshToken": "<refresh-token>",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "username": "john_admin",
  "role": "ROLE_USER"
}
```

## Sample Error Response

```json
{
  "timestamp": "2026-03-28T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/auth/register",
  "details": [
    "password: Password must contain uppercase, lowercase, number, and special character"
  ]
}
```

## Running

1. Set `JWT_SECRET` to a secure Base64-encoded secret of at least 32 bytes.
2. Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.
3. Optionally set `BOOTSTRAP_ADMIN_ENABLED=true` plus `BOOTSTRAP_ADMIN_USERNAME`, `BOOTSTRAP_ADMIN_EMAIL`, and `BOOTSTRAP_ADMIN_PASSWORD` to seed an admin account.
4. Run `./mvnw spring-boot:run`

## Postman Testing

### Register

- Method: `POST`
- URL: `http://localhost:8080/api/auth/register`
- Header: `Content-Type: application/json`

```json
{
  "username": "john_user",
  "email": "john@example.com",
  "password": "StrongPass1!"
}
```

### Login

- Method: `POST`
- URL: `http://localhost:8080/api/auth/login`
- Header: `Content-Type: application/json`

```json
{
  "username": "john_user",
  "password": "StrongPass1!"
}
```

### Profile

- Method: `GET`
- URL: `http://localhost:8080/api/user/profile`
- Header: `Authorization: Bearer <access-token>`

### Refresh Token

- Method: `POST`
- URL: `http://localhost:8080/api/auth/refresh-token`
- Header: `Content-Type: application/json`

```json
{
  "refreshToken": "<refresh-token>"
}
```
=======
# secure-auth-system
>>>>>>> feeeaab5a90c25278349139b9ed8acdb94b4af15
