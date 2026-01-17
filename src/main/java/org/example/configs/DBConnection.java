package org.example.configs;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.exception.MyClassException;
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
                Properties prop = new Properties();

                    try(FileInputStream fis=new FileInputStream("c:\\Users\\User\\IdeaProjects\\HealthCare\\liquibase.properties")) {
                        prop.load(fis);
                        HikariConfig config = new HikariConfig();
                        config.setDriverClassName(prop.getProperty("driver"));
                        config.setJdbcUrl(prop.getProperty("url"));
                        config.setUsername(prop.getProperty("username"));
                        config.setPassword(prop.getProperty("password"));
                        config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("hikari.maximumPool")));
                        config.setMinimumIdle(Integer.parseInt(prop.getProperty("hikari.minimumIdle")));
                        config.setConnectionTimeout((Integer.parseInt(prop.getProperty("hikari.connectionTimeout"))));
                        dataSource = new HikariDataSource(config);
                    }catch (Exception e){
                        throw new MyClassException("DB is not connected", e);
                    }

            }
        }
        return dataSource;
    }
}



