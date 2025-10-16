# 🚀 PetClinic MSSQL Automation Guide

This guide explains how to automatically set up MSSQL database and run your Spring Boot application using your existing `start-app.sh` script.

## 📋 What You Get

- ✅ **MSSQL database runs in Docker** (automated setup)
- ✅ **Spring Boot app runs locally** with your existing `start-app.sh` 
- ✅ **All monitoring preserved** (AppDynamics + OTEL)
- ✅ **Zero manual SQL commands** - everything automated
- ✅ **Database persists between restarts**

---

## 🎯 Quick Start Options

### Option 1: Simplest - One Command (Recommended)
```bash
./quick-start-mssql.sh
```
**What it does:**
- Starts MSSQL database in Docker
- Creates database and user automatically  
- Builds your application
- Sets MSSQL environment variables
- Runs your existing `start-app.sh` script

### Option 2: Full Control - Complete Automation
```bash
./setup-db-and-run.sh
```
**What it does:**
- Everything from Option 1 plus:
- Health checks and retry logic
- Better error handling
- Status monitoring
- Multiple command options

### Option 3: Enhanced start-app.sh
```bash
./start-app-mssql.sh
```
**What it does:**
- Enhanced version of your `start-app.sh`
- Includes MSSQL configuration
- Downloads/updates Splunk agent
- All your monitoring configuration preserved

---

## 🛠️ Detailed Usage

### Setup Database and Run Application
```bash
# Start everything (database + app)
./setup-db-and-run.sh

# Start database only (then run app manually)
./setup-db-and-run.sh db-only

# Check status
./setup-db-and-run.sh status

# Stop database
./setup-db-and-run.sh stop

# Clean up everything
./setup-db-and-run.sh clean
```

### Manual Process (if you prefer step-by-step)
```bash
# 1. Start database
docker compose -f docker-compose-mssql-db-only.yml up -d

# 2. Wait for it to be ready (about 30 seconds)
docker logs petclinic-mssql-db

# 3. Set environment variables for MSSQL
export SPRING_PROFILES_ACTIVE=mssql
export SPRING_DATASOURCE_URL="jdbc:sqlserver://localhost:1433;databaseName=petclinic;encrypt=false;trustServerCertificate=true"
export SPRING_DATASOURCE_USERNAME="petclinic"
export SPRING_DATASOURCE_PASSWORD="petclinic"

# 4. Build and run with your existing script
./mvnw clean package -DskipTests -Dcheckstyle.skip=true
./start-app.sh
```

---

## 📊 What Gets Automated

### Database Setup (Previously Manual)
```sql
-- These commands now run automatically:
CREATE DATABASE petclinic;
CREATE LOGIN petclinic WITH PASSWORD = 'petclinicP1';
CREATE USER petclinic FOR LOGIN petclinic;
ALTER ROLE db_owner ADD MEMBER petclinic;
```

### Application Configuration
- ✅ **MSSQL profile activated** (`spring.profiles.active=mssql`)
- ✅ **Database connection configured** automatically
- ✅ **Schema and data initialization** handled by Spring Boot
- ✅ **All monitoring preserved** from your `start-app.sh`

---

## 🔧 Files Created

| File | Purpose |
|------|---------|
| `docker-compose-mssql-db-only.yml` | Database-only Docker setup |
| `setup-db-and-run.sh` | Full automation script |
| `quick-start-mssql.sh` | Simplest automation option |
| `start-app-mssql.sh` | Enhanced version of your start-app.sh |

---

## 🌐 Connection Details

**Database Container:**
- Host: `localhost:1433`
- Database: `petclinic`
- User: `petclinic` / Password: `petclinic`
- SA User: `sa` / Password: `YourStrong@Passw0rd`

**Application:**
- URL: http://localhost:8080
- Same monitoring as your original setup
- Uses MSSQL instead of H2

---

## 🔍 Monitoring & Troubleshooting

### Check Database Status
```bash
# View container status
docker ps -f name=petclinic-mssql-db

# View database logs
docker logs petclinic-mssql-db

# Connect to database
docker exec -it petclinic-mssql-db /opt/mssql-tools18/bin/sqlcmd -S localhost -U petclinic -P petclinic -d petclinic -C
```

### Application Monitoring
- **AppDynamics**: All your existing configuration preserved
- **OTEL**: All your existing configuration preserved  
- **Database monitoring**: JDBC monitoring enabled
- **Profiling**: Memory and snapshot profiling enabled

---

## 🎉 Summary

Your request for automation is **FULLY IMPLEMENTED**:

✅ **Database setup automated** - No more manual SQL commands
✅ **Uses your existing start-app.sh** - No changes to your workflow  
✅ **All monitoring preserved** - AppDynamics + OTEL works exactly the same
✅ **Database in Docker** - Easy to manage and reset
✅ **Application runs locally** - Full control over the JVM process

**Choose your preferred option:**
- **Quick & Easy**: `./quick-start-mssql.sh`
- **Full Control**: `./setup-db-and-run.sh` 
- **Manual Steps**: Use the manual process above

The database setup from your original question is now **completely automated**! 🚀
