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
     * Tạo ghế tự động cho phòng chiếu theo layout chuẩn
     * Layout: 6 hàng (A-F) x 10 cột (1-10)
     * - Hàng A, B, F: Ghế thường
     * - Hàng C, D: Ghế VIP
     * - Hàng E: Ghế Couple
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

            // Create seats for room (6 rows x 10 columns)
            String insertSql = "INSERT INTO Ghe (maPhong, soGhe, hang, loaiGhe) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                int seatCount = 0;

                for (char hang = 'A'; hang <= 'F'; hang++) {
                    for (int cot = 1; cot <= 10; cot++) {
                        String soGhe = hang + String.valueOf(cot);
                        String loaiGhe;

                        // Determine seat type based on row
                        if (hang == 'C' || hang == 'D') {
                            loaiGhe = "VIP";
                        } else if (hang == 'E') {
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
                System.out.println("Đã tạo " + seatCount + " ghế cho phòng " + maPhong);
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
