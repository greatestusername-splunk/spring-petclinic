#!/bin/bash

# Enhanced start-app.sh for MSSQL
# This preserves all your existing AppDynamics/OTEL configuration 
# and adds MSSQL database connection

# Original AppDynamics and OTEL configuration (preserved from your start-app.sh)
export APPDYNAMICS_AGENT_ACCOUNT_ACCESS_KEY=#######
export APPDYNAMICS_AGENT_ACCOUNT_NAME=se-lab
export APPDYNAMICS_CONTROLLER_HOST_NAME=se-lab.saas.appdynamics.com
export APPDYNAMICS_JAVA_AGENT_REUSE_NODE_NAME_PREFIX="acct-vetting-node"
export APPDYNAMICS_AGENT_APPLICATION_NAME=acct-vetting-service
export APPDYNAMICS_AGENT_TIER_NAME=acct-vetting
export APPDYNAMICS_CONTROLLER_PORT=443
export APPDYNAMICS_CONTROLLER_SSL_ENABLED=true
export APPDYNAMICS_AGENT_NODE_NAME="reuse"
export APPDYNAMICS_JAVA_AGENT_REUSE_NODE_NAME="true"
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318
export OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
export OTEL_SERVICE_NAME=acct-vetting-service
export OTEL_RESOURCE_ATTRIBUTES=service.name=acct-vetting-service,deployment.environment=test-petclinic,service.version=1.0.0,service.namespace=acct-vetting
export AGENT_DEPLOYMENT_MODE=dual

# MSSQL Database Configuration (NEW)
export SPRING_PROFILES_ACTIVE=mssql
export SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL:-"jdbc:sqlserver://localhost:1433;databaseName=petclinic;encrypt=false;trustServerCertificate=true"}
export SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME:-"petclinic"}
export SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD:-"petclinic"}

echo "🚀 Starting PetClinic with MSSQL Database..."
echo "📊 Database: $SPRING_DATASOURCE_URL"
echo "👤 User: $SPRING_DATASOURCE_USERNAME"
echo "🔍 Monitoring: AppDynamics + OTEL enabled"

# Download/update Splunk agent (preserved from your deploy script logic)
if [ ! -f "splunk-otel-javaagent.jar" ]; then
    echo "📥 Downloading latest Splunk OTEL Java agent..."
    curl -L https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent.jar -o splunk-otel-javaagent.jar
fi

# Copy to AppDynamics location (as per your deploy script)
if [ -f "splunk-otel-javaagent.jar" ] && [ -d "appdynamics/otel" ]; then
    echo "🔄 Updating Splunk agent in AppDynamics package..."
    cp splunk-otel-javaagent.jar appdynamics/otel/splunk-otel-javaagent-2.19.0.jar
fi

# Run the application with all the original monitoring configuration
java -javaagent:appdynamics/javaagent.jar \
     -Dagent.deployment.mode=dual \
     -Dotel.instrumentation.jdbc.enabled=true \
     -Dsplunk.profiler.enabled=true \
     -Dsplunk.profiler.memory.enabled=true \
     -Dsplunk.snapshot.profiler.enabled=true \
     -Dsplunk.snapshot.selection.probability=0.2 \
     -Dotel.exporter.otlp.endpoint=http://localhost:4318 \
     -Dappdynamics.sim.enabled=true \
     -jar target/*.jar
