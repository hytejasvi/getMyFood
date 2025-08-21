# 🍔 Food Delivery Platform

## 🎯 Project Goal
This is a personal technical project to design and implement a **scalable and resilient backend system** for a food delivery platform.
The primary goal is to **gain hands-on experience** and demonstrate proficiency in:

- **Microservices Architecture** → decomposing into independent, single-responsibility services.
- **Event-Driven Messaging** → asynchronous communication via **Kafka** for decoupled, highly available systems.
- **RESTful APIs** → clean, well-documented APIs for each service.
- **Data Integrity** → tackling distributed system challenges using the **Transactional Outbox Pattern** to ensure consistency.

---

## ⚙️ Approach
The project is built with **Java + Spring Boot**, following a **modular and iterative approach**. Each phase introduces new architectural concepts:

### Phase 1 → Core Microservices
- Build **Order Service**, **Restaurant Service**, and **User Service**.
- Each service has its own database and REST API.

### Phase 2 → Event-Driven Communication
- Integrate **Kafka** for inter-service communication.
- Achieve decoupling, scalability, and resilience.

### Phase 3 → Transactional Outbox
- Ensure **data consistency** between database and Kafka.
- Implement the **Transactional Outbox Pattern** with a relay process.

---

## 📋 TDD / To-Do List

### ✅ Phase 1: Core Services & RESTful APIs
**User Service**
- [ ] Implement user registration API (`POST /users/register`)
- [ ] Implement user login API (`POST /users/login`)
- [ ] Set up database schema for user data

**Restaurant Service**
- [ ] Implement API to list restaurants (`GET /restaurants`)
- [ ] Implement API to fetch a restaurant's menu (`GET /restaurants/{restaurantId}/menu`)
- [ ] Set up database schema for restaurant & menu data

**Order Service**
- [ ] Implement API to create a new order (`POST /orders`)
- [ ] Implement API to get order status (`GET /orders/{orderId}`)
- [ ] Set up database schema for order details

---

### 🚀 Phase 2: Event-Driven Messaging
- [ ] Set up local **Kafka** instance with Docker  
- [ ] Create Kafka topics for order events (e.g., `order-placed`)
- [ ] Integrate **Kafka Producer** in Order Service
- [ ] Integrate **Kafka Consumer** in Restaurant Service

---

### 🔄 Phase 3: Transactional Outbox
- [ ] Add **outbox table** to Order Service DB
- [ ] Implement atomic transaction (order + outbox record)
- [ ] Build message relay to publish outbox → Kafka

---

## 📝 Notes
This `README.md` will be **continuously updated** to reflect progress, challenges, and next steps.
The final outcome will showcase a **production-style distributed backend system** suitable for real-world food delivery use cases.
