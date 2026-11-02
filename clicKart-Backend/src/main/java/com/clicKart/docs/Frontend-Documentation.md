# clicKart — Frontend Documentation

## 1. Frontend Overview

The clicKart frontend is built using **React** and communicates with the Spring Boot backend through REST APIs.

### Frontend Technology Stack

| Technology    | Purpose                 |
| ------------- | ----------------------- |
| React         | UI development          |
| React Router  | Client-side routing     |
| Redux Toolkit | Global state management |
| Axios         | REST API communication  |
| JavaScript    | Frontend development    |

The frontend follows a component-based and modular architecture.

---

# 2. Frontend Architecture

High-level frontend architecture:

```text
User
  ↓
React UI
  ↓
React Router
  ↓
Pages
  ↓
Reusable Components
  ↓
Redux Toolkit / Local State
  ↓
API Modules
  ↓
Axios
  ↓
Spring Boot REST API
```

The frontend is responsible for presentation and user interaction.

The backend remains responsible for:

* Authentication
* Authorization
* Business rules
* Validation
* Inventory validation
* Order processing
* Payment verification
* Database operations

---

# 3. Frontend Folder Structure

Recommended structure:

```text
frontend/
│
├── public/
│
├── src/
│   │
│   ├── api/
│   │   ├── axiosInstance.js
│   │   ├── authApi.js
│   │   ├── userApi.js
│   │   ├── addressApi.js
│   │   ├── categoryApi.js
│   │   ├── productApi.js
│   │   ├── cartApi.js
│   │   ├── orderApi.js
│   │   └── paymentApi.js
│   │
│   ├── components/
│   │   ├── common/
│   │   ├── layout/
│   │   ├── product/
│   │   ├── cart/
│   │   ├── order/
│   │   └── admin/
│   │
│   ├── pages/
│   │   ├── auth/
│   │   ├── account/
│   │   ├── admin/
│   │   ├── Home.jsx
│   │   ├── Products.jsx
│   │   ├── ProductDetails.jsx
│   │   ├── CategoryProducts.jsx
│   │   ├── SearchResults.jsx
│   │   ├── Cart.jsx
│   │   ├── Checkout.jsx
│   │   ├── PaymentSuccess.jsx
│   │   └── PaymentFailed.jsx
│   │
│   ├── store/
│   │   ├── store.js
│   │   └── slices/
│   │       ├── authSlice.js
│   │       ├── productSlice.js
│   │       ├── cartSlice.js
│   │       └── orderSlice.js
│   │
│   ├── routes/
│   │   ├── AppRoutes.jsx
│   │   ├── ProtectedRoute.jsx
│   │   └── AdminRoute.jsx
│   │
│   ├── hooks/
│   ├── utils/
│   │
│   ├── App.jsx
│   └── main.jsx
│
└── package.json
```

This structure can evolve as the frontend grows.

---

# 4. Page Architecture

The frontend pages are divided into:

```text
Authentication
Customer Shopping
User Account
Cart & Checkout
Payment
Admin
```

---

# 5. Authentication Pages

Location:

```text
src/pages/auth/
```

Pages:

```text
Login.jsx
Register.jsx
ForgotPassword.jsx       ← Future/Optional
```

### Login

Route:

```text
/login
```

Responsibilities:

* Accept email/password
* Call authentication API
* Receive JWT
* Store authentication state
* Redirect authenticated user

Flow:

```text
Login Form
   ↓
POST Login API
   ↓
Backend Authentication
   ↓
JWT
   ↓
Redux Auth State
   ↓
Home
```

### Register

Route:

```text
/register
```

Responsibilities:

* Collect user information
* Validate form
* Call user registration API
* Redirect to login after successful registration

---

# 6. Main Shopping Pages

## Home

File:

```text
Home.jsx
```

Route:

```text
/
```

or:

```text
/home
```

Responsibilities:

* Store introduction
* Featured products
* Categories
* Search
* Navigation

---

## Products

File:

```text
Products.jsx
```

Route:

```text
/products
```

Responsibilities:

* Display products
* Product filtering
* Product sorting
* Product search
* Navigate to product details

---

## Product Details

File:

```text
ProductDetails.jsx
```

Route:

```text
/products/:id
```

Responsibilities:

* Display product information
* Display price
* Display stock/availability
* Display category
* Select quantity
* Add product to cart

One page handles all products.

Example:

```text
/products/1
/products/2
/products/25
```

---

## Category Products

File:

```text
CategoryProducts.jsx
```

Route:

```text
/category/:categoryId
```

Responsibilities:

* Display products belonging to a category.

Example:

```text
/category/3
```

---

## Search Results

File:

```text
SearchResults.jsx
```

Route:

```text
/search
```

Responsibilities:

* Read search query
* Call product search API
* Display matching products

Example:

```text
/search?name=mouse
```

---

# 7. User Account Pages

Location:

```text
src/pages/account/
```

Recommended pages:

```text
Profile.jsx
Addresses.jsx
Orders.jsx
OrderDetails.jsx
OrderTracking.jsx
```

---

## Profile

Route:

```text
/profile
```

Uses:

```text
GET /api/users/me
PUT /api/users/me
```

Responsibilities:

* Display user information
* Update profile information
* Display account information

---

## Addresses

Route:

```text
/addresses
```

Uses:

```text
POST   /api/users/me/addresses
GET    /api/users/me/addresses
PUT    /api/users/me/addresses/{id}
DELETE /api/users/me/addresses/{id}
```

Responsibilities:

* Display saved addresses
* Add address
* Edit address
* Delete address
* Select address during checkout

---

# 8. Cart Pages

## Cart

File:

```text
Cart.jsx
```

Route:

```text
/cart
```

Backend APIs:

```text
GET    /api/cart
POST   /api/cart/items
PUT    /api/cart/items/{productId}
DELETE /api/cart/items/{productId}
```

Responsibilities:

* Display cart items
* Increase quantity
* Decrease quantity
* Remove product
* Display subtotal/total
* Navigate to checkout

Flow:

```text
Product Details
      ↓
Add to Cart
      ↓
Cart
      ↓
Update Quantity
      ↓
Cart Summary
      ↓
Checkout
```

---

# 9. Checkout Page

File:

```text
Checkout.jsx
```

Route:

```text
/checkout
```

The checkout page combines:

```text
Address
+
Cart Summary
+
Order Creation
+
Payment
```

Flow:

```text
Checkout
   │
   ├── Select Address
   │
   ├── Review Cart
   │
   ├── Create Order
   │
   └── Start Payment
```

---

# 10. Order Pages

## My Orders

File:

```text
Orders.jsx
```

Route:

```text
/orders
```

API:

```text
GET /api/orders
```

Responsibilities:

* Display user's orders
* Show order status
* Navigate to order details
* Navigate to tracking

---

## Order Details

File:

```text
OrderDetails.jsx
```

Route:

```text
/orders/:id
```

Responsibilities:

* Display order information
* Display order items
* Display total
* Display shipping address
* Display payment status
* Display order status
* Allow cancellation if applicable

---

## Order Tracking

File:

```text
OrderTracking.jsx
```

Route:

```text
/orders/:id/tracking
```

Responsibilities:

* Display order progress
* Display current order status

Example:

```text
PENDING
   ↓
CONFIRMED
   ↓
SHIPPED
   ↓
DELIVERED
```

Cancellation:

```text
PENDING / CONFIRMED
        ↓
    CANCELLED
```

---

# 11. Payment Pages

## Payment Success

File:

```text
PaymentSuccess.jsx
```

Route:

```text
/payment/success
```

Responsibilities:

* Display successful payment message
* Display order/payment information
* Navigate to order details

---

## Payment Failed

File:

```text
PaymentFailed.jsx
```

Route:

```text
/payment/failed
```

Responsibilities:

* Display payment failure
* Allow retry
* Return to checkout/order

---

# 12. Razorpay Frontend Flow

```text
Checkout
   ↓
Create Order
   ↓
POST /api/payments/create
   ↓
Receive Razorpay Order Details
   ↓
Open Razorpay Checkout
   ↓
Customer Pays
   ↓
Razorpay Handler
   ↓
Receive:
   - razorpayOrderId
   - razorpayPaymentId
   - razorpaySignature
   ↓
POST /api/payments/verify
   ↓
Backend Verification
   ↓
Payment PAID
   ↓
Order CONFIRMED
   ↓
Payment Success Page
```

Important:

The Razorpay secret key must never be stored in React.

---

# 13. Admin Pages

Location:

```text
src/pages/admin/
```

Recommended pages:

```text
AdminDashboard.jsx

AdminProducts.jsx
AddProduct.jsx
EditProduct.jsx

AdminCategories.jsx
AddCategory.jsx
EditCategory.jsx

AdminOrders.jsx
AdminOrderDetails.jsx

AdminUsers.jsx
```

---

# 14. Admin Dashboard

Route:

```text
/admin/dashboard
```

Responsibilities:

* Display business summary
* Product statistics
* Order statistics
* User statistics
* Inventory information

The exact dashboard metrics can be expanded later.

---

# 15. Admin Product Management

Pages:

```text
/admin/products
/admin/products/add
/admin/products/edit/:id
```

Responsibilities:

### Product List

* Display products
* Search products
* Edit product
* Delete/deactivate product

### Add Product

* Product name
* Description
* Brand
* Price
* Stock
* Category

### Edit Product

* Update product information
* Update price
* Update stock
* Update category
* Update active status

---

# 16. Admin Category Management

Pages:

```text
/admin/categories
/admin/categories/add
/admin/categories/edit/:id
```

Responsibilities:

* List categories
* Add category
* Edit category
* Delete category

---

# 17. Admin Order Management

Pages:

```text
/admin/orders
/admin/orders/:id
```

Responsibilities:

* View orders
* View order details
* View customer information
* View order status
* Manage order lifecycle according to backend capabilities

---

# 18. Admin User Management

Page:

```text
/admin/users
```

Responsibilities may include:

* View users
* View user details
* Enable/disable users
* Manage roles according to backend authorization rules

---

# 19. React Router Structure

Recommended route structure:

```text
/
├── /login
├── /register
│
├── /home
├── /products
├── /products/:id
├── /category/:categoryId
├── /search
│
├── /cart
├── /checkout
├── /payment/success
├── /payment/failed
│
├── /profile
├── /addresses
├── /orders
├── /orders/:id
├── /orders/:id/tracking
│
└── /admin
    ├── /dashboard
    ├── /products
    ├── /products/add
    ├── /products/edit/:id
    ├── /categories
    ├── /categories/add
    ├── /categories/edit/:id
    ├── /orders
    ├── /orders/:id
    └── /users
```

---

# 20. Route Protection

The frontend uses protected routes for authenticated pages.

```text
ProtectedRoute.jsx
```

Conceptual flow:

```text
Protected Page
      ↓
Is User Logged In?
      │
   ┌──┴──┐
   │     │
  YES    NO
   │     │
   ▼     ▼
 Page   /login
```

Admin routes use:

```text
AdminRoute.jsx
```

Flow:

```text
Admin Page
     ↓
Authenticated?
     │
     ▼
Role = ADMIN?
     │
 ┌───┴────┐
 │        │
YES       NO
 │        │
 ▼        ▼
Page    Unauthorized
```

Frontend protection is for user experience.

The backend remains responsible for actual authorization.

---

# 21. Reusable Components

Pages should be built using reusable components.

## Common Components

```text
src/components/common/
```

Recommended:

```text
Button.jsx
Input.jsx
Modal.jsx
Loader.jsx
ErrorMessage.jsx
```

---

## Layout Components

```text
src/components/layout/
```

Recommended:

```text
Navbar.jsx
Footer.jsx
MainLayout.jsx
```

---

## Product Components

```text
src/components/product/
```

Recommended:

```text
ProductCard.jsx
ProductGrid.jsx
ProductFilter.jsx
ProductSearch.jsx
```

---

## Cart Components

```text
src/components/cart/
```

Recommended:

```text
CartItem.jsx
CartSummary.jsx
```

---

## Order Components

```text
src/components/order/
```

Recommended:

```text
OrderCard.jsx
OrderItem.jsx
OrderStatus.jsx
```

---

## Admin Components

```text
src/components/admin/
```

Recommended:

```text
AdminSidebar.jsx
AdminNavbar.jsx
AdminTable.jsx
```

---

# 22. API Architecture

Frontend API calls should be separated from UI components.

Location:

```text
src/api/
```

Files:

```text
axiosInstance.js
authApi.js
userApi.js
addressApi.js
categoryApi.js
productApi.js
cartApi.js
orderApi.js
paymentApi.js
```

Architecture:

```text
React Page
   ↓
API Module
   ↓
Axios Instance
   ↓
Backend API
```

Avoid writing raw Axios calls repeatedly inside every component.

---

# 23. Axios Architecture

`axiosInstance.js` should centralize:

* Backend base URL
* JWT attachment
* Common request configuration
* Common response/error handling

Conceptually:

```text
Component
   ↓
productApi.js
   ↓
axiosInstance
   ↓
JWT Interceptor
   ↓
Spring Boot
```

---

# 24. Redux Architecture

Redux Toolkit will manage application-wide state.

Recommended slices:

```text
src/store/slices/

authSlice.js
productSlice.js
cartSlice.js
orderSlice.js
```

Possible state:

```text
auth
├── user
├── token
└── isAuthenticated

cart
├── items
├── totalAmount
└── loading

products
├── products
├── selectedProduct
└── loading

orders
├── orders
├── selectedOrder
└── loading
```

Not all state should be stored in Redux.

Use local React state for component-specific temporary state.

---

# 25. Authentication State

Authentication flow:

```text
Login
  ↓
Receive JWT
  ↓
Store Authentication State
  ↓
Axios Sends JWT
  ↓
Protected API Access
```

On logout:

```text
Logout
  ↓
Clear Authentication State
  ↓
Clear Token
  ↓
Redirect to Login/Home
```

---

# 26. Main Customer Journey

The complete customer journey:

```text
Register
   ↓
Login
   ↓
Home
   ↓
Browse Products
   ↓
Product Details
   ↓
Add to Cart
   ↓
Cart
   ↓
Checkout
   ↓
Select Address
   ↓
Create Order
   ↓
Razorpay Checkout
   ↓
Payment Verification
   ↓
Payment Success
   ↓
Order Details
   ↓
My Orders
   ↓
Order Tracking
```

---

# 27. Development Phases

Frontend development will follow this order.

## Phase 1 — Foundation

```text
1. React Setup
2. Folder Structure
3. React Router
4. Axios Configuration
5. Redux Store
```

## Phase 2 — Authentication

```text
6. Login
7. Register
8. JWT Handling
9. ProtectedRoute
10. AdminRoute
```

## Phase 3 — Main Shopping

```text
11. Navbar
12. Home
13. Products
14. Product Details
15. Category Products
16. Search
```

## Phase 4 — User

```text
17. Profile
18. Addresses
```

## Phase 5 — Cart

```text
19. Cart
20. Add to Cart
21. Update Quantity
22. Remove Item
23. Cart Summary
```

## Phase 6 — Orders

```text
24. Checkout
25. Address Selection
26. Create Order
27. My Orders
28. Order Details
29. Order Tracking
30. Cancel Order
```

## Phase 7 — Payment

```text
31. Razorpay Checkout
32. Payment Verification
33. Payment Success
34. Payment Failed
35. Retry Payment
```

## Phase 8 — Admin

```text
36. Admin Dashboard
37. Product Management
38. Category Management
39. Order Management
40. User Management
```

## Phase 9 — Final UI

```text
41. Responsive Design
42. Loading States
43. Error States
44. Empty States
45. Notifications
46. Final Testing
```

---

# 28. Frontend Page Checklist

## Authentication

* [ ] Login
* [ ] Register
* [ ] Forgot Password — Optional/Future

## Customer

* [ ] Home
* [ ] Products
* [ ] Product Details
* [ ] Category Products
* [ ] Search Results
* [ ] Cart
* [ ] Checkout
* [ ] Payment Success
* [ ] Payment Failed

## Account

* [ ] Profile
* [ ] Addresses
* [ ] My Orders
* [ ] Order Details
* [ ] Order Tracking

## Admin

* [ ] Admin Dashboard
* [ ] Products
* [ ] Add Product
* [ ] Edit Product
* [ ] Categories
* [ ] Add Category
* [ ] Edit Category
* [ ] Orders
* [ ] Order Details
* [ ] Users

---

# 29. Final Frontend Page Map

```text
clicKart Frontend
│
├── Authentication
│   ├── Login
│   └── Register
│
├── Customer
│   ├── Home
│   ├── Products
│   ├── Product Details
│   ├── Category Products
│   ├── Search
│   ├── Cart
│   ├── Checkout
│   ├── Payment Success
│   └── Payment Failed
│
├── Account
│   ├── Profile
│   ├── Addresses
│   ├── Orders
│   ├── Order Details
│   └── Order Tracking
│
└── Admin
    ├── Dashboard
    ├── Products
    ├── Add Product
    ├── Edit Product
    ├── Categories
    ├── Add Category
    ├── Edit Category
    ├── Orders
    ├── Order Details
    └── Users
```

---

# 30. Frontend Implementation Rule

clicKart frontend will be implemented **one module at a time**.

Recommended implementation order:

```text
React Setup
     ↓
Project Structure
     ↓
Routing
     ↓
Axios
     ↓
Redux
     ↓
Authentication
     ↓
User
     ↓
Address
     ↓
Product
     ↓
Category
     ↓
Cart
     ↓
Order
     ↓
Payment
     ↓
Admin
     ↓
Final UI
```

For each module:

```text
Understand Backend API
        ↓
Create API Function
        ↓
Create Redux State if Required
        ↓
Create Components
        ↓
Create Page
        ↓
Add Route
        ↓
Connect Backend
        ↓
Test
        ↓
Move to Next Module
```

---

# 31. Important Frontend Principles

### 1. Reusable Components

Avoid duplicating UI code.

### 2. API Separation

Keep API calls outside UI components whenever practical.

### 3. Centralized Authentication

JWT handling should be centralized.

### 4. Protected Routes

Authenticated pages should be protected.

### 5. Role-Based UI

Admin functionality should be separated from customer functionality.

### 6. Backend as Final Authority

Never trust frontend validation for security or business-critical rules.

### 7. Loading States

Every API-driven page should handle loading states.

### 8. Error States

API failures should produce useful user-facing feedback.

### 9. Empty States

Handle situations such as:

```text
Empty Cart
No Orders
No Search Results
No Addresses
No Products
```

### 10. Responsive Design

The application should work across:

```text
Desktop
Tablet
Mobile
```

---

# 32. Final Frontend Architecture

```text
                         clicKart Frontend
                                │
          ┌─────────────────────┼─────────────────────┐
          │                     │                     │
          ▼                     ▼                     ▼
    Authentication          Customer UI            Admin UI
          │                     │                     │
          ▼                     ▼                     ▼
       Login              Products / Cart       Dashboard
       Register           Orders / Checkout     Products
                           Account              Categories
                                                Orders
                                                Users
          │                     │                     │
          └─────────────────────┼─────────────────────┘
                                │
                                ▼
                         Redux Toolkit
                                │
                                ▼
                           API Modules
                                │
                                ▼
                              Axios
                                │
                                ▼
                       Spring Boot Backend
```

---

# 33. Documentation Status

Current clicKart documentation:

```text
docs/
│
├── Backend-Documentation.md       ✅
├── Database-Design.md             ✅
├── API-Documentation.md           ✅
├── Architecture.md                ✅
└── Frontend-Documentation.md      ✅
```

The frontend documentation should be updated during implementation if the actual architecture changes.
