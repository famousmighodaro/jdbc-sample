package org.jdbc.learning;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManagement {
       private final String url ;//= "jdbc:mysql://localhost:3306/sakila";
       private final Properties properties;
        String query = "select * from customer where customer_id<20";


    public DatabaseConnectionManagement(String host, String databaseName,
                                        String userName, String password) {
        this.url = "jdbc:mysql://"+host+"/"+databaseName;
        this.properties = new Properties();
        this.properties.setProperty("user", userName);
        properties.setProperty("password", password);
    }

    public Connection getConnection() throws SQLException{
        return DriverManager.getConnection(this.url, this.properties);
    }
}
