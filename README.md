# 🔐 Secure Authentication Backend (Spring Boot + JWT)

## 📌 Overview

This project is a **secure authentication and authorization backend system** built using Spring Boot, Spring Security, JWT, and MySQL.

It provides a production-style implementation of:

* User Registration & Login
* JWT-based Authentication (stateless)
* Refresh Token mechanism
* Role-Based Access Control (USER / ADMIN)
* Account Lock after multiple failed login attempts
* Rate Limiting to prevent abuse

---

## ⚙️ Tech Stack

* **Backend:** Spring Boot
* **Security:** Spring Security
* **Authentication:** JWT (jjwt)
* **Database:** MySQL
* **ORM:** JPA / Hibernate
* **Build Tool:** Maven
* **Testing:** Postman

---

## 🏗️ Architecture

The project follows a layered architecture:

Controller → Service → Repository → Database

* **Controller:** Handles HTTP requests
* **Service:** Contains business logic
* **Repository:** Interacts with database
* **Entity:** Maps Java objects to database tables

---

## 🚀 Features

* 🔐 Secure authentication using JWT
* 🔑 Refresh token support for session continuity
* 👤 Role-based authorization (USER / ADMIN)
* 🔒 Password hashing using BCrypt
* 🚫 Account lock after 5 failed login attempts
* ⚡ Rate limiting for API protection
* ✅ Input validation and global exception handling

---

## 🛢️ Database Design

### Users Table

* id
* username
* email
* password (hashed)
* role
* failed_attempts
* account_locked

### Refresh Tokens Table

* token
* user reference
* expiry
* revoked flag

---

## 🔐 Security Implementation

### 🔸 BCrypt Password Hashing

* Passwords are never stored in plain text
* Hashed using `BCryptPasswordEncoder`
* Verified using `matches()` during login

---

### 🔸 JWT Authentication

* Stateless authentication (no session storage)
* Token contains user identity
* Signed using a secret key

---

### 🔸 JWT Filter

* Intercepts every request
* Extracts token from header
* Validates token
* Sets authentication context

---

### 🔸 Role-Based Access

* `/api/auth/**` → Public
* `/api/user/**` → Authenticated users
* `/api/admin/**` → Admin only

---

### 🔸 Refresh Token

* Stored in database
* Used to generate new access tokens
* Supports token rotation

---

### 🔸 Account Lock

* Locks account after 5 failed attempts
* Prevents brute-force attacks

---

### 🔸 Rate Limiting

* Limits repeated requests
* Protects system from abuse

---

## 🔁 API Endpoints

### 🔐 Authentication

* **POST** `/api/auth/register`
* **POST** `/api/auth/login`
* **POST** `/api/auth/refresh-token`

---

### 👤 User

* **GET** `/api/user/profile`

Header:
Authorization: Bearer <accessToken>

---

### 👑 Admin

* **GET** `/api/admin/dashboard`

---

## 🧪 Testing (Postman)

1. Register a user
2. Login using credentials
3. Copy the access token
4. Use token to call protected APIs

---

## ⚙️ Setup & Run

### 1. Clone Repository

git clone https://github.com/ajayrao-29/secure-auth-system.git

---

### 2. Configure Environment Variables

Windows (PowerShell):

JWT_SECRET=your_base64_secret
DB_URL=jdbc:mysql://localhost:3306/secure_auth_db
DB_USERNAME=root
DB_PASSWORD=your_password

---

### 3. Run Application

./mvnw spring-boot:run

---

## 📁 Project Structure

src/main/java/com/secureauth/system

* controller/
* service/
* repository/
* entity/
* dto/
* security/
* config/
* exception/
* util/

---

## ⚠️ Important Notes

* Do NOT expose real database credentials
* Always use environment variables for secrets
* JWT_SECRET must be strong and Base64 encoded

---

## 📈 Future Improvements

* OAuth2 integration (Google / GitHub login)
* Redis for caching and distributed rate limiting
* Email verification & password reset flow
* API documentation using Swagger/OpenAPI
* Design and integrate a frontend UI (React) to build a full-stack application

---

## 🎯 Key Learnings

* Spring Security internals
* JWT authentication flow
* Secure password handling
* API protection strategies
* Real-world backend security design

---

## 👨‍💻 Author

Ajay Rao

---
