package entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoaDon implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer maHoaDon;
    private Timestamp ngayLap = new Timestamp(System.currentTimeMillis());
    private Double tongTien = 0.0;
    private String trangThaiThanhToan = "Chua thanh toan";

    private KhachHang khachHang;
    private List<Ve> danhSachVe = new ArrayList<>();

    // Constructors
    public HoaDon() {}

    public HoaDon(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    // Getters & Setters
    public Integer getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(Integer maHoaDon) { this.maHoaDon = maHoaDon; }

    public Timestamp getNgayLap() { return ngayLap; }
    public void setNgayLap(Timestamp ngayLap) { this.ngayLap = ngayLap; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public String getTrangThaiThanhToan() { return trangThaiThanhToan; }
    public void setTrangThaiThanhToan(String trangThaiThanhToan) { this.trangThaiThanhToan = trangThaiThanhToan; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public List<Ve> getDanhSachVe() { return danhSachVe; }
    public void setDanhSachVe(List<Ve> danhSachVe) { this.danhSachVe = danhSachVe; }

    @Override
    public String toString() {
        return "HD#" + maHoaDon + " - " + khachHang.getTenKhachHang() + " - " + tongTien + "đ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HoaDon)) return false;
        HoaDon hoaDon = (HoaDon) o;
        return Objects.equals(maHoaDon, hoaDon.maHoaDon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHoaDon);
    }
}