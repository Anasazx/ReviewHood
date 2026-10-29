# syntax=docker/dockerfile:1

# ---------- Stage 1: build the jar ----------
# Full JDK + Maven wrapper. This stage is thrown away after the build.
FROM eclipse-temurin:21-jdk AS build
WORKDIR /src

# Copy only the files Maven needs to resolve dependencies first.
# As long as pom.xml doesn't change, Docker reuses this layer (faster rebuilds).
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B -q dependency:go-offline

# Now copy the source and build. No tests exist yet, so skip them.
COPY src src
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -B -q package -DskipTests \
 && cp target/*.jar /src/app.jar

# ---------- Stage 2: run the jar ----------
# JRE only: smaller image, no compiler or source code inside.
FROM eclipse-temurin:21-jre
WORKDIR /app

# Run as a non-root user so a compromised app can't own the container.
RUN groupadd --system app && useradd --system --gid app --home /app app \
 && mkdir -p /app/uploads /app/logs \
 && chown -R app:app /app

COPY --from=build --chown=app:app /src/app.jar /app/app.jar

USER app
EXPOSE 8080

# JVM flags (memory limits etc.) come from JAVA_TOOL_OPTIONS, set in docker-compose.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
