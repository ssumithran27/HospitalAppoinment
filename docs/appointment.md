```mermaid
sequenceDiagram
actor User
participant LF as LoginFilter
participant AS as AppointmentServlet
participant ADAO as AppointmentDAO
participant LogDAO as LoggingDAO
participant DB as DBConnection

    User->>LF: Book / View Appointment
    LF->>AS: Forward Request
    AS->>ADAO: createAppointment() / getAppointments()
    ADAO->>DB: getConnection()
    DB-->>ADAO: Connection

    ADAO->>LogDAO: insertLog()
    LogDAO->>DB: getConnection()
    DB-->>LogDAO: Connection

    ADAO-->>AS: Appointment Confirmed / Data
    AS-->>User: Response

    alt Exception Occurs
        ADAO-->>AS: MyClassException
        AS-->>User: Error Message
    end
