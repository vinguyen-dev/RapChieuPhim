package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    private static final String URL =
            "jdbc:sqlserver://DESKTOP-C9B0PSF\\TUONGVISERVER:1433;" +
                    "databaseName=RapChieuPhim;" +
                    "encrypt=true;" +
                    "trustServerCertificate=true;";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sapassword";
    private static Connection connection = null;

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        Connection conn = JDBCUtil.getConnection();
        if (conn != null) {
            System.out.println("Connection established successfully.");
            JDBCUtil.closeConnection();
        } else {
            System.out.println("Failed to establish connection.");
        }
    }
}
