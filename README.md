# Task Manager

A full-stack task manager with user authentication. Register, log in, then create, view, update and delete your own tasks.

- **Backend:** Java 21, Spring Boot 3.5, Spring Security (JWT), Spring Data JPA, PostgreSQL 15
- **Frontend:** Vue 3, Vite, Vue Router, Pinia (plain JavaScript, hand-written CSS with a retro wood theme)
- **Infrastructure:** Docker and Docker Compose

## Features

- User registration and login with JWT authentication (tokens expire after 1 hour)
- Protected task endpoints; the frontend redirects to the login page when the session is invalid
- Create, list, edit, complete (`TODO` / `DONE`) and delete tasks
- Server-side validation with field-level error messages shown in the UI
- Whole stack runs with a single `docker compose up --build`

## Project structure

```
taskmanager/
├── api/                      # Spring Boot backend
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                 # Vue 3 + Vite frontend
│   ├── src/
│   │   ├── api/              # client.js (fetch wrapper), auth.js, tasks.js
│   │   ├── components/       # reusable pieces (TaskItem)
│   │   ├── router/           # routes and auth guard
│   │   ├── stores/           # Pinia stores (auth)
│   │   ├── views/            # pages (Login, Register, Tasks)
│   │   ├── assets/           # global CSS
│   │   ├── App.vue
│   │   └── main.js
│   ├── vite.config.js
│   └── Dockerfile
├── docker-compose.yml
├── .env.example
└── README.md
```

## Getting started

### Prerequisites

- Docker and Docker Compose
- For local development without Docker: JDK 21, Maven (or the included wrapper), Node.js 20.19+ or 22.12+

### Run everything with Docker

1. Clone the repository:

   ```bash
   git clone https://github.com/<your-username>/taskmanager.git
   cd taskmanager
   ```

2. Create your environment file from the template and fill in the values:

   ```bash
   cp .env.example .env
   ```

3. Build and start all services:

   ```bash
   docker compose up --build
   ```

4. Open the app. See the [Ports](#ports) table for the addresses.

To stop the stack, press `Ctrl+C` or run `docker compose down`. To also **delete the database data**, run `docker compose down -v`.

### Ports

| Service | Address |
|---|---|
| Frontend | `http://localhost:<FRONTEND_PORT>` (see `docker-compose.yml`) |
| API | `http://localhost:8080` |
| PostgreSQL (from your machine) | `localhost:5433` |

Inside the Docker network, services talk to each other by service name (the API reaches the database at `db:5432`). The `5433` mapping is only for connecting from your own machine with a database client.

## Configuration

All configuration comes from environment variables. Copy `.env.example` to `.env` and adjust it. **Never commit `.env`**; it is git-ignored, and only `.env.example` (placeholders) is tracked.

The API reads these Spring properties from the environment:

| Variable | Purpose |
|---|---|
| `SPRING_DATASOURCE_URL` | JDBC URL, must start with `jdbc:postgresql://` |
| `SPRING_DATASOURCE_USERNAME` | Database user |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `PORT` | API port (defaults to `8080`) |

The PostgreSQL container uses `POSTGRES_DB`, `POSTGRES_USER` and `POSTGRES_PASSWORD`. The username, password and database name must match the ones in the datasource variables. See `.env.example` for the exact variable names used in this project, including any JWT settings.

> **Note:** PostgreSQL only applies its credentials on the first start of a fresh volume. If you change them later, run `docker compose down -v` to re-initialize the database (this deletes its data).

## API reference

Base path: `/api`. All `/api/tasks` endpoints require the header `Authorization: Bearer <token>`.

### Auth

| Method | Path | Body | Response |
|---|---|---|---|
| `POST` | `/api/auth/register` | `{ "username", "email", "password" }` | Created user confirmation |
| `POST` | `/api/auth/login` | `{ "email", "password" }` | JWT as a plain-text body |

### Tasks

| Method | Path | Body | Response |
|---|---|---|---|
| `GET` | `/api/tasks` | none | `200` list of tasks |
| `POST` | `/api/tasks` | `{ "title", "description" }` | `201` created task |
| `PUT` | `/api/tasks/{id}` | `{ "title", "description", "status" }` | `200` updated task |
| `DELETE` | `/api/tasks/{id}` | none | `204` no content |

A task looks like:

```json
{
  "id": 1,
  "title": "Write README",
  "description": "Document the project",
  "status": "TODO"
}
```

`status` is either `TODO` or `DONE`.

### Errors

Validation failures return `400` with the field name as key:

```json
{ "title": "Title is required" }
```

Other errors return a JSON body of the form `{ "message": "..." }`.

## Local development (without Docker for the app)

Run only the database in Docker, and the API and frontend on your machine.

**1. Database**

```bash
docker compose up db
```

**2. API** (from `api/`, with the datasource variables exported and pointing at `localhost:5433`)

```bash
./mvnw spring-boot:run
```

**3. Frontend** (from `frontend/`)

```bash
npm install
npm run dev
```

The dev server runs at `http://localhost:5173`. `vite.config.js` proxies every `/api` request to the Spring Boot API, so the browser only talks to one origin and **no CORS configuration is needed in development**.

## How it works

- **Auth flow:** the login response contains a JWT. The frontend stores it in `localStorage`, attaches it to every request through `src/api/client.js`, and clears it on logout or when the server rejects it.
- **Route guard:** the router redirects unauthenticated users to `/login` and logged-in users away from the login and register pages.
- **API layer:** only `src/api/` knows about URLs and `fetch`. Components call small functions such as `getTasks()` or `createTask()`.
- **Dev vs production:** in development, Vite's proxy forwards `/api` to the backend. In a production setup, a reverse proxy such as nginx plays the same role by serving the built frontend and forwarding `/api` to Spring.

## Known limitations and roadmap

- [ ] Return a clean `401` JSON response for expired or invalid JWTs from the security filter (currently handled on the frontend by checking token expiry)
- [ ] Use one consistent JSON shape for all API responses and errors
- [ ] Verify and enforce that tasks are scoped to the logged-in user
- [ ] Store the token in an httpOnly cookie instead of `localStorage`
- [ ] Add automated tests (JUnit for the API, Vitest for the frontend)
- [ ] Add task due dates, filtering and search
- [ ] Migrate the frontend to TypeScript

## Troubleshooting

| Symptom | Likely cause and fix |
|---|---|
| API crashes with `'url' must start with "jdbc"` | The datasource variables are not reaching the container. Check the `environment` / `env_file` entries in `docker-compose.yml` and your `.env`. |
| Every request in the UI fails with a generic error | The `/api` proxy is missing or the API is not running. Check `vite.config.js` and restart `npm run dev`. |
| Database credentials changes have no effect | The existing volume keeps the old credentials. Run `docker compose down -v` (deletes data). |
| Logged out unexpectedly | JWTs expire after 1 hour; log in again. |
| `Failed to resolve import` in Vite | A file name or path is wrong. Linux paths are case-sensitive (`TasksView.vue`, not `TaskView.vue`). |

## License

Add a license of your choice (for example MIT) and place the text in a `LICENSE` file.
