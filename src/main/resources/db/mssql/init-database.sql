-- This script creates the database and user if they don't exist
-- It should be run with administrative privileges before the main schema/data scripts

-- Check if database exists, create if not
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'petclinic')
BEGIN
    CREATE DATABASE petclinic;
    PRINT 'Database petclinic created successfully';
END
GO

-- Use the petclinic database
USE petclinic;
GO

-- Create login if it doesn't exist (requires sysadmin privileges)
IF NOT EXISTS (SELECT name FROM sys.server_principals WHERE name = 'petclinic')
BEGIN
    CREATE LOGIN petclinic WITH PASSWORD = 'petclPPP123!';
    PRINT 'Login petclinic created successfully';
END
GO

-- Create user if it doesn't exist
IF NOT EXISTS (SELECT name FROM sys.database_principals WHERE name = 'petclinic')
BEGIN
    CREATE USER petclinic FOR LOGIN petclinic;
    PRINT 'User petclinic created successfully';
END
GO

-- Grant necessary permissions
IF IS_ROLEMEMBER('db_owner', 'petclinic') = 0
BEGIN
    ALTER ROLE db_owner ADD MEMBER petclinic;
    PRINT 'Granted db_owner role to petclinic user';
END
GO

PRINT 'Database initialization completed successfully';
GO
