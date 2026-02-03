package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.exception.MyClassException;
import org.example.model.LoggingUser;
import org.example.service.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID=1L;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final LoggingService loggingService = new LoggingService();
    private static final Logger LOG = LoggerFactory.getLogger(RegisterServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try{ LoggingUser user = objectMapper.readValue(
                request.getInputStream(),
                LoggingUser.class);
            loggingService.addUser(user);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.getWriter().write("User Registered Successfully");
            LOG.info("Registered Successfully");
        }
        catch (Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            throw new MyClassException("Failed",e);
        }
    }
}
