package org.example.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.PatientDAO;
import org.example.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Date;


public class PatientServlet extends HttpServlet {

    private static final Logger LOGGER= LoggerFactory.getLogger(PatientServlet.class);
    private final PatientDAO pd = new PatientDAO();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)  {
       LOGGER.info("patientServlet-doPost Started");

       try {
           String name=request.getParameter("name");
           String agestr=request.getParameter("age");
           String gender=request.getParameter("gender");
           String dobstr=request.getParameter("dob");
           String phone=request.getParameter("phone");
           String city=request.getParameter("city");
           String bloodGroup=request.getParameter("bloodGroup");
           if((name==null || name.trim().isEmpty()) || (agestr==null || agestr.trim().isEmpty()) || (gender==null || gender.trim().isEmpty()))
                    {
               response.sendError(HttpServletResponse.SC_BAD_REQUEST,"All fields are required");
               return;
           }
           if((phone==null || phone.trim().isEmpty()) || (dobstr==null || dobstr.trim().isEmpty())
                   || (bloodGroup==null || bloodGroup.trim().isEmpty()) || (city==null || city.trim().isEmpty())){
               response.sendError(HttpServletResponse.SC_BAD_REQUEST,"All fields are required");
               return;
           }
           int age;
           Date dob;
           try{
           age=Integer.parseInt(agestr.trim());
           dob=Date.valueOf(dobstr.trim());
           }catch(NumberFormatException e) {
              LOGGER.warn("Invalid age or date format",e);
              response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
              return;
           }
           Patient p=new Patient();
           p.setName(name);
           p.setAge(age);
           p.setGender(gender);
           p.setDateOfBirth (dob);
           p.setPhone(phone);
           p.setCity(city);
           p.setBloodGroup(bloodGroup);
           try{
               pd.create(p);
               LOGGER.info("patient saved successfully:{}", p.getName());
               response.sendRedirect("patient.html");
           }catch(Exception e){
               LOGGER.error("database error while saving patient",e);
               response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           }
       } catch(Exception e){
           LOGGER.error("Database error:",e);
           response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
       }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
       LOGGER.info("PatientServlet-doGet started");
       response.setContentType("text/html");
       try(PrintWriter out=response.getWriter()) {
           out.println("<h2> Patient List </h2>");
           pd.findAll().forEach(patient->{
               out.println(
                   patient.getPatientId()+"|"+patient.getName()+"|" +patient.getAge()+"|"+patient.getGender()+"|"
                  +"|"+patient.getDateOfBirth()+"|"+patient.getBloodGroup()+"|"+ patient.getPhone()+"|"+patient.getCity()
               );
               LOGGER.info("Patient details fetched successfully");
           });
       } catch(Exception e){
           LOGGER.error("database error:",e);
           response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

       }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {

        try {
            LOGGER.info("PatientServlet-doPut started");
            ObjectMapper mapper=new ObjectMapper();
            Patient p=mapper.readValue(request.getInputStream(),Patient.class);
            boolean updated=pd.update(p);
            response.setContentType("application/json");

            if (updated) {
                response.getWriter().write("{\"message\":\"Patient updated successfully\"}");
            }else{
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.getWriter().write("{\"message\":\"Patient not found\"}");
            }
        } catch(Exception e) {
            LOGGER.error("Database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response)  {

        try {
            LOGGER.info("PatientServlet-doDelete started");
            String idStr=request.getParameter("id");
            if(idStr==null){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"patient_id is required");
            }
            int id= Integer.parseInt(idStr);

            boolean deleted=pd.delete(id);
            response.setContentType("application/json");
            if(deleted) {
                response.getWriter().write("{\"message\":\"patient deleted successfully\"}");
            } else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.error("database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }

}


