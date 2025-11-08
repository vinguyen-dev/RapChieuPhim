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
     * Get total tickets sold (accurately counting paid and booked tickets)
     * Uses trigger TRG_Ve_TinhTongTien for accurate total calculation
     */
    public int getTongVeDaBan() {
        String sql = "SELECT COUNT(*) as soVe FROM Ve " +
                     "WHERE trangThai IN (N'Da dat', N'Da thanh toan')";

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
     * Get tickets sold by seat type (leveraging database pricing logic)
     */
    public Map<String, Integer> getSoVeTheoLoaiGhe() {
        Map<String, Integer> result = new LinkedHashMap<>();
        String sql = "SELECT g.loaiGhe, COUNT(v.maVe) as soVe " +
                     "FROM Ve v " +
                     "INNER JOIN Ghe g ON v.maGhe = g.maGhe " +
                     "WHERE v.trangThai IN (N'Da dat', N'Da thanh toan') " +
                     "GROUP BY g.loaiGhe " +
                     "ORDER BY soVe DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String loaiGhe = rs.getString("loaiGhe");
                int soVe = rs.getInt("soVe");
                result.put(loaiGhe, soVe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get revenue by seat type (showing pricing effectiveness)
     */
    public Map<String, Double> getDoanhThuTheoLoaiGhe() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT g.loaiGhe, SUM(v.giaVe) as tongDoanhThu " +
                     "FROM Ve v " +
                     "INNER JOIN Ghe g ON v.maGhe = g.maGhe " +
                     "WHERE v.trangThai IN (N'Da dat', N'Da thanh toan') " +
                     "GROUP BY g.loaiGhe " +
                     "ORDER BY tongDoanhThu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String loaiGhe = rs.getString("loaiGhe");
                double doanhThu = rs.getDouble("tongDoanhThu");
                result.put(loaiGhe, doanhThu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get top customers by total spending
     * Uses HoaDon.tongTien which is auto-calculated by trigger TRG_Ve_TinhTongTien
     */
    public Map<String, Double> getTopKhachHang(int limit) {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT TOP " + limit + " kh.tenKhachHang, SUM(hd.tongTien) as tongChiTieu " +
                     "FROM KhachHang kh " +
                     "INNER JOIN HoaDon hd ON kh.maKhachHang = hd.maKhachHang " +
                     "WHERE hd.trangThaiThanhToan = N'Da thanh toan' " +
                     "GROUP BY kh.tenKhachHang, kh.maKhachHang " +
                     "ORDER BY tongChiTieu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("tenKhachHang"), rs.getDouble("tongChiTieu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get revenue by date range
     */
    public Map<String, Double> getDoanhThuTheoNgay(int soDayTruoc) {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT FORMAT(hd.ngayLap, 'dd/MM') as ngay, SUM(hd.tongTien) as doanhThu " +
                     "FROM HoaDon hd " +
                     "WHERE hd.trangThaiThanhToan = N'Da thanh toan' " +
                     "  AND hd.ngayLap >= DATEADD(DAY, -" + soDayTruoc + ", GETDATE()) " +
                     "GROUP BY FORMAT(hd.ngayLap, 'dd/MM'), CAST(hd.ngayLap AS DATE) " +
                     "ORDER BY CAST(hd.ngayLap AS DATE)";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("ngay"), rs.getDouble("doanhThu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get revenue by theater/room
     */
    public Map<String, Double> getDoanhThuTheoPhong() {
        Map<String, Double> result = new LinkedHashMap<>();
        String sql = "SELECT pc.tenPhong, SUM(v.giaVe) as tongDoanhThu " +
                     "FROM Ve v " +
                     "INNER JOIN LichChieu lc ON v.maLichChieu = lc.maLichChieu " +
                     "INNER JOIN PhongChieu pc ON lc.maPhong = pc.maPhong " +
                     "WHERE v.trangThai IN (N'Da dat', N'Da thanh toan') " +
                     "GROUP BY pc.tenPhong, pc.maPhong " +
                     "ORDER BY tongDoanhThu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("tenPhong"), rs.getDouble("tongDoanhThu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Get detailed statistics summary
     */
    public Map<String, Object> getThongKeTongHop() {
        Map<String, Object> stats = new HashMap<>();

        try (Connection conn = JDBCUtil.getConnection()) {
            // Use single query for efficiency
            String sql = "SELECT " +
                         "  (SELECT ISNULL(SUM(tongTien), 0) FROM HoaDon WHERE trangThaiThanhToan = N'Da thanh toan') as tongDoanhThu, " +
                         "  (SELECT COUNT(*) FROM Ve WHERE trangThai IN (N'Da dat', N'Da thanh toan')) as tongVe, " +
                         "  (SELECT COUNT(*) FROM KhachHang) as tongKhachHang, " +
                         "  (SELECT COUNT(*) FROM Phim) as tongPhim, " +
                         "  (SELECT COUNT(*) FROM PhongChieu) as tongPhong, " +
                         "  (SELECT COUNT(*) FROM LichChieu) as tongLichChieu, " +
                         "  (SELECT COUNT(*) FROM HoaDon WHERE trangThaiThanhToan = N'Da thanh toan') as tongHoaDon, " +
                         "  (SELECT ISNULL(AVG(tongTien), 0) FROM HoaDon WHERE trangThaiThanhToan = N'Da thanh toan') as trungBinhHoaDon";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    stats.put("tongDoanhThu", rs.getDouble("tongDoanhThu"));
                    stats.put("tongVe", rs.getInt("tongVe"));
                    stats.put("tongKhachHang", rs.getInt("tongKhachHang"));
                    stats.put("tongPhim", rs.getInt("tongPhim"));
                    stats.put("tongPhong", rs.getInt("tongPhong"));
                    stats.put("tongLichChieu", rs.getInt("tongLichChieu"));
                    stats.put("tongHoaDon", rs.getInt("tongHoaDon"));
                    stats.put("trungBinhHoaDon", rs.getDouble("trungBinhHoaDon"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
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
