package org.example.service;
import org.example.dao.LoggingDAO;
import org.example.model.LoggingUser;
import org.example.util.Hashing;
import org.example.util.JwtToken;

public class LoggingService {
      LoggingDAO loggingDAO =new LoggingDAO();
        public void addUser(LoggingUser loggingUser) {

            String pass = loggingUser.getLoggingPassword();
            loggingUser.setPassword(Hashing.hashPassword(pass));

            loggingDAO.loggingUser(loggingUser);

        }

        public String verify(String loggingUsername , String password){
            String hashPassword = loggingDAO.getPass(loggingUsername);
            if(hashPassword !=null) {
                boolean isValid = Hashing.verifyPassword(password, hashPassword);
                if (isValid) {
                    return JwtToken.generateToken(loggingUsername);
                } else {
                    return "Password wrong";
                }
            }
            else {
                return "No username found in this user please register";
            }
        }



    }
