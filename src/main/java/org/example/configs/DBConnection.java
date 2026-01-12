package org.example.configs;
import org.example.Exception.MyClassException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class DBConnection {

    private DBConnection() {
        throw new UnsupportedOperationException("utility class");
    }

    private static final String configFile1="configs.properties";

    public static Connection getConnection() {
        Properties prop=new Properties();
        try (FileInputStream fis=new FileInputStream(configFile1)){
            prop.load(fis);
            String url= prop.getProperty("db.url");
            String user=prop.getProperty("db.user");
            String password=prop.getProperty("db.password");
            return DriverManager.getConnection(url,user,password);
        }catch(SQLException|IOException e){
            throw new MyClassException("Error in connection",e);
        }
    }
}


