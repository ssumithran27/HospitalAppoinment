```mermaid
sequenceDiagram
actor User
participant LF as LoginFilter
participant PayS as PaymentServlet
participant PayDAO as PaymentDAO
participant LogDAO as LoggingDAO
participant DB as DBConnection

    User->>LF: Make / View Payment
    LF->>PayS: Forward Request
    PayS->>PayDAO: makePayment() / getPayments()
    PayDAO->>DB: getConnection()
    DB-->>PayDAO: Connection

    PayDAO->>LogDAO: insertLog()
    LogDAO->>DB: getConnection()
    DB-->>LogDAO: Connection

    PayDAO-->>PayS: Payment Success / Data
    PayS-->>User: Receipt / Response

    alt Exception Occurs
        PayDAO-->>PayS: MyClassException
        PayS-->>User: Error Message
    end
