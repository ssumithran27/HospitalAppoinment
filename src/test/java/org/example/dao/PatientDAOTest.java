package org.example.dao;
import org.example.configs.DBConnection;
import org.example.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PatientDAOTest {
private PatientDAO patientDAO;
private Connection connection;
private PreparedStatement preparedStatement;
private ResultSet resultSet;
private MockedStatic<DBConnection> mockedDB;
private DataSource dataSource;

@BeforeEach
 void setUp() throws Exception  {
    patientDAO = new PatientDAO();
    dataSource=mock(DataSource.class);
    connection = mock(Connection.class);
    preparedStatement = mock(PreparedStatement.class);
    resultSet = mock(ResultSet.class);
    mockedDB = mockStatic(DBConnection.class);
    mockedDB.when(DBConnection::getConnect).thenReturn(dataSource);
    when(dataSource.getConnection()).thenReturn(connection);
}
@AfterEach
 void tearDown() {
    if(mockedDB !=null){
        mockedDB.close();
    }
}


@Test
 void testCreatePatient()throws Exception{
    Patient p=new Patient();
    p.setName("John");
    p.setAge(30);
    p.setGender("Male");
    p.setDateOfBirth(Date.valueOf("1994-01-01"));
    p.setPhone("9876543210");
    p.setCity("Chennai");
    p.setBloodGroup("AB+");
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    patientDAO.create(p);
     verify(preparedStatement).setString(1,"John");
     verify(preparedStatement).setInt(2,30);
     verify(preparedStatement).setString(3,"Male");
     verify(preparedStatement).setDate(4,Date.valueOf("1994-01-01"));
     verify(preparedStatement).setString(5,"9876543210");
     verify(preparedStatement).setString(6,"Chennai");
     verify(preparedStatement).setString(7,"AB+");
     verify(preparedStatement).executeUpdate();
}
@Test
    void testFindAll() throws Exception{
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeQuery()).thenReturn(resultSet);
    when(resultSet.next()).thenReturn(true,false);
    when(resultSet.getInt("patient_id")).thenReturn(1);
    when(resultSet.getString("name")).thenReturn("Alice");
    when(resultSet.getInt("age")).thenReturn(25);
    when(resultSet.getString("gender")).thenReturn("Male");
    when(resultSet.getDate("date_of_birth")).thenReturn(Date.valueOf("1995-05-10"));
    when(resultSet.getString("phone")).thenReturn("9876543210");
    when(resultSet.getString("city")).thenReturn("Chennai");
    when(resultSet.getString("blood_group")).thenReturn("A+");
    List<Patient> patients= patientDAO.findAll();
    assertEquals(1,patients.size());
}
@Test
    void testUpdatePatient() throws Exception{
    Patient p= new Patient();
    p.setPatientId(1);
    p.setName("Bob");
    p.setAge(31);
    p.setGender("Male");
    p.setDateOfBirth(Date.valueOf("1993-01-01"));
    p.setPhone("9876543212");
    p.setCity("Coimbatore");
    p.setBloodGroup("O+");
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeUpdate()).thenReturn(1);
    boolean updated =patientDAO.update(p);
    assertTrue(updated);
    verify(preparedStatement).setString(1,"Bob");
    verify(preparedStatement).setInt(2,31);
    verify(preparedStatement).setString(3,"Male");
    verify(preparedStatement).setDate(4, Date.valueOf("1993-01-01"));
    verify(preparedStatement).setString(5,"9876543212");
    verify(preparedStatement).setString(6,"Coimbatore");
    verify(preparedStatement).setString(7,"O+");
    verify(preparedStatement).setInt(8,1);
    verify(preparedStatement).executeUpdate();

}
@Test
    void testDeletePatient() throws Exception{
    when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    when(preparedStatement.executeUpdate()).thenReturn(1);
    boolean deleted= patientDAO.delete(1);
    assertTrue(deleted);
    verify(preparedStatement).setInt(8,1);
    verify(preparedStatement).executeUpdate();
}
}
