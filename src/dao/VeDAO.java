package dao;

import entity.*;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeDAO implements IVeDAO {
    private static VeDAO instance;

    public static VeDAO getInstance() {
        if (instance == null) {
            instance = new VeDAO();
        }
        return instance;
    }

    @Override
    public boolean themVe(Ve ve) {
        String sql = "INSERT INTO Ve (maLichChieu, maGhe, maHoaDon, giaVe, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ve.getLichChieu().getMaLichChieu());
            stmt.setInt(2, ve.getGhe().getMaGhe());
            stmt.setInt(3, ve.getHoaDon().getMaHoaDon());
            stmt.setDouble(4, ve.getGiaVe());
            stmt.setString(5, ve.getTrangThai());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    ve.setMaVe(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatVe(Ve ve) {
        String sql = "UPDATE Ve SET maLichChieu = ?, maGhe = ?, maHoaDon = ?, giaVe = ?, trangThai = ? WHERE maVe = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ve.getLichChieu().getMaLichChieu());
            stmt.setInt(2, ve.getGhe().getMaGhe());
            stmt.setInt(3, ve.getHoaDon().getMaHoaDon());
            stmt.setDouble(4, ve.getGiaVe());
            stmt.setString(5, ve.getTrangThai());
            stmt.setInt(6, ve.getMaVe());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaVe(int maVe) {
        String sql = "DELETE FROM Ve WHERE maVe = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maVe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Ve timVeTheoMa(int maVe) {
        String sql = "SELECT * FROM v_ChiTietVe WHERE maVe = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maVe);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapVe(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ve> layDanhSachVe() {
        List<Ve> danhSachVe = new ArrayList<>();
        String sql = "SELECT * FROM v_ChiTietVe ORDER BY ngayChieu DESC, gioChieu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                danhSachVe.add(mapVe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachVe;
    }

    @Override
    public List<Ve> layVeTheoHoaDon(int maHoaDon) {
        List<Ve> danhSachVe = new ArrayList<>();
        String sql = "SELECT * FROM v_ChiTietVe WHERE maHoaDon = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maHoaDon);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachVe.add(mapVe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachVe;
    }

    @Override
    public List<Ve> layVeTheoLichChieu(int maLichChieu) {
        List<Ve> danhSachVe = new ArrayList<>();
        String sql = "SELECT * FROM v_ChiTietVe WHERE maLichChieu = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maLichChieu);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachVe.add(mapVe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachVe;
    }

    @Override
    public boolean kiemTraGheDaDat(int maLichChieu, int maGhe) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE maLichChieu = ? AND maGhe = ? AND trangThai != N'Huy'";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maLichChieu);
            stmt.setInt(2, maGhe);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean datVe(int maLichChieu, int maGhe, int maHoaDon, double giaVe) {
        String sql = "{CALL sp_DatVe(?, ?, ?, ?)}";
        try (Connection conn = JDBCUtil.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, maLichChieu);
            stmt.setInt(2, maGhe);
            stmt.setInt(3, maHoaDon);
            stmt.setDouble(4, giaVe);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean huyVe(int maVe) {
        String sql = "UPDATE Ve SET trangThai = N'Huy' WHERE maVe = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maVe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Ve> layVeTheoTrangThai(String trangThai) {
        List<Ve> danhSachVe = new ArrayList<>();
        String sql = "SELECT * FROM v_ChiTietVe WHERE trangThaiVe = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trangThai);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachVe.add(mapVe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachVe;
    }

    private Ve mapVe(ResultSet rs) throws SQLException {
        Ve ve = new Ve();
        ve.setMaVe(rs.getInt("maVe"));
        ve.setGiaVe(rs.getDouble("giaVe"));
        ve.setTrangThai(rs.getString("trangThaiVe"));

        // Map LichChieu
        LichChieu lichChieu = new LichChieu();
        lichChieu.setMaLichChieu(rs.getInt("maLichChieu"));
        lichChieu.setNgayChieu(rs.getDate("ngayChieu").toLocalDate());
        lichChieu.setGioChieu(rs.getTime("gioChieu").toLocalTime());
        lichChieu.setGiaVe(rs.getDouble("giaVeLichChieu"));

        // Map Phim
        Phim phim = new Phim();
        phim.setMaPhim(rs.getInt("maPhim"));
        phim.setTenPhim(rs.getString("tenPhim"));
        phim.setTheLoai(rs.getString("theLoai"));
        phim.setThoiLuong(rs.getInt("thoiLuong"));

        // Map PhongChieu
        PhongChieu phongChieu = new PhongChieu();
        phongChieu.setMaPhong(rs.getInt("maPhong"));
        phongChieu.setTenPhong(rs.getString("tenPhong"));

        lichChieu.setPhim(phim);
        lichChieu.setPhongChieu(phongChieu);

        // Map Ghe
        Ghe ghe = new Ghe();
        ghe.setMaGhe(rs.getInt("maGhe"));
        ghe.setSoGhe(rs.getString("soGhe"));
        ghe.setHang(rs.getString("hang"));
        ghe.setLoaiGhe(rs.getString("loaiGhe"));
        ghe.setPhongChieu(phongChieu);

        // Map HoaDon
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(rs.getInt("maHoaDon"));
        hoaDon.setNgayLap(rs.getTimestamp("ngayLap").toLocalDateTime());
        hoaDon.setTongTien(rs.getDouble("tongTien"));
        hoaDon.setTrangThaiThanhToan(rs.getString("trangThaiThanhToan"));

        // Map KhachHang
        KhachHang khachHang = new KhachHang();
        khachHang.setMaKhachHang(rs.getInt("maKhachHang"));
        khachHang.setTenKhachHang(rs.getString("tenKhachHang"));
        khachHang.setSoDienThoai(rs.getString("soDienThoai"));

        hoaDon.setKhachHang(khachHang);

        ve.setLichChieu(lichChieu);
        ve.setGhe(ghe);
        ve.setHoaDon(hoaDon);

        return ve;
    }
}
