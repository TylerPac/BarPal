############################
# Build stage
############################
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

COPY mvnw* pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw

# Copy source after dependency download to leverage caching
COPY src src

RUN ./mvnw -q -DskipTests clean package

############################
# Runtime stage
############################
FROM eclipse-temurin:21-jre
WORKDIR /app

# Add a non-root user
RUN useradd -u 1001 spring && mkdir -p /app && chown -R spring:spring /app

COPY --from=build /workspace/target/barpal-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
USER spring

ENV JAVA_OPTS=""

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
