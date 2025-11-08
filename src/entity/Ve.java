package entity;

import java.io.Serializable;
import java.util.Objects;

public class Ve implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer maVe;
    private Double giaVe;
    private String trangThai = "Da dat";

    private LichChieu lichChieu;
    private Ghe ghe;
    private HoaDon hoaDon;

    // Constructors
    public Ve() {}

    public Ve(Double giaVe, LichChieu lichChieu, Ghe ghe, HoaDon hoaDon) {
        this.giaVe = giaVe;
        this.lichChieu = lichChieu;
        this.ghe = ghe;
        this.hoaDon = hoaDon;
    }

    // Getters & Setters
    public Integer getMaVe() { return maVe; }
    public void setMaVe(Integer maVe) { this.maVe = maVe; }

    public Double getGiaVe() { return giaVe; }
    public void setGiaVe(Double giaVe) { this.giaVe = giaVe; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public LichChieu getLichChieu() { return lichChieu; }
    public void setLichChieu(LichChieu lichChieu) { this.lichChieu = lichChieu; }

    public Ghe getGhe() { return ghe; }
    public void setGhe(Ghe ghe) { this.ghe = ghe; }

    public HoaDon getHoaDon() { return hoaDon; }
    public void setHoaDon(HoaDon hoaDon) { this.hoaDon = hoaDon; }

    @Override
    public String toString() {
        return "Vé #" + maVe + " - Ghế " + ghe.getSoGhe() +
                " - " + lichChieu.getPhim().getTenPhim() +
                " (" + lichChieu.getGioChieu() + ") - " + giaVe + "đ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ve ve)) return false;
        return Objects.equals(maVe, ve.maVe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maVe);
    }
}