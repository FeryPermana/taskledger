# TaskLedger Enterprise

TaskLedger Enterprise adalah sistem Manajemen Proyek & Tugas yang berfokus pada backend dan dibangun menggunakan Java dan Spring Boot.

Project ini dikembangkan sebagai project pembelajaran dan portofolio untuk berlatih membangun REST API yang terstruktur dengan aturan bisnis nyata, relasi database, validasi, manajemen transaksi, dan pencatatan aktivitas.

Tujuannya bukan hanya membuat fungsi CRUD, tetapi juga memahami bagaimana aplikasi backend bergaya enterprise dirancang dan diorganisasi.

---

## 🚧 Status Project

**Backend Development — In Progress**

### Sudah Diimplementasikan

- Organization Management

- User Management

- Manajemen Proyek

- Project Member Management

- Manajemen Tugas

- Task Assignment

- Task Status Management

- Comment Management

- Log Aktivitas

- Validasi

- Global Penanganan Exception

- DTO & Mapper Pattern

- JPA / Hibernate

- Flyway Migrasi Database

- Standardized API Response

- Manajemen Transaksi

### Sedang Dikembangkan

- Autentikasi

- Hash Password

- Spring Security

- JWT Autentikasi

- Otorisasi Berbasis Role

- Pagination

- Search

- Filtering

- Dashboard

- Testing

- Production Konfigurasi

- Logging

- Vue.js Frontend Integration

---

# 🎯 Tujuan Project

TaskLedger Enterprise dirancang untuk mensimulasikan aplikasi internal perusahaan yang memungkinkan organisasi mengelola:

- Users

- Projects

- Project members

- Tasks

- Task assignments

- Task status

- Comments

- Activity history

Tujuan pembelajaran utama:

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

# 🏗️ Arsitektur

Project ini menggunakan arsitektur backend berlapis.

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

Lapisan pendukung:

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

    ├── Aturan Bisnis

    ├── Validasi

    └── Manajemen Transaksi

Repository

    │

    ▼

JPA / Hibernate

    │

    ▼

MySQL

```

---

# 🛠️ Teknologi yang Digunakan

## Backend

* Java 21

* Spring Boot 4

* Spring Web

* Spring Data JPA

* Hibernate

* Maven

* Bean Validasi

## Database

* MySQL 8

* Flyway

## Tools Pengembangan

* IntelliJ IDEA

* Postman

* Git

* GitHub

## Frontend yang Direncanakan

* Vue.js

---

# 📦 Struktur Project

```text

taskledger/

│

├── src/

│   ├── main/

│   │   ├── java/

│   │   │   └── com/

│   │   │       └── ferry/

│   │   │           └── taskledger/

│   │   │               │

│   │   │               ├── controller/

│   │   │               ├── dto/

│   │   │               │   ├── request/

│   │   │               │   └── response/

│   │   │               ├── entity/

│   │   │               ├── mapper/

│   │   │               ├── repository/

│   │   │               ├── service/

│   │   │               └── ...

│   │   │

│   │   └── resources/

│   │       ├── db/

│   │       │   └── migration/

│   │       └── application.properties

│   │

│   └── test/

│

├── pom.xml

├── mvnw

├── mvnw.cmd

└── README.md

```

---

# 🗄️ Desain Database

Database saat ini terdiri dari entitas utama berikut:

```text

organizations

      │

      └── users

            │

            ├── projects

            │       │

            │       ├── project_members

            │       │

            │       └── tasks

            │               │

            │               └── comments

            │

            └── activity_logs

```

## Tabel Utama

### Organisasi

Menyimpan informasi organisasi.

```text

organizations

├── id

├── name

├── description

├── status

├── created_at

└── updated_at

```

Status organisasi:

```text

ACTIVE

INACTIVE

```

---

### Pengguna

Menyimpan pengguna yang tergabung dalam organisasi.

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

Role pengguna:

```text

SUPER_ADMIN

ADMIN

PROJECT_MANAGER

TEAM_LEAD

MEMBER

```

Status pengguna:

```text

ACTIVE

INACTIVE

```

---

### Proyek

Menyimpan proyek yang dimiliki oleh organisasi.

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

Status proyek:

```text

PLANNING

IN_PROGRESS

ON_HOLD

COMPLETED

CANCELLED

```

---

### Anggota Proyek

Menentukan pengguna yang tergabung dalam suatu proyek.

```text

project_members

├── id

├── project_id

├── user_id

└── created_at

```

Seorang pengguna tidak dapat ditambahkan lebih dari satu kali ke proyek yang sama.

---

### Tugas

Menyimpan tugas yang terkait dengan proyek.

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

Status tugas:

```text

TODO

IN_PROGRESS

IN_REVIEW

DONE

CANCELLED

```

Prioritas tugas:

```text

LOW

MEDIUM

HIGH

URGENT

```

---

### Komentar

Menyimpan komentar yang terkait dengan tugas.

```text

comments

├── id

├── task_id

├── user_id

├── content

├── created_at

└── updated_at

```

Hanya pengguna yang tergabung dalam proyek yang dapat menambahkan komentar pada tugasnya.

---

### Log Aktivitas

Menyimpan aktivitas bisnis yang penting.

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

Contoh:

```text

action       : CREATED

entity_type  : TASK

entity_id    : 15

description  : Task "Implement Login API" created

```

Contoh lainnya:

```text

action       : STATUS_CHANGED

entity_type  : TASK

entity_id    : 15

description  : Task status changed from TODO to IN_PROGRESS

```

---

# 🔐 Aturan Bisnis

TaskLedger menerapkan berbagai aturan bisnis pada service layer.

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

URL Dasar:

```text

/api

```

Semua API menggunakan JSON.

---

## Organisasi

```http

GET    /api/organizations

POST   /api/organizations

GET    /api/organizations/{id}

PUT    /api/organizations/{id}

DELETE /api/organizations/{id}

```

---

## Pengguna

```http

GET    /api/users

POST   /api/users

GET    /api/users/{id}

GET    /api/users/organization/{organizationId}

PUT    /api/users/{id}

DELETE /api/users/{id}

PATCH  /api/users/{id}/activate

```

---

## Proyek

```http

GET    /api/projects

POST   /api/projects

GET    /api/projects/{id}

PUT    /api/projects/{id}

DELETE /api/projects/{id}

```

---

## Anggota Proyek

```http

POST   /api/project-members

GET    /api/project-members/project/{projectId}

DELETE /api/project-members/project/{projectId}/user/{userId}

```

---

## Tugas

```http

GET    /api/tasks

POST   /api/tasks

GET    /api/tasks/{id}

GET    /api/tasks/project/{projectId}

PUT    /api/tasks/{id}

PATCH  /api/tasks/{id}/status

PATCH  /api/tasks/{id}/assignee

DELETE /api/tasks/{id}

```

---

## Komentar

```http

POST   /api/comments

GET    /api/comments/task/{taskId}

```

---

## Log Aktivitas

```http

GET /api/activity-logs/entity?entityType=TASK&entityId=1

GET /api/activity-logs/organization/{organizationId}

GET /api/activity-logs/user/{userId}

```

Activity logs are intended to be generated by business operations rather than directly manipulated by the client.

Currently, Task operations generate activity logs automatically for:

```text

Tugas Dibuat

Tugas Diperbarui

Status Tugas Diubah

Penanggung Jawab Tugas Diubah

```

---

# 📄 Format Respons API

API menggunakan struktur respons yang terstandarisasi.

## Berhasil

```json

{

    "status": 200,

    "message": "Success",

    "data": {}

}

```

## Error Validasi

```json

{

    "status": 400,

    "message": "Validasi failed",

    "errors": {

        "title": "Title is required"

    }

}

```

## Error Bisnis

Contoh:

```json

{

    "status": 400,

    "message": "Assignee is not a member of this project",

    "data": null

}

```

## Tidak Ditemukan

```json

{

    "status": 404,

    "message": "Task not found",

    "data": null

}

```

---

# 🧩 Pola DTO

Project ini memisahkan request/response API dari entity JPA.

Contoh:

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

Hal ini mencegah entity JPA diekspos secara langsung sebagai kontrak API.

---

# 🔄 Manajemen Transaksi

Operasi bisnis Task yang mengubah data Task dan Log Aktivitas menggunakan manajemen transaksi.

Contoh:

```text

Create Task

     │

     ├── Save Task

     │

     └── Save Log Aktivitas

             │

             ▼

          COMMIT

```

Jika sebuah operasi gagal:

```text

Save Task       ✅

Save Log        ❌

      │

      ▼

   ROLLBACK

```

Hal ini membantu menjaga konsistensi antara data bisnis dan riwayat aktivitas.

---

# 🗃️ Migrasi Database

Perubahan struktur database dikelola menggunakan Flyway.

Migrasi saat ini:

```text

V1__create_organizations_table.sql

V2__create_users_table.sql

V3__create_projects_table.sql

V4__create_project_members_table.sql

V5__create_tasks_table.sql

V6__create_comments_table.sql

V7__create_activity_logs_table.sql

```

Flyway memastikan migrasi database memiliki versi dan dijalankan secara berurutan.

---

# 🚀 Menjalankan Project

## Persyaratan

Pastikan tersedia:

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

## 2. Membuat Database

Buat database MySQL:

```sql

CREATE DATABASE taskledger;

```

---

## 3. Konfigurasi Database

Ubah:

```text

src/main/resources/application.properties

```

Contoh:

```properties

spring.application.name=taskledger

spring.datasource.url=jdbc:mysql://localhost:3306/taskledger

spring.datasource.username=root

spring.datasource.password=

spring.jpa.hibernate.ddl-auto=validate

spring.jpa.show-sql=true

```

---

## 4. Menjalankan Aplikasi

Windows:

```powershell

.\mvnw.cmd spring-boot:run

```

Atau:

```bash

./mvnw spring-boot:run

```

Aplikasi berjalan di:

```text

http://localhost:8080

```

---

# 🧪 Pengujian

Endpoint API saat ini diuji secara manual menggunakan Postman.

Pengujian mencakup:

* Valid requests

* Validasi errors

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

Pengujian otomatis akan dikembangkan seiring kemajuan project.

---

# 🛣️ Roadmap Pengembangan

Project dikembangkan secara bertahap.

```text

Tahap 1 — Fondasi

├── Setup Project

├── Spring Boot

├── JPA

├── Flyway

├── DTO

├── Mapper

├── Validasi

├── Penanganan Exception

└── Respons API Terstandarisasi

Tahap 2 — Modul Inti

├── Organization

├── User

├── Project

├── Project Member

├── Task

└── Comment

Tahap 3 — Pelacakan Bisnis

├── Log Aktivitas

└── Manajemen Transaksi

Tahap 4 — Keamanan

├── Autentikasi

├── Hash Password

├── Spring Security

├── JWT

└── Otorisasi Berbasis Role

Tahap 5 — Peningkatan API

├── Pagination

├── Search

├── Filtering

└── Dashboard

Tahap 6 — Kualitas & Produksi

├── Pengujian Otomatis

├── Konfigurasi

├── Logging

└── Penguatan untuk Produksi

Tahap 7 — Frontend

├── Vue.js

├── Autentikasi Integration

├── Dashboard

├── Manajemen Proyek

└── Manajemen Tugas

```

---

# 📚 Yang Sedang Dipelajari

Melalui project ini, saya sedang mempraktikkan:

### Spring Boot

* REST Controller

* Dependency Injection

* Service Layer

* Repository Layer

* Konfigurasi

### Spring Data JPA

* Pemetaan Entity

* `@ManyToOne`

* Relasi

* Query Method Repository

* Lazy Loading

### Database

* Desain Database Relasional

* Foreign Key

* Unique Constraint

* Index

* Migrasi Database

* Flyway

### Arsitektur Backend

* Arsitektur Berlapis

* DTO Pattern

* Mapper Pattern

* Aturan Bisnis

* Penanganan Exception

* Manajemen Transaksi

### Pengembangan API

* REST API

* HTTP Method

* HTTP Status Code

* Validasi

* Respons API Terstandarisasi

### Keamanan — Direncanakan

* Hash Password

* Spring Security

* Autentikasi

* JWT

* Authorization

* Role-Based Access Control

---

# 💡 Pendekatan Pengembangan

Project ini sengaja dikembangkan secara bertahap.

Daripada mengimplementasikan semua fitur sekaligus, setiap modul dibangun dan diuji terlebih dahulu sebelum melanjutkan ke modul berikutnya.

Prinsip pengembangannya adalah:

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

Tujuannya adalah membangun pemahaman backend yang kuat, bukan sekadar menyelesaikan tutorial.

---

# 📈 Progress Saat Ini

```text

Organization          ████████████████████ 100%

User                  ████████████████████ 100%

Project               ████████████████████ 100%

Project Member        ████████████████████ 100%

Task                  ████████████████████ 100%

Comment               ████████████████████ 100%

Log Aktivitas          ████████████████████ 100%

Transaction           ████████████████████ 100%

Autentikasi        ░░░░░░░░░░░░░░░░░░░░   0%

JWT                   ░░░░░░░░░░░░░░░░░░░░   0%

Authorization         ░░░░░░░░░░░░░░░░░░░░   0%

Pagination             ░░░░░░░░░░░░░░░░░░░░   0%

Search                ░░░░░░░░░░░░░░░░░░░░   0%

Filtering             ░░░░░░░░░░░░░░░░░░░░   0%

Dashboard             ░░░░░░░░░░░░░░░░░░░░   0%

Testing               ░░░░░░░░░░░░░░░░░░░░   0%

Vue.js Frontend       ░░░░░░░░░░░░░░░░░░░░   0%

```

> Progress menunjukkan tahap pengembangan project saat ini dan akan berubah seiring implementasi modul baru.

---

# 🔗 Repository

GitHub:

[https://github.com/FeryPermana/taskledger](https://github.com/FeryPermana/taskledger)

---

# 👨‍💻 Pengembang

**Muhammad Pandi Ferry Permana**

Full Stack Web Developer

Berfokus pada PHP, Laravel, Vue.js, dan saat ini memperluas keahlian backend dengan Java & Spring Boot.

---

# 📌 Catatan

TaskLedger Enterprise adalah project pembelajaran dan portofolio yang masih terus dikembangkan.

Arsitektur, aturan bisnis, API, dan implementasi dapat berkembang seiring dipelajarinya konsep baru dan aplikasi menjadi semakin lengkap.

```