--liquibase formatted sql


--changeset dev1:create-patient-table
CREATE TABLE patient(
                        patient_id  SERIAL PRIMARY KEY,
                        name VARCHAR(100),
                        age INT,
                        gender VARCHAR(50),
                        date_of_birth DATE,
                        phone INT,
                        city VARCHAR(100),
                        blood_group VARCHAR(50)
);

--changeset dev1:create-doctor-table
CREATE TABLE doctor(
                       doctor_id  SERIAL PRIMARY KEY,
                       name VARCHAR(200),
                       specialization VARCHAR(150),
                       availability VARCHAR(50)
);


--changeset dev1:create-appointment-table
CREATE TABLE appointment(
                            appointment_id SERIAL PRIMARY KEY,
                            patient_id INT,
                            doctor_id INT,
                            appointment_date DATE,
                            appointment_time VARCHAR(200),

                            CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id)
                                REFERENCES patient(patient_id)
                                ON DELETE CASCADE,

                            CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id)
                                REFERENCES doctor(doctor_id)
                                ON DELETE CASCADE
);

--changeset dev1:create-payment-table
CREATE TABLE payment(
                        payment_id SERIAL PRIMARY KEY,
                        appointment_id INT,
                        amount DECIMAL(10,3),
                        payment_type VARCHAR(50),
                        payment_status VARCHAR(100),

                        CONSTRAINT fk_payment_appointment FOREIGN KEY(appointment_id)
                            REFERENCES appointment(appointment_id)
                            ON DELETE CASCADE
);