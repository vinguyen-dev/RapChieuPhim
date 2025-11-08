package dao;

import entity.HoaDon;
import entity.KhachHang;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO implements IHoaDonDAO {
    private static HoaDonDAO instance;

    public static HoaDonDAO getInstance() {
        if (instance == null) {
            instance = new HoaDonDAO();
        }
        return instance;
    }

    @Override
    public int themHoaDon(HoaDon hoaDon) {
        String sql = "INSERT INTO HoaDon (maKhachHang, ngayLap, tongTien, trangThaiThanhToan) VALUES (?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, hoaDon.getKhachHang().getMaKhachHang());
            stmt.setTimestamp(2, hoaDon.getNgayLap());
            stmt.setDouble(3, hoaDon.getTongTien());
            stmt.setString(4, hoaDon.getTrangThaiThanhToan());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int maHoaDon = rs.getInt(1);
                    hoaDon.setMaHoaDon(maHoaDon);
                    return maHoaDon;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean capNhatHoaDon(HoaDon hoaDon) {
        String sql = "UPDATE HoaDon SET maKhachHang = ?, ngayLap = ?, tongTien = ?, trangThaiThanhToan = ? WHERE maHoaDon = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, hoaDon.getKhachHang().getMaKhachHang());
            stmt.setTimestamp(2, hoaDon.getNgayLap());
            stmt.setDouble(3, hoaDon.getTongTien());
            stmt.setString(4, hoaDon.getTrangThaiThanhToan());
            stmt.setInt(5, hoaDon.getMaHoaDon());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaHoaDon(int maHoaDon) {
        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maHoaDon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public HoaDon timHoaDonTheoMa(int maHoaDon) {
        String sql = "SELECT hd.*, kh.tenKhachHang, kh.soDienThoai, kh.email " +
                     "FROM HoaDon hd " +
                     "INNER JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "WHERE hd.maHoaDon = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapHoaDon(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HoaDon> layDanhSachHoaDon() {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT hd.*, kh.tenKhachHang, kh.soDienThoai, kh.email " +
                     "FROM HoaDon hd " +
                     "INNER JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "ORDER BY hd.ngayLap DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                danhSachHoaDon.add(mapHoaDon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachHoaDon;
    }

    @Override
    public List<HoaDon> layHoaDonTheoKhachHang(int maKhachHang) {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT hd.*, kh.tenKhachHang, kh.soDienThoai, kh.email " +
                     "FROM HoaDon hd " +
                     "INNER JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "WHERE hd.maKhachHang = ? " +
                     "ORDER BY hd.ngayLap DESC";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maKhachHang);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachHoaDon.add(mapHoaDon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachHoaDon;
    }

    @Override
    public List<HoaDon> layHoaDonTheoNgay(Timestamp tuNgay, Timestamp denNgay) {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT hd.*, kh.tenKhachHang, kh.soDienThoai, kh.email " +
                     "FROM HoaDon hd " +
                     "INNER JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "WHERE hd.ngayLap BETWEEN ? AND ? " +
                     "ORDER BY hd.ngayLap DESC";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, tuNgay);
            stmt.setTimestamp(2, denNgay);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachHoaDon.add(mapHoaDon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachHoaDon;
    }

    @Override
    public List<HoaDon> layHoaDonTheoTrangThai(String trangThai) {
        List<HoaDon> danhSachHoaDon = new ArrayList<>();
        String sql = "SELECT hd.*, kh.tenKhachHang, kh.soDienThoai, kh.email " +
                     "FROM HoaDon hd " +
                     "INNER JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang " +
                     "WHERE hd.trangThaiThanhToan = ? " +
                     "ORDER BY hd.ngayLap DESC";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trangThai);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachHoaDon.add(mapHoaDon(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachHoaDon;
    }

    @Override
    public boolean thanhToanHoaDon(int maHoaDon) {
        String sql = "{CALL sp_ThanhToanHoaDon(?)}";
        try (Connection conn = JDBCUtil.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, maHoaDon);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean huyHoaDon(int maHoaDon) {
        String sql = "UPDATE HoaDon SET trangThaiThanhToan = N'Huy' WHERE maHoaDon = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maHoaDon);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public double tinhTongTien(int maHoaDon) {
        String sql = "SELECT tongTien FROM HoaDon WHERE maHoaDon = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("tongTien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private HoaDon mapHoaDon(ResultSet rs) throws SQLException {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(rs.getInt("maHoaDon"));
        hoaDon.setNgayLap(rs.getTimestamp("ngayLap"));
        hoaDon.setTongTien(rs.getDouble("tongTien"));
        hoaDon.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));

        // Map KhachHang
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKhachHang(rs.getInt("maKhachHang"));
        khachHang.setTenKhachHang(rs.getString("tenKhachHang"));
        khachHang.setSoDienThoai(rs.getString("soDienThoai"));
        khachHang.setEmail(rs.getString("email"));

        hoaDon.setKhachHang(khachHang);
        return hoaDon;
    }
}
