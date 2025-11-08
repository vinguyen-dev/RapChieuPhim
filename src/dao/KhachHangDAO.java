package dao;

import entity.KhachHang;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO implements IKhachHangDAO {
    private static KhachHangDAO instance;

    public static KhachHangDAO getInstance() {
        if (instance == null) {
            instance = new KhachHangDAO();
        }
        return instance;
    }

    @Override
    public boolean themKhachHang(KhachHang khachHang) {
        String sql = "INSERT INTO KhachHang (tenKhachHang, soDienThoai, email) VALUES (?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, khachHang.getTenKhachHang());
            stmt.setString(2, khachHang.getSoDienThoai());
            stmt.setString(3, khachHang.getEmail());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    khachHang.setMaKhachHang(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatKhachHang(KhachHang khachHang) {
        String sql = "UPDATE KhachHang SET tenKhachHang = ?, soDienThoai = ?, email = ? WHERE maKhachHang = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, khachHang.getTenKhachHang());
            stmt.setString(2, khachHang.getSoDienThoai());
            stmt.setString(3, khachHang.getEmail());
            stmt.setInt(4, khachHang.getMaKhachHang());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaKhachHang(int maKhachHang) {
        String sql = "DELETE FROM KhachHang WHERE maKhachHang = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maKhachHang);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public KhachHang timKhachHangTheoMa(int maKhachHang) {
        String sql = "SELECT * FROM KhachHang WHERE maKhachHang = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maKhachHang);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapKhachHang(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<KhachHang> layDanhSachKhachHang() {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                danhSachKhachHang.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhachHang;
    }

    @Override
    public List<KhachHang> timKhachHangTheoTen(String tenKhachHang) {
        List<KhachHang> danhSachKhachHang = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE tenKhachHang LIKE ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + tenKhachHang + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachKhachHang.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachKhachHang;
    }

    @Override
    public KhachHang timKhachHangTheoSDT(String soDienThoai) {
        String sql = "SELECT * FROM KhachHang WHERE soDienThoai = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, soDienThoai);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapKhachHang(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KhachHang timKhachHangTheoEmail(String email) {
        String sql = "SELECT * FROM KhachHang WHERE email = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapKhachHang(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private KhachHang mapKhachHang(ResultSet rs) throws SQLException {
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKhachHang(rs.getInt("maKhachHang"));
        khachHang.setTenKhachHang(rs.getString("tenKhachHang"));
        khachHang.setSoDienThoai(rs.getString("soDienThoai"));
        khachHang.setEmail(rs.getString("email"));
        return khachHang;
    }
}
