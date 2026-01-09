package org.example.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.AppointmentDAO;
import org.example.model.Appointment;
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
import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AppointmentServletTest {
    private AppointmentServlet servlet;
    private AppointmentDAO appointmentDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setup() throws Exception {
        servlet = new AppointmentServlet();
        appointmentDAO = mock(AppointmentDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        Field field = AppointmentServlet.class.getDeclaredField("ad");
        field.setAccessible(true);
        field.set(servlet, appointmentDAO);
        PrintWriter writer=new PrintWriter(System.out);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoPost_success() throws Exception {
        when(request.getParameter("patientId")).thenReturn("1");
        when(request.getParameter("doctorId")).thenReturn("101");
        when(request.getParameter("appointmentDate")).thenReturn("2025-01-12");
        when(request.getParameter("appointmentTime")).thenReturn("17:54:20");
        doNothing().when(appointmentDAO).create(any((Appointment.class)));
        servlet.doPost(request, response);
        verify(appointmentDAO).create(any());
        verify(response,times(1)).sendRedirect("appointment.html");
    }


    @Test
    void testDoGet_success() throws Exception {
        Appointment a = new Appointment();
        a.setPatientId(1);
        a.setDoctorId(Integer.parseInt("101"));
        a.setAppointmentDate(Date.valueOf("2025-01-12"));
        a.setAppointmentTime("17:54:20");
        when(appointmentDAO.findAll()).thenReturn(List.of(a));
        PrintWriter writer= mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
        verify(response).setContentType("text/html");
        verify(appointmentDAO).findAll();
        verify(writer,atLeastOnce()).println(anyString());
    }

    @Test
    void testDoPut_success() throws Exception {
        Appointment a = new Appointment();
        a.setAppointmentId(1);
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = mapper.writeValueAsBytes(a);
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
        when(appointmentDAO.update(any(Appointment.class))).thenReturn(true);
        servlet.doPut(request,response);
        verify(appointmentDAO).update(any(Appointment.class));

    }
    @Test
    void testDelete_success()throws Exception{
        when(request.getParameter("id")).thenReturn("1");
        when(appointmentDAO.delete(1)).thenReturn(true);
        servlet.doDelete(request,response);
        verify(appointmentDAO).delete(1);
    }

    @Test
    void testDoDelete_notFound() throws Exception{
        when(request.getParameter("id")).thenReturn("2");
        when(appointmentDAO.delete(2)).thenReturn(false);
        servlet.doDelete(request,response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

}
