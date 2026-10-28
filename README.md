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
