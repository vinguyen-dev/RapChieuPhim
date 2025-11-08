package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhongChieu implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer maPhong;
    private String tenPhong;
    private Integer soGhe;
    private String loaiPhong = "2D";

    private List<Ghe> danhSachGhe = new ArrayList<>();
    private List<LichChieu> danhSachLichChieu = new ArrayList<>();

    // Constructors
    public PhongChieu() {}

    public PhongChieu(String tenPhong, Integer soGhe, String loaiPhong) {
        this.tenPhong = tenPhong;
        this.soGhe = soGhe;
        this.loaiPhong = loaiPhong != null ? loaiPhong : "2D";
    }

    // Getters & Setters
    public Integer getMaPhong() { return maPhong; }
    public void setMaPhong(Integer maPhong) { this.maPhong = maPhong; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public Integer getSoGhe() { return soGhe; }
    public void setSoGhe(Integer soGhe) { this.soGhe = soGhe; }

    public String getLoaiPhong() { return loaiPhong; }
    public void setLoaiPhong(String loaiPhong) { this.loaiPhong = loaiPhong; }

    public List<Ghe> getDanhSachGhe() { return danhSachGhe; }
    public void setDanhSachGhe(List<Ghe> danhSachGhe) { this.danhSachGhe = danhSachGhe; }

    public List<LichChieu> getDanhSachLichChieu() { return danhSachLichChieu; }
    public void setDanhSachLichChieu(List<LichChieu> danhSachLichChieu) { this.danhSachLichChieu = danhSachLichChieu; }

    @Override
    public String toString() {
        return tenPhong + " - " + soGhe + " ghế (" + loaiPhong + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhongChieu)) return false;
        PhongChieu that = (PhongChieu) o;
        return Objects.equals(maPhong, that.maPhong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPhong);
    }
}