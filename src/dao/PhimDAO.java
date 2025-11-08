package dao;

import entity.Phim;
import util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhimDAO implements IPhimDAO {
    private static PhimDAO instance;

    public static PhimDAO getInstance() {
        if (instance == null) {
            instance = new PhimDAO();
        }
        return instance;
    }

    @Override
    public boolean themPhim(Phim phim) {
        String sql = "INSERT INTO Phim (tenPhim, theLoai, thoiLuong, moTa, hinhAnh) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, phim.getTenPhim());
            stmt.setString(2, phim.getTheLoai());
            stmt.setInt(3, phim.getThoiLuong());
            stmt.setString(4, phim.getMoTa());
            stmt.setString(5, phim.getHinhAnh());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    phim.setMaPhim(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhatPhim(Phim phim) {
        String sql = "UPDATE Phim SET tenPhim = ?, theLoai = ?, thoiLuong = ?, moTa = ?, hinhAnh = ? WHERE maPhim = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phim.getTenPhim());
            stmt.setString(2, phim.getTheLoai());
            stmt.setInt(3, phim.getThoiLuong());
            stmt.setString(4, phim.getMoTa());
            stmt.setString(5, phim.getHinhAnh());
            stmt.setInt(6, phim.getMaPhim());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean xoaPhim(int maPhim) {
        String sql = "DELETE FROM Phim WHERE maPhim = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhim);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Phim timPhimTheoMa(int maPhim) {
        String sql = "SELECT * FROM Phim WHERE maPhim = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, maPhim);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapPhim(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Phim> layDanhSachPhim() {
        List<Phim> danhSachPhim = new ArrayList<>();
        String sql = "SELECT * FROM Phim";

        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                danhSachPhim.add(mapPhim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhim;
    }

    @Override
    public List<Phim> timPhimTheoTen(String tenPhim) {
        List<Phim> danhSachPhim = new ArrayList<>();
        String sql = "SELECT * FROM Phim WHERE tenPhim LIKE ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + tenPhim + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachPhim.add(mapPhim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhim;
    }

    @Override
    public List<Phim> timPhimTheoTheLoai(String theLoai) {
        List<Phim> danhSachPhim = new ArrayList<>();
        String sql = "SELECT * FROM Phim WHERE theLoai LIKE ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + theLoai + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                danhSachPhim.add(mapPhim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return danhSachPhim;
    }

    private Phim mapPhim(ResultSet rs) throws SQLException {
        Phim phim = new Phim();
        phim.setMaPhim(rs.getInt("maPhim"));
        phim.setTenPhim(rs.getString("tenPhim"));
        phim.setTheLoai(rs.getString("theLoai"));
        phim.setThoiLuong(rs.getInt("thoiLuong"));
        phim.setMoTa(rs.getString("moTa"));
        phim.setHinhAnh(rs.getString("hinhAnh"));
        return phim;
    }
}
