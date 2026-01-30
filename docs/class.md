```mermaid
classDiagram

%% =========================
%% CONFIGURATION
%% =========================
class DBConnection {
    +getConnection()
}

%% =========================
%% FILTER
%% =========================
class LoginFilter {
    +doFilter()
}

%% =========================
%% SERVLETS
%% =========================
class HttpServlet {
    +doGet()
    +doPost()
}

class PatientServlet {
    +doPost()
    +doGet()
}

class DoctorServlet {
    +doPost()
    +doGet()
}

class AppointmentServlet {
    +doPost()
    +doGet()
}

class PaymentServlet {
    +doPost()
    +doGet()
}

PatientServlet --|> HttpServlet
DoctorServlet --|> HttpServlet
AppointmentServlet --|> HttpServlet
PaymentServlet --|> HttpServlet

LoginFilter --> PatientServlet
LoginFilter --> DoctorServlet
LoginFilter --> AppointmentServlet
LoginFilter --> PaymentServlet

%% =========================
%% DAO LAYER
%% =========================
class PatientDAO {
    +insertPatient()
    +getAllPatients()
}

class DoctorDAO {
    +insertDoctor()
    +getAllDoctors()
}

class AppointmentDAO {
    +createAppointment()
    +getAppointments()
}

class PaymentDAO {
    +makePayment()
    +getPayments()
}

class LoggingDAO {
    +insertLog()
}

PatientServlet --> PatientDAO
DoctorServlet --> DoctorDAO
AppointmentServlet --> AppointmentDAO
PaymentServlet --> PaymentDAO

PatientDAO --> DBConnection
DoctorDAO --> DBConnection
AppointmentDAO --> DBConnection
PaymentDAO --> DBConnection
LoggingDAO --> DBConnection

%% =========================
%% MODELS
%% =========================
class Patient {
    -patientId
    -name
    -age
    -gender
    -phone
}

class Doctor {
    -doctorId
    -name
    -specialization
}

class Appointment {
    -appointmentId
    -patientId
    -doctorId
    -date
    -status
}

class Payment {
    -paymentId
    -appointmentId
    -amount
    -paymentStatus
}

PatientDAO --> Patient
DoctorDAO --> Doctor
AppointmentDAO --> Appointment
PaymentDAO --> Payment

%% =========================
%% EXCEPTION
%% =========================
class MyClassException {
    +message

}
