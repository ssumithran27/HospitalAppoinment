package org.example.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.PaymentDAO;
import org.example.model.Payment;
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

public class PaymentServletTest {
    private PaymentServlet servlet;
    private PaymentDAO paymentDAO;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setup() throws Exception {
        servlet = new PaymentServlet();
        paymentDAO = mock(PaymentDAO.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        Field field = PaymentServlet.class.getDeclaredField("pyd");
        field.setAccessible(true);
        field.set(servlet, paymentDAO);
    }

    @Test
    void testDoPost_success() throws Exception {
        when(request.getParameter("appointmentId")).thenReturn("1");
        when(request.getParameter("amount")).thenReturn("8000.00");
        when(request.getParameter("paymentType")).thenReturn("cash");
        when(request.getParameter("paymentStatus")).thenReturn("completed");
        doNothing().when(paymentDAO).create(any((Payment.class)));
        servlet.doPost(request, response);
        verify(paymentDAO).create(any());
        verify(response, times(1)).sendRedirect("payment.html");
    }


    @Test
    void testDoGet_success() throws Exception {
        Payment py = new Payment();
        py.setAppointmentId(1);
        py.setAmount(Double.parseDouble("8000.00"));
        py.setPaymentType("cash");
        py.setPaymentStatus("done");
        when(paymentDAO.findAll()).thenReturn(List.of(py));
        PrintWriter writer= mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        servlet.doGet(request,response);
        verify(response).setContentType("text/html");
        verify(paymentDAO).findAll();
        verify(writer,atLeastOnce()).println(anyString());
    }

    @Test
    void testDoPut_success() throws Exception {
        Payment p = new Payment();
        p.setPaymentId(1);
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
        when(paymentDAO.update(any(Payment.class))).thenReturn(true);
        servlet.doPut(request,response);
        verify(paymentDAO).update(any(Payment.class));

    }
    @Test
    void testDelete_success()throws Exception{
        when(request.getParameter("id")).thenReturn("1");
        when(paymentDAO.delete(1)).thenReturn(true);
        servlet.doDelete(request,response);
        verify(paymentDAO).delete(1);
    }
    @Test
    void testDoDelete_notFound() throws Exception{
        when(request.getParameter("id")).thenReturn("2");
        when(paymentDAO.delete(2)).thenReturn(false);
        servlet.doDelete(request,response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

}
