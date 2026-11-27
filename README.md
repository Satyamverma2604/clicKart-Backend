# clicKart – Backend

**clicKart** is a full-stack e-commerce application powered by a Spring Boot REST API. It provides secure authentication, product and category management, shopping cart operations, order processing, inventory management, and online payment integration.

The backend follows a layered architecture and communicates with the React frontend through RESTful APIs.

## Project Links

- **Live Application:** [clicKart](https://clic-kart-frontend.vercel.app/)
- **Backend API:** [https://clickart-backend-lgyv.onrender.com](https://clickart-backend-lgyv.onrender.com)
- **Frontend Repository:** [clicKart-frontend](https://github.com/Satyamverma2604/clicKart-frontend)
- **Backend Repository:** [clicKart-Backend](https://github.com/Satyamverma2604/clicKart-Backend)

## Tech Stack

| Technology | Purpose |
|---|---|
| Java | Backend programming language |
| Spring Boot | Backend application framework |
| Spring MVC | REST API development |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| Spring Data JPA | Database operations |
| Hibernate | ORM |
| MySQL | Relational database |
| Jakarta Bean Validation | Request validation |
| Razorpay | Online payment integration |
| Spring Mail | Email notifications |
| Maven | Build and dependency management |
| Postman | API testing |

## Features

### Authentication and Security
- User registration and login.
- JWT-based authentication.
- Role-based access control (USER and ADMIN).
- Password hashing using BCrypt.
- Protected customer and admin endpoints.
- Stateless session management.

### Customer Management
- User profile management.
- Address management.
- Password change functionality.
- Account management.

### Product and Category Management
- Product creation, updating, and deletion.
- Product listing and search.
- Category-based product filtering.
- Product availability management.
- Category management.

### Shopping Cart and Orders
- Add products to cart.
- Update product quantities.
- Remove cart items.
- Order creation and management.
- Order cancellation.
- Automatic inventory updates during order processing.

### Payment Integration
- Razorpay payment gateway integration.
- Payment order creation.
- Payment signature verification.
- Payment status management.

### Admin Management
- Admin dashboard data.
- Product and category management.
- Customer management.
- Order and payment status management.
- Inventory monitoring.
- Low-stock and out-of-stock tracking.

### Email Notifications
- Automated order confirmation emails.
- HTML-based email templates.
- Order details and product summaries in email notifications.

### Additional Backend Features
- Layered architecture.
- DTO-based request and response handling.
- Centralized exception handling.
- Jakarta Bean Validation.
- RESTful API design.

## Project Architecture

The backend follows a layered architecture to maintain separation of concerns, improve maintainability, and organize business logic.

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

External Integrations:

- **Razorpay:** Online payment processing.
- **SMTP:** Email notifications.

## Project Structure

```text
src/main/java/com/clicKart/
│
├── user/
├── admin/
├── address/
├── category/
├── product/
├── cart/
├── order/
├── payment/
├── auth/
├── security/
├── exception/
├── mail/
└── config/
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

### Prerequisites

Make sure you have installed:

- Java JDK (version compatible with the project configuration).
- Maven.
- MySQL.
- Git.
- IDE such as IntelliJ IDEA or Eclipse.

### 1. Clone the Repository

```bash
git clone https://github.com/Satyamverma2604/clicKart-Backend.git
```

Navigate to the project directory:

```bash
cd clicKart-Backend
```

### 2. Configure MySQL

Create a MySQL database:

```sql
CREATE DATABASE clickart;
```

### 3. Configure Environment Variables

Configure the following environment variables according to your Spring Boot application configuration:

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

Make sure the environment variable names match the placeholders configured in your `application.properties` or `application.yml`.

**Important:** Never commit actual database credentials, JWT secrets, Razorpay credentials, or email passwords to GitHub.

### 4. Run the Application

Build the project:

```bash
mvn clean install
```

Start the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend will be available locally at:

```text
http://localhost:8080
```

### 5. Backend Deployment

The backend is deployed on **Render**.

Production Backend URL:

```text
https://clickart-backend-lgyv.onrender.com
```

API Base URL:

```text
https://clickart-backend-lgyv.onrender.com/api
```

The frontend communicates with this deployed backend through Axios.

**Note:** If the Render service is using a free instance that spins down during inactivity, the first request after a period of inactivity may take longer to respond.

## API Modules

| Module | Description |
|---|---|
| Authentication | User registration, login, and JWT authentication |
| User | User profile and account management |
| Address | Shipping address management |
| Category | Product category operations |
| Product | Product listing and management |
| Cart | Shopping cart operations |
| Order | Order creation, retrieval, and cancellation |
| Payment | Razorpay payment processing and verification |
| Inventory | Stock management and low-stock monitoring |
| Admin | Administrative operations |
| Security | JWT authentication and role-based authorization |

## API Testing

APIs can be tested using **Postman**.

Local Base URL:

```text
http://localhost:8080/api
```

Production Base URL:

```text
https://clickart-backend-lgyv.onrender.com/api
```

For protected endpoints, include the JWT access token in the request headers:

```http
Authorization: Bearer <your_access_token>
```

## Security

The application implements several security measures:

- Stateless authentication using JWT.
- Password hashing using BCrypt.
- Role-based access control.
- Protected customer and admin endpoints.
- Request validation.
- Centralized exception handling.
- Secure handling of payment credentials.
- Backend-side Razorpay payment verification.

## Future Enhancements

- Google OAuth 2.0 authentication.
- AI-powered shopping assistant.
- Product recommendations.
- Product reviews and ratings.
- Advanced analytics and reporting.
- Further inventory management enhancements.

## Author

**Satyam Verma**

Computer Science Engineering Student | Full Stack Developer

- **GitHub:** [Satyamverma2604](https://github.com/Satyamverma2604)
- **Frontend:** [clicKart Frontend](https://github.com/Satyamverma2604/clicKart-frontend)
- **Live Project:** [clicKart](https://clic-kart-frontend.vercel.app/)

---

If you find this project interesting, consider giving the repository a star!
