package org.example.dao;

import org.example.configs.DBConnection;
import org.example.exception.MyClassException;
import org.example.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentDAO.class);

    static final int PATIENT_ID =1;
    static final int DOCTOR_ID=2;
    static final int APPOINTMENT_DATE=3;
    static final int APPOINTMENT_TIME=4;
    static final int APPOINTMENT_ID=5;
    public void create(Appointment a)  {
        String sql= """
                INSERT INTO appointment (patient_id,doctor_id,appointment_date,
                appointment_time) VALUES (?,?,?,?)
                """;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps= con.prepareStatement(sql)) {
            ps.setInt(PATIENT_ID,a.getPatientId());
            ps.setInt(DOCTOR_ID,a.getDoctorId());
            ps.setDate(APPOINTMENT_DATE, a.getAppointmentDate());
            ps.setString(APPOINTMENT_TIME,a.getAppointmentTime());
            int affectedRows=ps.executeUpdate();
            if(affectedRows==0){
                LOGGER.error("Insert failed,no rows affected for appointment{} :",a.getAppointmentId());
            } else{
                LOGGER.info("successfully inserted the Appointment details{}: ",a.getAppointmentId());
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
        boolean update;
        try(Connection con= DBConnection.getConnect().getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setInt(PATIENT_ID,a.getPatientId());
            ps.setInt(DOCTOR_ID,a.getDoctorId());
            ps.setDate(APPOINTMENT_DATE,a.getAppointmentDate());
            ps.setString (APPOINTMENT_TIME,a.getAppointmentTime());
            ps.setInt(APPOINTMENT_ID,a.getAppointmentId());
            update= ps.executeUpdate()>0;
        }catch(SQLException e){
           throw new MyClassException("Error updating appointment details:",e);
        }
        return update;
    }

    public  boolean delete(int id) {
        String sql=" delete from appointment where appointment_id=?";
        boolean delete;
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