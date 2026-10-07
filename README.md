# Bank System API

A learning project that provides a REST API for creating bank accounts, making deposits and withdrawals, transferring money, and viewing transaction history. Built with Java 21, Spring Boot, Spring Data JPA, and PostgreSQL.

## Features

- Register users and create a bank account for a user
- Deposit, withdraw, and transfer money
- View accounts and transaction history
- Validate user input and amounts
- Handle missing users/accounts, role violations, and rejected transactions
- Explore the API with Swagger UI

## Requirements

- Java 21
- Docker with Docker Compose

## Run locally

Clone the repository and open its root directory. Start PostgreSQL:

```bash
docker compose up -d
```

The Compose file starts the database only. Run the Spring Boot application locally with the Maven Wrapper.

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`. Swagger UI is at `http://localhost:8080/swagger-ui/index.html`.

The database connection settings are in `src/main/resources/application.properties` and match the local PostgreSQL service in `docker-compose.yml`. Hibernate updates the schema automatically for local development.

To stop PostgreSQL:

```bash
docker compose down
```

The database volume persists between runs. To remove it and all local database data, use `docker compose down -v`.

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/users` | Register a user |
| `GET` | `/users` | List users |
| `GET` | `/users/{id}` | Find a user by ID |
| `POST` | `/accounts/{userId}` | Create an account for a user |
| `GET` | `/accounts` | List accounts |
| `GET` | `/accounts/{accountNumber}` | Find an account |
| `POST` | `/accounts/{accountNumber}/deposits` | Deposit money |
| `POST` | `/accounts/{accountNumber}/withdraws` | Withdraw money |
| `POST` | `/accounts/{accountNumber}/transfer/{receiverAccountNumber}` | Transfer money |
| `GET` | `/transactions` | List all transactions |
| `GET` | `/transactions/{accountNumber}` | List transactions for an account |

### Register a user

```http
POST /users
Content-Type: application/json
```

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

The response contains the generated user ID and role. Use that ID in `POST /accounts/{userId}` to create an account.

### Deposit money

Amounts are sent as a JSON number in the request body:

```http
POST /accounts/12345/deposits
Content-Type: application/json
```

```json
100.00
```

The withdrawal and transfer endpoints use the same amount format. Amounts must be positive and support up to 15 integer digits and 2 decimal places.

## Validation and errors

- Registration requires a valid email and a password between 8 and 128 characters. The user ID must be omitted or `null`.
- User IDs and account numbers in path parameters must be positive.
- Deposits, withdrawals, and transfers reject non-positive amounts, insufficient funds, and transfers to the same account.
- Missing users or accounts return `404 Not Found`; role violations return `403 Forbidden`; rejected transactions return `400 Bad Request`.

## Tests

Start PostgreSQL first, then run:

On Windows:

```powershell
.\mvnw.cmd test
```

On macOS/Linux:

```bash
./mvnw test
```

## Security status

Authentication and authorization have not been implemented yet, and passwords are not encoded yet. This project is for local learning only; do not use real credentials or expose it publicly with real data.
**Planned:** Password hashing and authentication and authorization with Spring Security.
