package org.example.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.MyClassException;
import org.example.dao.PatientDAO;
import org.example.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;
import static org.mockito.Mockito.*;


public class PatientServletTest {
    private PatientServlet servlet;
    private PatientDAO patientDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setup() throws Exception {
        servlet = new PatientServlet();
        patientDAO = mock(PatientDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        Field field = PatientServlet.class.getDeclaredField("pd");
        field.setAccessible(true);
        field.set(servlet, patientDAO);
    }

    @Test
    void testDoPost_success() throws Exception {
        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("age")).thenReturn("32");
        when(request.getParameter("gender")).thenReturn("Male");
        when(request.getParameter("dob")).thenReturn("1994-01-01");
        when(request.getParameter("phone")).thenReturn("9876543210");
        when(request.getParameter("city")).thenReturn("Chennai");
        when(request.getParameter("bloodGroup")).thenReturn("AB+");
        doNothing().when(patientDAO).create(any((Patient.class)));
        servlet.doPost(request, response);
        verify(patientDAO).create(any());
        verify(response, times(1)).sendRedirect("patient.html");
    }


   @Test
  void testDoGet_success() throws Exception {
        Patient p = new Patient();
        p.setPatientId(1);
        p.setName("John");
        p.setAge(32);
        p.setGender("Male");
        p.setDateOfBirth(Date.valueOf("1994-01-01"));
        p.setBloodGroup("AB+");
        p.setPhone("9876543210");
       p.setCity("Chennai");
        when(patientDAO.findAll()).thenReturn(List.of(p));
       PrintWriter writer= mock(PrintWriter.class);
       when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
        verify(response).setContentType("text/html");
        verify(patientDAO).findAll();
        verify(writer,atLeastOnce()).println(anyString());
    }

    @Test
    void testDoPut_success() throws Exception {
       Patient p = new Patient();
       p.setName("Updated Name");
       ObjectMapper mapper = new ObjectMapper();
        byte[] json = mapper.writeValueAsBytes(p);
       ServletInputStream inputStream = new ServletInputStream() {
            private final InputStream stream = new ByteArrayInputStream(json);

          @Override
           public int read() throws IOException {
               return stream.read();
           }

           @Override
           public boolean isFinished() {
               return false;
            }

           @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(javax.servlet.ReadListener readListener) {}
            };

          when(request.getInputStream()).thenReturn(inputStream);
          when(patientDAO.update(any(Patient.class))).thenReturn(true);
          servlet.doPut(request,response);
          verify(patientDAO).update(any(Patient.class));

        }
       @Test
    void testDelete_success() {
        when(request.getParameter("id")).thenReturn("1");
        when(patientDAO.delete(1)).thenReturn(true);
        servlet.doDelete(request,response);
        verify(patientDAO).delete(1);
       }
       @Test
       void testDoDelete_notFound() throws MyClassException{
        when(request.getParameter("id")).thenReturn("2");
        when(patientDAO.delete(2)).thenReturn(false);
        servlet.doDelete(request,response);
           verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
       }

    }
