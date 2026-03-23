# FestivalGo

A full-stack **festival management and discovery** platform: browse music festivals, buy tickets with **Stripe**, get **genre-based recommendations** from a quiz, chat in **groups**, and explore festival venues on an interactive **map**. Built as a bachelor’s thesis project.

---

## Overview

**FestivalGo** pairs a **Spring Boot** REST API with a **Vue 3** single-page app. Administrators curate festivals (including images and map markers); regular users discover events, join or purchase attendance, take a recommendation quiz, and coordinate with others in group chat.

---

## Features

| Area | What it does |
|------|----------------|
| **Festivals** | List, create, update, and delete festivals with name, location, description, dates, **genre**, **price**, and **cover image** (multipart upload). Images are served from `/uploads/**`. |
| **Accounts** | Register and log in. Passwords are hashed with **BCrypt**. Users with an email ending in `@festivalgo.com` are assigned the **ADMIN** role; others are **USER**. |
| **Login protection** | **Bucket4j** rate limiting on login attempts per client IP (mitigates brute-force attempts). |
| **Tickets & attendance** | Register interest (`participari`), cancel participation, and **Stripe Checkout** for paid tickets; payment completion is finalized via a dedicated API endpoint. |
| **Recommendations** | Admins manage quiz **questions**; users submit answers and receive a computed **preferred genre** (e.g. EDM, Rock, Jazz) used for personalization. |
| **Maps** | Per-festival **GPS points** (Leaflet on the frontend); admins can add and bulk-update markers. |
| **Chat (VibeTalk)** | Create groups, add members, and exchange messages within a group. |

> **Note:** Route and role checks for `/admin` vs user pages are enforced in the **Vue Router** (`meta.requiresAuth` + `role`). The API layer uses permissive Spring Security (`permitAll`); treat the backend accordingly if you deploy publicly.

---

## Tech stack

**Backend** (`backend (1)/backend`)

- Java **23**, **Spring Boot 3.4.3**
- **Spring Data JPA** + **MySQL**
- **Spring Security** (password encoder; CSRF disabled for API usage)
- **Stripe Java SDK** for Checkout sessions
- **Bucket4j** for login rate limiting
- **Lombok** (compiler plugin in `pom.xml`)
- File uploads with configurable multipart limits (**10MB**)

**Frontend** (`festivalgo-frontend`)

- **Vue 3** + **Vue Router 4**
- **Vite 6**
- **Axios** for HTTP
- **Leaflet** for maps
- **Chart.js** + **vue-chartjs** (where charts are used)
- **Stripe.js** for payment flows

---

## Repository layout

```
Licenta/
├── backend (1)/backend/    # Spring Boot API (Maven)
│   └── src/main/java/...     # Controllers, services, entities
└── festivalgo-frontend/      # Vue 3 SPA
    └── src/
        ├── pages/            # Views (festival list, quiz, chat, admin, …)
        └── router/index.js   # Routes and role guards
```

---

## Prerequisites

- **JDK 23** (matches `pom.xml`)
- **Maven** (or use the included `mvnw` / `mvnw.cmd`)
- **Node.js** (LTS recommended) and **npm**
- **MySQL** server

---

## Configuration (backend)

Edit `backend (1)/backend/src/main/resources/application.properties` (or override with environment-specific settings):

| Property | Purpose |
|----------|---------|
| `spring.datasource.*` | MySQL URL, username, password |
| `spring.jpa.hibernate.ddl-auto` | Schema mode (e.g. `update` for development) |
| `server.port` | API port (**8081** in the current setup) |
| `stripe.secret.key` | Stripe secret key for server-side Checkout |
| `spring.servlet.multipart.*` | Max upload size for festival images |

**Security:** Do not commit real database passwords or Stripe keys. Use local overrides, environment variables, or a secrets manager for production.

**CORS:** `WebConfig` allows `http://localhost:5173` (default Vite dev server) with credentials.

---

## Running locally

### 1. Database

Create a MySQL database (or rely on `createDatabaseIfNotExist=true` in the JDBC URL if configured) and align the name with `spring.datasource.url`.

### 2. Backend

```bash
cd "backend (1)/backend"
./mvnw spring-boot:run
# Windows: mvnw.cmd spring-boot:run
```

API base URL: `http://localhost:8081`

### 3. Frontend

```bash
cd festivalgo-frontend
npm install
npm run dev
```

App URL: `http://localhost:5173` (Vite default)

The frontend calls the API at `http://localhost:8081`. If you change host or port, update the URLs in the Vue pages (or introduce a shared `axios` base URL / env variable).

---

## API summary

Base path examples (all under the running server, port **8081**):

| Method & path | Description |
|---------------|-------------|
| `POST /users/register` | Register user (role from email domain) |
| `POST /users/login` | Login; returns `id`, `username`, `role` |
| `GET /users/{id}` | User profile including `preferredGenre` |
| `GET /users` | List users with role **USER** (for adding chat members) |
| `GET /festivals`, `GET /festivals/{id}` | List / get festival |
| `POST /festivals/post` | Create festival (multipart) |
| `PUT /festivals/{id}` | Update festival (multipart, optional new image) |
| `DELETE /festivals/{id}` | Delete festival |
| `GET/POST /map-points/{festivalId}` | Get / add map points |
| `PUT /map-points/update/{festivalId}` | Replace points for a festival |
| `GET/POST … /participari/*` | User participations, cancel, confirm ticket |
| `POST /api/payment/checkout-session` | Stripe Checkout session URL |
| `POST /api/payment/finalize` | Finalize payment (JSON body) |
| `GET/POST/DELETE /api/quiz/questions` | Quiz CRUD (admin UI) |
| `POST /api/recommendation/calculate/{userId}` | Submit answers → saved genre |
| `POST/GET /chat/groups/*` | Groups, messages, membership |

Static files: `GET /uploads/{filename}` (served from the `uploads` directory on disk).

---

## Frontend routes (high level)

- **`/login`**, **`/register`** — Authentication  
- **`/welcome`**, **`/festivals`**, **`/festival/:id`** — Discovery and detail (with map)  
- **`/my-participations`**, **`/payment-success`** — Tickets and Stripe return flow  
- **`/user/quiz-page`** — Recommendation quiz  
- **`/chat`** — Group chat hub  
- **`/admin/*`** — Admin dashboard, add/update festivals, map editor, quiz question manager  

---

## License & attribution

Developed as part of a **licență** (bachelor’s thesis) project.
