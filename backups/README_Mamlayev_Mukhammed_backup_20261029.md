# mamlayev-mukhammed-student-api

Student CRUD REST API built with Spring Boot, PostgreSQL, Docker and Jenkins.

> Project status: CRUD complete (create, read, update, delete) and documented — maintained by Mukhammed Mamlayev

**Author:** Mukhammed Mamlayev
**Course:** DevOps — midterm project

## Tech stack

- Java 17+ / Spring Boot 3.5 (Web, Data JPA, Validation, Actuator)
- PostgreSQL 16
- Gradle (wrapper included)
- Docker, Docker Compose, Jenkins

## API endpoints

| Method | URL                   | Description             |
|--------|-----------------------|-------------------------|
| GET    | `/api/info`           | Owner and version info  |
| GET    | `/api/students`       | List all students       |
| GET    | `/api/students/{id}`  | Get one student by ID   |
| POST   | `/api/students`       | Create a student        |
| PUT    | `/api/students/{id}`  | Update a student        |
| DELETE | `/api/students/{id}`  | Delete a student        |

## Change log

- **Update operation** (branch `feature/mamlayev-mukhammed-crud`, by Mukhammed Mamlayev):
  added `PUT /api/students/{id}`. It validates the request body, returns
  `404 Not Found` for an unknown ID and `409 Conflict` when the new e-mail
  already belongs to another student. Covered by three new integration tests.

## Project structure

```
mamlayev-mukhammed-student-api/
├── scripts/      Bash scripts (Mamlayev_Mukhammed_system_report.sh)
├── logs/         System report log and log analysis
├── backups/      README backup
├── src/          Spring Boot source code and tests
├── jenkins/      Local Jenkins with Docker CLI (for the CI part)
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── build.gradle
└── README.md
```

## Run the tests

```bash
./gradlew test
```

## Run with Docker Compose

```bash
cp .env.example .env          # then set your own POSTGRES_PASSWORD
docker compose up -d --build
curl http://localhost:8080/api/info
```

The application reaches PostgreSQL at `jdbc:postgresql://db:5432/...` — `db` is the
Compose service name, resolved by Docker's internal DNS on the
`mamlayev-mukhammed-net` network. Data is kept in the named volume
`mamlayev-mukhammed-pgdata`.

Docker image: `mamlayev-mukhammed-student-api:v1.0-mamlayev-mukhammed`

## CI with Jenkins

Jenkins job: `Mamlayev_Mukhammed_Student_API` — pipeline stages
Checkout → Build → Test → Docker Build → Verify (see `Jenkinsfile`).
A local Jenkins that can run Docker is started with:

```bash
docker compose -f jenkins/docker-compose.jenkins.yml up -d --build
```

## Security

`.env` holds real passwords and is excluded by `.gitignore`; only `.env.example`
with placeholder values is committed. Jenkins keeps the GitHub token in
Jenkins Credentials, never in the repository.
