# clicKart – Backend

A Spring Boot REST API for **clicKart**, a full-stack e-commerce application. It provides secure authentication, product and category management, shopping cart, order processing, and online payment integration.

## Tech Stack

- **Language:** Java
- **Framework:** Spring Boot
- **Security:** Spring Security, JWT Authentication
- **ORM:** Spring Data JPA, Hibernate
- **Database:** MySQL
- **Validation:** Jakarta Bean Validation
- **Payment Gateway:** Razorpay
- **Email:** Spring Mail
- **Build Tool:** Maven
- **API Testing:** Postman

## Features

- User registration and login
- JWT-based authentication and authorization
- Role-based access control (USER and ADMIN)
- Product and category management
- Address management
- Shopping cart management
- Order creation and cancellation
- Razorpay payment integration
- Email notifications
- RESTful API architecture
- Centralized exception handling
- Request validation

## Project Architecture

The backend follows a layered architecture to maintain separation of concerns and improve maintainability.

```text
Client (React)
      |
      | REST API / JSON
      v
Spring Boot Backend
      |
      v
Spring Security + JWT
      |
      v
Controller Layer
      |
      v
Service Layer
      |
      v
Repository Layer
      |
      v
MySQL Database
```

External integrations:

- Razorpay – Payment processing
- SMTP – Email notifications

## Project Structure

```text
src/main/java/com/clicKart/
│
├── user/
├── address/
├── category/
├── product/
├── cart/
├── order/
├── payment/
├── auth/
├── security/
├── exception/
├── config/
└── chatbot/
```

Each business module follows a structured organization:

```text
module/
├── controller/
├── dto/
├── entity/
├── repository/
└── service/
    ├── Service.java
    └── ServiceImpl.java
```

## Getting Started

### 1. Clone the Repository

```bash
git clone <https://github.com/Satyamverma2604/clicKart-Backend>
cd clicKart-backend
```

### 2. Configure MySQL

Create a database:

```sql
CREATE DATABASE clickart;
```

### 3. Configure Environment Variables

Configure the following environment variables before running the application:

```properties
DB_URL=jdbc:mysql://localhost:3306/clickart
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password

JWT_SECRET=your_secure_jwt_secret
JWT_ACCESS_EXP=300000
JWT_REFRESH_EXP=2592000000

RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret

MAIL_USERNAME=your_email
MAIL_PASSWORD=your_email_app_password
```

Make sure your Spring Boot configuration references these environment variables.

**Important:** Never commit actual credentials, JWT secrets, or API keys to GitHub.

### 4. Run the Application

Using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

The backend will run at:

```text
http://localhost:8080
```

## API Modules

| Module | Description |
|---|---|
| Authentication | Registration, login and JWT authentication |
| User | User profile management |
| Address | Shipping address management |
| Category | Product category management |
| Product | Product listing and management |
| Cart | Shopping cart operations |
| Order | Order creation and management |
| Payment | Razorpay payment processing |
| Security | JWT authentication and role-based authorization |

## Security

- Stateless authentication using JWT.
- Password hashing.
- Role-based access control.
- Protected user and admin endpoints.
- Secure handling of payment credentials.

## API Testing

APIs can be tested using **Postman**.

Base URL:

```text
http://localhost:8080
```

For protected endpoints, include the JWT token:

```http
Authorization: Bearer <your_access_token>
```

## Future Enhancements

- Google OAuth 2.0 authentication
- AI-powered shopping assistant
- Admin analytics dashboard
- Advanced product filtering and sorting
- Product reviews and ratings
- Inventory management enhancements

## Author

**Satyam Verma**

Computer Science Engineering Student

GitHub: [Your GitHub Profile](https://github.com/Satyamverma2604)

---

⭐ If you find this project interesting, consider giving it a star!
