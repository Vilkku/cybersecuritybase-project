package sec.project;

import org.h2.tools.RunScript;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
public class CyberSecurityBaseProjectApplication {

    public static void main(String[] args) throws Throwable {
        String databaseAddress = "jdbc:h2:file:./database";
        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        try {
            RunScript.execute(connection, new FileReader("sql/database-schema.sql"));
        } catch (Throwable t) {
            System.err.println(t.getMessage());
        }

        connection.close();

        SpringApplication.run(CyberSecurityBaseProjectApplication.class);
    }
}
