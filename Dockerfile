# ---- build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./
COPY ./.git ./.git
RUN chmod +x ./gradlew && ./gradlew --no-daemon dependencies

# Copy sources
COPY src/ src/

# Build boot jar
RUN ./gradlew --no-daemon clean bootJar

# ---- runtime stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app

#run as non-root
RUN useradd -r -u 10001 appuser
USER appuser

# Copy the built jar. Telemetry is produced in-app via Micrometer Tracing +
# the OpenTelemetry OTLP exporter (no java agent needed); see build.gradle and
# application.yaml. OTLP endpoints are configured through environment variables.
COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]