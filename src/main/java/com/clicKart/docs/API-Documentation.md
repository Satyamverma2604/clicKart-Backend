# clicKart — API Documentation

## 1. API Overview

clicKart follows a RESTful API architecture using Spring Boot.

The API is divided into business modules:

```text
User
Address
Category
Product
Cart
Order
Payment
```

The application uses **JWT-based authentication** for authenticated user operations.

Instead of receiving the user's ID from the frontend for every user-specific operation, the backend identifies the authenticated user from the JWT.

---

# 2. API Design Principles

The API follows these principles:

* RESTful resource-based URLs
* JWT-based authentication
* `/me` endpoints for the currently authenticated user
* Role-based authorization
* DTO-based request and response handling
* Centralized exception handling
* Jakarta Bean Validation
* Service-layer business logic
* Payment gateway abstraction

---

# 3. HTTP Methods

| Method   | Purpose       |
| -------- | ------------- |
| `GET`    | Retrieve data |
| `POST`   | Create data   |
| `PUT`    | Update data   |
| `DELETE` | Delete data   |

---

# 4. Authentication

Authenticated APIs require a JWT token.

The frontend sends the token using:

```text
Authorization: Bearer <JWT_TOKEN>
```

### Authentication Flow

```text
React
  ↓
Login
  ↓
JWT Token
  ↓
Axios
  ↓
Authorization Header
  ↓
Spring Security
  ↓
JWT Validation
  ↓
Authenticated User
  ↓
Controller
  ↓
Service
```

The backend obtains the authenticated user's identity from Spring Security rather than trusting a `userId` supplied by the frontend.

---

# 5. `/me` Endpoint Design

For operations belonging to the currently authenticated user, clicKart uses `/me`.

### Old Approach

```text
GET /api/users/{userId}/addresses
GET /api/users/{userId}/cart
GET /api/orders/user/{userId}
```

This requires the frontend to send the user ID.

### JWT-Based Approach

```text
GET /api/users/me/addresses
GET /api/cart
GET /api/orders
```

The backend obtains the user ID from the authenticated JWT.

### Benefits

* Better security
* Prevents users from easily manipulating another user's ID
* Cleaner frontend APIs
* Less user identity information in URLs
* Centralized authorization

---

# 6. User APIs

Base URL:

```text
/api/users
```

## 6.1 Create User

```text
POST /api/users
```

### Purpose

Creates a new user account.

### Example Request

```json
{
  "name": "Rahul",
  "email": "rahul@example.com",
  "password": "Password@123",
  "phone": "9876543210"
}
```

---

## 6.2 Get Current User

```text
GET /api/users/me
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Retrieves the profile of the currently authenticated user.

The backend identifies the user from the JWT.

---

## 6.3 Update Current User

```text
PUT /api/users/me
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Updates the profile of the currently authenticated user.

---

## 6.4 Delete Current User

```text
DELETE /api/users/me
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Deletes/deactivates the currently authenticated user's account according to the application's user-management policy.

---

# 7. Address APIs

Base URL:

```text
/api/users/me/addresses
```

All address operations belong to the authenticated user.

---

## 7.1 Create Address

```text
POST /api/users/me/addresses
```

### Authentication

```text
Bearer JWT required
```

### Example Request

```json
{
  "houseNo": "12-4",
  "street": "MG Road",
  "city": "Hyderabad",
  "state": "Telangana",
  "country": "India",
  "postalCode": "500001",
  "addressType": "HOME"
}
```

### Purpose

Creates a new address for the authenticated user.

The backend obtains the user ID from the JWT.

---

## 7.2 Get My Addresses

```text
GET /api/users/me/addresses
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Retrieves all addresses belonging to the authenticated user.

---

## 7.3 Update My Address

```text
PUT /api/users/me/addresses/{addressId}
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Updates an address belonging to the authenticated user.

The backend verifies ownership before updating the address.

---

## 7.4 Delete My Address

```text
DELETE /api/users/me/addresses/{addressId}
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Deletes an address belonging to the authenticated user.

The backend verifies ownership before deletion.

---

# 8. Category APIs

Base URL:

```text
/api/categories
```

Category management is primarily an administrative operation.

---

## 8.1 Create Category

```text
POST /api/categories
```

### Purpose

Creates a new product category.

### Authorization

```text
ADMIN
```

---

## 8.2 Get Category

```text
GET /api/categories/{id}
```

### Purpose

Retrieves a category by ID.

---

## 8.3 Get All Categories

```text
GET /api/categories
```

### Purpose

Retrieves available categories.

---

## 8.4 Update Category

```text
PUT /api/categories/{id}
```

### Purpose

Updates an existing category.

### Authorization

```text
ADMIN
```

---

## 8.5 Delete Category

```text
DELETE /api/categories/{id}
```

### Purpose

Deletes a category.

### Authorization

```text
ADMIN
```

---

# 9. Product APIs

Base URL:

```text
/api/products
```

---

## 9.1 Create Product

```text
POST /api/products
```

### Authorization

```text
ADMIN
```

### Example Request

```json
{
  "name": "Wireless Mouse",
  "description": "Wireless optical mouse",
  "brand": "Logitech",
  "price": 999.00,
  "stockQuantity": 50,
  "categoryId": 1
}
```

---

## 9.2 Get Product

```text
GET /api/products/{id}
```

### Purpose

Retrieves a product by ID.

---

## 9.3 Get All Products

```text
GET /api/products
```

### Purpose

Retrieves products.

---

## 9.4 Get Active Products

```text
GET /api/products/active
```

### Purpose

Retrieves products currently marked as active.

This endpoint is useful for customer-facing product listing.

---

## 9.5 Get Products by Category

```text
GET /api/products/category/{categoryId}
```

### Example

```text
GET /api/products/category/1
```

### Purpose

Retrieves products belonging to a specific category.

---

## 9.6 Search Products

```text
GET /api/products/search?name={name}
```

### Example

```text
GET /api/products/search?name=mouse
```

### Purpose

Searches products by name.

---

## 9.7 Update Product

```text
PUT /api/products/{id}
```

### Authorization

```text
ADMIN
```

### Purpose

Updates an existing product.

---

## 9.8 Delete Product

```text
DELETE /api/products/{id}
```

### Authorization

```text
ADMIN
```

### Purpose

Deletes a product according to the application's product-management policy.

---

# 10. Cart APIs

Cart operations are based on the currently authenticated user.

Base URL:

```text
/api/cart
```

The frontend does **not** send `userId`.

---

## 10.1 Get My Cart

```text
GET /api/cart
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Retrieves the cart belonging to the authenticated user.

### Flow

```text
JWT
 ↓
Authenticated User
 ↓
User ID
 ↓
Find Cart
 ↓
Return Cart
```

---

## 10.2 Add Product to Cart

```text
POST /api/cart/items
```

### Authentication

```text
Bearer JWT required
```

### Example Request

```json
{
  "productId": 5,
  "quantity": 2
}
```

### Business Validation

The backend verifies:

* User is authenticated
* Product exists
* Product is active
* Quantity is greater than zero
* Requested quantity does not exceed stock

---

## 10.3 Update Cart Item

```text
PUT /api/cart/items/{productId}
```

### Authentication

```text
Bearer JWT required
```

### Example

```text
PUT /api/cart/items/5
```

### Example Request

```json
{
  "quantity": 3
}
```

### Purpose

Updates the quantity of an existing product in the authenticated user's cart.

---

## 10.4 Remove Cart Item

```text
DELETE /api/cart/items/{productId}
```

### Authentication

```text
Bearer JWT required
```

### Example

```text
DELETE /api/cart/items/5
```

### Purpose

Removes a product from the authenticated user's cart.

---

# 11. Order APIs

Orders are associated with the authenticated user.

Base URL:

```text
/api/orders
```

---

## 11.1 Create Order

```text
POST /api/orders
```

### Authentication

```text
Bearer JWT required
```

### Example Request

```json
{
  "shippingAddressId": 2
}
```

The user identity is obtained from the JWT.

### Business Flow

```text
JWT
 ↓
Authenticated User
 ↓
Find User
 ↓
Validate Shipping Address
 ↓
Verify Address Ownership
 ↓
Find User Cart
 ↓
Verify Cart is Not Empty
 ↓
Create Order
```

---

## 11.2 Get My Orders

```text
GET /api/orders
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Retrieves orders belonging to the authenticated user.

The backend obtains the user ID from the JWT.

---

## 11.3 Get My Order

```text
GET /api/orders/{orderId}
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Retrieves a specific order.

The backend must verify that the requested order belongs to the authenticated user unless the requester has appropriate administrative authority.

---

## 11.4 Cancel My Order

```text
PUT /api/orders/{orderId}/cancel
```

### Authentication

```text
Bearer JWT required
```

### Purpose

Cancels an eligible order belonging to the authenticated user.

### Business Rules

The backend verifies:

1. Order exists.
2. Order belongs to the authenticated user.
3. Order status allows cancellation.
4. Product stock is restored.
5. Order status becomes `CANCELLED`.

### Stock Restoration

```text
Product Stock = 10

Order Quantity = 3

After Order:
Stock = 7

After Cancellation:
Stock = 10
```

---

# 12. Payment APIs

Base URL:

```text
/api/payments
```

Payment processing uses Razorpay.

---

## 12.1 Create Payment

```text
POST /api/payments/create
```

### Authentication

```text
Bearer JWT required
```

### Request

```json
{
  "orderId": 1
}
```

### Purpose

Creates a Razorpay payment order for an existing clicKart order.

### Important Security Rule

The backend must verify that the order belongs to the authenticated user.

The frontend should not be able to create a payment for another user's order simply by changing the `orderId`.

### Response

```json
{
  "paymentId": 1,
  "orderId": 1,
  "razorpayOrderId": "order_xxxxxxxxx",
  "razorpayKeyId": "rzp_xxxxxxxxx",
  "amount": 3997.00,
  "currency": "INR"
}
```

---

## 12.2 Verify Payment

```text
POST /api/payments/verify
```

### Authentication

```text
Bearer JWT required
```

### Request

```json
{
  "orderId": 1,
  "razorpayOrderId": "order_xxxxxxxxx",
  "razorpayPaymentId": "pay_xxxxxxxxx",
  "razorpaySignature": "xxxxxxxxx"
}
```

### Purpose

Verifies the payment response generated by Razorpay Checkout.

### Payment Flow

```text
React
 ↓
POST /api/payments/create
 ↓
Razorpay Order Created
 ↓
React Opens Razorpay Checkout
 ↓
Customer Completes Payment
 ↓
Razorpay Returns Payment Details
 ↓
React Sends Details to /verify
 ↓
Backend Verifies Signature
 ↓
Payment = PAID
 ↓
Order Payment Status = PAID
 ↓
Order Status = CONFIRMED
 ↓
Payment Success Email
```

The Razorpay secret key remains on the backend.

It must never be exposed to the React frontend.

---

# 13. Authentication and Authorization Summary

The API authorization model is:

```text
                    Spring Security
                          │
                          ▼
                     JWT Token
                          │
             ┌────────────┴────────────┐
             │                         │
          USER ROLE                 ADMIN ROLE
             │                         │
             ▼                         ▼
      User Resources            Admin Resources
      /me                       Product Management
      Cart                      Category Management
      Orders                    User Management
      Payments
```

### User-specific resources

```text
/api/users/me/**
/api/cart/**
/api/orders/**
/api/payments/**
```

### Administrative resources

```text
/api/admin/**
```

### Public/read resources

Depending on the final Spring Security configuration, product and category read operations can remain publicly accessible.

---

# 14. `/me` Security Model

The `/me` pattern is important because the frontend does not control the identity of the requested user.

Example:

```text
GET /api/users/me/addresses
```

The backend internally performs:

```text
JWT
 ↓
Authentication
 ↓
Authenticated User
 ↓
User ID
 ↓
findByUserId(userId)
```

Therefore, the client cannot simply change:

```text
/user/5
```

to:

```text
/user/6
```

to access another user's data.

Ownership checks are still required for resources such as orders and addresses.

---

# 15. Validation

The backend uses Jakarta Bean Validation.

Common annotations include:

```java
@NotNull
@NotBlank
@NotEmpty
@Size
@Positive
@PositiveOrZero
@Email
```

Example:

```java
@NotNull(message = "Order ID is required")
private Long orderId;
```

Invalid request data is rejected before normal business processing.

---

# 16. Exception Handling

clicKart uses centralized exception handling.

| Exception                    | HTTP Status | Purpose                          |
| ---------------------------- | ----------: | -------------------------------- |
| `ResourceNotFoundException`  |         404 | Resource does not exist          |
| `BadRequestException`        |         400 | Invalid request                  |
| `ResourceOwnershipException` |         403 | Resource does not belong to user |
| `DuplicateResourceException` |         409 | Duplicate resource               |
| `InvalidOperationException`  |         409 | Business operation not allowed   |

---

# 17. Standard HTTP Status Codes

|                 Status Code | Meaning                       |
| --------------------------: | ----------------------------- |
|                    `200 OK` | Successful request            |
|               `201 Created` | Resource successfully created |
|           `400 Bad Request` | Invalid request               |
|             `403 Forbidden` | Access or ownership violation |
|             `404 Not Found` | Resource not found            |
|              `409 Conflict` | Business conflict             |
| `500 Internal Server Error` | Unexpected server error       |

---

# 18. Complete API Summary

## User

| Method | Endpoint        | Authentication |
| ------ | --------------- | -------------- |
| POST   | `/api/users`    | Public         |
| GET    | `/api/users/me` | USER           |
| PUT    | `/api/users/me` | USER           |
| DELETE | `/api/users/me` | USER           |

## Address

| Method | Endpoint                              | Authentication |
| ------ | ------------------------------------- | -------------- |
| POST   | `/api/users/me/addresses`             | USER           |
| GET    | `/api/users/me/addresses`             | USER           |
| PUT    | `/api/users/me/addresses/{addressId}` | USER           |
| DELETE | `/api/users/me/addresses/{addressId}` | USER           |

## Category

| Method | Endpoint               | Authentication    |
| ------ | ---------------------- | ----------------- |
| POST   | `/api/categories`      | ADMIN             |
| GET    | `/api/categories/{id}` | Public/Configured |
| GET    | `/api/categories`      | Public/Configured |
| PUT    | `/api/categories/{id}` | ADMIN             |
| DELETE | `/api/categories/{id}` | ADMIN             |

## Product

| Method | Endpoint                              | Authentication    |
| ------ | ------------------------------------- | ----------------- |
| POST   | `/api/products`                       | ADMIN             |
| GET    | `/api/products/{id}`                  | Public/Configured |
| GET    | `/api/products`                       | Public/Configured |
| GET    | `/api/products/active`                | Public/Configured |
| GET    | `/api/products/category/{categoryId}` | Public/Configured |
| GET    | `/api/products/search?name={name}`    | Public/Configured |
| PUT    | `/api/products/{id}`                  | ADMIN             |
| DELETE | `/api/products/{id}`                  | ADMIN             |

## Cart

| Method | Endpoint                      | Authentication |
| ------ | ----------------------------- | -------------- |
| GET    | `/api/cart`                   | USER           |
| POST   | `/api/cart/items`             | USER           |
| PUT    | `/api/cart/items/{productId}` | USER           |
| DELETE | `/api/cart/items/{productId}` | USER           |

## Order

| Method | Endpoint                       | Authentication |
| ------ | ------------------------------ | -------------- |
| POST   | `/api/orders`                  | USER           |
| GET    | `/api/orders`                  | USER           |
| GET    | `/api/orders/{orderId}`        | USER           |
| PUT    | `/api/orders/{orderId}/cancel` | USER           |

## Payment

| Method | Endpoint               | Authentication |
| ------ | ---------------------- | -------------- |
| POST   | `/api/payments/create` | USER           |
| POST   | `/api/payments/verify` | USER           |

---

# 19. Complete Customer Flow

```text
Register
   ↓
Login
   ↓
Receive JWT
   ↓
Browse Categories
   ↓
Browse/Search Products
   ↓
Add Product to Cart
   ↓
Update Cart
   ↓
Manage Addresses
   ↓
Create Order
   ↓
Create Razorpay Payment
   ↓
Open Razorpay Checkout
   ↓
Complete Payment
   ↓
Verify Payment
   ↓
Order Confirmed
   ↓
Payment Success Email
```

The frontend uses the JWT for all authenticated operations and `/me`-based endpoints where the resource belongs to the currently logged-in user.

---

# 20. Frontend API Integration

The React frontend will communicate with these APIs using Axios.

Recommended frontend API organization:

```text
src/
├── api/
│   ├── userApi.js
│   ├── addressApi.js
│   ├── categoryApi.js
│   ├── productApi.js
│   ├── cartApi.js
│   ├── orderApi.js
│   └── paymentApi.js
│
├── store/
├── components/
├── pages/
├── routes/
└── App.jsx
```

Axios should automatically attach the JWT to authenticated requests.

Example:

```text
Authorization: Bearer <JWT_TOKEN>
```

This keeps authentication logic centralized instead of manually adding the token to every API call.

---

# 21. API Design Summary

The final clicKart API design follows these principles:

1. Use RESTful resource-oriented URLs.
2. Use JWT for authentication.
3. Use `/me` for currently authenticated user resources.
4. Do not trust client-provided user IDs for user-owned resources.
5. Verify resource ownership at the service layer.
6. Use role-based authorization for administrative operations.
7. Use DTOs for API communication.
8. Validate incoming requests.
9. Use centralized exception handling.
10. Keep payment gateway credentials on the backend.
11. Keep business logic inside the service layer.
12. Keep controllers focused on HTTP operations.
13. Keep external payment integration inside the payment gateway abstraction.
