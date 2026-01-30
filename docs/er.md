```mermaid
erDiagram
    PATIENT ||--o{ APPOINTMENT : books
    DOCTOR ||--o{ APPOINTMENT : attends
    APPOINTMENT ||--o{ PAYMENT : has

    PATIENT {
        int patient_id PK
        string name
        int age
        string gender
        date date_of_birth
        int phone
        string city
        string blood_group
    }

    DOCTOR {
        int doctor_id PK
        string name
        string specialization
        string availability
    }

    APPOINTMENT {
        int appointment_id PK
        date appointment_date
        string appointment_time
        int patient_id FK
        int doctor_id FK
    }

    PAYMENT {
        int payment_id PK
        decimal amount
        string payment_type
        string payment_status
        int appointment_id FK
    }

