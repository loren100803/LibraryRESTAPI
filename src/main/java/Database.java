import java.sql.*;


public class Database {
    public static String[] getData(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }
        final String DB_URL = "jdbc:mysql://rds-mysql-main.ck4bf8ke7zt9.eu-central-1.rds.amazonaws.com/School";
        final String USER = "admin";
        final String PASS = "Ym4&lrSyAGv";
        String[] data = new String[3];
        data[0] = DB_URL;
        data[1] = USER;
        data[2] = PASS;
        return data;
    }
}
