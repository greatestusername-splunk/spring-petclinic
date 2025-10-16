#!/bin/bash

# Quick Start Script for PetClinic with MSSQL
# This is the simplest way to get everything running

echo "🚀 PetClinic MSSQL Quick Start"
echo "================================"

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

echo "1️⃣  Starting MSSQL database in Docker..."
docker-compose -f docker-compose-mssql-db-only.yml up -d

echo "2️⃣  Waiting for database to be ready..."
sleep 15

echo "3️⃣  Setting up database and user..."
docker exec petclinic-mssql-db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'YourStrong@Passw0rd' -Q "
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'petclinic')
BEGIN
    CREATE DATABASE [petclinic];
    PRINT 'Database created';
END

IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name = 'petclinic')
BEGIN
    CREATE LOGIN [petclinic] WITH PASSWORD = 'petclinic';
    PRINT 'Login created';
END
" -b -C

docker exec petclinic-mssql-db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P 'YourStrong@Passw0rd' -d petclinic -Q "
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = 'petclinic')
BEGIN
    CREATE USER [petclinic] FOR LOGIN [petclinic];
    ALTER ROLE db_owner ADD MEMBER [petclinic];
    PRINT 'User created and permissions granted';
END
" -b -C

echo "4️⃣  Building application..."
./mvnw clean package -DskipTests -Dcheckstyle.skip=true

echo "5️⃣  Starting application with your monitoring configuration..."

# Set MSSQL environment variables
export SPRING_PROFILES_ACTIVE=mssql
export SPRING_DATASOURCE_URL="jdbc:sqlserver://localhost:1433;databaseName=petclinic;encrypt=false;trustServerCertificate=true"
export SPRING_DATASOURCE_USERNAME="petclinic"
export SPRING_DATASOURCE_PASSWORD="petclinic"

echo "✅ Database ready! Starting application with start-app.sh..."
echo "🌐 Application will be available at http://localhost:8080"
echo ""

# Use your existing start-app.sh script
./start-app.sh
