# SOA-SKILL-7 — Securing Library Service with JWT

## Architecture

- Eureka Server: `8761`
- Auth Service: `8001`
- Book Service: `8002`

### Authentication flow

1. Client sends username/password to Auth Service.
2. Auth Service validates the credentials.
3. Auth Service returns a signed JWT containing the username and role.
4. Client sends the JWT as `Authorization: Bearer <token>`.
5. Book Service validates the JWT.
6. Book Service extracts the role and permits only `ADMIN` or `LIBRARIAN` for POST, PUT and DELETE.

## Demo users

| Username | Password | Role |
|---|---|---|
| admin | admin123 | ADMIN |
| librarian | admin123 | LIBRARIAN |
| user | user123 | USER |

## Requirements

- Java 17+
- Maven 3.9+
- Spring Tool Suite / Eclipse
- Postman

## Run order

1. Start `EurekaServerApplication`
2. Start `AuthServiceApplication`
3. Start `BookServiceApplication`

Open Eureka:
`http://localhost:8761`

## Postman

### 1. Admin login

POST `http://localhost:8001/auth/login`

Body:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

Copy the `token`.

### 2. Normal user login

POST `http://localhost:8001/auth/login`

Body:
```json
{
  "username": "user",
  "password": "user123"
}
```

Copy the `token`.

### 3. Public GET

GET `http://localhost:8002/books`

No token required.

### 4. Protected ADD

POST `http://localhost:8002/books`

Header:
`Authorization: Bearer <ADMIN_TOKEN>`

Body:
```json
{
  "title": "Spring Boot in Action",
  "author": "Craig Walls"
}
```

Expected: `201 Created`

### 5. Protected UPDATE

PUT `http://localhost:8002/books/1`

Header:
`Authorization: Bearer <ADMIN_TOKEN>`

Body:
```json
{
  "title": "Clean Code Updated",
  "author": "Robert C. Martin"
}
```

Expected: `200 OK`

### 6. Protected DELETE

DELETE `http://localhost:8002/books/1`

Header:
`Authorization: Bearer <ADMIN_TOKEN>`

Expected: `200 OK`

### 7. Normal user must be denied

Use the USER token for POST, PUT or DELETE.

Expected: `403 Forbidden`

### 8. Invalid/missing token

For a protected operation, use no token or an invalid token.

Expected: `401 Unauthorized` for missing/invalid authentication.

## Important

This demonstration stores books in memory so the project runs immediately without PostgreSQL. The JWT secret is intentionally a demo secret. For a production system, store secrets securely and use a database/user service.


