# Bank System API

A learning project that provides a REST API for registering users, creating bank accounts, making deposits and withdrawals, transferring money, and viewing transaction history. Built with Java 21, Spring Boot, Spring Data JPA, Spring Security, and PostgreSQL.

## Features

- Register users and create one bank account per user
- Deposit, withdraw, and transfer money
- View personal account and transaction history
- Provide admin-only endpoints for listing users, accounts, and transactions
- Validate registration data, account numbers, and transaction amounts
- Hash passwords with BCrypt
- Explore the API with Swagger UI

## Requirements

- Java 21
- Docker with Docker Compose

## Run locally

Clone the repository, open its root directory, and start PostgreSQL:

```bash
docker compose up -d
```

The Compose file starts the database only. Before starting the application, set the password for the initial administrator. `ADMIN_EMAIL` is optional and defaults to `admin@example.com`. Use a unique password for your local administrator account. Do not use real or production credentials in this learning project, and never commit passwords or other secrets to the repository.

On Windows PowerShell:

```powershell
$env:ADMIN_PASSWORD = "replace-with-a-local-password"
# Optional:
$env:ADMIN_EMAIL = "admin@example.com"

.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
export ADMIN_PASSWORD='replace-with-a-local-password'
# Optional:
export ADMIN_EMAIL='admin@example.com'

./mvnw spring-boot:run
```

The Maven Wrapper runs the application using the project's Maven configuration. Tests are executed as part of the normal Maven lifecycle where applicable.

The API runs at `http://localhost:8080`. Swagger UI is at `http://localhost:8080/swagger-ui/index.html`.

The database connection settings in `src/main/resources/application.properties` match the local PostgreSQL service in `docker-compose.yml`. Hibernate updates the schema automatically for local development.

To stop PostgreSQL:

```bash
docker compose down
```

The database volume persists between runs. To remove it and all local database data, use `docker compose down -v`.

## Authentication and roles

The API uses HTTP Basic authentication. User registration and Swagger UI are public. All other API endpoints require authentication; the `/admin` endpoints require the `ADMIN` role.

New registrations receive the `USER` role. After creating an account with `POST /accounts/me`, the user's role becomes `CLIENT`. A user can create only one account.

At startup, the application creates an administrator using `ADMIN_EMAIL` and `ADMIN_PASSWORD` if that email is not already in the database. The password is stored as a BCrypt hash. If the account already exists, changing the environment variables does not update its existing credentials.

Most of the time, you will see an authentication prompt. If not, send Basic credentials with protected requests. For example:

```bash
curl -u user@example.com:password123 http://localhost:8080/accounts/me
```

## API endpoints

| Method | Endpoint | Description | Access |
| --- | --- | --- | --- |
| `POST` | `/users/registration` | Register a user | Public |
| `GET` | `/users/me` | Get the authenticated user's profile | Authenticated |
| `GET` | `/users/admin` | List users | Admin |
| `POST` | `/accounts/me` | Create an account for the authenticated user | Authenticated |
| `GET` | `/accounts/me` | Get the authenticated user's account | Authenticated |
| `POST` | `/accounts/me/deposits` | Deposit money | Authenticated |
| `POST` | `/accounts/me/withdraws` | Withdraw money | Authenticated |
| `POST` | `/accounts/me/transfer/{receiverAccountNumber}` | Transfer money to another account | Authenticated |
| `GET` | `/accounts/admin` | List all accounts | Admin |
| `GET` | `/transactions/me` | Get the authenticated user's transactions | Authenticated |
| `GET` | `/transactions/admin` | List all transactions | Admin |

## Register a user

Registration is public:

```http
POST /users/registration
Content-Type: application/json
```

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

The response contains the generated user ID, email, and role. Use the same email and password for HTTP Basic authentication when calling protected endpoints.

## Create an account

An authenticated user with the `USER` role can create one account:

```bash
curl -u user@example.com:password123 -X POST http://localhost:8080/accounts/me
```

The account starts with a zero balance. After creation, the user's role changes to `CLIENT`.

## Deposit, withdraw, and transfer

Amounts are sent as a JSON number in the request body. For example, deposit `100.00`:

```bash
curl -u user@example.com:password123 \
  -H "Content-Type: application/json" \
  -d "100.00" \
  http://localhost:8080/accounts/me/deposits
```

Use the same request format with `/accounts/me/withdraws` to withdraw. To transfer money, include the receiving account number in the URL:

```text
POST /accounts/me/transfer/{receiverAccountNumber}
```

Amounts must be positive and support up to 15 integer digits and 2 decimal places.

## Validation and errors

- Registration requires a valid email and a password between 8 and 128 characters.
- Account numbers in path parameters must be positive.
- Deposits, withdrawals, and transfers reject non-positive amounts; withdrawals and transfers also reject amounts greater than the available balance.
- Transfers to the same account are rejected.
- Missing users or accounts return `404 Not Found`.
- Invalid input and rejected transactions return `400 Bad Request`.
- Requests without valid credentials return `401 Unauthorized`; requests without the required role return `403 Forbidden`.

## Local development notes

The PostgreSQL credentials in the provided Compose configuration are for local development. The application disables CSRF protection for its REST API and uses `spring.jpa.hibernate.ddl-auto=update`; review these settings before deploying outside a local learning environment.
