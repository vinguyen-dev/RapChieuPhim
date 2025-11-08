package dao;

import entity.Ghe;
import entity.PhongChieu;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GheDAO implements IGheDAO {
    private static GheDAO instance;

    public static GheDAO getInstance() {
        if (instance == null) {
            instance = new GheDAO();
        }
        return instance;
    }

    @Override
    public boolean themGhe(Ghe ghe) {
        String sql = "INSERT INTO Ghe (maPhong, soGhe, hang, loaiGhe) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ghe.getPhongChieu().getMaPhong());
            stmt.setString(2, ghe.getSoGhe());
            stmt.setString(3, ghe.getHang());
            stmt.setString(4, ghe.getLoaiGhe());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    ghe.setMaGhe(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatGhe(Ghe ghe) {
        String sql = "UPDATE Ghe SET maPhong = ?, soGhe = ?, hang = ?, loaiGhe = ? WHERE maGhe = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ghe.getPhongChieu().getMaPhong());
            stmt.setString(2, ghe.getSoGhe());
            stmt.setString(3, ghe.getHang());
            stmt.setString(4, ghe.getLoaiGhe());
            stmt.setInt(5, ghe.getMaGhe());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaGhe(int maGhe) {
        String sql = "DELETE FROM Ghe WHERE maGhe = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maGhe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Ghe timGheTheoMa(int maGhe) {
        String sql = "SELECT g.*, pc.tenPhong, pc.soGhe as tongSoGhe, pc.loaiPhong " +
                     "FROM Ghe g " +
                     "INNER JOIN PhongChieu pc ON g.maPhong = pc.maPhong " +
                     "WHERE g.maGhe = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maGhe);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapGhe(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ghe> layDanhSachGhe() {
        List<Ghe> danhSachGhe = new ArrayList<>();
        String sql = "SELECT g.*, pc.tenPhong, pc.soGhe as tongSoGhe, pc.loaiPhong " +
                     "FROM Ghe g " +
                     "INNER JOIN PhongChieu pc ON g.maPhong = pc.maPhong";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                danhSachGhe.add(mapGhe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachGhe;
    }

    @Override
    public List<Ghe> layDanhSachGheTheoPhong(int maPhong) {
        List<Ghe> danhSachGhe = new ArrayList<>();
        String sql = "SELECT g.*, pc.tenPhong, pc.soGhe as tongSoGhe, pc.loaiPhong " +
                     "FROM Ghe g " +
                     "INNER JOIN PhongChieu pc ON g.maPhong = pc.maPhong " +
                     "WHERE g.maPhong = ? " +
                     "ORDER BY g.hang, g.soGhe";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhong);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachGhe.add(mapGhe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachGhe;
    }

    @Override
    public List<Ghe> layDanhSachGheTheoLoai(String loaiGhe) {
        List<Ghe> danhSachGhe = new ArrayList<>();
        String sql = "SELECT g.*, pc.tenPhong, pc.soGhe as tongSoGhe, pc.loaiPhong " +
                     "FROM Ghe g " +
                     "INNER JOIN PhongChieu pc ON g.maPhong = pc.maPhong " +
                     "WHERE g.loaiGhe = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loaiGhe);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachGhe.add(mapGhe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachGhe;
    }

    @Override
    public Ghe timGheTheoSoGheVaPhong(String soGhe, int maPhong) {
        String sql = "SELECT g.*, pc.tenPhong, pc.soGhe as tongSoGhe, pc.loaiPhong " +
                     "FROM Ghe g " +
                     "INNER JOIN PhongChieu pc ON g.maPhong = pc.maPhong " +
                     "WHERE g.soGhe = ? AND g.maPhong = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, soGhe);
            stmt.setInt(2, maPhong);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapGhe(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Ghe mapGhe(ResultSet rs) throws SQLException {
        Ghe ghe = new Ghe();
        ghe.setMaGhe(rs.getInt("maGhe"));
        ghe.setSoGhe(rs.getString("soGhe"));
        ghe.setHang(rs.getString("hang"));
        ghe.setLoaiGhe(rs.getString("loaiGhe"));

        // Map PhongChieu
        PhongChieu phongChieu = new PhongChieu();
        phongChieu.setMaPhong(rs.getInt("maPhong"));
        phongChieu.setTenPhong(rs.getString("tenPhong"));
        phongChieu.setSoGhe(rs.getInt("tongSoGhe"));
        phongChieu.setLoaiPhong(rs.getString("loaiPhong"));

        ghe.setPhongChieu(phongChieu);
        return ghe;
    }

    /**
     * Tạo ghế tự động cho phòng chiếu dựa trên số lượng ghế của phòng
     * Layout động:
     * - Tính toán số hàng và cột dựa trên tổng số ghế
     * - Hàng giữa: Ghế VIP
     * - Hàng áp cuối: Ghế Couple
     * - Các hàng còn lại: Ghế thường
     *
     * @param maPhong Mã phòng chiếu cần tạo ghế
     * @return true nếu tạo thành công, false nếu thất bại
     */
    public boolean taoGheChoPhong(int maPhong) {
        try (Connection conn = JDBCUtil.getConnection()) {
            // Check if room already has seats
            String checkSql = "SELECT COUNT(*) FROM Ghe WHERE maPhong = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, maPhong);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Phòng " + maPhong + " đã có ghế, không tạo lại.");
                    return true; // Already has seats
                }
            }

            // Get room info to determine seat count
            String roomSql = "SELECT soGhe, tenPhong FROM PhongChieu WHERE maPhong = ?";
            int totalSeats = 60; // Default
            String roomName = "Phòng " + maPhong;

            try (PreparedStatement roomStmt = conn.prepareStatement(roomSql)) {
                roomStmt.setInt(1, maPhong);
                ResultSet roomRs = roomStmt.executeQuery();
                if (roomRs.next()) {
                    totalSeats = roomRs.getInt("soGhe");
                    roomName = roomRs.getString("tenPhong");
                }
            }

            // Calculate layout based on total seats
            // Prefer 10 columns layout (like real cinema)
            int cols = 10;
            int rows = (int) Math.ceil((double) totalSeats / cols);

            // Limit to A-Z (26 rows max)
            if (rows > 26) {
                rows = 26;
                cols = (int) Math.ceil((double) totalSeats / rows);
            }

            System.out.println("Tạo " + totalSeats + " ghế cho " + roomName + " (" + rows + " hàng x " + cols + " cột)");

            // Create seats
            String insertSql = "INSERT INTO Ghe (maPhong, soGhe, hang, loaiGhe) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                int seatCount = 0;
                int vipStartRow = rows / 3; // VIP starts at 1/3
                int vipEndRow = (rows * 2) / 3; // VIP ends at 2/3
                int coupleRow = rows - 2; // Couple seat at second-to-last row

                for (int r = 0; r < rows && seatCount < totalSeats; r++) {
                    char hang = (char) ('A' + r);

                    for (int c = 1; c <= cols && seatCount < totalSeats; c++) {
                        String soGhe = hang + String.valueOf(c);
                        String loaiGhe;

                        // Determine seat type based on row position
                        if (r >= vipStartRow && r < vipEndRow) {
                            loaiGhe = "VIP";
                        } else if (r == coupleRow) {
                            loaiGhe = "Couple";
                        } else {
                            loaiGhe = "Thuong";
                        }

                        stmt.setInt(1, maPhong);
                        stmt.setString(2, soGhe);
                        stmt.setString(3, String.valueOf(hang));
                        stmt.setString(4, loaiGhe);
                        stmt.addBatch();
                        seatCount++;
                    }
                }

                int[] results = stmt.executeBatch();
                System.out.println("✓ Đã tạo " + seatCount + " ghế cho " + roomName);
                return results.length == seatCount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra và tạo ghế tự động nếu phòng chưa có ghế
     * @param maPhong Mã phòng cần kiểm tra
     */
    public void kiemTraVaTaoGhe(int maPhong) {
        List<Ghe> danhSachGhe = layDanhSachGheTheoPhong(maPhong);
        if (danhSachGhe.isEmpty()) {
            System.out.println("Phòng " + maPhong + " chưa có ghế. Đang tạo ghế tự động...");
            taoGheChoPhong(maPhong);
        }
    }
}
