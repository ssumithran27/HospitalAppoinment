```mermaid
sequenceDiagram
actor User
participant LF as LoginFilter
participant DS as DoctorServlet
participant DDAO as DoctorDAO
participant DB as DBConnection

    User->>LF: Add / View Doctor
    LF->>DS: Forward Request
    DS->>DDAO: insertDoctor() / getAllDoctors()
    DDAO->>DB: getConnection()
    DB-->>DDAO: Connection
    DDAO-->>DS: Doctor Data / Success
    DS-->>User: Response

    alt Exception Occurs
        DDAO-->>DS: MyClassException
        DS-->>User: Error Message
    end

