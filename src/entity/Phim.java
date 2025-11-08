package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Phim implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer maPhim;
    private String tenPhim;
    private String theLoai;
    private Integer thoiLuong;
    private String moTa;
    private String hinhAnh;

    private List<LichChieu> danhSachLichChieu = new ArrayList<>();

    // Constructors
    public Phim() {}

    public Phim(String tenPhim, String theLoai, Integer thoiLuong, String moTa, String hinhAnh) {
        this.tenPhim = tenPhim;
        this.theLoai = theLoai;
        this.thoiLuong = thoiLuong;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    // Getters & Setters
    public Integer getMaPhim() { return maPhim; }
    public void setMaPhim(Integer maPhim) { this.maPhim = maPhim; }

    public String getTenPhim() { return tenPhim; }
    public void setTenPhim(String tenPhim) { this.tenPhim = tenPhim; }

    public String getTheLoai() { return theLoai; }
    public void setTheLoai(String theLoai) { this.theLoai = theLoai; }

    public Integer getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(Integer thoiLuong) { this.thoiLuong = thoiLuong; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public List<LichChieu> getDanhSachLichChieu() { return danhSachLichChieu; }
    public void setDanhSachLichChieu(List<LichChieu> danhSachLichChieu) { this.danhSachLichChieu = danhSachLichChieu; }

    @Override
    public String toString() {
        return tenPhim + " (" + thoiLuong + " phút)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Phim)) return false;
        Phim phim = (Phim) o;
        return Objects.equals(maPhim, phim.maPhim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhim);
    }
}