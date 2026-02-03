```mermaid
sequenceDiagram
actor User
participant LF as LoginFilter
participant PS as PatientServlet
participant PDAO as PatientDAO
participant DB as DBConnection

    User->>LF: Register / View Patient
    LF->>PS: Forward Request
    PS->>PDAO: insertPatient() / getAllPatients()
    PDAO->>DB: getConnection()
    DB-->>PDAO: Connection
    PDAO-->>PS: Patient Data / Success
    PS-->>User: Response

    alt Exception Occurs
        PDAO-->>PS: MyClassException
        PS-->>User: Error Message
    end
