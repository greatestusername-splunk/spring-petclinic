#!/bin/bash

# PetClinic MSSQL Database Setup and App Startup Script
# This script starts the MSSQL database in Docker and then runs the app with start-app.sh

set -e  # Exit on any error

echo "🚀 Starting PetClinic with MSSQL Database in Docker..."

# Configuration
MSSQL_CONTAINER_NAME="petclinic-mssql-db"
MSSQL_HOST="localhost"
MSSQL_PORT="1433"
MSSQL_SA_PASSWORD="YourStrong@Passw0rd"
PETCLINIC_DB="petclinic"
PETCLINIC_USER="petclinic"
PETCLINIC_PASSWORD="petclPPP123!"
MAX_WAIT_TIME=120  # Maximum time to wait for database (seconds)

echo "📋 Configuration:"
echo "   - Database Host: $MSSQL_HOST:$MSSQL_PORT"
echo "   - Database Name: $PETCLINIC_DB"
echo "   - App User: $PETCLINIC_USER"
echo "   - Container: $MSSQL_CONTAINER_NAME"

# Function to check if Docker is running
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        echo "❌ Docker is not running. Please start Docker first."
        exit 1
    fi
    echo "✅ Docker is running"
}

# Function to start MSSQL database container
start_database() {
    echo "🗄️  Starting MSSQL database container..."
    
    # Check if container already exists and is running
    if docker ps -q -f name=$MSSQL_CONTAINER_NAME | grep -q .; then
        echo "✅ MSSQL container is already running"
        return 0
    fi
    
    # Check if container exists but is stopped
    if docker ps -a -q -f name=$MSSQL_CONTAINER_NAME | grep -q .; then
        echo "🔄 Starting existing MSSQL container..."
        docker start $MSSQL_CONTAINER_NAME
    else
        echo "🆕 Creating new MSSQL container..."
        docker compose -f docker-compose-mssql-db-only.yml up -d
    fi
    
    echo "⏳ Waiting for MSSQL to be ready..."
    local wait_time=0
    while [ $wait_time -lt $MAX_WAIT_TIME ]; do
        if docker exec $MSSQL_CONTAINER_NAME /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -Q "SELECT 1" -b -C > /dev/null 2>&1; then
            echo "✅ MSSQL database is ready!"
            return 0
        fi
        echo "   Waiting... ($wait_time/${MAX_WAIT_TIME}s)"
        sleep 5
        wait_time=$((wait_time + 5))
    done
    
    echo "❌ MSSQL database failed to start within $MAX_WAIT_TIME seconds"
    echo "   Check container logs: docker logs $MSSQL_CONTAINER_NAME"
    exit 1
}

# Function to setup database and user
setup_database() {
    echo "🔧 Setting up database and user..."
    
    # Create database if it doesn't exist
    docker exec $MSSQL_CONTAINER_NAME /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -Q "
    IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = '$PETCLINIC_DB')
    BEGIN
        CREATE DATABASE [$PETCLINIC_DB];
        PRINT 'Database $PETCLINIC_DB created successfully';
    END
    ELSE
    BEGIN
        PRINT 'Database $PETCLINIC_DB already exists';
    END
    " -b -C
    
    # Create login and user
    docker exec $MSSQL_CONTAINER_NAME /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -Q "
    IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name = '$PETCLINIC_USER')
    BEGIN
        CREATE LOGIN [$PETCLINIC_USER] WITH PASSWORD = '$PETCLINIC_PASSWORD';
        PRINT 'Login $PETCLINIC_USER created successfully';
    END
    ELSE
    BEGIN
        PRINT 'Login $PETCLINIC_USER already exists';
    END
    " -b -C
    
    # Create user in database and grant permissions
    docker exec $MSSQL_CONTAINER_NAME /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "$MSSQL_SA_PASSWORD" -d "$PETCLINIC_DB" -Q "
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
    " -b -C
    
    echo "✅ Database setup completed"
}

# Function to build the application
build_application() {
    echo "🔨 Building application..."
    ./mvnw clean package -DskipTests -Dcheckstyle.skip=true
    echo "✅ Application built successfully"
}

# Function to start the application using start-app.sh
start_application() {
    echo "🚀 Starting Spring Boot application with start-app.sh..."
    echo "   Using MSSQL profile and database connection"
    
    # Check if start-app.sh exists
    if [ ! -f "start-app.sh" ]; then
        echo "❌ start-app.sh not found in current directory"
        echo "   Please run this script from the project root directory"
        exit 1
    fi
    
    if [ ! -x "start-app.sh" ]; then
        echo "🔧 Making start-app.sh executable..."
        chmod +x start-app.sh
    fi
    
    # Set Spring Boot profile and database connection for this session
    export SPRING_PROFILES_ACTIVE=mssql
    export SPRING_DATASOURCE_URL="jdbc:sqlserver://$MSSQL_HOST:$MSSQL_PORT;databaseName=$PETCLINIC_DB;encrypt=false;trustServerCertificate=true"
    export SPRING_DATASOURCE_USERNAME="$PETCLINIC_USER"
    export SPRING_DATASOURCE_PASSWORD="$PETCLINIC_PASSWORD"
    
    echo "📊 Database Connection:"
    echo "   URL: $SPRING_DATASOURCE_URL"
    echo "   User: $SPRING_DATASOURCE_USERNAME"
    echo ""
    echo "🎯 Starting application with your existing start-app.sh script..."
    echo "   All your original monitoring configuration will be preserved:"
    echo "   ✅ AppDynamics monitoring"
    echo "   ✅ OTEL monitoring" 
    echo "   ✅ Splunk profiling"
    echo "   ✅ JDBC monitoring"
    echo ""
    
    # Run the application using the existing start-app.sh script
    # The environment variables set above will be available to start-app.sh
    ./start-app.sh
}

# Function to show status
show_status() {
    echo ""
    echo "📊 System Status:"
    echo "   Database: docker ps -f name=$MSSQL_CONTAINER_NAME"
    docker ps -f name=$MSSQL_CONTAINER_NAME --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
    echo ""
    echo "🌐 Application will be available at: http://localhost:8080"
    echo ""
    echo "🛠️  Useful commands:"
    echo "   Stop database: docker stop $MSSQL_CONTAINER_NAME"
    echo "   View database logs: docker logs $MSSQL_CONTAINER_NAME"
    echo "   Connect to database: docker exec -it $MSSQL_CONTAINER_NAME /opt/mssql-tools18/bin/sqlcmd -S localhost -U $PETCLINIC_USER -P $PETCLINIC_PASSWORD -d $PETCLINIC_DB -C"
}

# Function to cleanup on script exit
cleanup() {
    echo ""
    echo "🛑 Shutting down..."
    echo "   Database container will keep running. Use 'docker stop $MSSQL_CONTAINER_NAME' to stop it."
}

# Set trap for cleanup
trap cleanup EXIT

# Main execution
main() {
    case "${1:-start}" in
        "start")
            check_docker
            start_database
            setup_database
            build_application
            show_status
            start_application
            ;;
        "db-only")
            check_docker
            start_database
            setup_database
            show_status
            echo "✅ Database is ready. You can now run './start-app.sh' with MSSQL environment variables."
            ;;
        "stop")
            echo "🛑 Stopping database..."
            docker stop $MSSQL_CONTAINER_NAME || true
            echo "✅ Database stopped"
            ;;
        "clean")
            echo "🧹 Cleaning up database container and volumes..."
            docker compose -f docker-compose-mssql-db-only.yml down -v
            echo "✅ Cleanup completed"
            ;;
        "status")
            show_status
            ;;
        *)
            echo "Usage: $0 {start|db-only|stop|clean|status}"
            echo ""
            echo "Commands:"
            echo "  start    - Start database and run application (default)"
            echo "  db-only  - Start database only, don't run application"
            echo "  stop     - Stop the database container"
            echo "  clean    - Remove database container and volumes"
            echo "  status   - Show current status"
            exit 1
            ;;
    esac
}

# Show usage if help requested
if [[ "$1" == "--help" || "$1" == "-h" ]]; then
    echo "PetClinic MSSQL Database + Local App Startup"
    echo ""
    echo "This script starts MSSQL database in Docker and runs the Spring Boot"
    echo "application locally using your existing start-app.sh script."
    echo ""
    echo "Usage: $0 [command]"
    echo ""
    echo "Commands:"
    echo "  start    - Start database and run application (default)"
    echo "  db-only  - Start database only, don't run application"  
    echo "  stop     - Stop the database container"
    echo "  clean    - Remove database container and volumes"
    echo "  status   - Show current status"
    echo ""
    echo "Environment Variables:"
    echo "  All AppDynamics and OTEL variables from start-app.sh will be preserved"
    echo "  Additional MSSQL connection variables will be set automatically"
    echo ""
    echo "Examples:"
    echo "  $0              # Start everything"
    echo "  $0 start        # Start everything" 
    echo "  $0 db-only      # Start database only"
    echo "  $0 stop         # Stop database"
    exit 0
fi

# Run main function
main "$@"
