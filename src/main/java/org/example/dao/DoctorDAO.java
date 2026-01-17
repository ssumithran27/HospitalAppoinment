package org.example.dao;
import org.example.configs.DBConnection;
import org.example.exception.MyClassException;
import org.example.model.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDAO.class);

    static final int DOCTOR_NAME=1;
    static final int DOCTOR_SPECIALIZATION=2;
    static final int DOCTOR_AVAILABILITY=3;
    static final int DOCTOR_ID=4;
    public void create (Doctor d) {
        String sql="""
                   INSERT INTO doctor(name,specialization,availability)
                    VALUES (?,?,?)""";

        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(DOCTOR_NAME,d.getName());
            ps.setString(DOCTOR_SPECIALIZATION,d.getSpecialization());
            ps.setString(DOCTOR_AVAILABILITY,d.getAvailability());
           int affectedRows= ps.executeUpdate();
            if(affectedRows==0){
                LOGGER.error("Insert failed,no rows affected for doctor{} :",d.getDoctorId());
            }else{
                LOGGER.info("successfully inserted the doctor details{}: ",d.getDoctorId());
            }
        } catch (SQLException e) {
            throw new MyClassException("Error Creating doctor details:",e);
        }
    }

    public List<Doctor> findAll(){
        List<Doctor> doctors =new ArrayList<>();
        String sql="""
                  select doctor_id,name,specialization,availability from doctor
                  """;
        try(Connection con =DBConnection.getConnect().getConnection();
            PreparedStatement ps =con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()) {
            while (rs.next()){
                Doctor d=new Doctor();
                d.setDoctorId((rs.getInt("doctor id")));
                d.setName(rs.getString("name"));
                d.setSpecialization(rs.getString("specialization"));
                d.setAvailability(rs.getString("availability"));
                doctors.add(d);
            }
        } catch(SQLException e) {
            throw new MyClassException(" error  fetching doctor details",e);
        }
        return doctors;
    }

    public boolean update(Doctor d)
    {
        String sql= "update doctor set name=?,specialization=?,availability=? where doctor_id=?";

         boolean update;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString (DOCTOR_NAME,d.getName());
            ps.setString(DOCTOR_SPECIALIZATION,d.getSpecialization());
            ps.setString(DOCTOR_AVAILABILITY,d.getAvailability());
            ps.setInt(DOCTOR_ID,d.getDoctorId());
            update= ps.executeUpdate()>0;
        } catch (SQLException e) {
            throw new MyClassException("Error updating doctor details: ",e);
        }
        return update;
    }

    public boolean delete(int id)  {
        String sql="""
                   delete from patient where doctor_id=?""";
        boolean delete;

        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(DOCTOR_ID,id);
            delete= ps.executeUpdate()>0;
        } catch (SQLException e) {
            throw new MyClassException("Error deleting doctor details: ",e);
        }
     return delete;
    }

}