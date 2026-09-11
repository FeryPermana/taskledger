# TaskLedger Enterprise

TaskLedger Enterprise is a backend-focused Project & Task Management System built with Java and Spring Boot.

This project is developed as a learning and portfolio project to practice building a structured REST API with real-world business rules, database relationships, validation, transaction management, and activity tracking.

The goal is not only to build CRUD functionality, but also to understand how an enterprise-style backend application is designed and organized.

---

## 🚧 Project Status

**Backend Development — In Progress**

### Currently Implemented

- Organization Management
- User Management
- Project Management
- Project Member Management
- Task Management
- Task Assignment
- Task Status Management
- Comment Management
- Activity Log
- Validation
- Global Exception Handling
- DTO & Mapper Pattern
- JPA / Hibernate
- Flyway Database Migration
- Standardized API Response
- Transaction Management

### Currently in Development

- Authentication
- Password Hashing
- Spring Security
- JWT Authentication
- Role-Based Authorization
- Pagination
- Search
- Filtering
- Dashboard
- Testing
- Production Configuration
- Logging
- Vue.js Frontend Integration

---

# 🎯 Project Goals

TaskLedger Enterprise is designed to simulate an internal business application where organizations can manage:

- Users
- Projects
- Project members
- Tasks
- Task assignments
- Task status
- Comments
- Activity history

The main learning objectives are:

- Build REST APIs using Spring Boot
- Understand Spring Data JPA
- Design relational database structures
- Implement entity relationships
- Apply business validation
- Separate DTOs from entities
- Implement service-layer business logic
- Handle exceptions globally
- Manage database transactions
- Implement authentication and authorization
- Write maintainable backend architecture
- Prepare an application for frontend integration

---

# 🏗️ Architecture

The project follows a layered backend architecture.

```text
Client
   │
   ▼
Controller
   │
   ▼
Service
   │
   ▼
Repository
   │
   ▼
Database
````

Supporting layers:

```text
Controller
    │
    ├── Request DTO
    │
    └── Response DTO
            │
            ▼
          Mapper

Service
    │
    ├── Business Rules
    ├── Validation
    └── Transaction Management

Repository
    │
    ▼
JPA / Hibernate
    │
    ▼
MySQL
```

---

# 🛠️ Tech Stack

## Backend

* Java 21
* Spring Boot 4
* Spring Web
* Spring Data JPA
* Hibernate
* Maven
* Bean Validation

## Database

* MySQL 8
* Flyway

## Development Tools

* IntelliJ IDEA
* Postman
* Git
* GitHub

## Planned Frontend

* Vue.js

---

# 📦 Project Structure

```text
taskledger/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── ferry/
│   │   │           └── taskledger/
│   │   │               │
│   │   │               ├── controller/
│   │   │               ├── dto/
│   │   │               │   ├── request/
│   │   │               │   └── response/
│   │   │               ├── entity/
│   │   │               ├── mapper/
│   │   │               ├── repository/
│   │   │               ├── service/
│   │   │               └── ...
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

# 🗄️ Database Design

The current database consists of the following main entities:

```text
organizations
      │
      └── users
            │
            ├── projects
            │       │
            │       ├── project_members
            │       │
            │       └── tasks
            │               │
            │               └── comments
            │
            └── activity_logs
```

## Main Tables

### Organizations

Stores organization information.

```text
organizations
├── id
├── name
├── description
├── status
├── created_at
└── updated_at
```

Organization status:

```text
ACTIVE
INACTIVE
```

---

### Users

Stores users belonging to an organization.

```text
users
├── id
├── organization_id
├── name
├── email
├── password
├── role
├── status
├── created_at
└── updated_at
```

User roles:

```text
SUPER_ADMIN
ADMIN
PROJECT_MANAGER
TEAM_LEAD
MEMBER
```

User status:

```text
ACTIVE
INACTIVE
```

---

### Projects

Stores projects belonging to an organization.

```text
projects
├── id
├── organization_id
├── name
├── description
├── status
├── start_date
├── due_date
├── created_by
├── created_at
└── updated_at
```

Project status:

```text
PLANNING
IN_PROGRESS
ON_HOLD
COMPLETED
CANCELLED
```

---

### Project Members

Defines which users belong to a project.

```text
project_members
├── id
├── project_id
├── user_id
└── created_at
```

A user cannot be added to the same project more than once.

---

### Tasks

Stores tasks belonging to projects.

```text
tasks
├── id
├── project_id
├── title
├── description
├── status
├── priority
├── assignee_id
├── due_date
├── created_by
├── created_at
└── updated_at
```

Task status:

```text
TODO
IN_PROGRESS
IN_REVIEW
DONE
CANCELLED
```

Task priority:

```text
LOW
MEDIUM
HIGH
URGENT
```

---

### Comments

Stores comments related to tasks.

```text
comments
├── id
├── task_id
├── user_id
├── content
├── created_at
└── updated_at
```

Only users who belong to the project can add comments to its tasks.

---

### Activity Logs

Stores important business activities.

```text
activity_logs
├── id
├── organization_id
├── user_id
├── action
├── entity_type
├── entity_id
├── description
└── created_at
```

Example:

```text
action       : CREATED
entity_type  : TASK
entity_id    : 15
description  : Task "Implement Login API" created
```

Another example:

```text
action       : STATUS_CHANGED
entity_type  : TASK
entity_id    : 15
description  : Task status changed from TODO to IN_PROGRESS
```

---

# 🔐 Business Rules

TaskLedger implements several business rules at the service layer.

### Organization

* Organization must exist before being referenced.
* Inactive organizations cannot perform normal business operations.

### User

* User must belong to an organization.
* Email must be unique.
* Inactive users cannot perform normal operations.
* User status is controlled by the backend.

### Project

* Project must belong to an organization.
* Project creator must belong to the same organization.
* Project creator must be active.
* Due date cannot be earlier than start date.

### Project Member

* User and project must exist.
* User must belong to the same organization as the project.
* User must be active.
* A user cannot be added twice to the same project.

### Task

* Task must belong to an existing project.
* Task creator must belong to the project's organization.
* Task creator must be active.
* Assignee must belong to the same organization.
* Assignee must be active.
* Assignee must be a member of the project.
* New tasks start with `TODO`.
* Task priority is controlled through defined enum values.

### Comment

* Task must exist.
* User must exist.
* User must be active.
* User must belong to the same organization as the task.
* User must be a member of the project.

---

# 🌐 REST API

Base URL:

```text
/api
```

All APIs use JSON.

---

## Organizations

```http
GET    /api/organizations
POST   /api/organizations
GET    /api/organizations/{id}
PUT    /api/organizations/{id}
DELETE /api/organizations/{id}
```

---

## Users

```http
GET    /api/users
POST   /api/users
GET    /api/users/{id}
GET    /api/users/organization/{organizationId}
PUT    /api/users/{id}
DELETE /api/users/{id}
PATCH  /api/users/{id}/activate
```

---

## Projects

```http
GET    /api/projects
POST   /api/projects
GET    /api/projects/{id}
PUT    /api/projects/{id}
DELETE /api/projects/{id}
```

---

## Project Members

```http
POST   /api/project-members
GET    /api/project-members/project/{projectId}
DELETE /api/project-members/project/{projectId}/user/{userId}
```

---

## Tasks

```http
GET    /api/tasks
POST   /api/tasks
GET    /api/tasks/{id}
GET    /api/tasks/project/{projectId}
PUT    /api/tasks/{id}
PATCH  /api/tasks/{id}/status
PATCH  /api/tasks/{id}/assignee
DELETE /api/tasks/{id}
```

---

## Comments

```http
POST   /api/comments
GET    /api/comments/task/{taskId}
```

---

## Activity Logs

```http
GET /api/activity-logs/entity?entityType=TASK&entityId=1

GET /api/activity-logs/organization/{organizationId}

GET /api/activity-logs/user/{userId}
```

Activity logs are intended to be generated by business operations rather than directly manipulated by the client.

Currently, Task operations generate activity logs automatically for:

```text
Task Created
Task Updated
Task Status Changed
Task Assignee Changed
```

---

# 📄 API Response Format

The API uses a standardized response structure.

## Success

```json
{
    "status": 200,
    "message": "Success",
    "data": {}
}
```

## Validation Error

```json
{
    "status": 400,
    "message": "Validation failed",
    "errors": {
        "title": "Title is required"
    }
}
```

## Business Error

Example:

```json
{
    "status": 400,
    "message": "Assignee is not a member of this project",
    "data": null
}
```

## Not Found

```json
{
    "status": 404,
    "message": "Task not found",
    "data": null
}
```

---

# 🧩 DTO Pattern

The project separates API requests/responses from JPA entities.

Example:

```text
Request
   ↓
CreateTaskRequest
   ↓
TaskService
   ↓
Task Entity
   ↓
TaskMapper
   ↓
TaskResponse
   ↓
Response
```

This prevents JPA entities from being directly exposed as the API contract.

---

# 🔄 Transaction Management

Task business operations that modify both Task data and Activity Logs use transaction management.

Example:

```text
Create Task
     │
     ├── Save Task
     │
     └── Save Activity Log
             │
             ▼
          COMMIT
```

If an operation fails:

```text
Save Task       ✅
Save Log        ❌
      │
      ▼
   ROLLBACK
```

This helps maintain consistency between business data and activity history.

---

# 🗃️ Database Migration

Database schema changes are managed using Flyway.

Current migrations:

```text
V1__create_organizations_table.sql
V2__create_users_table.sql
V3__create_projects_table.sql
V4__create_project_members_table.sql
V5__create_tasks_table.sql
V6__create_comments_table.sql
V7__create_activity_logs_table.sql
```

Flyway ensures database migrations are versioned and executed in order.

---

# 🚀 Running the Project

## Requirements

Make sure you have:

* Java 21
* MySQL 8
* Maven or Maven Wrapper
* Git

---

## 1. Clone Repository

```bash
git clone https://github.com/FeryPermana/taskledger.git
```

```bash
cd taskledger
```

---

## 2. Create Database

Create a MySQL database:

```sql
CREATE DATABASE taskledger;
```

---

## 3. Configure Database

Update:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.application.name=taskledger

spring.datasource.url=jdbc:mysql://localhost:3306/taskledger
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

---

## 4. Run Application

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Or:

```bash
./mvnw spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

---

# 🧪 Testing

API endpoints are currently tested manually using Postman.

Testing covers:

* Valid requests
* Validation errors
* Resource not found
* Duplicate data
* Inactive users
* Inactive organizations
* Organization consistency
* Project membership
* Task assignment
* Task status changes
* Comment access
* Activity log generation

Automated testing will be expanded as the project progresses.

---

# 🛣️ Development Roadmap

The project is being developed incrementally.

```text
Phase 1 — Foundation
├── Project setup
├── Spring Boot
├── JPA
├── Flyway
├── DTO
├── Mapper
├── Validation
├── Exception Handling
└── Standard API Response

Phase 2 — Core Modules
├── Organization
├── User
├── Project
├── Project Member
├── Task
└── Comment

Phase 3 — Business Tracking
├── Activity Log
└── Transaction Management

Phase 4 — Security
├── Authentication
├── Password Hashing
├── Spring Security
├── JWT
└── Role-Based Authorization

Phase 5 — API Improvements
├── Pagination
├── Search
├── Filtering
└── Dashboard

Phase 6 — Quality & Production
├── Automated Testing
├── Configuration
├── Logging
└── Production Hardening

Phase 7 — Frontend
├── Vue.js
├── Authentication Integration
├── Dashboard
├── Project Management
└── Task Management
```

---

# 📚 What I Am Learning

Through this project, I am practicing:

### Spring Boot

* REST Controller
* Dependency Injection
* Service Layer
* Repository Layer
* Configuration

### Spring Data JPA

* Entity Mapping
* `@ManyToOne`
* Relationships
* Repository Query Methods
* Lazy Loading

### Database

* Relational Database Design
* Foreign Keys
* Unique Constraints
* Indexes
* Database Migration
* Flyway

### Backend Architecture

* Layered Architecture
* DTO Pattern
* Mapper Pattern
* Business Rules
* Exception Handling
* Transaction Management

### API Development

* REST API
* HTTP Methods
* HTTP Status Codes
* Validation
* Standardized API Responses

### Security — Planned

* Password Hashing
* Spring Security
* Authentication
* JWT
* Authorization
* Role-Based Access Control

---

# 💡 Development Approach

The project is intentionally developed step by step.

Instead of implementing every feature at once, each module is built and tested before moving to the next one.

The development principle is:

```text
Understand
    ↓
Design
    ↓
Implement
    ↓
Test
    ↓
Refactor
    ↓
Move to next module
```

The objective is to build a strong understanding of backend development rather than simply completing a tutorial.

---

# 📈 Current Progress

```text
Organization          ████████████████████ 100%
User                  ████████████████████ 100%
Project               ████████████████████ 100%
Project Member        ████████████████████ 100%
Task                  ████████████████████ 100%
Comment               ████████████████████ 100%
Activity Log          ████████████████████ 100%
Transaction           ████████████████████ 100%

Authentication        ░░░░░░░░░░░░░░░░░░░░   0%
JWT                   ░░░░░░░░░░░░░░░░░░░░   0%
Authorization         ░░░░░░░░░░░░░░░░░░░░   0%
Pagination             ░░░░░░░░░░░░░░░░░░░░   0%
Search                ░░░░░░░░░░░░░░░░░░░░   0%
Filtering             ░░░░░░░░░░░░░░░░░░░░   0%
Dashboard             ░░░░░░░░░░░░░░░░░░░░   0%
Testing               ░░░░░░░░░░░░░░░░░░░░   0%
Vue.js Frontend       ░░░░░░░░░░░░░░░░░░░░   0%
```

> Progress represents the current development stage of the project and will change as new modules are implemented.

---

# 🔗 Repository

GitHub:

[https://github.com/FeryPermana/taskledger](https://github.com/FeryPermana/taskledger)

---

# 👨‍💻 Author

**Muhammad Pandi Ferry Permana**

Full Stack Web Developer
Focused on PHP, Laravel, Vue.js, and currently expanding backend expertise with Java & Spring Boot.

---

# 📌 Note

TaskLedger Enterprise is an ongoing learning and portfolio project.

The architecture, business rules, APIs, and implementation may evolve as new concepts are learned and the application becomes more complete.

```