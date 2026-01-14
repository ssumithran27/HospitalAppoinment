package org.example.dao;
import org.example.configs.DBConnection;
import org.example.Exception.MyClassException;
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
    private static final Logger logger = LoggerFactory.getLogger(DoctorDAO.class);

    static final int Doctor_Name=1;
    static final int Doctor_Specialization=2;
    static final int Doctor_Availability=3;
    static final int Doctor_Id=4;
    public void create (Doctor d) {
        String sql="""
                   INSERT INTO doctor(name,specialization,availability)
                    VALUES (?,?,?)""";

        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(Doctor_Name,d.getName());
            ps.setString(Doctor_Specialization,d.getSpecialization());
            ps.setString(Doctor_Availability,d.getAvailability());
           int affectedRows= ps.executeUpdate();
            if(affectedRows==0){
                logger.error("Insert failed,no rows affected for doctor{} :",d.getDoctorId());
            }else{
                logger.info("successfully inserted the doctor details{}: ",d.getDoctorId());
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

         boolean update=false;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString (Doctor_Name,d.getName());
            ps.setString(Doctor_Specialization,d.getSpecialization());
            ps.setString(Doctor_Availability,d.getAvailability());
            ps.setInt(Doctor_Id,d.getDoctorId());
            update= ps.executeUpdate()>0;
        } catch (SQLException e) {
            throw new MyClassException("Error updating doctor details: ",e);
        }
        return update;
    }

    public boolean delete(int id)  {
        String sql="""
                   delete from patient where doctor_id=?""";
        boolean delete=false;

        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(Doctor_Id,id);
            delete= ps.executeUpdate()>0;
        } catch (SQLException e) {
            throw new MyClassException("Error deleting doctor details: ",e);
        }
     return delete;
    }

}