# TuniRate

TuniRate is a Spring Boot backend for a product discovery and review platform focused on helping people explore products, compare brands, read authentic feedback, and share their own experiences.

This repository contains the core application logic, REST API, authentication, role-based access, product/company management, review workflows, community engagement features, and persistence layer for the platform.

## Why TuniRate?

Consumers often have to gather product information from multiple sources before making a decision. TuniRate brings that information together in one place by combining product data, company profiles, reviews, ratings, and user-generated discussion in a structured platform.

## Features

- Product catalog and product details
- Company and brand management
- Reviews and star ratings
- Comments and replies on reviews
- Like and reaction tracking
- User authentication and registration
- Email verification and account activation
- Google sign-in support
- Product suggestions and moderation workflows
- Admin and company dashboard summaries
- Secure file upload support for product and company assets

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Security
- JWT-based authentication
- Spring Data JPA
- PostgreSQL
- Flyway database migrations
- Maven
- Lombok
- Thymeleaf for email templates

## Architecture

The project follows a Spring Boot layered architecture with distinct responsibilities for controllers, services, repositories, security, and persistence.

```text
Client
  │
  ▼
REST API (Spring Boot)
  ├── Controllers
  ├── Services / Business logic
  ├── Security & authentication
  ├── DTOs / mappers
  ├── Repositories / JPA entities
  └── Database migrations
  │
  ▼
PostgreSQL
```

## Project Structure

```text
.
├── src/
│   ├── main/
│   │   ├── java/tn/anasazx/tunirate/
│   │   │   ├── authentication/
│   │   │   ├── category/
│   │   │   ├── comment/
│   │   │   ├── company/
│   │   │   ├── dashboard/
│   │   │   ├── email/
│   │   │   ├── invitation/
│   │   │   ├── like/
│   │   │   ├── membership/
│   │   │   ├── product/
│   │   │   ├── review/
│   │   │   ├── security/
│   │   │   ├── user/
│   │   │   └── ...
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── db/migration/
│   │       └── templates/
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
├── .env
├── logs/
├── uploads/
├── README.md
└── todo.md
```

## Getting Started

### Prerequisites

- Java 21+
- Maven
- PostgreSQL
- Git
- A mail provider / SMTP credentials for email verification

### 1) Clone the repository

```bash
git clone https://github.com/<your-username>/TuniRate.git
cd TuniRate
```

### 2) Configure environment variables

Create a `.env` file in the project root and add the required variables before starting the app.

Example:

```dotenv
DB_URL=jdbc:postgresql://localhost:5432/tunirate
JWT_SECRET=your-very-long-random-secret
FILE_UPLOAD_DIR=uploads
FILE_BASE_URL=http://localhost:8080/uploads
GOOGLE_CLIENT_ID=your-google-client-id
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@example.com
MAIL_PASSWORD=your-app-password
```

> Do not commit real secrets to GitHub. Keep production credentials in a secure environment.

### 3) Start PostgreSQL

Make sure your PostgreSQL database is running and that the database name from `DB_URL` exists.

### 4) Run the application

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
mvnw.cmd spring-boot:run
```

The application will start using the Spring Boot configuration available in `src/main/resources/application.properties`.

### 5) Run tests

```bash
./mvnw test
```

## Configuration Notes

The project loads environment variables from `.env` via Spring configuration and uses them for:

- database connection
- JWT signing
- file upload handling
- Google auth client ID
- email delivery

Key settings are defined in `src/main/resources/application.properties`.

## API Overview

This repository exposes a backend API for:

- user registration and login
- company creation and membership
- product publishing and updates
- review submission and moderation
- product suggestion workflows
- dashboard statistics and admin operations

The exact endpoints are defined in the controllers under `src/main/java/tn/anasazx/tunirate/*`.

## Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run the relevant tests
5. Open a pull request with a clear description

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

For questions or collaboration, you can reach out through the project owner or repository maintainer.
