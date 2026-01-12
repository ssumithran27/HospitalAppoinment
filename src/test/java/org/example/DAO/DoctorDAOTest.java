package org.example.DAO;
import org.example.configs.DBConnection;
import org.example.dao.DoctorDAO;
import org.example.model.Doctor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DoctorDAOTest {
    private DoctorDAO doctorDAO;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private MockedStatic<DBConnection> mockedDB;

    @BeforeEach
    void setUp() throws Exception {
        doctorDAO = new DoctorDAO();
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
    void testCreateDoctor()throws Exception{
        Doctor d=new Doctor();
        d.setName("Ab");
        d.setSpecialization("ortho");
        d.setAvailability("yes");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        doctorDAO.create(d);
        verify(preparedStatement).setString(1,"Ab");
        verify(preparedStatement).setString(2,"ortho");
        verify(preparedStatement).setString(3,"yes");
        verify(preparedStatement).executeUpdate();

    }
    @Test
    void testFindAll() throws Exception{
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true,false);
        when(resultSet.getInt("doctor_id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Ab");
        when(resultSet.getString("specialization")).thenReturn("Ortho");
        when(resultSet.getString("availability")).thenReturn("yes");
        List<Doctor> doctors= doctorDAO.findAll();
        assertEquals(1,doctors.size());
    }
    @Test
    void testUpdatePatient() throws Exception{
        Doctor d= new Doctor();
        d.setDoctorId(1);
        d.setName("Ab");
        d.setSpecialization("ortho");
        d.setAvailability("yes");
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        boolean updated =doctorDAO.update(d);
        assertTrue(updated);
        verify(preparedStatement).setString(1,"Ab");
        verify(preparedStatement).setString(2,"ortho");
        verify(preparedStatement).setString(3,"yes");
        verify(preparedStatement).executeUpdate();

    }
    @Test
    void testDeletePatient() throws Exception{
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        boolean deleted= doctorDAO.delete(1);
        assertTrue(deleted);
        verify(preparedStatement).setInt(1,1);
        verify(preparedStatement).executeUpdate();
    }
}
