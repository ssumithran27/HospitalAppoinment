package org.example.dao;
import org.example.DBConnection;
import org.example.Exception.MyClassException;
import org.example.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PatientDAO {
    private static final Logger logger = LoggerFactory.getLogger(PatientDAO.class);
    public void create (Patient p) {
        String sql= """
                      
                INSERT INTO patient (name,age,gender,date_of_birth,phone,city,blood_group)
               values (?,?,?,?,?,?,?)
                      """;
        try(Connection con= DBConnection.getConnection();
            PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setString (1,p.getName());
            ps.setInt(2,p.getAge());
            ps.setString(3,p.getGender());
            ps.setDate(4, p.getDateOfBirth());
            ps.setString (5,p.getPhone());
            ps.setString(6,p.getCity());
            ps.setString(7,p.getBloodGroup());
            int affectedRows=ps.executeUpdate();
            if(affectedRows==0){
                logger.error("Insert failed,no rows affected for patient{} :",p.getPatientId());
            }
            else{
                logger.info("successfully inserted the patient details{}: ",p.getPatientId());
            }
        } catch (SQLException e) {
            throw new MyClassException("Error Creating patient details:",e);
        }
    }
    public List<Patient> findAll()  {
        List<Patient> patients =new ArrayList<>();
        String sql="""
                  select patient_id,name,age,gender,date_of_birth,phone,city,blood_group from patient
                  """;
        try(Connection con =DBConnection.getConnection();
        PreparedStatement ps =con.prepareStatement(sql);

        ResultSet rs=ps.executeQuery()) {
            while (rs.next()){
                Patient p=new Patient();
                p.setPatientId(rs.getInt("patient_id"));
                p.setName(rs.getString("name"));
                p.setAge(rs.getInt("age"));
                p.setGender(rs.getString("gender"));
                p.setDateOfBirth(rs.getDate("date_of_birth"));
                p.setPhone(rs.getString("phone"));
                p.setCity(rs.getString("city"));
                p.setBloodGroup(rs.getString("blood_group"));
                patients.add(p);

            }
        } catch(SQLException e) {
            throw new MyClassException("Error Fetching patient details:",e);
        }
        return patients;
    }
    public boolean update(Patient p)  {
        String sql= """
    UPDATE patient SET name=?,age=?,gender=?,date_of_birth=?,phone=?,city=?,blood_group=? where patient_id=?""";
        boolean update=false;
        try(Connection con= DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {

            ps.setString (1,p.getName());
            ps.setInt(2,p.getAge());
            ps.setString(3,p.getGender());
            ps.setDate(4, p.getDateOfBirth());
            ps.setString (5,p.getPhone());
            ps.setString(6,p.getCity());
            ps.setString(7,p.getBloodGroup());
            ps.setInt(8,p.getPatientId());
            update=ps.executeUpdate()>0;
        }catch(SQLException e){
            throw new MyClassException("Error Updating patient details:",e);
        }
        return update;
    }

    public boolean delete(int id)  {
        String sql="""
                    delete from patient where patient_id=?""";
        boolean delete=false;

        try(Connection con= DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,id);
            delete= ps.executeUpdate()>0;
        }catch(SQLException e){
            throw new MyClassException("Error Deleting patient details:",e);
        }
        return delete;

    }

}
