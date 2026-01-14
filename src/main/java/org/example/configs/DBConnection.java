package org.example.configs;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.Exception.MyClassException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.util.Properties;

public final class DBConnection {
    private static HikariDataSource dataSource;

    private DBConnection() {
    }

    public static DataSource getConnect() {
        if (dataSource == null) {
            synchronized(DBConnection.class) {
                if (dataSource == null)
                    try {
                        Properties prop = new Properties();
                        prop.load(new FileInputStream("c:\\Users\\User\\IdeaProjects\\HealthCare\\liquibase.properties"));
                        HikariConfig config = new HikariConfig();
                        config.setDriverClassName(prop.getProperty("driver"));
                        config.setJdbcUrl(prop.getProperty("url"));
                        config.setUsername(prop.getProperty("username"));
                        config.setPassword(prop.getProperty("password"));
                        config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("hikari.maximumPool")));
                        config.setMinimumIdle(Integer.parseInt(prop.getProperty("hikari.minimumIdle")));
                        config.setConnectionTimeout((Integer.parseInt(prop.getProperty("hikari.connectionTimeout"))));
                        dataSource = new HikariDataSource(config);
                    } catch (Exception e) {
                        throw new MyClassException("DB is not connected", e);
                    }
            }
        }
        return dataSource;
    }
}
//    public static DataSource getConnect(){
//    return dataSource;
//    }

//    private static final String configFile1="configs.properties";
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



