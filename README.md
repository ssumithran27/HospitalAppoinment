# Hospital Appointment Management System – Java Servlet & JDBC

## Description

HospitalAppointmentManagementSystem is a Java Servlet–based web application designed to manage hospital operations such as patient registration, doctor management, appointment booking, and payment processing. The application follows a layered architecture using Servlets, Services, DAOs, and Models, with MySQL as the backend database. It is packaged as a WAR file and deployed on Apache Tomcat, showcasing core Java web development concepts including JDBC, filters, authentication, and logging.

## Features

* Patient registration and management
* Doctor creation and availability management
* Appointment booking and retrieval
* Secure login and registration using Filters
* JWT-based authentication and password hashing
* Payment processing and tracking
* Centralized exception handling
* Reusable JDBC database connection utility
* Logging using Logback
* Unit testing for DAO and Servlet layers

## Technologies Used

* Java (Servlets)
* JDBC
* MySQL
* Apache Tomcat
* Maven
* JWT (JSON Web Token)
* Logback
* JUnit

## Project Structure

* **configs** – Database configuration and JDBC connection utility
* **dao** – Data Access Objects for database operations
* **model** – POJO classes representing database entities
* **service** – Business logic layer
* **servlet** – HTTP request and response handling
* **filter** – Login and registration validation filters
* **util** – Utility classes for hashing and JWT token generation
* **exception** – Custom exception handling
* **resources** – SQL changelog and logging configuration
* **webapp** – Web configuration (web.xml)
* **test** – Unit tests for DAO and Servlet layers

## Core Modules

### Patient

* Register patient
* Get patient details
* Update patient information

### Doctor

* Add doctor
* Get doctor list
* Manage doctor details

### Appointment

* Book appointment
* Get appointments by patient or doctor
* Appointment persistence using JDBC

### Payment

* Process payment for appointments
* Retrieve payment history

### Authentication & Security

* Password hashing utility
* JWT token generation and validation
* Servlet filters for login and registration validation

## API Endpoints

### Patient APIs

* POST /patient
* GET /patient?id=1

### Doctor APIs

* POST /doctor
* GET /doctor

### Appointment APIs

* POST /appointment
* GET /appointment?patientId=1

### Payment APIs

* POST /payment
* GET /payment?appointmentId=1

## Database

* postgresql database
* Schema managed using SQL changelog
* Optimized queries for CRUD operations

## Testing

* DAO layer tested using JUnit
* Servlet layer tested with mock requests

## Summary

This project demonstrates a complete Java web application using Servlets and JDBC with proper layering, security, logging, and testing. It is suitable for learning and showcasing core backend development concepts without using external frameworks like Spring.
