# ---- build stage ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle settings.gradle ./
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

# Copy the built jar
COPY --from=build /workspace/build/libs/*.jar app.jar
COPY opentelemetry-javaagent.jar ./opentelemetry-javaagent.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-javaagent:./opentelemetry-javaagent.jar" \
    OTEL_TRACES_EXPORTER=console \
    OTEL_METRICS_EXPORTER=console \
    OTEL_LOGS_EXPORTER=console \
    OTEL_METRIC_EXPORT_INTERVAL=15000

ENTRYPOINT ["java","-jar","/app/app.jar"]