# mamlayev-mukhammed-student-api

Student CRUD REST API built with Spring Boot, PostgreSQL, Docker and Jenkins.

> Project status: initial version

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
| DELETE | `/api/students/{id}`  | Delete a student        |

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
