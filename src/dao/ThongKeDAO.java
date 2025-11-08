package dao;

import util.JDBCUtil;

import java.sql.*;
import java.util.*;

/**
 * DAO for statistics and reports
 */
public class ThongKeDAO {
    private static ThongKeDAO instance;

    public static ThongKeDAO getInstance() {
        if (instance == null) {
            instance = new ThongKeDAO();
        }
        return instance;
    }

    /**
     * Get revenue by movie
     */
    public Map<String, Double> getDoanhThuTheoPhim() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT TOP 10 p.tenPhim, SUM(v.giaVe) as tongDoanhThu " +
                     "FROM Ve v " +
                     "INNER JOIN LichChieu lc ON v.maLichChieu = lc.maLichChieu " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "WHERE v.trangThai = N'Da dat' " +
                     "GROUP BY p.tenPhim, p.maPhim " +
                     "ORDER BY tongDoanhThu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("tenPhim"), rs.getDouble("tongDoanhThu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get ticket count by movie
     */
    public Map<String, Double> getSoVeTheoPhim() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT TOP 10 p.tenPhim, COUNT(v.maVe) as soVe " +
                     "FROM Ve v " +
                     "INNER JOIN LichChieu lc ON v.maLichChieu = lc.maLichChieu " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "WHERE v.trangThai = N'Da dat' " +
                     "GROUP BY p.tenPhim, p.maPhim " +
                     "ORDER BY soVe DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("tenPhim"), (double) rs.getInt("soVe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get revenue by genre
     */
    public Map<String, Double> getDoanhThuTheoTheLoai() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT p.theLoai, SUM(v.giaVe) as tongDoanhThu " +
                     "FROM Ve v " +
                     "INNER JOIN LichChieu lc ON v.maLichChieu = lc.maLichChieu " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "WHERE v.trangThai = N'Da dat' AND p.theLoai IS NOT NULL " +
                     "GROUP BY p.theLoai " +
                     "ORDER BY tongDoanhThu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String theLoai = rs.getString("theLoai");
                if (theLoai != null && !theLoai.isEmpty()) {
                    result.put(theLoai, rs.getDouble("tongDoanhThu"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get total revenue
     */
    public double getTongDoanhThu() {
        String sql = "SELECT ISNULL(SUM(tongTien), 0) as tongDoanhThu " +
                     "FROM HoaDon " +
                     "WHERE trangThaiThanhToan = N'Da thanh toan'";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("tongDoanhThu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get total tickets sold
     */
    public int getTongVeDaBan() {
        String sql = "SELECT COUNT(*) as soVe FROM Ve WHERE trangThai = N'Da dat'";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("soVe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get total customers
     */
    public int getTongKhachHang() {
        String sql = "SELECT COUNT(*) as soKhachHang FROM KhachHang";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("soKhachHang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get total movies
     */
    public int getTongPhim() {
        String sql = "SELECT COUNT(*) as soPhim FROM Phim";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("soPhim");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get revenue by month (last 6 months)
     */
    public Map<String, Double> getDoanhThuTheoThang() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT FORMAT(hd.ngayLap, 'MM/yyyy') as thang, SUM(hd.tongTien) as doanhThu " +
                     "FROM HoaDon hd " +
                     "WHERE hd.trangThaiThanhToan = N'Da thanh toan' " +
                     "  AND hd.ngayLap >= DATEADD(MONTH, -6, GETDATE()) " +
                     "GROUP BY FORMAT(hd.ngayLap, 'MM/yyyy'), YEAR(hd.ngayLap), MONTH(hd.ngayLap) " +
                     "ORDER BY YEAR(hd.ngayLap), MONTH(hd.ngayLap)";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("thang"), rs.getDouble("doanhThu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get occupancy rate by theater
     */
    public Map<String, Double> getTyLeLapDayTheoPhong() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT pc.tenPhong, " +
                     "  CAST(COUNT(v.maVe) * 100.0 / NULLIF(SUM(pc.soGhe), 0) AS DECIMAL(5,2)) as tyLe " +
                     "FROM PhongChieu pc " +
                     "LEFT JOIN LichChieu lc ON pc.maPhong = lc.maPhong " +
                     "LEFT JOIN Ve v ON lc.maLichChieu = v.maLichChieu AND v.trangThai = N'Da dat' " +
                     "GROUP BY pc.tenPhong, pc.maPhong " +
                     "ORDER BY tyLe DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String tenPhong = rs.getString("tenPhong");
                double tyLe = rs.getDouble("tyLe");
                result.put(tenPhong, tyLe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
