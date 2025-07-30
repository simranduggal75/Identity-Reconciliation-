# 🧠 Identity Reconciliation - Bitespeed Backend Task

A Spring Boot-based backend service to manage and reconcile customer identities using email and phone number across orders.

---

## 🔗 Live Endpoint

**POST** [`/identify`](https://identity-reconciliation-production-4b66.up.railway.app/identify)  
➡️ `https://identity-reconciliation-production-4b66.up.railway.app/identify`

---

## 📦 Tech Stack

- **Language**: Java 17  
- **Framework**: Spring Boot 3  
- **Database**: H2 (in-memory)  
- **ORM**: Spring Data JPA  
- **Testing**: JUnit 5 + AssertJ  
- **Build Tool**: Maven  
- **Hosting**: Railway


---

## 🔍 API Overview

### `POST /identify`

Receives email and/or phoneNumber, links or creates identity in the DB.

### Request Body (JSON)

```json
{
  "email": "mcfly@hillvalley.edu",
  "phoneNumber": "123456"
}

### 📁 Postman Collection

You can import the following collection into Postman to quickly test the API:

[`Bitespeed.postman_collection.json`](./Bitespeed.postman_collection.json)
