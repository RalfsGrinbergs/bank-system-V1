# Bank System API

A learning project that provides a REST API for registering users, creating bank accounts, making deposits and withdrawals, transferring money, and viewing transaction history. Built with Java 21, Spring Boot, Spring Data JPA, Spring Security, JWT, and PostgreSQL.

## Features

- Register users and create one bank account per user
- Deposit, withdraw, and transfer money
- View personal account details and transaction history
- Provide admin-only endpoints for listing users, accounts, and transactions
- Validate registration data, account numbers, and transaction amounts
- Hash passwords with BCrypt
- Authenticate API requests with JWT
- Explore and authorize requests with Swagger UI

## Requirements

- Java 21
- Docker with Docker Compose

## Run locally

Clone the repository, open its root directory, and start PostgreSQL:

```bash
docker compose up -d
```

The Compose file starts the database only. Before starting the application, set a password for the initial administrator and a JWT signing secret. `ADMIN_EMAIL` is optional and defaults to `admin@example.com`. Use local development credentials, never commit passwords or secrets, and do not use real or production credentials in this learning project.

The JWT secret must be at least 32 bytes for the configured HS256 signing algorithm.

On Windows PowerShell:

```powershell
$env:ADMIN_PASSWORD = "replace-with-a-local-password"
$env:SECRET_KEY = "replace-with-a-secret-at-least-32-characters-long"

# Optional:
$env:ADMIN_EMAIL = "admin@example.com"

.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```bash
export ADMIN_PASSWORD='replace-with-a-local-password'
export SECRET_KEY='replace-with-a-secret-at-least-32-characters-long'

# Optional:
export ADMIN_EMAIL='admin@example.com'

./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`. Swagger UI is at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html), and the OpenAPI document is at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

The database connection settings in `src/main/resources/application.properties` match the local PostgreSQL service in `docker-compose.yml`. Hibernate updates the schema automatically for local development.

To stop PostgreSQL:

```bash
docker compose down
```

The database volume persists between runs. To remove it and all local database data:

```bash
docker compose down -v
```

## Authentication and roles

The API uses JWT Bearer authentication. Registration and login are public. Protected API endpoints require a valid token; the `/admin` endpoints require the `ADMIN` role.

New registrations receive the `USER` role. A user can create one account with `POST /accounts/me`; after the account is created, the user's role changes to `CLIENT`.

At startup, the application creates an administrator using `ADMIN_EMAIL` and `ADMIN_PASSWORD` if that email is not already in the database. The password is stored as a BCrypt hash. If the account already exists, changing the environment variables does not update its existing credentials.

A JWT contains the user's role as it was at login. After creating an account, log in again if you need a token containing the updated role.

### Get a token

Send the registered email and password to the login endpoint:

```http
POST /users/login
Content-Type: application/json
```

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

The response contains a token:

```json
{
  "token": "eyJ..."
}
```

For protected requests, send the token in the `Authorization` header:

```http
Authorization: Bearer <token>
```

In Swagger UI, click **Authorize** and paste the token value. Swagger adds the `Bearer` prefix for you.

Example using curl:

```bash
curl -H "Authorization: Bearer <token>" \
  http://localhost:8080/accounts/me
```

Tokens expire after 10 hours.

## API endpoints

| Method | Endpoint | Description | Access |
| --- | --- | --- | --- |
| `POST` | `/users/registration` | Register a user | Public |
| `POST` | `/users/login` | Authenticate and receive a JWT | Public |
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

The response contains the generated user ID, email, and role. Registration requires a valid email and a password between 8 and 128 characters.

## Create an account

First register and log in to get a JWT. Then create one account:

```http
POST /accounts/me
Authorization: Bearer <token>
```

The account starts with a zero balance. The user's role changes from `USER` to `CLIENT`.

## Deposit, withdraw, and transfer

Amounts are sent as a JSON number in the request body. For example, deposit `100.00`:

```http
POST /accounts/me/deposits
Authorization: Bearer <token>
Content-Type: application/json

100.00
```

Use the same request format with `/accounts/me/withdraws` to withdraw. To transfer money, include the receiving account number in the URL:

```http
POST /accounts/me/transfer/{receiverAccountNumber}
Authorization: Bearer <token>
Content-Type: application/json

100.00
```

Amounts must be positive and support up to 15 integer digits and 2 decimal places. Withdrawals and transfers cannot exceed the available balance. Transfers to the same account are rejected.

## Local development notes

The PostgreSQL credentials in the provided Compose configuration are for local development. The application disables CSRF protection for its REST API and uses `spring.jpa.hibernate.ddl-auto=update`. Review these settings before deploying outside a local learning environment.
~~~~
