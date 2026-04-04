# 📌 Task Manager API

A **Spring Boot backend API** for managing tasks with authentication, built with a production-ready setup using **Docker, PostgreSQL, and cloud deployment**.

---

## 🚀 Features

- 🔐 User Authentication (JWT-based)
- 📝 Create, update, delete tasks
- 📋 Fetch user-specific tasks
- 🛡️ Protected routes
- 🐳 Dockerized backend
- 🗄️ PostgreSQL database
- ☁️ Deployed on Render

---

## 🛠️ Tech Stack

- **Backend:** Spring Boot  
- **Database:** PostgreSQL  
- **Authentication:** JWT  
- **Build Tool:** Maven  
- **Containerization:** Docker  
- **Orchestration (Local):** Docker Compose  
- **Deployment:** Render  

---

## ⚙️ Local Setup

### 1. Clone the repository

```bash
git clone https://github.com/NiteshSangroula/taskmanger-api.git
cd taskmanger-api
```

### 2. Run with Docker (Recommended)

```bash
docker-compose up --build
```

### 3. Access the API

```bash
http://localhost:8080
```

---

### 🔑 Environment Variables

The application uses environment variables for configuration:
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/<db>
SPRING_DATASOURCE_USERNAME=<username>
SPRING_DATASOURCE_PASSWORD=<password>
```

---

### 🧪 API Endpoints

#### 🔐 Auth
```bash
POST /api/auth/register → Register user
POST /api/auth/login → Login user
```

#### 📋 Tasks (Protected)
```bash
GET /api/tasks → Get all tasks
POST /api/tasks → Create task
PUT /api/tasks/{id} → Update task
DELETE /api/tasks/{id} → Delete task
```

--- 

### 🐳 Docker

Build Image
```bash
docker build -t taskmanager-api .
```
Run Container
```bash
docker run -p 8080:8080 taskmanager-api
```

### 🧱 Project Structure
```bash
src/
 ├── controller
 ├── service
 ├── repository
 ├── model
 └── security
```

### ☁️ Deployment

The application is deployed using:

- Docker (multi-stage build)
- Render (Web Service)
- Render PostgreSQL (managed database)

---

### ⚠️ Notes

- Uses environment-based configuration (no hardcoded credentials)
- PostgreSQL is required (H2 removed for production setup)
- Free tier deployment may experience cold starts

---

### 🧠 What I Learned

- Containerizing applications with Docker
- Managing multi-service architecture with Docker Compose
- Connecting applications to external databases
- Deploying backend services to the cloud
- Handling environment variables for different environments

---

### 📌 Future Improvements

- Input validation
- Pagination & filtering
- API documentation (Swagger)
- CI/CD pipeline (GitHub Actions)
- Frontend integration

---

### 👤 Author
Nitesh Sangroula

#### 🌍 Live API
```bash
https://taskmanger-api.onrender.com/
```
