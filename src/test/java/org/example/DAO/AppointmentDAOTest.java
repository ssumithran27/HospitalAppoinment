package org.example.DAO;

import org.example.configs.DBConnection;
import org.example.dao.AppointmentDAO;
import org.example.model.Appointment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AppointmentDAOTest {
    private AppointmentDAO appointmentDAO;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private MockedStatic<DBConnection> mockedDB;

    @BeforeEach
    void setUp() throws Exception {
        appointmentDAO = new AppointmentDAO();
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        mockedDB = mockStatic(DBConnection.class);
        mockedDB.when(DBConnection::getConnect).thenReturn(connection);
    }
    @AfterEach
    void tearDown() {
        if(mockedDB !=null) {
            mockedDB.close();
        }
    }


    @Test
    void testCreateAppointment()throws Exception{
        Appointment a=new Appointment();
        a.setPatientId(1);
        a.setDoctorId(100);
        a.setAppointmentDate(Date.valueOf("2025-01-01"));
        a.setAppointmentTime("17:20:10");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        appointmentDAO.create(a);
        verify(preparedStatement).setInt(1,1);
        verify(preparedStatement).setInt(2,100);
        verify(preparedStatement).setDate(3,Date.valueOf("2025-01-01"));
        verify(preparedStatement).setString(4,"17:20:10");
        verify(preparedStatement).executeUpdate();

    }
    @Test
    void testFindAll() throws Exception{
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true,false);
        when(resultSet.getInt("appointment_id")).thenReturn(101);
        when(resultSet.getInt("patient_id")).thenReturn(1);
        when(resultSet.getInt("doctor_id")).thenReturn(100);
        when(resultSet.getDate("appointment_date")).thenReturn(Date.valueOf("2025-01-01"));
        when(resultSet.getString("appointment_time")).thenReturn("17:20:10");
        List<Appointment> appointments= appointmentDAO.findAll();
        assertEquals(1,appointments.size());
    }
    @Test
    void testUpdatePatient() throws Exception{

        Appointment a= new Appointment();
        a.setAppointmentId(1);
        a.setPatientId(101);
        a.setDoctorId(100);
        a.setAppointmentDate(Date.valueOf("2025-01-03"));
        a.setAppointmentTime("18:15:40");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        boolean updated =appointmentDAO.update(a);
        assertTrue(updated);
        verify(preparedStatement).setInt(1,101);
        verify(preparedStatement).setInt(2,100);
        verify(preparedStatement).setDate(3, Date.valueOf("2025-01-03"));
        verify(preparedStatement).setString(4,"18:15:40");
        verify(preparedStatement).setInt(5,1);
        verify(preparedStatement).executeUpdate();
    }
    @Test
    void testDeleteAppointment() throws Exception{
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        boolean deleted= appointmentDAO.delete(1);
        assertTrue(deleted);
        verify(preparedStatement).setInt(1,1);
    }

}
