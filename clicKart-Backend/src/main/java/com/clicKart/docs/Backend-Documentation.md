# clicKart Backend Documentation

## 1. Project Overview
   Part 1

## 2. Backend Architecture & Package Structure
   Part 2

## 3. Database Design
   Part 3

## 4. Authentication & Authorization
   Part 4

## 5. User Module
   Part 5




# clicKart — Backend Documentation

## 1. Project Overview

**Project Name:** clicKart

**Project Type:** Full-Stack E-Commerce Application

**Backend:** Java + Spring Boot

**Database:** MySQL

### Purpose

clicKart is an industry-standard e-commerce application developed to provide a complete online shopping workflow.

The backend is responsible for:

* User registration and management
* User authentication and authorization
* Address management
* Product and category management
* Shopping cart management
* Order creation and cancellation
* Stock management
* Payment processing through Razorpay
* Payment verification
* Email notifications
* Validation and exception handling

### Main Business Flow

```text
User
 ↓
Authentication
 ↓
Browse Products
 ↓
Add Products to Cart
 ↓
Create Order
 ↓
Make Payment
 ↓
Razorpay Payment Verification
 ↓
Order Confirmation
 ↓
Payment Success Email
```

### Backend Architecture

The backend follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

External integrations are separated from business logic:

```text
PaymentService
      ↓
PaymentGateway
      ↓
Razorpay

EmailService
      ↓
Email Provider
```

This separation keeps the application modular, maintainable, and easier to test.

### Core Modules

The backend contains the following major modules:

1. User
2. Address
3. Category
4. Product
5. Cart
6. Order
7. Payment
8. Email
9. Security
10. Exception Handling

### Key Backend Features

* RESTful API architecture
* Spring Data JPA for database operations
* Hibernate ORM
* JWT-based authentication
* Role-based authorization
* Jakarta Bean Validation
* Custom exception handling
* Transaction management
* Lazy loading for appropriate entity relationships
* Razorpay payment integration
* Payment signature verification
* Email notification after successful payment
* Stock restoration during order cancellation
* User/resource ownership validation

### Technology Stack

| Technology         | Purpose                           |
| ------------------ | --------------------------------- |
| Java               | Backend programming language      |
| Spring Boot        | Backend application framework     |
| Spring Web         | REST API development              |
| Spring Data JPA    | Data access layer                 |
| Hibernate          | ORM                               |
| Spring Security    | Authentication and authorization  |
| JWT                | Stateless authentication          |
| Lombok             | Boilerplate reduction             |
| Jakarta Validation | Request validation                |
| MySQL              | Relational database               |
| Razorpay           | Payment gateway                   |
| Email Service      | Transactional email notifications |

### Development Approach

The backend was developed module-by-module:

```text
Entity
  ↓
Repository
  ↓
DTO
  ↓
Service Interface
  ↓
Service Implementation
  ↓
Controller
  ↓
Postman Testing
```





## 2. Backend Architecture & Package Structure

### 2.1 Architecture

clicKart follows a **layered architecture** with modular package organization.

The main request flow is:

```text
Client
  ↓
Controller
  ↓
Service Interface
  ↓
Service Implementation
  ↓
Repository
  ↓
MySQL Database
```

Each layer has a specific responsibility.

### 2.2 Controller Layer

The Controller layer exposes REST APIs to the frontend.

Responsibilities:

* Receive HTTP requests
* Validate request data
* Call the appropriate service
* Return HTTP responses
* Avoid implementing business logic

Example:

```text
PaymentController
        ↓
PaymentService
```

Controllers should remain lightweight.

### 2.3 Service Layer

The Service layer contains the application's business logic.

Responsibilities:

* Apply business rules
* Validate business conditions
* Coordinate multiple repositories
* Handle transactions
* Communicate with external integrations through appropriate interfaces

Example:

```text
PaymentServiceImpl
       ↓
PaymentRepository
OrderRepository
PaymentGateway
```

### 2.4 Repository Layer

The Repository layer communicates with the database using Spring Data JPA.

Responsibilities:

* Save entities
* Find entities
* Update entities
* Delete entities
* Execute custom database queries

Example:

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {
}
```

### 2.5 Entity Layer

The Entity layer represents database tables.

Entities are mapped using JPA annotations such as:

```java
@Entity
@Table
@Id
@GeneratedValue
@OneToMany
@ManyToOne
@OneToOne
```

Examples:

```text
User
Address
Category
Product
Cart
CartItem
Order
OrderItem
Payment
```

### 2.6 DTO Layer

DTOs (Data Transfer Objects) are used to transfer data between the client and backend without directly exposing entity objects.

The project uses separate request and response DTOs where appropriate.

Example:

```text
PaymentCreateRequest
        ↓
PaymentService
        ↓
PaymentCreateResponse
```

Benefits:

* Prevents direct entity exposure
* Controls API input/output
* Improves security
* Makes API contracts clearer
* Allows entity structure to change independently from API structure

### 2.7 Exception Handling Layer

clicKart uses custom exceptions instead of generic `RuntimeException`.

Main exceptions:

| Exception                  | HTTP Status | Purpose                          |
| -------------------------- | ----------: | -------------------------------- |
| ResourceNotFoundException  |         404 | Requested resource doesn't exist |
| BadRequestException        |         400 | Invalid request                  |
| ResourceOwnershipException |         403 | User doesn't own the resource    |
| DuplicateResourceException |         409 | Duplicate resource               |
| InvalidOperationException  |         409 | Operation isn't allowed          |

A global exception handler converts these exceptions into appropriate HTTP responses.

### 2.8 Security Layer

Spring Security and JWT are used for authentication and authorization.

General flow:

```text
Login Request
     ↓
Authentication
     ↓
JWT Generated
     ↓
Client Stores JWT
     ↓
JWT Sent With Requests
     ↓
Spring Security Filter
     ↓
Authentication
     ↓
Authorization
     ↓
Controller
```

JWT allows the backend to identify the authenticated user without maintaining a server-side session.

### 2.9 Payment Gateway Abstraction

Payment processing follows an abstraction-based design.

```text
PaymentService
      ↓
PaymentGateway
      ↓
RazorpayPaymentGateway
      ↓
Razorpay API
```

`PaymentGateway` acts as an interface between the application and the external payment provider.

This makes the payment implementation replaceable in the future.

For example:

```text
PaymentGateway
      ├── RazorpayPaymentGateway
      ├── StripePaymentGateway
      └── OtherPaymentGateway
```

The business layer does not need to directly depend on Razorpay-specific implementation details.

### 2.10 Complete Package Structure

The main backend package structure is:

```text
com.clicKart
│
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── address
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── category
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── product
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── cart
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── order
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   └── service
│
├── payment
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── repository
│   ├── service
│   └── gateway
│
├── email
│   ├── service
│   └── implementation
│
├── security
│   ├── config
│   ├── filter
│   ├── jwt
│   └── service
│
├── common
│   ├── enums
│   └── constants
│
└── exception
    ├── ResourceNotFoundException
    ├── BadRequestException
    ├── ResourceOwnershipException
    ├── DuplicateResourceException
    ├── InvalidOperationException
    └── GlobalExceptionHandler
```

### 2.11 General Design Principles

The backend follows these principles:

* Separation of concerns
* Layered architecture
* Dependency Injection
* Interface-based service design
* DTO-based API communication
* Repository abstraction
* Transaction management
* Validation
* Centralized exception handling
* Secure credential management
* External service abstraction

The objective is to keep the backend **modular, maintainable, testable, and scalable**.






