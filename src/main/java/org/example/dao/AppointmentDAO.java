package org.example.dao;
import org.example.configs.DBConnection;
import org.example.Exception.MyClassException;
import org.example.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentDAO.class);

    static final int Patient_Id =1;
    static final int Doctor_Id=2;
    static final int Appointment_Date=3;
    static final int Appointment_Time=4;
    static final int Appointment_Id=5;
    public void create(Appointment a)  {
        String sql= """
                INSERT INTO appointment (patient_id,doctor_id,appointment_date,
                appointment_time) VALUES (?,?,?,?)
                """;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps= con.prepareStatement(sql)) {
            ps.setInt(Patient_Id,a.getPatientId());
            ps.setInt(Doctor_Id,a.getDoctorId());
            ps.setDate(Appointment_Date,(Date) a.getAppointmentDate());
            ps.setString(Appointment_Time,a.getAppointmentTime());
            int affectedRows=ps.executeUpdate();
            if(affectedRows==0){
                logger.error("Insert failed,no rows affected for appointment{} :",a.getAppointmentId());
            } else{
                logger.info("successfully inserted the Appointment details{}: ",a.getAppointmentId());
            }
        } catch (SQLException e) {
         throw new MyClassException("error in creating appointment",e);

        }
    }

    public List<Appointment> findAll()  {
        List<Appointment> appointments=new ArrayList<>();
        String sql="""
                  select appointment_id,patient_id,doctor_id,appointment_date,appointment_time from appointment
                  """;
        try(Connection con =DBConnection.getConnect().getConnection();
            PreparedStatement ps =con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery()) {
            while (rs.next()){
                Appointment a=new Appointment();
                a.setAppointmentId(rs.getInt("appointmentId"));
                a.setPatientId(rs.getInt("patientId"));
                a.setDoctorId(rs.getInt("doctorId"));
                a.setAppointmentDate(rs.getDate("appointmentDate"));
                a.setAppointmentTime(rs.getString("appointmentTime"));
                appointments.add(a);
            }
        } catch(SQLException e) {
            throw new MyClassException("Error in fetching appointment",e);
        }
        return appointments;
    }

    public boolean update(Appointment a) {
        String sql= "update patient set patient_id=?,doctor_id=?,appointment_date=?,appointment_time=?,status=?where appointment_id=?";
        boolean update=false;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(Patient_Id,a.getPatientId());
            ps.setInt(Doctor_Id,a.getDoctorId());
            ps.setDate(Appointment_Date,a.getAppointmentDate());
            ps.setString (Appointment_Time,a.getAppointmentTime());
            ps.setInt(Appointment_Id,a.getAppointmentId());
            update= ps.executeUpdate()>0;
        }catch(SQLException e){
           throw new MyClassException("Error updating appointment details:",e);
        }
        return update;
    }

    public  boolean delete(int id) {
        String sql=" delete from appointment where appointment_id=?";


        boolean delete=false;

        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(1,id);
            delete= ps.executeUpdate()>0;
        } catch (SQLException e) {
            throw new MyClassException("Error deleting appointment details:",e);
        }
        return delete;
    }

}