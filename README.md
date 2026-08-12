# Restaurant Platform

A production-style Restaurant Management System built using **Spring Boot Microservices**.

> This project is being developed step by step to learn real-world backend architecture, distributed systems, and cloud-native development.

---

# Tech Stack

## Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Validation

## Database
- MySQL

## Build Tool
- Maven

## Version Control
- Git
- GitHub

---

# Planned Technologies

As the project evolves, the following technologies will be integrated:

- Spring Cloud Config
- Eureka Service Discovery
- Spring Cloud Gateway
- OpenFeign
- Resilience4j
- Redis
- Apache Kafka
- Docker
- Docker Compose
- Kubernetes
- Jenkins
- AWS

---

# Architecture (Current)

```
                +----------------------+
                |  Restaurant Service  |
                +----------------------+
```

---

# Planned Architecture

```
                        Client
                           |
                           |
                    API Gateway
                           |
        ---------------------------------------
        |         |          |         |       |
        |         |          |         |       |
 Restaurant   User     Order    Payment  Notification
  Service    Service   Service   Service     Service
        |
    MySQL Database
```

> This architecture will be implemented gradually.

---

# Learning Roadmap

## Phase 1 - Foundation

- [x] Project setup
- [x] Git & GitHub setup
- [x] Initial repository structure
- [ ] Complete Restaurant Service
- [ ] CRUD APIs
- [ ] Validation
- [ ] Exception Handling
- [ ] Swagger

---

## Phase 2 - Microservices Fundamentals

- [ ] Service Discovery (Eureka)
- [ ] API Gateway
- [ ] Inter-service Communication
- [ ] OpenFeign
- [ ] Centralized Configuration

---

## Phase 3 - Production Features

- [ ] Redis
- [ ] Kafka
- [ ] Circuit Breaker
- [ ] Distributed Logging

---

## Phase 4 - DevOps

- [ ] Docker
- [ ] Docker Compose
- [ ] Kubernetes
- [ ] Jenkins CI/CD

---

## Phase 5 - Cloud

- [ ] AWS Deployment

---

# Current Project Structure

```
restaurant-platform
│
├── README.md
│
├── restaurant-service
│   ├── src
│   ├── pom.xml
│   ├── mvnw
│   └── ...
│
├── user-service            (Upcoming)
├── order-service           (Upcoming)
├── payment-service         (Upcoming)
├── inventory-service       (Upcoming)
├── notification-service    (Upcoming)
├── discovery-server        (Upcoming)
├── api-gateway             (Upcoming)
└── config-server           (Upcoming)
```

---

# Git Commit History

| Commit | Status |
|---------|--------|
| Initial restaurant service setup | ✅ |

---

# Progress

Current Progress:

**Phase 1 → In Progress**

Next Milestone:

**Complete Restaurant Service**

---

# License

This project is created for learning purposes and portfolio demonstration.