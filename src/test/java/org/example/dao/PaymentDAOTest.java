package org.example.dao;

import org.example.configs.DBConnection;
import org.example.model.Payment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PaymentDAOTest {
    private PaymentDAO paymentDAO;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private MockedStatic<DBConnection> mockedDB;
    private DataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {

        dataSource=mock(DataSource.class);
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        mockedDB = mockStatic(DBConnection.class);
        mockedDB.when(DBConnection::getConnect).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
        paymentDAO = new PaymentDAO();
    }
    @AfterEach
    void tearDown() {
        if(mockedDB !=null) {
            mockedDB.close();
        }
    }


    @Test
    void testCreateAppointment()throws Exception{
        Payment py=new Payment();
        py.setAppointmentId(101);
        py.setAmount(4000.00);
        py.setPaymentType("cash");
        py.setPaymentStatus("completed");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        paymentDAO.create(py);
        verify(preparedStatement).setInt(1,101);
        verify(preparedStatement).setDouble(2,4000.00);
        verify(preparedStatement).setString(3,"cash");
        verify(preparedStatement).setString(4,"completed");
        verify(preparedStatement).executeUpdate();

    }
    @Test
    void testFindAll() throws Exception{
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true,false);
        when(resultSet.getInt("payment_id")).thenReturn(11);
        when(resultSet.getInt("appointment_id")).thenReturn(101);
        when(resultSet.getDouble("amount")).thenReturn(4000.00);
        when(resultSet.getString("payment_type")).thenReturn("cash");
        when(resultSet.getString("payment_status")).thenReturn("completed");
        List<Payment> payments= paymentDAO.findAll();
        assertEquals(1,payments.size());
    }
    @Test
    void testUpdatePatient() throws Exception{

        Payment py= new Payment();
        py.setPaymentId(1);
        py.setAppointmentId(110);
        py.setAmount(6000.00);
        py.setPaymentType("credit");
        py.setPaymentStatus("completed");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        boolean updated =paymentDAO.update(py);
        assertTrue(updated);
        verify(preparedStatement).setInt(1,110);
        verify(preparedStatement).setDouble(2, Double.parseDouble("6000.00"));
        verify(preparedStatement).setString(3,"credit");
        verify(preparedStatement).setString(4,"completed");
        verify(preparedStatement).setInt(5,1);
        verify(preparedStatement).executeUpdate();


    }
    @Test
    void testDeleteAppointment() throws Exception{
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        boolean deleted= paymentDAO.delete(1);
        assertTrue(deleted);
        verify(preparedStatement).setInt(1,1);
    }
}
