package org.example.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.DoctorDAO;
import org.example.model.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;



@WebServlet("/doctor")
public class DoctorServlet extends HttpServlet {

    private static final Logger LOGGER= LoggerFactory.getLogger(DoctorServlet.class);
    private final DoctorDAO dd = new DoctorDAO();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
        LOGGER.info("DoctorServlet-doPost Started");
        try {

            String name=request.getParameter("name");
            String specialization=request.getParameter("specialization");
            String availability=request.getParameter("availability");
            if((name==null|| name.trim().isEmpty()) || (specialization==null ||specialization.trim().isEmpty()) ||(availability==null ||availability.trim().isEmpty())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            Doctor d=new Doctor();
            d.setName(name);
            d.setSpecialization(specialization);
            d.setAvailability(availability);
            try{
                dd.create(d);
            LOGGER.info("doctor details created  successfully:{}", d.getName());
            response.sendRedirect("doctor.html");
            }catch(Exception e){
                LOGGER.error("database error while saving patient",e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e){
            LOGGER.error("database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)  {

        try (PrintWriter out = response.getWriter()) {
            LOGGER.info("DoctorServlet-doGet started");
            response.setContentType("text/html");
            out.println("<h2> Doctor List </h2>");
            dd.findAll().forEach(doctor -> {
                out.println(
                        doctor.getDoctorId()+"|"+doctor.getName() + "|" + doctor.getSpecialization() + "|" + doctor.getAvailability()

                );
                LOGGER.info("Doctor details fetched successfully");
            });
        } catch (Exception e) {
            LOGGER.error("database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);


        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            LOGGER.info("DoctorServlet-doPut started");
            ObjectMapper mapper=new ObjectMapper();
            Doctor d=mapper.readValue(request.getInputStream(), Doctor.class);
            boolean updated=dd.update(d);
            response.setContentType("application/json");

            if (updated) {
                response.getWriter().write("{\"message\":\"Doctor updated successfully\"}");
            }else{
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"message\":\"Doctor not found\"}");
            }
        } catch(Exception e) {
            LOGGER.error("Database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response)
    {
        try {
            LOGGER.info("DoctorServlet-doDelete started");
            String idStr=request.getParameter("id");

            if(idStr==null){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Doctor_id is required");
            }
            int id= Integer.parseInt(idStr);

            boolean deleted=dd.delete(id);
            response.setContentType("application/json");
            if(deleted) {
                response.getWriter().write("{\"message\":\"doctor deleted successfully\"}");
            }
            else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.error("Database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
}