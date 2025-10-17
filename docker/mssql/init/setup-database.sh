#!/bin/bash

# Wait for SQL Server to start up
echo "Waiting for SQL Server to start..."
sleep 30s

# Run the database setup
echo "Setting up petclinic database..."

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P YourStrong@Passw0rd -d master -Q "
-- Create database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'petclinic')
BEGIN
    CREATE DATABASE petclinic;
    PRINT 'Database petclinic created successfully';
END
ELSE
BEGIN
    PRINT 'Database petclinic already exists';
END

-- Create login if it doesn't exist  
IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name = 'petclinic')
BEGIN
    CREATE LOGIN petclinic WITH PASSWORD = 'petclPPP123!';
    PRINT 'Login petclinic created successfully';
END
ELSE
BEGIN
    PRINT 'Login petclinic already exists';
END
"

/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P YourStrong@Passw0rd -d petclinic -Q "
-- Create user if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = 'petclinic')
BEGIN
    CREATE USER petclinic FOR LOGIN petclinic;
    PRINT 'User petclinic created successfully';
END
ELSE
BEGIN
    PRINT 'User petclinic already exists';
END

-- Grant necessary permissions
ALTER ROLE db_owner ADD MEMBER petclinic;
PRINT 'Database setup completed successfully';
"

echo "Database setup completed!"
