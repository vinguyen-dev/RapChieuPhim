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

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            Connection conn = JDBCUtil.getConnection();
            if (conn != null) {
                System.out.println("Connection established successfully.");
                JDBCUtil.closeConnection(conn);
            }
        } catch (SQLException e) {
            System.out.println("Failed to establish connection.");
            e.printStackTrace();
        }
    }
}
