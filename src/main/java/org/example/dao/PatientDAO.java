package org.example.dao;
import org.example.configs.DBConnection;
import org.example.exception.MyClassException;
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

    static final int PATIENT_NAME=1;
    static final int PATIENT_AGE=2;
    static final int PATIENT_GENDER=3;
    static final int PATIENT_DATE_OF_BIRTH=4;
    static final int PATIENT_PHONENO=5;
    static final int PATIENT_CITY=6;
    static final int PATIENT_BLOODGROUP=7;
    static final int PATIENT_ID=8;
    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDAO.class);
    public void create (Patient p) throws Exception {
        String sql= " INSERT INTO patient (name,age,gender,date_of_birth,phone,city,blood_group) values (?,?,?,?,?,?,?)";
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps =con.prepareStatement(sql)) {
            ps.setString (PATIENT_NAME,p.getName());
            ps.setInt(PATIENT_AGE,p.getAge());
            ps.setString(PATIENT_GENDER,p.getGender());
            ps.setDate(PATIENT_DATE_OF_BIRTH, p.getDateOfBirth());
            ps.setString (PATIENT_PHONENO,p.getPhone());
            ps.setString(PATIENT_CITY,p.getCity());
            ps.setString(PATIENT_BLOODGROUP,p.getBloodGroup());
            int affectedRows=ps.executeUpdate();
            if(affectedRows==0){
                LOGGER.error("Insert failed,no rows affected for patient{} :",p.getPatientId());
            }
            else{
                LOGGER.info("successfully inserted the patient details{}: ",p.getPatientId());
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
        try(Connection con =DBConnection.getConnect().getConnection();
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
        boolean update;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString (PATIENT_NAME,p.getName());
            ps.setInt(PATIENT_AGE,p.getAge());
            ps.setString(PATIENT_GENDER,p.getGender());
            ps.setDate(PATIENT_DATE_OF_BIRTH, p.getDateOfBirth());
            ps.setString (PATIENT_PHONENO,p.getPhone());
            ps.setString(PATIENT_CITY,p.getCity());
            ps.setString(PATIENT_BLOODGROUP,p.getBloodGroup());
            ps.setInt(PATIENT_ID,p.getPatientId());
            update=ps.executeUpdate()>0;
        }catch(SQLException e){
            throw new MyClassException("Error Updating patient details:",e);
        }
        return update;
    }

    public boolean delete(int id)  {
        String sql="""
                    delete from patient where patient_id=?""";

        boolean delete;

        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(PATIENT_ID,id);
            delete= ps.executeUpdate()>0;
        }catch(SQLException e){
            throw new MyClassException("Error Deleting patient details:",e);
        }
        return delete;

    }

}
