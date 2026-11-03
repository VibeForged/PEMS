# Personal Expense Management System

PEMS is a full-stack personal finance application for tracking income, expenses,
categories, savings, and dashboard analytics.

## Stack

- Backend: Java 21, Spring Boot, Spring Security, JPA, MySQL, JWT
- Frontend: React, Vite, Axios, Recharts

## Prerequisites

- Java 21
- Node.js 20 or later
- MySQL 8

## Configuration

Copy `example.env` to `.env` and set database, JWT, and administrator values:

```powershell
Copy-Item example.env .env
```

The initial administrator is created automatically from `ADMIN_NAME`,
`ADMIN_EMAIL`, and `ADMIN_PASSWORD`. All public registrations create normal
`USER` accounts.

## Run locally

Start MySQL and create the `pems_db` database. Run `init.sql` to create tables
and seed the default categories, then start the backend:

```powershell
.\run.ps1
```

In a second terminal, start the frontend:

```powershell
cd pems-frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

## Create a distributable build

Run the root build script:

```powershell
.\build.ps1
```

It creates:

- Backend JAR: `target\pems-0.0.1-SNAPSHOT.jar`
- Frontend static files: `pems-frontend\dist`

Run the packaged backend with Java 21 and the required environment variables:

```powershell
java -jar target\pems-0.0.1-SNAPSHOT.jar
```

## Roles

- `USER`: manages only their own income, expenses, profile, and views active categories.
- `ADMIN`: manages user accounts and the shared category catalog, and views aggregate platform metrics. Admins cannot view individual financial records.

## API documentation

After starting the backend, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui.html
```
