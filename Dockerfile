# =====================================================================
#  Multi-stage Dockerfile - mamlayev-mukhammed-student-api
#  Author: Mukhammed Mamlayev
#  Build : docker build -t mamlayev-mukhammed-student-api:v1.0-mamlayev-mukhammed .
# =====================================================================

# ---------- Stage 1: build the jar with Gradle on a full JDK ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy the Gradle wrapper and build files first: this layer (and the
# downloaded dependencies) is cached until build.gradle changes.
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

# Now copy the source code and build the executable jar.
# Tests are executed in the Jenkins "Test" stage, so they are skipped here.
COPY src ./src
RUN ./gradlew --no-daemon bootJar -x test

# ---------- Stage 2: small runtime image with only a JRE ----------
FROM eclipse-temurin:21-jre
LABEL maintainer="Mukhammed Mamlayev" \
      org.opencontainers.image.title="mamlayev-mukhammed-student-api" \
      org.opencontainers.image.description="Student CRUD API by Mukhammed Mamlayev" \
      org.opencontainers.image.version="1.0.0"

ENV APP_STUDENT_NAME="Mukhammed Mamlayev" \
    JAVA_OPTS=""

WORKDIR /app

# Run the application as a non-root user.
RUN groupadd --system spring && useradd --system --gid spring --no-create-home spring

COPY --from=build /workspace/build/libs/app.jar /app/app.jar
RUN chown spring:spring /app/app.jar
USER spring

# The application listens on server.port=8080
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
