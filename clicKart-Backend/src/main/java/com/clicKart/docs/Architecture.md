# clicKart — System Architecture

## 1. Project Overview

**clicKart** is a full-stack e-commerce application designed using a modern layered architecture.

The system provides:

* User registration and authentication
* JWT-based authorization
* Product and category management
* Address management
* Shopping cart management
* Order management
* Razorpay payment integration
* Payment verification
* Email notifications
* Admin management
* AI chatbot integration planned

The application follows a separation of concerns between the frontend, backend, database, and external services.

---

# 2. High-Level System Architecture

```text
                         ┌──────────────────────┐
                         │       Customer       │
                         │      / Admin         │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │      React UI        │
                         │      Frontend        │
                         └──────────┬───────────┘
                                    │
                         HTTP / REST / JSON
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │       Axios         │
                         │    API Client       │
                         └──────────┬───────────┘
                                    │
                                    ▼
                  ┌──────────────────────────────────┐
                  │          Spring Boot API         │
                  │                                  │
                  │ Controller → Service → Repository│
                  └───────────────┬──────────────────┘
                                  │
              ┌───────────────────┼───────────────────┐
              │                   │                   │
              ▼                   ▼                   ▼
      ┌──────────────┐    ┌──────────────┐   ┌──────────────┐
      │    MySQL     │    │   Razorpay   │   │ Email Service│
      │   Database   │    │   Gateway    │   │              │
      └──────────────┘    └──────────────┘   └──────────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │   AI Chatbot     │
                         │    Planned       │
                         └──────────────────┘
```

---

# 3. Technology Stack

## Frontend

| Technology    | Purpose                 |
| ------------- | ----------------------- |
| React         | UI development          |
| React Router  | Client-side routing     |
| Redux Toolkit | Global state management |
| Axios         | HTTP communication      |
| JavaScript    | Frontend development    |

## Backend

| Technology         | Purpose                          |
| ------------------ | -------------------------------- |
| Java               | Backend programming language     |
| Spring Boot        | Application framework            |
| Spring Web         | REST API development             |
| Spring Data JPA    | Database access                  |
| Hibernate          | ORM                              |
| Spring Security    | Authentication and authorization |
| JWT                | Stateless authentication         |
| Lombok             | Boilerplate reduction            |
| Jakarta Validation | Request validation               |

## Database

| Technology | Purpose             |
| ---------- | ------------------- |
| MySQL      | Relational database |

## External Services

| Service       | Purpose              |
| ------------- | -------------------- |
| Razorpay      | Payment processing   |
| Email Service | Transactional emails |
| AI Chatbot    | Customer assistance  |

---

# 4. Architectural Style

clicKart follows a **layered modular architecture**.

The backend is organized into business modules and application layers.

```text
                    Client
                      │
                      ▼
                Controller Layer
                      │
                      ▼
                 Service Layer
                      │
                      ▼
               Repository Layer
                      │
                      ▼
                  Database
```

External integrations are separated from core business logic.

```text
Payment Service
      │
      ▼
Payment Gateway Interface
      │
      ▼
Razorpay Implementation
```

This allows the payment provider to be changed without heavily modifying the payment business logic.

---

# 5. Frontend Architecture

The React application follows a modular component-based architecture.

```text
frontend/
│
├── public/
│
├── src/
│   │
│   ├── api/
│   │   ├── userApi.js
│   │   ├── addressApi.js
│   │   ├── categoryApi.js
│   │   ├── productApi.js
│   │   ├── cartApi.js
│   │   ├── orderApi.js
│   │   └── paymentApi.js
│   │
│   ├── components/
│   │
│   ├── pages/
│   │
│   ├── routes/
│   │
│   ├── store/
│   │
│   ├── hooks/
│   │
│   ├── utils/
│   │
│   ├── services/
│   │
│   ├── App.jsx
│   └── main.jsx
│
└── package.json
```

The exact folder structure can evolve as the frontend implementation grows.

---

# 6. Frontend Responsibilities

The frontend is responsible for:

* Rendering the user interface
* Handling user interactions
* Client-side routing
* Maintaining UI state
* Maintaining authentication state
* Calling backend APIs
* Displaying API responses
* Form validation
* Product browsing
* Cart management
* Order management
* Payment checkout integration
* Admin dashboard UI

The frontend should **not** contain critical business rules that must be trusted for security.

For example:

```text
Frontend:
"User selected quantity = 5"

Backend:
"Is quantity 5 actually available in stock?"
```

The backend remains the final authority.

---

# 7. Backend Architecture

The backend follows a modular layered architecture.

```text
com.clicKart
│
├── user
├── address
├── category
├── product
├── cart
├── order
├── payment
├── email
├── security
├── common
└── exception
```

Each business module generally follows:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
MySQL
```

---

# 8. Controller Layer

The Controller layer handles HTTP requests and responses.

Responsibilities:

* Define API endpoints
* Receive request data
* Validate request DTOs
* Obtain authentication information
* Call service methods
* Return HTTP responses

Example:

```text
POST /api/orders
        │
        ▼
OrderController
        │
        ▼
OrderService
```

Controllers should remain thin.

Business logic should not be placed directly inside controllers.

---

# 9. Service Layer

The Service layer contains business logic.

Responsibilities include:

* User ownership validation
* Product availability validation
* Cart business rules
* Order creation
* Stock management
* Order cancellation
* Payment processing
* Payment verification
* Email triggering

Example:

```text
OrderController
      ↓
OrderService
      ↓
Validate User
      ↓
Validate Address
      ↓
Validate Cart
      ↓
Create Order
      ↓
Update Stock
```

---

# 10. Repository Layer

The Repository layer communicates with the database through Spring Data JPA.

Examples:

```text
UserRepository
AddressRepository
CategoryRepository
ProductRepository
CartRepository
CartItemRepository
OrderRepository
OrderItemRepository
PaymentRepository
```

Repositories should primarily handle data access.

Business rules should remain in the Service layer.

---

# 11. Entity Layer

Entities represent database tables.

Main entities:

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

Relationships:

```text
User
 ├── Address
 ├── Cart
 └── Order

Category
 └── Product

Cart
 └── CartItem
       └── Product

Order
 └── OrderItem
       └── Product

Order
 └── Payment
```

---

# 12. DTO Architecture

DTOs are used to separate API contracts from database entities.

Typical structure:

```text
Request DTO
     ↓
Controller
     ↓
Service
     ↓
Entity
     ↓
Repository
```

For responses:

```text
Repository
     ↓
Entity
     ↓
Service
     ↓
Response DTO
     ↓
Controller
     ↓
Frontend
```

This prevents entities from being directly exposed as API contracts.

---

# 13. Exception Architecture

clicKart uses centralized exception handling.

```text
Service
   │
   ├── ResourceNotFoundException
   ├── BadRequestException
   ├── ResourceOwnershipException
   ├── DuplicateResourceException
   └── InvalidOperationException
                │
                ▼
     GlobalExceptionHandler
                │
                ▼
        HTTP Error Response
```

Exception mapping:

| Exception                    | HTTP Status |
| ---------------------------- | ----------: |
| `ResourceNotFoundException`  |         404 |
| `BadRequestException`        |         400 |
| `ResourceOwnershipException` |         403 |
| `DuplicateResourceException` |         409 |
| `InvalidOperationException`  |         409 |

---

# 14. Security Architecture

clicKart uses:

```text
Spring Security
       +
JWT Authentication
       +
Role-Based Authorization
```

Main roles:

```text
ROLE_USER
ROLE_ADMIN
```

---

# 15. JWT Authentication Flow

```text
                React
                  │
                  │ Login
                  ▼
          POST /login
                  │
                  ▼
        Spring Security
                  │
                  ▼
        Validate Credentials
                  │
                  ▼
             Generate JWT
                  │
                  ▼
               React
                  │
                  │ Store token
                  ▼
         Authenticated Requests
                  │
                  │ Authorization:
                  │ Bearer <JWT>
                  ▼
          JWT Authentication Filter
                  │
                  ▼
           Validate JWT
                  │
                  ▼
       SecurityContext populated
                  │
                  ▼
             Controller
```

---

# 16. `/me` Security Architecture

For user-owned resources, the API uses `/me`.

Example:

```text
GET /api/users/me
```

The frontend does not send the user's ID.

The backend obtains the authenticated user from the JWT.

```text
JWT
 ↓
Authentication
 ↓
Authenticated User
 ↓
User ID
 ↓
Service
 ↓
User Data
```

This pattern is used for:

```text
/api/users/me
/api/users/me/addresses
/api/cart
/api/orders
/api/payments
```

Resource ownership is still verified for child resources such as:

```text
/api/users/me/addresses/{id}
/api/orders/{id}
/api/orders/{id}/cancel
```

---

# 17. Role-Based Authorization

The authorization model is:

```text
                     Spring Security
                           │
              ┌────────────┴────────────┐
              │                         │
          ROLE_USER                 ROLE_ADMIN
              │                         │
              ▼                         ▼
        User Resources             Admin Resources
        Cart                       Product Management
        Orders                     Category Management
        Payments                   User Management
```

User-facing resources:

```text
/api/users/**
/api/cart/**
/api/orders/**
/api/payments/**
```

Administrative resources:

```text
/api/admin/**
```

Public read operations can be configured for product and category browsing.

---

# 18. Database Architecture

MySQL is the primary relational database.

Main tables:

```text
users
addresses
categories
products
carts
cart_items
orders
order_items
payments
```

High-level relationship:

```text
User
 │
 ├───────────────┐
 │               │
 ▼               ▼
Address         Cart
 │               │
 │               ▼
 │            CartItem
 │               │
 │               ▼
 │            Product
 │               │
 ▼               │
Order ───────────┘
 │
 ▼
OrderItem
 │
 ▼
Product

Order
 │
 ▼
Payment
```

Detailed database information is maintained separately in:

```text
docs/Database-Design.md
```

---

# 19. Product and Inventory Architecture

Product inventory is maintained through the `stockQuantity` field.

The backend validates stock before allowing operations such as:

```text
Add to Cart
Update Cart Quantity
Create Order
```

Example:

```text
Product Stock = 10

User adds quantity = 4

Available:
10 >= 4

Operation allowed.
```

After order creation:

```text
10 - 4 = 6
```

If an eligible order is cancelled:

```text
6 + 4 = 10
```

Inventory updates must be handled by the backend.

---

# 20. Cart Architecture

The cart belongs to exactly one user.

```text
User
 │
 │ 1 : 1
 ▼
Cart
 │
 │ 1 : N
 ▼
CartItem
 │
 │ N : 1
 ▼
Product
```

Cart flow:

```text
User
 ↓
GET /api/cart
 ↓
Retrieve Cart
 ↓
Retrieve Cart Items
 ↓
Retrieve Product Information
 ↓
Calculate/Return Cart Details
```

Cart operations:

```text
Add Product
Update Quantity
Remove Product
View Cart
```

---

# 21. Order Architecture

Order creation connects:

```text
User
 ↓
Shipping Address
 ↓
Cart
 ↓
Cart Items
 ↓
Products
 ↓
Order
 ↓
Order Items
```

Order creation flow:

```text
POST /api/orders
        │
        ▼
Authenticate User
        │
        ▼
Validate Shipping Address
        │
        ▼
Verify Address Ownership
        │
        ▼
Find User Cart
        │
        ▼
Check Cart is Not Empty
        │
        ▼
Validate Product Stock
        │
        ▼
Create Order
        │
        ▼
Create Order Items
        │
        ▼
Update Product Stock
        │
        ▼
Save Order
```

---

# 22. Order Status Architecture

Order status:

```text
PENDING
   │
   ▼
CONFIRMED
   │
   ▼
SHIPPED
   │
   ▼
DELIVERED
```

Cancellation:

```text
PENDING / CONFIRMED
          │
          ▼
      CANCELLED
```

The service layer controls valid state transitions.

---

# 23. Payment Architecture

Payment processing is separated from order management.

```text
React
  │
  ▼
PaymentController
  │
  ▼
PaymentService
  │
  ▼
PaymentGateway
  │
  ▼
RazorpayPaymentGateway
  │
  ▼
Razorpay
```

The important design principle is:

```text
PaymentService
      ↓
PaymentGateway Interface
      ↓
RazorpayPaymentGateway
```

The service does not directly depend on Razorpay-specific implementation details.

This makes the system easier to maintain and replace with another payment provider if required.

---

# 24. Razorpay Payment Flow

Complete payment flow:

```text
                     React
                       │
                       │ 1. Create Payment
                       ▼
              POST /api/payments/create
                       │
                       ▼
                PaymentService
                       │
                       ▼
              RazorpayPaymentGateway
                       │
                       ▼
                   Razorpay
                       │
                       ▼
              Razorpay Order ID
                       │
                       ▼
                     React
                       │
                       │ 2. Open Checkout
                       ▼
               Razorpay Checkout
                       │
                       │ Customer Payment
                       ▼
                Razorpay Response
                       │
                       │ payment_id
                       │ order_id
                       │ signature
                       ▼
                     React
                       │
                       │ 3. Verify Payment
                       ▼
              POST /
```
