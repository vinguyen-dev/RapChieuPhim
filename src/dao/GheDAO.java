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
}
