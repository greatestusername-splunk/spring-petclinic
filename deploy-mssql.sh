#!/bin/bash

# PetClinic MSSQL Deployment Script
# This script automates the deployment process for MSSQL Server

set -e  # Exit on any error

echo "🚀 Starting PetClinic MSSQL Deployment..."

# Configuration
MSSQL_HOST=${MSSQL_HOST:-localhost}
MSSQL_PORT=${MSSQL_PORT:-1433}
MSSQL_SA_PASSWORD=${MSSQL_SA_PASSWORD:-YourStrong@Passw0rd}
PETCLINIC_DB=${PETCLINIC_DB:-petclinic}
PETCLINIC_USER=${PETCLINIC_USER:-petclinic}
PETCLINIC_PASSWORD=${PETCLINIC_PASSWORD:-petclinic}
DEPLOYMENT_MODE=${DEPLOYMENT_MODE:-flyway}  # Options: flyway, spring-boot, docker

echo "📋 Deployment Configuration:"
echo "   - Host: $MSSQL_HOST:$MSSQL_PORT"
echo "   - Database: $PETCLINIC_DB"
echo "   - User: $PETCLINIC_USER"
echo "   - Mode: $DEPLOYMENT_MODE"

# Function to check if SQL Server is accessible
check_sqlserver() {
    echo "🔍 Checking SQL Server connectivity..."
    if command -v sqlcmd &> /dev/null; then
        if sqlcmd -S "$MSSQL_HOST,$MSSQL_PORT" -U sa -P "$MSSQL_SA_PASSWORD" -Q "SELECT 1" -h -1 > /dev/null 2>&1; then
            echo "✅ SQL Server is accessible"
            return 0
        else
            echo "❌ Cannot connect to SQL Server"
            return 1
        fi
    else
        echo "⚠️  sqlcmd not found. Skipping connectivity check."
        return 0
    fi
}

# Function to setup database and user
setup_database() {
    echo "🗄️  Setting up database and user..."
    
    if command -v sqlcmd &> /dev/null; then
        # Create database if it doesn't exist
        sqlcmd -S "$MSSQL_HOST,$MSSQL_PORT" -U sa -P "$MSSQL_SA_PASSWORD" -Q "
        IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = '$PETCLINIC_DB')
        BEGIN
            CREATE DATABASE [$PETCLINIC_DB];
            PRINT 'Database $PETCLINIC_DB created successfully';
        END
        ELSE
        BEGIN
            PRINT 'Database $PETCLINIC_DB already exists';
        END
        "
        
        # Create login and user
        sqlcmd -S "$MSSQL_HOST,$MSSQL_PORT" -U sa -P "$MSSQL_SA_PASSWORD" -Q "
        IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name = '$PETCLINIC_USER')
        BEGIN
            CREATE LOGIN [$PETCLINIC_USER] WITH PASSWORD = '$PETCLINIC_PASSWORD';
            PRINT 'Login $PETCLINIC_USER created successfully';
        END
        ELSE
        BEGIN
            PRINT 'Login $PETCLINIC_USER already exists';
        END
        "
        
        # Create user in database and grant permissions
        sqlcmd -S "$MSSQL_HOST,$MSSQL_PORT" -U sa -P "$MSSQL_SA_PASSWORD" -d "$PETCLINIC_DB" -Q "
        IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = '$PETCLINIC_USER')
        BEGIN
            CREATE USER [$PETCLINIC_USER] FOR LOGIN [$PETCLINIC_USER];
            PRINT 'User $PETCLINIC_USER created successfully';
        END
        ELSE
        BEGIN
            PRINT 'User $PETCLINIC_USER already exists';
        END
        
        ALTER ROLE db_owner ADD MEMBER [$PETCLINIC_USER];
        PRINT 'Permissions granted to $PETCLINIC_USER';
        "
        
        echo "✅ Database setup completed"
    else
        echo "⚠️  sqlcmd not available. Database setup skipped."
        echo "   Please create database '$PETCLINIC_DB' and user '$PETCLINIC_USER' manually."
    fi
}

# Function to deploy using Docker
deploy_docker() {
    echo "🐳 Deploying with Docker..."
    
    if command -v docker-compose &> /dev/null; then
        # Stop existing containers
        docker compose -f docker-compose-mssql.yml down
        
        # Start MSSQL and application
        docker compose -f docker-compose-mssql.yml up -d
        
        echo "✅ Docker deployment completed"
        echo "🌐 Application will be available at http://localhost:8080"
    else
        echo "❌ docker-compose not found. Please install Docker Compose."
        exit 1
    fi
}

# Function to deploy using Flyway
deploy_flyway() {
    echo "🦅 Deploying with Flyway migrations..."
    
    # Build the application
    echo "🔨 Building application..."
    ./mvnw clean package -DskipTests -Dcheckstyle.skip=true
    
    # Run with Flyway profile
    echo "🚀 Starting application with Flyway..."
    export SPRING_PROFILES_ACTIVE=mssql-flyway
    export SPRING_DATASOURCE_URL="jdbc:sqlserver://$MSSQL_HOST:$MSSQL_PORT;databaseName=$PETCLINIC_DB;encrypt=false;trustServerCertificate=true"
    export SPRING_DATASOURCE_USERNAME="$PETCLINIC_USER"
    export SPRING_DATASOURCE_PASSWORD="$PETCLINIC_PASSWORD"
    
    java -jar target/spring-petclinic-3.3.0-SNAPSHOT.jar
}

# Function to deploy using Spring Boot initialization
deploy_springboot() {
    echo "🍃 Deploying with Spring Boot initialization..."
    
    # Build the application
    echo "🔨 Building application..."
    ./mvnw clean package -DskipTests -Dcheckstyle.skip=true
    
    # Run with MSSQL profile
    echo "🚀 Starting application..."

    export SPRING_PROFILES_ACTIVE=mssql
    export SPRING_DATASOURCE_URL="jdbc:sqlserver://$MSSQL_HOST:$MSSQL_PORT;databaseName=$PETCLINIC_DB;encrypt=false;trustServerCertificate=true"
    export SPRING_DATASOURCE_USERNAME="$PETCLINIC_USER"
    export SPRING_DATASOURCE_PASSWORD="$PETCLINIC_PASSWORD"

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

  # run our appd agent download script (commented out for now to pin version)
  # ./downloadAppDJavaAgentLatest-ubuntu.sh

  # we download the splunk agent and then we copy it overwriting the old splunk instrumentation
  # in the appd agent packaging (so we can get callgraph and stuff)
  curl -L https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent.jar -o splunk-otel-javaagent.jar

  cp splunk-otel-javaagent.jar /home/splunk/spring-petclinic/appdynamics/otel/splunk-otel-javaagent-2.19.0.jar

  #java -javaagent:splunk-otel-javaagent.jar -Dotel.instrumentation.jdbc.enabled=true -Dsplunk.profiler.enabled=true -Dsplunk.profiler.memory.enabled=true -Dsplunk.snapshot.profiler.enabled=true -Dotel.exporter.otlp.endpoint=http://localhost:4318  -jar target/*.jar

  java -javaagent:appdynamics/javaagent.jar -Dagent.deployment.mode=dual -Dotel.instrumentation.jdbc.enabled=true -Dsplunk.profiler.enabled=true -Dsplunk.profiler.memory.enabled=true -Dsplunk.snapshot.profiler.enabled=true -Dsplunk.snapshot.selection.probability=0.2  -Dotel.exporter.otlp.endpoint=http://localhost:4318 -Dappdynamics.sim.enabled=true -jar target/*.jar
}

# Main deployment logic
main() {
    case $DEPLOYMENT_MODE in
        "docker")
            deploy_docker
            ;;
        "flyway")
            if check_sqlserver; then
                setup_database
            fi
            deploy_flyway
            ;;
        "spring-boot")
            if check_sqlserver; then
                setup_database
            fi
            deploy_springboot
            ;;
        *)
            echo "❌ Unknown deployment mode: $DEPLOYMENT_MODE"
            echo "   Supported modes: docker, flyway, spring-boot"
            exit 1
            ;;
    esac
}

# Show usage if help requested
if [[ "$1" == "--help" || "$1" == "-h" ]]; then
    echo "PetClinic MSSQL Deployment Script"
    echo ""
    echo "Usage: $0 [options]"
    echo ""
    echo "Environment Variables:"
    echo "  MSSQL_HOST         SQL Server host (default: localhost)"
    echo "  MSSQL_PORT         SQL Server port (default: 1433)"
    echo "  MSSQL_SA_PASSWORD  SA password for database setup"
    echo "  PETCLINIC_DB       Database name (default: petclinic)"
    echo "  PETCLINIC_USER     Application user (default: petclinic)"
    echo "  PETCLINIC_PASSWORD Application password (default: petclinic)"
    echo "  DEPLOYMENT_MODE    Deployment mode: docker|flyway|spring-boot (default: flyway)"
    echo ""
    echo "Examples:"
    echo "  # Deploy with Docker (fully automated)"
    echo "  DEPLOYMENT_MODE=docker $0"
    echo ""
    echo "  # Deploy with Flyway migrations"
    echo "  DEPLOYMENT_MODE=flyway MSSQL_SA_PASSWORD=MyPassword123 $0"
    echo ""
    echo "  # Deploy with Spring Boot initialization"
    echo "  DEPLOYMENT_MODE=spring-boot $0"
    exit 0
fi

# Run main deployment
main

echo "🎉 Deployment completed successfully!"
