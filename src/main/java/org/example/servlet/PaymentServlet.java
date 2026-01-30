package org.example.servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dao.PaymentDAO;
import org.example.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;





public class PaymentServlet extends HttpServlet   {

    private static final Logger LOGGER= LoggerFactory.getLogger(PaymentServlet.class);
    private final PaymentDAO pyd=new PaymentDAO();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)  {

        try {
            LOGGER.info("PaymentServlet-doPost Started");
            String appointmentIdStr=request.getParameter("appointmentId");
            String amountStr=request.getParameter("amount");
            String paymentType=request.getParameter("paymentType");
            String paymentStatus=request.getParameter("paymentStatus");
            if((appointmentIdStr==null || appointmentIdStr.trim().isEmpty()) ||(amountStr==null || amountStr.trim().isEmpty()) || (paymentType==null ||paymentType.trim().isEmpty()) ||
                    (paymentStatus==null || paymentStatus.trim().isEmpty())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"All fields are required");
                return;
            }
            int appointmentId;
            double amount;
            try{
                appointmentId=Integer.parseInt(appointmentIdStr);
                amount=Double.parseDouble(amountStr);
            }catch(NumberFormatException e) {
                LOGGER.warn("Invalid ID or Amount",e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
              return;
            }
            Payment py=new Payment();
            py.setAppointmentId(appointmentId);
            py.setAmount(amount);
            py.setPaymentType(paymentType);
            py.setPaymentStatus(paymentStatus);
            try{
                pyd.create(py);
                LOGGER.info("payment saved successfully:{}",py.getPaymentId());
                response.sendRedirect("payment.html");
            }catch(Exception e){
                LOGGER.error("database error while saving  appointment",e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            LOGGER.error("database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        LOGGER.info("PaymentServlet-doGet started");
        response.setContentType("text/html");
        try(PrintWriter out=response.getWriter()) {
            out.println("<h2> Payment List </h2>");
            pyd.findAll().forEach(payment->{
                out.println(
                        payment.getPaymentId()+"|"+payment.getAppointmentId()+"|"+payment.getAmount()+"|"+payment.getPaymentType()+"|"+payment.getPaymentStatus()

                );
                LOGGER.info("Payment details fetched successfully");
            });
        }catch (Exception e){
            LOGGER.error("Database error",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {

        try {
            LOGGER.info("PaymentServlet-doPut started");
            ObjectMapper mapper=new ObjectMapper();
            Payment py=mapper.readValue(request.getInputStream(),Payment.class);
            boolean updated=pyd.update(py);

            response.setContentType("application/json");

            if (updated) {
                response.getWriter().write("{\"message\":\"Payment updated successfully\"}");
            }else{
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.getWriter().write("{\"message\":\"Payment not found\"}");
            }
        } catch(Exception e) {
            LOGGER.error("Database error",e);
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
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Payment_id is required");
            }
            int id= Integer.parseInt(idStr);
            boolean deleted=pyd.delete(id);
            response.setContentType("application/json");
            if(deleted) {
                response.getWriter().write("{\"message\":\"patient deleted successfully\"}");
            }
            else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.error("database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }

}