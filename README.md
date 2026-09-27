**# TaskLedger Enterprise**

TaskLedger Enterprise adalah sistem Manajemen Proyek & Tugas yang berfokus pada backend dan dibangun menggunakan Java dan Spring Boot.

Project ini dikembangkan sebagai project pembelajaran dan portofolio untuk berlatih membangun REST API yang terstruktur dengan aturan bisnis nyata, relasi database, validasi, manajemen transaksi, dan pencatatan aktivitas.

Tujuannya bukan hanya membuat fungsi CRUD, tetapi juga memahami bagaimana aplikasi backend bergaya enterprise dirancang dan diorganisasi.

\---

**## ðŸš§ Status Project**

**\*\*Backend Development â€” In Progress\*\***

**### Sudah Diimplementasikan**

\- Organization Management

\- User Management

\- Manajemen Proyek

\- Project Member Management

\- Manajemen Tugas

\- Task Assignment

\- Task Status Management

\- Comment Management

\- Log Aktivitas
- Task Attachment / File Upload
- File Type Validation
- File Size Validation (10 MB)
- File Download / Preview Endpoint

\- Validasi

\- Global Penanganan Exception

\- DTO & Mapper Pattern

\- JPA / Hibernate

\- Flyway Migrasi Database

\- Standardized API Response

\- Manajemen Transaksi

**### Sedang Dikembangkan**

\- Autentikasi

\- Hash Password

\- Spring Security

\- JWT Autentikasi

\- Otorisasi Berbasis Role

\- Pagination

\- Search

\- Filtering

\- Dashboard

\- Testing

\- Production Konfigurasi

\- Logging

\- Vue.js Frontend Integration

\---

**# ðŸŽ¯ Tujuan Project**

TaskLedger Enterprise dirancang untuk mensimulasikan aplikasi internal perusahaan yang memungkinkan organisasi mengelola:

\- Users

\- Projects

\- Project members

\- Tasks

\- Task assignments

\- Task status

\- Comments

\- Activity history

Tujuan pembelajaran utama:

\- Build REST APIs using Spring Boot

\- Understand Spring Data JPA

\- Design relational database structures

\- Implement entity relationships

\- Apply business validation

\- Separate DTOs from entities

\- Implement service-layer business logic

\- Handle exceptions globally

\- Manage database transactions

\- Implement authentication and authorization

\- Write maintainable backend architecture

\- Prepare an application for frontend integration

\---

**# ðŸ—ï¸ Arsitektur**

Project ini menggunakan arsitektur backend berlapis.

\`\`\`text

Client

Â  Â â”‚

Â  Â â–¼

Controller

Â  Â â”‚

Â  Â â–¼

Service

Â  Â â”‚

Â  Â â–¼

Repository

Â  Â â”‚

Â  Â â–¼

Database

\`\`\`\`

Lapisan pendukung:

\`\`\`text

Controller

Â  Â  â”‚

Â  Â  â”œâ”€â”€ Request DTO

Â  Â  â”‚

Â  Â  â””â”€â”€ Response DTO

Â  Â  Â  Â  Â  Â  â”‚

Â  Â  Â  Â  Â  Â  â–¼

Â  Â  Â  Â  Â  Mapper

Service

Â  Â  â”‚

Â  Â  â”œâ”€â”€ Aturan Bisnis

Â  Â  â”œâ”€â”€ Validasi

Â  Â  â””â”€â”€ Manajemen Transaksi

Repository

Â  Â  â”‚

Â  Â  â–¼

JPA / Hibernate

Â  Â  â”‚

Â  Â  â–¼

MySQL

\`\`\`

\---

**# ðŸ› ï¸ Teknologi yang Digunakan**

**## Backend**

\* Java 21

\* Spring Boot 4

\* Spring Web

\* Spring Data JPA

\* Hibernate

\* Maven

\* Bean Validasi

**## Database**

\* MySQL 8

\* Flyway

**## Tools Pengembangan**

\* IntelliJ IDEA

\* Postman

\* Git

\* GitHub

**## Frontend yang Direncanakan**

\* Vue.js

\---

**# ðŸ“¦ Struktur Project**

\`\`\`text

taskledger/

â”‚

â”œâ”€â”€ src/

â”‚ Â  â”œâ”€â”€ main/

â”‚ Â  â”‚ Â  â”œâ”€â”€ java/

â”‚ Â  â”‚ Â  â”‚ Â  â””â”€â”€ com/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  â””â”€â”€ ferry/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  â””â”€â”€ taskledger/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”‚

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”œâ”€â”€ controller/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”œâ”€â”€ dto/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”‚ Â  â”œâ”€â”€ request/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”‚ Â  â””â”€â”€ response/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”œâ”€â”€ entity/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”œâ”€â”€ mapper/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”œâ”€â”€ repository/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”œâ”€â”€ service/

â”‚ Â  â”‚ Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â””â”€â”€ ...

â”‚ Â  â”‚ Â  â”‚

â”‚ Â  â”‚ Â  â””â”€â”€ resources/

â”‚ Â  â”‚ Â  Â  Â  â”œâ”€â”€ db/

â”‚ Â  â”‚ Â  Â  Â  â”‚ Â  â””â”€â”€ migration/

â”‚ Â  â”‚ Â  Â  Â  â””â”€â”€ application.properties

â”‚ Â  â”‚

â”‚ Â  â””â”€â”€ test/

â”‚

â”œâ”€â”€ pom.xml

â”œâ”€â”€ mvnw

â”œâ”€â”€ mvnw\.cmd

â””â”€â”€ README.md

\`\`\`

\---

**# ðŸ—„ï¸ Desain Database**

Database saat ini terdiri dari entitas utama berikut:

\`\`\`text

organizations

Â  Â  Â  â”‚

Â  Â  Â  â””â”€â”€ users

Â  Â  Â  Â  Â  Â  â”‚

Â  Â  Â  Â  Â  Â  â”œâ”€â”€ projects

Â  Â  Â  Â  Â  Â  â”‚ Â  Â  Â  â”‚

Â  Â  Â  Â  Â  Â  â”‚ Â  Â  Â  â”œâ”€â”€ project\_members

Â  Â  Â  Â  Â  Â  â”‚ Â  Â  Â  â”‚

Â  Â  Â  Â  Â  Â  â”‚ Â  Â  Â  â””â”€â”€ tasks

Â  Â  Â  Â  Â  Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â”‚

Â  Â  Â  Â  Â  Â  â”‚ Â  Â  Â  Â  Â  Â  Â  â””â”€â”€ comments

Â  Â  Â  Â  Â  Â  â”‚

Â  Â  Â  Â  Â  Â  â””â”€â”€ activity\_logs

\`\`\`

**## Tabel Utama**

**### Organisasi**

Menyimpan informasi organisasi.

\`\`\`text

organizations

â”œâ”€â”€ id

â”œâ”€â”€ name

â”œâ”€â”€ description

â”œâ”€â”€ status

â”œâ”€â”€ created\_at

â””â”€â”€ updated\_at

\`\`\`

Status organisasi:

\`\`\`text

ACTIVE

INACTIVE

\`\`\`

\---

**### Pengguna**

Menyimpan pengguna yang tergabung dalam organisasi.

\`\`\`text

users

â”œâ”€â”€ id

â”œâ”€â”€ organization\_id

â”œâ”€â”€ name

â”œâ”€â”€ email

â”œâ”€â”€ password

â”œâ”€â”€ role

â”œâ”€â”€ status

â”œâ”€â”€ created\_at

â””â”€â”€ updated\_at

\`\`\`

Role pengguna:

\`\`\`text

SUPER\_ADMIN

ADMIN

PROJECT\_MANAGER

TEAM\_LEAD

MEMBER

\`\`\`

Status pengguna:

\`\`\`text

ACTIVE

INACTIVE

\`\`\`

\---

**### Proyek**

Menyimpan proyek yang dimiliki oleh organisasi.

\`\`\`text

projects

â”œâ”€â”€ id

â”œâ”€â”€ organization\_id

â”œâ”€â”€ name

â”œâ”€â”€ description

â”œâ”€â”€ status

â”œâ”€â”€ start\_date

â”œâ”€â”€ due\_date

â”œâ”€â”€ created\_by

â”œâ”€â”€ created\_at

â””â”€â”€ updated\_at

\`\`\`

Status proyek:

\`\`\`text

PLANNING

IN\_PROGRESS

ON\_HOLD

COMPLETED

CANCELLED

\`\`\`

\---

**### Anggota Proyek**

Menentukan pengguna yang tergabung dalam suatu proyek.

\`\`\`text

project\_members

â”œâ”€â”€ id

â”œâ”€â”€ project\_id

â”œâ”€â”€ user\_id

â””â”€â”€ created\_at

\`\`\`

Seorang pengguna tidak dapat ditambahkan lebih dari satu kali ke proyek yang sama.

\---

**### Tugas**

Menyimpan tugas yang terkait dengan proyek.

\`\`\`text

tasks

â”œâ”€â”€ id

â”œâ”€â”€ project\_id

â”œâ”€â”€ title

â”œâ”€â”€ description

â”œâ”€â”€ status

â”œâ”€â”€ priority

â”œâ”€â”€ assignee\_id

â”œâ”€â”€ due\_date

â”œâ”€â”€ created\_by

â”œâ”€â”€ created\_at

â””â”€â”€ updated\_at

\`\`\`

Status tugas:

\`\`\`text

TODO

IN\_PROGRESS

IN\_REVIEW

DONE

CANCELLED

\`\`\`

Prioritas tugas:

\`\`\`text

LOW

MEDIUM

HIGH

URGENT

\`\`\`

\---

**### Komentar**

Menyimpan komentar yang terkait dengan tugas.

\`\`\`text

comments

â”œâ”€â”€ id

â”œâ”€â”€ task\_id

â”œâ”€â”€ user\_id

â”œâ”€â”€ content

â”œâ”€â”€ created\_at

â””â”€â”€ updated\_at

\`\`\`

Hanya pengguna yang tergabung dalam proyek yang dapat menambahkan komentar pada tugasnya.

\---

**### Log Aktivitas**

Menyimpan aktivitas bisnis yang penting.

\`\`\`text

activity\_logs

â”œâ”€â”€ id

â”œâ”€â”€ organization\_id

â”œâ”€â”€ user\_id

â”œâ”€â”€ action

â”œâ”€â”€ entity\_type

â”œâ”€â”€ entity\_id

â”œâ”€â”€ description

â””â”€â”€ created\_at

\`\`\`

Contoh:

\`\`\`text

action Â  Â  Â  : CREATED

entity\_type Â : TASK

entity\_id Â  Â : 15

description Â : Task "Implement Login API" created

\`\`\`

Contoh lainnya:

\`\`\`text

action Â  Â  Â  : STATUS\_CHANGED

entity\_type Â : TASK

entity\_id Â  Â : 15

description Â : Task status changed from TODO to IN\_PROGRESS

\`\`\`

\---

**# ðŸ” Aturan Bisnis**

TaskLedger menerapkan berbagai aturan bisnis pada service layer.

**### Organization**

\* Organization must exist before being referenced.

\* Inactive organizations cannot perform normal business operations.

**### User**

\* User must belong to an organization.

\* Email must be unique.

\* Inactive users cannot perform normal operations.

\* User status is controlled by the backend.

**### Project**

\* Project must belong to an organization.

\* Project creator must belong to the same organization.

\* Project creator must be active.

\* Due date cannot be earlier than start date.

**### Project Member**

\* User and project must exist.

\* User must belong to the same organization as the project.

\* User must be active.

\* A user cannot be added twice to the same project.

**### Task**

\* Task must belong to an existing project.

\* Task creator must belong to the project's organization.

\* Task creator must be active.

\* Assignee must belong to the same organization.

\* Assignee must be active.

\* Assignee must be a member of the project.

\* New tasks start with \`TODO\`.

\* Task priority is controlled through defined enum values.

**### Comment**

\* Task must exist.

\* User must exist.

\* User must be active.

\* User must belong to the same organization as the task.

\* User must be a member of the project.

\---

**# ðŸŒ REST API**

URL Dasar:

\`\`\`text

/api

\`\`\`

Semua API menggunakan JSON.

\---

**## Organisasi**

\`\`\`http

GET Â  Â /api/organizations

POST Â  /api/organizations

GET Â  Â /api/organizations/{id}

PUT Â  Â /api/organizations/{id}

DELETE /api/organizations/{id}

\`\`\`

\---

**## Pengguna**

\`\`\`http

GET Â  Â /api/users

POST Â  /api/users

GET Â  Â /api/users/{id}

GET Â  Â /api/users/organization/{organizationId}

PUT Â  Â /api/users/{id}

DELETE /api/users/{id}

PATCH Â /api/users/{id}/activate

\`\`\`

\---

**## Proyek**

\`\`\`http

GET Â  Â /api/projects

POST Â  /api/projects

GET Â  Â /api/projects/{id}

PUT Â  Â /api/projects/{id}

DELETE /api/projects/{id}

\`\`\`

\---

**## Anggota Proyek**

\`\`\`http

POST Â  /api/project-members

GET Â  Â /api/project-members/project/{projectId}

DELETE /api/project-members/project/{projectId}/user/{userId}

\`\`\`

\---

**## Tugas**

\`\`\`http

GET Â  Â /api/tasks

POST Â  /api/tasks

GET Â  Â /api/tasks/{id}

GET Â  Â /api/tasks/project/{projectId}

PUT Â  Â /api/tasks/{id}

PATCH Â /api/tasks/{id}/status

PATCH Â /api/tasks/{id}/assignee

DELETE /api/tasks/{id}

\`\`\`

\---

**## Komentar**

\`\`\`http

POST Â  /api/comments

GET Â  Â /api/comments/task/{taskId}

\`\`\`

\---

**## Log Aktivitas**

\`\`\`http

GET /api/activity-logs/entity?entityType=TASK&entityId=1

GET /api/activity-logs/organization/{organizationId}

GET /api/activity-logs/user/{userId}

\`\`\`

Activity logs are intended to be generated by business operations rather than directly manipulated by the client.

Currently, Task operations generate activity logs automatically for:

\`\`\`text

Tugas Dibuat

Tugas Diperbarui

Status Tugas Diubah

Penanggung Jawab Tugas Diubah

\`\`\`

\---

**# ðŸ“„ Format Respons API**

API menggunakan struktur respons yang terstandarisasi.

**## Berhasil**

\`\`\`json

{

Â  Â  "status": 200,

Â  Â  "message": "Success",

Â  Â  "data": {}

}

\`\`\`

**## Error Validasi**

\`\`\`json

{

Â  Â  "status": 400,

Â  Â  "message": "Validasi failed",

Â  Â  "errors": {

Â  Â  Â  Â  "title": "Title is required"

Â  Â  }

}

\`\`\`

**## Error Bisnis**

Contoh:

\`\`\`json

{

Â  Â  "status": 400,

Â  Â  "message": "Assignee is not a member of this project",

Â  Â  "data": null

}

\`\`\`

**## Tidak Ditemukan**

\`\`\`json

{

Â  Â  "status": 404,

Â  Â  "message": "Task not found",

Â  Â  "data": null

}

\`\`\`

\---

**# ðŸ§© Pola DTO**

Project ini memisahkan request/response API dari entity JPA.

Contoh:

\`\`\`text

Request

Â  Â â†“

CreateTaskRequest

Â  Â â†“

TaskService

Â  Â â†“

Task Entity

Â  Â â†“

TaskMapper

Â  Â â†“

TaskResponse

Â  Â â†“

Response

\`\`\`

Hal ini mencegah entity JPA diekspos secara langsung sebagai kontrak API.

\---

**# ðŸ”„ Manajemen Transaksi**

Operasi bisnis Task yang mengubah data Task dan Log Aktivitas menggunakan manajemen transaksi.

Contoh:

\`\`\`text

Create Task

Â  Â  Â â”‚

Â  Â  Â â”œâ”€â”€ Save Task

Â  Â  Â â”‚

Â  Â  Â â””â”€â”€ Save Log Aktivitas

Â  Â  Â  Â  Â  Â  Â â”‚

Â  Â  Â  Â  Â  Â  Â â–¼

Â  Â  Â  Â  Â  COMMIT

\`\`\`

Jika sebuah operasi gagal:

\`\`\`text

Save Task Â  Â  Â  âœ…

Save Log Â  Â  Â  Â âŒ

Â  Â  Â  â”‚

Â  Â  Â  â–¼

Â  Â ROLLBACK

\`\`\`

Hal ini membantu menjaga konsistensi antara data bisnis dan riwayat aktivitas.

\---

**# ðŸ—ƒï¸ Migrasi Database**

Perubahan struktur database dikelola menggunakan Flyway.

Migrasi saat ini:

\`\`\`text

V1\_\_create\_organizations\_table.sql

V2\_\_create\_users\_table.sql

V3\_\_create\_projects\_table.sql

V4\_\_create\_project\_members\_table.sql

V5\_\_create\_tasks\_table.sql

V6\_\_create\_comments\_table.sql

V7\_\_create\_activity\_logs\_table.sql

\`\`\`

Flyway memastikan migrasi database memiliki versi dan dijalankan secara berurutan.

\---

**# ðŸš€ Menjalankan Project**

**## Persyaratan**

Pastikan tersedia:

\* Java 21

\* MySQL 8

\* Maven or Maven Wrapper

\* Git

\---

**## 1. Clone Repository**

\`\`\`bash

git clone https\://github.com/FeryPermana/taskledger.git

\`\`\`

\`\`\`bash

cd taskledger

\`\`\`

\---

**## 2. Membuat Database**

Buat database MySQL:

\`\`\`sql

CREATE DATABASE taskledger;

\`\`\`

\---

**## 3. Konfigurasi Database**

Ubah:

\`\`\`text

src/main/resources/application.properties

\`\`\`

Contoh:

\`\`\`properties

spring.application.name=taskledger

spring.datasource.url=jdbc\:mysql://localhost:3306/taskledger

spring.datasource.username=root

spring.datasource.password=

spring.jpa.hibernate.ddl-auto=validate

spring.jpa.show-sql=true

\`\`\`

\---

**## 4. Menjalankan Aplikasi**

Windows:

\`\`\`powershell

.\mvnw\.cmd spring-boot\:run

\`\`\`

Atau:

\`\`\`bash

./mvnw spring-boot\:run

\`\`\`

Aplikasi berjalan di:

\`\`\`text

http\://localhost:8080

\`\`\`

\---

**# ðŸ§ª Pengujian**

Endpoint API saat ini diuji secara manual menggunakan Postman.

Pengujian mencakup:

\* Valid requests

\* Validasi errors

\* Resource not found

\* Duplicate data

\* Inactive users

\* Inactive organizations

\* Organization consistency

\* Project membership

\* Task assignment

\* Task status changes

\* Comment access

\* Activity log generation
* Authentication / JWT
* Role-based authorization
* Task attachment upload
* File type validation
* File size validation
* Attachment download / preview
* Attachment deletion and physical file cleanup

Pengujian otomatis akan dikembangkan seiring kemajuan project.

\---

**# ðŸ›£ï¸ Roadmap Pengembangan**

Project dikembangkan secara bertahap.

\`\`\`text

Tahap 1 â€” Fondasi

â”œâ”€â”€ Setup Project

â”œâ”€â”€ Spring Boot

â”œâ”€â”€ JPA

â”œâ”€â”€ Flyway

â”œâ”€â”€ DTO

â”œâ”€â”€ Mapper

â”œâ”€â”€ Validasi

â”œâ”€â”€ Penanganan Exception

â””â”€â”€ Respons API Terstandarisasi

Tahap 2 â€” Modul Inti

â”œâ”€â”€ Organization

â”œâ”€â”€ User

â”œâ”€â”€ Project

â”œâ”€â”€ Project Member

â”œâ”€â”€ Task

â””â”€â”€ Comment

Tahap 3 â€” Pelacakan Bisnis

â”œâ”€â”€ Log Aktivitas

â””â”€â”€ Manajemen Transaksi

Tahap 4 â€” Keamanan

â”œâ”€â”€ Autentikasi

â”œâ”€â”€ Hash Password

â”œâ”€â”€ Spring Security

â”œâ”€â”€ JWT

â””â”€â”€ Otorisasi Berbasis Role

Tahap 5 â€” Peningkatan Task & API

â”œâ”€â”€ Task Attachment / File Upload

â”œâ”€â”€ Pagination

â”œâ”€â”€ Search

â”œâ”€â”€ Filtering

â””â”€â”€ Dashboard

Tahap 6 â€” Kualitas & Produksi

â”œâ”€â”€ Pengujian Otomatis

â”œâ”€â”€ Konfigurasi

â”œâ”€â”€ Logging

â””â”€â”€ Penguatan untuk Produksi

Tahap 7 â€” Frontend

â”œâ”€â”€ Vue.js

â”œâ”€â”€ Autentikasi Integration

â”œâ”€â”€ Dashboard

â”œâ”€â”€ Manajemen Proyek

â””â”€â”€ Manajemen Tugas

\`\`\`

\---

**# ðŸ“š Yang Sedang Dipelajari**

Melalui project ini, saya sedang mempraktikkan:

**### Spring Boot**

\* REST Controller

\* Dependency Injection

\* Service Layer

\* Repository Layer

\* Konfigurasi

**### Spring Data JPA**

\* Pemetaan Entity

\* \`@ManyToOne\`

\* Relasi

\* Query Method Repository

\* Lazy Loading

**### Database**

\* Desain Database Relasional

\* Foreign Key

\* Unique Constraint

\* Index

\* Migrasi Database

\* Flyway

**### Arsitektur Backend**

\* Arsitektur Berlapis

\* DTO Pattern

\* Mapper Pattern

\* Aturan Bisnis

\* Penanganan Exception

\* Manajemen Transaksi

**### Pengembangan API**

\* REST API

\* HTTP Method

\* HTTP Status Code

\* Validasi

\* Respons API Terstandarisasi

**### Keamanan â€” Sudah Diimplementasikan**

\* Hash Password

\* Spring Security

\* Autentikasi

\* JWT

\* Authorization

\* Role-Based Access Control

\---

**# ðŸ’¡ Pendekatan Pengembangan**

Project ini sengaja dikembangkan secara bertahap.

Daripada mengimplementasikan semua fitur sekaligus, setiap modul dibangun dan diuji terlebih dahulu sebelum melanjutkan ke modul berikutnya.

Prinsip pengembangannya adalah:

\`\`\`text

Understand

Â  Â  â†“

Design

Â  Â  â†“

Implement

Â  Â  â†“

Test

Â  Â  â†“

Refactor

Â  Â  â†“

Move to next module

\`\`\`

Tujuannya adalah membangun pemahaman backend yang kuat, bukan sekadar menyelesaikan tutorial.

\---

**# ðŸ“ˆ Progress Saat Ini

```text
Organization             â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
User                     â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Project                  â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Project Member           â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Task                     â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Comment                  â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Log Aktivitas             â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Transaction               â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Authentication            â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Password Hashing          â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Spring Security           â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
JWT                       â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Authorization / RBAC      â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Task Attachment           â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
File Upload               â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
File Download / Preview   â–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆâ–ˆ 100%
Pagination                â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘   0%
Search                    â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘   0%
Filtering                 â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘   0%
Dashboard                 â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘   0%
Automated Testing         â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘   0%
Production Hardening      â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘   0%
Vue.js Frontend           â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘   0%
Frontend Integration      â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘â–‘   0%
```

> Progress menunjukkan tahap pengembangan project saat ini dan akan berubah seiring implementasi modul baru.

---

# ðŸ”— Repository**

GitHub:

[https\://github.com/FeryPermana/taskledger]\(https\://github.com/FeryPermana/taskledger)

\---

**# ðŸ‘¨â€ðŸ’» Pengembang**

**\*\*Muhammad Pandi Ferry Permana\*\***

Full Stack Web Developer

Berfokus pada PHP, Laravel, Vue.js, dan saat ini memperluas keahlian backend dengan Java & Spring Boot.

\---

**# ðŸ“Œ Catatan**

TaskLedger Enterprise adalah project pembelajaran dan portofolio yang masih terus dikembangkan.

Arsitektur, aturan bisnis, API, dan implementasi dapat berkembang seiring dipelajarinya konsep baru dan aplikasi menjadi semakin lengkap.

\`\`\`