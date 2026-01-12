package org.example.configs;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.Exception.MyClassException;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



public final class DBConnection {

//    private static final String configFile1="configs.properties";
  private static final HikariDataSource dataSource;
    static{
        try{
            Properties prop=new Properties();
            prop.load(new FileInputStream("c:\\Users\\User\\IdeaProjects\\HealthCare\\liquibase.properties"));
            HikariConfig config= new HikariConfig();
            config.setDriverClassName(prop.getProperty("driver"));
            config.setJdbcUrl(prop.getProperty("url"));
            config.setUsername(prop.getProperty("username"));
            config.setPassword(prop.getProperty("password"));
            config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("hikari.maximumPool")));
            dataSource=new HikariDataSource(config);
        }catch (Exception e){
            throw new MyClassException("DB is not connected",e);
        }
        }
    public static DataSource getConnect(){
    return dataSource;
    }
    }

//
//    public static Connection getConnection() {
//        Properties prop=new Properties();
//        try (FileInputStream fis=new FileInputStream(configFile1)){
//            prop.load(fis);
//            String url= prop.getProperty("db.url");
//            String user=prop.getProperty("db.user");
//            String password=prop.getProperty("db.password");
//            return DriverManager.getConnection(url,user,password);
//        }catch(SQLException|IOException e){
//            throw new MyClassException("Error in connection",e);
//        }
//
//
//    }



