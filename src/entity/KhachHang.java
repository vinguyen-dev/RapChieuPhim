package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KhachHang implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer maKhachHang;
    private String tenKhachHang;
    private String soDienThoai;
    private String email;

    private List<HoaDon> danhSachHoaDon = new ArrayList<>();

    // Constructors
    public KhachHang() {}

    public KhachHang(String tenKhachHang, String soDienThoai, String email) {
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    // Getters & Setters
    public Integer getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(Integer maKhachHang) { this.maKhachHang = maKhachHang; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<HoaDon> getDanhSachHoaDon() { return danhSachHoaDon; }
    public void setDanhSachHoaDon(List<HoaDon> danhSachHoaDon) { this.danhSachHoaDon = danhSachHoaDon; }

    @Override
    public String toString() {
        return tenKhachHang + " - " + soDienThoai;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KhachHang)) return false;
        KhachHang that = (KhachHang) o;
        return Objects.equals(maKhachHang, that.maKhachHang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKhachHang);
    }
}