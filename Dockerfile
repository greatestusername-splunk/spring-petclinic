FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Install curl (required for downloading the Splunk agent)
#RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy Maven wrapper and pom.xml first (for better layer caching)
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (this layer will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests -Dcheckstyle.skip=true

# Expose the port the app runs on
EXPOSE 8080

# Download the Splunk OTel Java agent
#RUN curl -L https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent.jar -o splunk-otel-javaagent.jar

# Copy the agent to the target directory to overwrite the old splunk instrumentation
#RUN cp splunk-otel-javaagent.jar appdynamics/otel/splunk-otel-javaagent-2.19.0.jar

# Set the command to run the Java application with all agents and configurations
CMD ["java", \
     "-javaagent:appdynamics/javaagent.jar", \
     "-Dagent.deployment.mode=dual", \
     "-Dotel.instrumentation.jdbc.enabled=true", \
     "-Dsplunk.profiler.enabled=true", \
     "-Dsplunk.profiler.memory.enabled=true", \
     "-Dsplunk.snapshot.profiler.enabled=true", \
     "-Dsplunk.snapshot.selection.probability=0.2", \
     "-Dotel.exporter.otlp.endpoint=http://localhost:4318", \
     "-Dappdynamics.sim.enabled=true", \
     "-jar", "target/*.jar"]
