-- This script runs automatically when the MSSQL container starts
-- Wait for SQL Server to be ready
:CONNECT localhost -U sa -P YourStrong@Passw0rd

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
GO

-- Use the database
USE petclinic;
GO

-- Create login if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name = 'petclinic')
BEGIN
    CREATE LOGIN petclinic WITH PASSWORD = 'petclinicP1';
    PRINT 'Login petclinic created successfully';
END
ELSE
BEGIN
    PRINT 'Login petclinic already exists';
END
GO

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
GO

-- Grant necessary permissions
ALTER ROLE db_owner ADD MEMBER petclinic;
GO

PRINT 'Database setup completed successfully';
GO
