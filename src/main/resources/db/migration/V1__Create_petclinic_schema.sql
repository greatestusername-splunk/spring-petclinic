-- Flyway Migration V1: Create PetClinic Schema
-- This migration creates all the necessary tables for the PetClinic application

-- Drop tables in proper order (children first) if they exist
IF OBJECT_ID('vet_specialties', 'U') IS NOT NULL DROP TABLE vet_specialties;
IF OBJECT_ID('visits', 'U') IS NOT NULL DROP TABLE visits;
IF OBJECT_ID('pets', 'U') IS NOT NULL DROP TABLE pets;
IF OBJECT_ID('owners', 'U') IS NOT NULL DROP TABLE owners;
IF OBJECT_ID('types', 'U') IS NOT NULL DROP TABLE types;
IF OBJECT_ID('specialties', 'U') IS NOT NULL DROP TABLE specialties;
IF OBJECT_ID('vets', 'U') IS NOT NULL DROP TABLE vets;

-- Create tables with IDENTITY columns for auto-increment
CREATE TABLE vets (
  id         INT IDENTITY(1,1) PRIMARY KEY,
  first_name NVARCHAR(30),
  last_name  NVARCHAR(30)
);
CREATE INDEX IX_vets_last_name ON vets (last_name);

CREATE TABLE specialties (
  id   INT IDENTITY(1,1) PRIMARY KEY,
  name NVARCHAR(80)
);
CREATE INDEX IX_specialties_name ON specialties (name);

CREATE TABLE vet_specialties (
  vet_id       INT NOT NULL,
  specialty_id INT NOT NULL
);
ALTER TABLE vet_specialties ADD CONSTRAINT FK_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id);
ALTER TABLE vet_specialties ADD CONSTRAINT FK_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);

CREATE TABLE types (
  id   INT IDENTITY(1,1) PRIMARY KEY,
  name NVARCHAR(80)
);
CREATE INDEX IX_types_name ON types (name);

CREATE TABLE owners (
  id         INT IDENTITY(1,1) PRIMARY KEY,
  first_name NVARCHAR(30),
  last_name  NVARCHAR(30),
  address    NVARCHAR(255),
  city       NVARCHAR(80),
  telephone  NVARCHAR(20)
);
CREATE INDEX IX_owners_last_name ON owners (last_name);

CREATE TABLE pets (
  id         INT IDENTITY(1,1) PRIMARY KEY,
  name       NVARCHAR(30),
  birth_date DATE,
  type_id    INT NOT NULL,
  owner_id   INT
);
ALTER TABLE pets ADD CONSTRAINT FK_pets_owners FOREIGN KEY (owner_id) REFERENCES owners (id);
ALTER TABLE pets ADD CONSTRAINT FK_pets_types FOREIGN KEY (type_id) REFERENCES types (id);
CREATE INDEX IX_pets_name ON pets (name);

CREATE TABLE visits (
  id          INT IDENTITY(1,1) PRIMARY KEY,
  pet_id      INT,
  visit_date  DATE,
  description NVARCHAR(255)
);
ALTER TABLE visits ADD CONSTRAINT FK_visits_pets FOREIGN KEY (pet_id) REFERENCES pets (id);
CREATE INDEX IX_visits_pet_id ON visits (pet_id);
