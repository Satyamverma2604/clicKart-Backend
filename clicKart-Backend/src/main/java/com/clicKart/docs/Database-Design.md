# clicKart — Database Design

## 1. Database Overview

clicKart uses **MySQL** as its relational database.

The database is designed to support the complete e-commerce workflow:

```text
User
 ↓
Address
 ↓
Product + Category
 ↓
Cart
 ↓
Order
 ↓
Payment
```

The database follows a relational model where entities are connected using primary keys and foreign keys.

### Main Objectives

The database design focuses on:

* Data consistency
* Referential integrity
* Proper entity relationships
* Reduced data duplication
* Transactional consistency
* Maintainability
* Scalability

---

# 2. Database Tables

The major tables in clicKart are:

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

Each table represents a specific business entity or supporting entity.

---

# 3. Users Table

The `users` table stores customer and administrator information.

### Responsibilities

* Store user profile information
* Store authentication credentials
* Store user role
* Store account status
* Maintain audit timestamps

### Main Fields

| Field        | Description                     |
| ------------ | ------------------------------- |
| `id`         | Primary key                     |
| `name`       | User name                       |
| `email`      | User email                      |
| `password`   | Encrypted/hashed password       |
| `phone`      | User phone number               |
| `role`       | User role such as USER or ADMIN |
| `enabled`    | Account activation status       |
| `created_at` | Account creation timestamp      |
| `updated_at` | Last update timestamp           |

### Constraints

* `id` is the primary key.
* `email` should be unique.
* `phone` should be unique where required.
* `role` determines the user's authorization level.
* `enabled` determines whether the account is active.

---

# 4. Addresses Table

The `addresses` table stores shipping addresses associated with users.

### Relationship

```text
User 1 ─────────── N Address
```

A user can have multiple addresses.

### Main Fields

| Field          | Description                       |
| -------------- | --------------------------------- |
| `id`           | Primary key                       |
| `user_id`      | Foreign key referencing users     |
| `house_no`     | House/flat number                 |
| `street`       | Street name                       |
| `city`         | City                              |
| `state`        | State                             |
| `country`      | Country                           |
| `postal_code`  | Postal/ZIP code                   |
| `address_type` | Address type such as HOME or WORK |

### Foreign Key

```text
addresses.user_id
        ↓
users.id
```

### Business Rule

An address belongs to a specific user.

Therefore, address operations must verify ownership before allowing a user to update or delete an address.

---

# 5. Categories Table

The `categories` table stores product categories.

### Main Fields

| Field         | Description          |
| ------------- | -------------------- |
| `id`          | Primary key          |
| `name`        | Category name        |
| `description` | Category description |

### Relationship

```text
Category 1 ─────────── N Product
```

A category can contain multiple products.

---

# 6. Products Table

The `products` table stores products available in the e-commerce application.

### Main Fields

| Field            | Description                      |
| ---------------- | -------------------------------- |
| `id`             | Primary key                      |
| `name`           | Product name                     |
| `description`    | Product description              |
| `brand`          | Product brand                    |
| `price`          | Product price                    |
| `stock_quantity` | Available inventory              |
| `active`         | Product availability             |
| `category_id`    | Foreign key referencing category |
| `created_at`     | Creation timestamp               |
| `updated_at`     | Last update timestamp            |

### Foreign Key

```text
products.category_id
        ↓
categories.id
```

### Product Availability

The `active` field determines whether a product is currently available for normal purchasing operations.

For example:

```text
active = true
    ↓
Product available

active = false
    ↓
Product unavailable
```

### Business Validations

The backend validates:

* Required product information
* Product price
* Stock quantity
* Category existence
* Duplicate product conditions
* Product availability

---

# 7. Carts Table

The `carts` table stores shopping carts associated with users.

### Relationship

```text
User 1 ─────────── 1 Cart
```

Each user has one active cart.

### Main Fields

| Field        | Description                   |
| ------------ | ----------------------------- |
| `id`         | Primary key                   |
| `user_id`    | Foreign key referencing users |
| `created_at` | Cart creation timestamp       |
| `updated_at` | Last update timestamp         |

### Important Constraint

`user_id` is unique because one user should have only one active cart.

```text
users.id
    │
    │ 1 : 1
    ▼
carts.user_id
```

---

# 8. Cart Items Table

The `cart_items` table represents individual products inside a cart.

### Relationship

```text
Cart 1 ─────────── N CartItem
```

### Main Fields

| Field        | Description                     |
| ------------ | ------------------------------- |
| `id`         | Primary key                     |
| `cart_id`    | Foreign key referencing cart    |
| `product_id` | Foreign key referencing product |
| `quantity`   | Requested product quantity      |

### Relationships

```text
cart_items.cart_id
        ↓
carts.id
```

and:

```text
cart_items.product_id
        ↓
products.id
```

### Example

```text
Cart #1
│
├── Product #10 × 2
├── Product #15 × 1
└── Product #20 × 3
```

The cart table itself does not need to store multiple product columns.

Instead, `cart_items` acts as the association between the cart and products.

### Business Rules

The backend validates:

* Product exists
* Product is active
* Quantity is greater than zero
* Requested quantity does not exceed available stock
* Product exists in the cart before removal

---

# 9. Orders Table

The `orders` table stores customer order information.

### Main Responsibilities

* Store order ownership
* Store shipping address
* Store order date
* Store order status
* Store total amount
* Store payment status
* Maintain timestamps

### Main Fields

| Field                 | Description                      |
| --------------------- | -------------------------------- |
| `id`                  | Primary key                      |
| `user_id`             | Foreign key referencing user     |
| `shipping_address_id` | Foreign key referencing address  |
| `order_date`          | Date/time when order was created |
| `status`              | Current order status             |
| `total_amount`        | Total order amount               |
| `payment_status`      | Current payment status           |
| `created_at`          | Creation timestamp               |
| `updated_at`          | Last update timestamp            |

### Relationships

```text
User 1 ─────────── N Order
```

and:

```text
Address 1 ─────── N Order
```

and:

```text
Order 1 ────────── N OrderItem
```

### Important Design Decision

The order stores a reference to the shipping address used during checkout.

This allows the order to identify the shipping address associated with the transaction.

---

# 10. Order Items Table

The `order_items` table stores individual products belonging to an order.

### Relationship

```text
Order 1 ─────────── N OrderItem
```

### Main Fields

| Field        | Description                     |
| ------------ | ------------------------------- |
| `id`         | Primary key                     |
| `order_id`   | Foreign key referencing order   |
| `product_id` | Foreign key referencing product |
| `quantity`   | Purchased quantity              |
| `price`      | Product price at order time     |

The exact fields should remain synchronized with the current `OrderItem` entity.

### Example

```text
Order #101
│
├── Product #10 × 2
├── Product #15 × 1
└── Product #20 × 3
```

### Historical Data

Order items are important because an order represents a historical transaction.

Product information such as the current catalog price may change later.

Therefore, order-related data should preserve the information required to correctly represent the original transaction.

---

# 11. Payments Table

The `payments` table stores payment information associated with an order.

### Relationship

```text
Order 1 ─────────── 1 Payment
```

### Main Fields

| Field                 | Description                   |
| --------------------- | ----------------------------- |
| `id`                  | Primary key                   |
| `order_id`            | Foreign key referencing order |
| `razorpay_order_id`   | Razorpay order identifier     |
| `razorpay_payment_id` | Razorpay payment identifier   |
| `amount`              | Payment amount                |
| `currency`            | Payment currency              |
| `status`              | Payment status                |
| `payment              |                               |
