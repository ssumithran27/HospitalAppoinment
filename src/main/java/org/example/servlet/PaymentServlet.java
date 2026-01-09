package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Exception.MyClassException;
import org.example.dao.PaymentDAO;
import org.example.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.Serial;


@WebServlet("/payment")
public class PaymentServlet extends HttpServlet  {
    @Serial
    private static final long serialVersionUID=1L;
    private static final Logger logger= LoggerFactory.getLogger(PaymentServlet.class);
    private final PaymentDAO pyd=new PaymentDAO();
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        try {
            logger.info("PaymentServlet-doPost Started");
            String appointmentIdStr=request.getParameter("appointmentId");
            String amountStr=request.getParameter("amount");
            String paymentType=request.getParameter("paymentType");
            String paymentStatus=request.getParameter("paymentStatus");
            if((appointmentIdStr==null || appointmentIdStr.trim().isEmpty()) ||(amountStr==null || amountStr.trim().isEmpty()) || (paymentType==null ||paymentType.trim().isEmpty()) ||
                    (paymentStatus==null || paymentStatus.trim().isEmpty())) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"All fields are required");
                return;
            }

            try{
                Integer.parseInt(appointmentIdStr);
                Double.parseDouble(amountStr);

            }catch(NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            }
            int appointmentId=Integer.parseInt(appointmentIdStr);
            double amount=Double.parseDouble(amountStr);


            Payment py=new Payment();
            py.setAppointmentId(appointmentId);
            py.setAmount(amount);
            py.setPaymentType(paymentType);
            py.setPaymentStatus(paymentStatus);
            pyd.create(py);
            logger.info("payment saved successfully:"+py.getPaymentId());
            response.sendRedirect("payment.html");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        logger.info("PaymentServlet-doGet started");
        response.setContentType("text/html");
        try(PrintWriter out=response.getWriter()) {
            out.println("<h2> Payment List </h2>");
            pyd.findAll().forEach(Payment->{
                out.println(
                        Payment.getPaymentId()+"|"+Payment.getAppointmentId()+"|"+Payment.getAmount()+"|"+Payment.getPaymentType()+"|"+Payment.getPaymentStatus()

                );
                logger.info("Payment details fetched successfully");
            });
        }catch (Exception e){
            logger.error("Database error",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new MyClassException("Error in creating Payment details",e);
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {

        try {
            logger.info("PaymentServlet-doPut started");
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
            logger.error("Database error",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }
    @Override
    public void doDelete(HttpServletRequest request,HttpServletResponse response)
    {

        try {
            logger.info("DoctorServlet-doDelete started");
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
            logger.error("database error:",e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        }
    }

}