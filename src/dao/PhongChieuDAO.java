package dao;

import entity.PhongChieu;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhongChieuDAO implements IPhongChieuDAO {
    private static PhongChieuDAO instance;

    public static PhongChieuDAO getInstance() {
        if (instance == null) {
            instance = new PhongChieuDAO();
        }
        return instance;
    }

    @Override
    public boolean themPhongChieu(PhongChieu phongChieu) {
        String sql = "INSERT INTO PhongChieu (tenPhong, soGhe, loaiPhong) VALUES (?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, phongChieu.getTenPhong());
            stmt.setInt(2, phongChieu.getSoGhe());
            stmt.setString(3, phongChieu.getLoaiPhong());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    phongChieu.setMaPhong(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatPhongChieu(PhongChieu phongChieu) {
        String sql = "UPDATE PhongChieu SET tenPhong = ?, soGhe = ?, loaiPhong = ? WHERE maPhong = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phongChieu.getTenPhong());
            stmt.setInt(2, phongChieu.getSoGhe());
            stmt.setString(3, phongChieu.getLoaiPhong());
            stmt.setInt(4, phongChieu.getMaPhong());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaPhongChieu(int maPhong) {
        String sql = "DELETE FROM PhongChieu WHERE maPhong = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhong);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public PhongChieu timPhongChieuTheoMa(int maPhong) {
        String sql = "SELECT * FROM PhongChieu WHERE maPhong = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhong);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapPhongChieu(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PhongChieu> layDanhSachPhongChieu() {
        List<PhongChieu> danhSachPhongChieu = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                danhSachPhongChieu.add(mapPhongChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhongChieu;
    }

    @Override
    public PhongChieu timPhongChieuTheoTen(String tenPhong) {
        String sql = "SELECT * FROM PhongChieu WHERE tenPhong = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tenPhong);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapPhongChieu(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PhongChieu> timPhongChieuTheoLoai(String loaiPhong) {
        List<PhongChieu> danhSachPhongChieu = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu WHERE loaiPhong = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, loaiPhong);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachPhongChieu.add(mapPhongChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhongChieu;
    }

    private PhongChieu mapPhongChieu(ResultSet rs) throws SQLException {
        PhongChieu phongChieu = new PhongChieu();
        phongChieu.setMaPhong(rs.getInt("maPhong"));
        phongChieu.setTenPhong(rs.getString("tenPhong"));
        phongChieu.setSoGhe(rs.getInt("soGhe"));
        phongChieu.setLoaiPhong(rs.getString("loaiPhong"));
        return phongChieu;
    }
}
