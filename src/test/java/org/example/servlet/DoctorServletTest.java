package org.example.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Exception.MyClassException;
import org.example.dao.DoctorDAO;
import org.example.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DoctorServletTest {
    private DoctorServlet servlet;
    private DoctorDAO doctorDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setup() throws Exception {
        servlet = new DoctorServlet();
        doctorDAO = mock(DoctorDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        Field field = DoctorServlet.class.getDeclaredField("dd");
        field.setAccessible(true);
        field.set(servlet, doctorDAO);
        PrintWriter writer=new PrintWriter(System.out);
        when(response.getWriter()).thenReturn(writer);
    }


    @Test
    void testDoPost_success() throws Exception {
        response.setContentType("text/html");
        when(request.getParameter("name")).thenReturn("John");
        when(request.getParameter("specialization")).thenReturn("Ortho");
        when(request.getParameter("availability")).thenReturn("yes");
        doNothing().when(doctorDAO).create(any((Doctor.class)));
        servlet.doPost(request, response);
        verify(doctorDAO).create(any());
        verify(response,times(1)).sendRedirect("doctor.html");
    }

    @Test
    void testDoGet_success() throws Exception {
        Doctor d = new Doctor();
        d.setDoctorId(1);
        d.setName("John");
        d.setSpecialization("Ortho");
        d.setAvailability("Chennai");
        when(doctorDAO.findAll()).thenReturn(List.of(d));
        PrintWriter writer= mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
        verify(response).setContentType("text/html");
        verify(doctorDAO).findAll();
        verify(writer,atLeastOnce()).println(anyString());
    }

    @Test
    void testDoPut_success() throws Exception {
        Doctor d = new Doctor();
        d.setName("Updated Name");
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = mapper.writeValueAsBytes(d);
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
        when(doctorDAO.update(any(Doctor.class))).thenReturn(true);
        servlet.doPut(request,response);
        verify(doctorDAO).update(any(Doctor.class));

    }
    @Test
    void testDelete_success()throws MyClassException{
        when(request.getParameter("id")).thenReturn("1");
        when(doctorDAO.delete(1)).thenReturn(true);
        servlet.doDelete(request,response);
        verify(doctorDAO).delete(1);
    }
    @Test
    void testDoDelete_notFound() throws MyClassException{
        when(request.getParameter("id")).thenReturn("2");
        when(doctorDAO.delete(2)).thenReturn(false);
        servlet.doDelete(request,response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

}


