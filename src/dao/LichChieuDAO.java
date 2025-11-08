package dao;

import entity.LichChieu;
import entity.Phim;
import entity.PhongChieu;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichChieuDAO implements ILichChieuDAO {
    private static LichChieuDAO instance;

    public static LichChieuDAO getInstance() {
        if (instance == null) {
            instance = new LichChieuDAO();
        }
        return instance;
    }

    @Override
    public boolean themLichChieu(LichChieu lichChieu) {
        String sql = "INSERT INTO LichChieu (maPhim, maPhong, ngayChieu, gioChieu, giaVe) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, lichChieu.getPhim().getMaPhim());
            stmt.setInt(2, lichChieu.getPhongChieu().getMaPhong());
            stmt.setDate(3, lichChieu.getNgayChieu());
            stmt.setTime(4, lichChieu.getGioChieu());
            stmt.setDouble(5, lichChieu.getGiaVe());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    lichChieu.setMaLichChieu(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatLichChieu(LichChieu lichChieu) {
        String sql = "UPDATE LichChieu SET maPhim = ?, maPhong = ?, ngayChieu = ?, gioChieu = ?, giaVe = ? WHERE maLichChieu = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lichChieu.getPhim().getMaPhim());
            stmt.setInt(2, lichChieu.getPhongChieu().getMaPhong());
            stmt.setDate(3, lichChieu.getNgayChieu());
            stmt.setTime(4, lichChieu.getGioChieu());
            stmt.setDouble(5, lichChieu.getGiaVe());
            stmt.setInt(6, lichChieu.getMaLichChieu());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaLichChieu(int maLichChieu) {
        String sql = "DELETE FROM LichChieu WHERE maLichChieu = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maLichChieu);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public LichChieu timLichChieuTheoMa(int maLichChieu) {
        String sql = "SELECT lc.*, p.tenPhim, p.theLoai, p.thoiLuong, p.moTa, p.hinhAnh, " +
                     "pc.tenPhong, pc.soGhe, pc.loaiPhong " +
                     "FROM LichChieu lc " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON lc.maPhong = pc.maPhong " +
                     "WHERE lc.maLichChieu = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maLichChieu);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapLichChieu(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LichChieu> layDanhSachLichChieu() {
        List<LichChieu> danhSachLichChieu = new ArrayList<>();
        String sql = "SELECT lc.*, p.tenPhim, p.theLoai, p.thoiLuong, p.moTa, p.hinhAnh, " +
                     "pc.tenPhong, pc.soGhe, pc.loaiPhong " +
                     "FROM LichChieu lc " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON lc.maPhong = pc.maPhong " +
                     "ORDER BY lc.ngayChieu DESC, lc.gioChieu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                danhSachLichChieu.add(mapLichChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLichChieu;
    }

    @Override
    public List<LichChieu> layLichChieuTheoPhim(int maPhim) {
        List<LichChieu> danhSachLichChieu = new ArrayList<>();
        String sql = "SELECT lc.*, p.tenPhim, p.theLoai, p.thoiLuong, p.moTa, p.hinhAnh, " +
                     "pc.tenPhong, pc.soGhe, pc.loaiPhong " +
                     "FROM LichChieu lc " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON lc.maPhong = pc.maPhong " +
                     "WHERE lc.maPhim = ? " +
                     "ORDER BY lc.ngayChieu DESC, lc.gioChieu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhim);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachLichChieu.add(mapLichChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLichChieu;
    }

    @Override
    public List<LichChieu> layLichChieuTheoPhong(int maPhong) {
        List<LichChieu> danhSachLichChieu = new ArrayList<>();
        String sql = "SELECT lc.*, p.tenPhim, p.theLoai, p.thoiLuong, p.moTa, p.hinhAnh, " +
                     "pc.tenPhong, pc.soGhe, pc.loaiPhong " +
                     "FROM LichChieu lc " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON lc.maPhong = pc.maPhong " +
                     "WHERE lc.maPhong = ? " +
                     "ORDER BY lc.ngayChieu DESC, lc.gioChieu DESC";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhong);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachLichChieu.add(mapLichChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLichChieu;
    }

    @Override
    public List<LichChieu> layLichChieuTheoNgay(Date ngayChieu) {
        List<LichChieu> danhSachLichChieu = new ArrayList<>();
        String sql = "SELECT lc.*, p.tenPhim, p.theLoai, p.thoiLuong, p.moTa, p.hinhAnh, " +
                     "pc.tenPhong, pc.soGhe, pc.loaiPhong " +
                     "FROM LichChieu lc " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON lc.maPhong = pc.maPhong " +
                     "WHERE lc.ngayChieu = ? " +
                     "ORDER BY lc.gioChieu";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, ngayChieu);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachLichChieu.add(mapLichChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLichChieu;
    }

    @Override
    public List<LichChieu> layLichChieuTheoPhongVaNgay(int maPhong, Date ngayChieu) {
        List<LichChieu> danhSachLichChieu = new ArrayList<>();
        String sql = "SELECT lc.*, p.tenPhim, p.theLoai, p.thoiLuong, p.moTa, p.hinhAnh, " +
                     "pc.tenPhong, pc.soGhe, pc.loaiPhong " +
                     "FROM LichChieu lc " +
                     "INNER JOIN Phim p ON lc.maPhim = p.maPhim " +
                     "INNER JOIN PhongChieu pc ON lc.maPhong = pc.maPhong " +
                     "WHERE lc.maPhong = ? AND lc.ngayChieu = ? " +
                     "ORDER BY lc.gioChieu";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhong);
            stmt.setDate(2, ngayChieu);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachLichChieu.add(mapLichChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachLichChieu;
    }

    @Override
    public boolean kiemTraLichChieuTrung(int maPhong, Date ngayChieu, Time gioChieu) {
        String sql = "SELECT COUNT(*) FROM LichChieu WHERE maPhong = ? AND ngayChieu = ? AND gioChieu = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhong);
            stmt.setDate(2, ngayChieu);
            stmt.setTime(3, gioChieu);
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
    public int demSoGheTrong(int maLichChieu) {
        String sql = "SELECT dbo.fn_DemGheTrong(?) AS soGheTrong";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maLichChieu);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("soGheTrong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private LichChieu mapLichChieu(ResultSet rs) throws SQLException {
        LichChieu lichChieu = new LichChieu();
        lichChieu.setMaLichChieu(rs.getInt("maLichChieu"));
        lichChieu.setNgayChieu(rs.getDate("ngayChieu"));
        lichChieu.setGioChieu(rs.getTime("gioChieu"));
        lichChieu.setGiaVe(rs.getDouble("giaVe"));

        // Map Phim
        Phim phim = new Phim();
        phim.setMaPhim(rs.getInt("maPhim"));
        phim.setTenPhim(rs.getString("tenPhim"));
        phim.setTheLoai(rs.getString("theLoai"));
        phim.setThoiLuong(rs.getInt("thoiLuong"));
        phim.setMoTa(rs.getString("moTa"));
        phim.setHinhAnh(rs.getString("hinhAnh"));

        // Map PhongChieu
        PhongChieu phongChieu = new PhongChieu();
        phongChieu.setMaPhong(rs.getInt("maPhong"));
        phongChieu.setTenPhong(rs.getString("tenPhong"));
        phongChieu.setSoGhe(rs.getInt("soGhe"));
        phongChieu.setLoaiPhong(rs.getString("loaiPhong"));

        lichChieu.setPhim(phim);
        lichChieu.setPhongChieu(phongChieu);
        return lichChieu;
    }
}
