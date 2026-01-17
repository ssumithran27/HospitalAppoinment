package org.example.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.AppointmentDAO;
import org.example.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.Date;

@WebServlet("/appointment")
public class AppointmentServlet extends HttpServlet {

    private static final Logger LOGGER= LoggerFactory.getLogger(AppointmentServlet.class);
    private final AppointmentDAO ad = new AppointmentDAO();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            LOGGER.info("appointmentServlet-doPost Started");
            String patientIdStr= request.getParameter("patientId");
            String doctorIdStr=request.getParameter("doctorId");
            String appointmentDateStr=request.getParameter("appointmentDate");
            String appointmentTime=request.getParameter("appointmentTime");

            if((patientIdStr==null || patientIdStr.trim().isEmpty()) || (doctorIdStr==null ||doctorIdStr.trim().isEmpty()) ||(appointmentDateStr==null || appointmentDateStr.trim().isEmpty())
                    || (appointmentTime==null || appointmentTime.trim().isEmpty())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
             int patientId;
             int doctorId;
             Date appointmentDate;
            try{
                patientId=Integer.parseInt(patientIdStr.trim());
                doctorId=Integer.parseInt(doctorIdStr.trim());
                appointmentDate=Date.valueOf(appointmentDateStr.trim());
            } catch(NumberFormatException e) {
                LOGGER.warn("Invalid age or date format",e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            Appointment a=new Appointment();
            a.setPatientId(patientId);
            a.setDoctorId(doctorId);
            a.setAppointmentDate(appointmentDate);
            a.setAppointmentTime(appointmentTime);
           try{
               ad.create(a);
            LOGGER.info("appointment details created successfully:{}", a.getAppointmentId());
            response.sendRedirect("appointment.html");
           }catch(Exception e){
               LOGGER.error("Database error:",e);
               response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           }

        }catch (Exception e) {
            LOGGER.error("Database error",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        LOGGER.info("AppointmentServlet-doGet started");
        response.setContentType("text/html");
        try(PrintWriter out=response.getWriter()) {
            out.println("<h2> Appointment List </h2>");
            ad.findAll().forEach(appointment->{
                out.println(
                        appointment.getAppointmentId()+"|" +appointment.getPatientId()+"|"+appointment.getDoctorId()+"|"
                                +appointment.getAppointmentDate()+"|"+appointment.getAppointmentTime()
                );
                LOGGER.info("appointment details fetched successfully");
            });
        } catch (Exception e) {
            LOGGER.error("Database error",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)  {

        try {
            LOGGER.info("AppointmentServlet-doPut started");
            ObjectMapper mapper=new ObjectMapper();
            Appointment a=mapper.readValue(request.getInputStream(),Appointment.class);
            boolean updated=ad.update(a);

            response.setContentType("application/json");

            if (updated) {
                response.getWriter().write("{\"message\":\"Appointment updated successfully\"}");
            }else{
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.getWriter().write("{\"message\":\"Appointment not found\"}");
            }
        } catch(Exception e) {
            LOGGER.error("Database error",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response)  {
        try {
            LOGGER.info("AppointmentServlet-doDelete started");
            String idStr=request.getParameter("id");

            if(idStr==null){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Appointment_id is required");
            }
            int id= Integer.parseInt(idStr);

            boolean deleted=ad.delete(id);
            response.setContentType("application/json");
            if(deleted) {
                response.getWriter().write("{\"message\".\":Appointment deleted successfully\"}");
            } else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.error("Database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }


}