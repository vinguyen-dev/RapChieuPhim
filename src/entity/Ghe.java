package entity;

import java.io.Serializable;
import java.util.Objects;

public class Ghe implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer maGhe;
    private String soGhe;
    private String hang;
    private String loaiGhe = "Thuong";

    private PhongChieu phongChieu;

    // Constructors
    public Ghe() {}

    public Ghe(String soGhe, String hang, String loaiGhe, PhongChieu phongChieu) {
        this.soGhe = soGhe;
        this.hang = hang;
        this.loaiGhe = loaiGhe != null ? loaiGhe : "Thuong";
        this.phongChieu = phongChieu;
    }

    // Getters & Setters
    public Integer getMaGhe() { return maGhe; }
    public void setMaGhe(Integer maGhe) { this.maGhe = maGhe; }

    public String getSoGhe() { return soGhe; }
    public void setSoGhe(String soGhe) { this.soGhe = soGhe; }

    public String getHang() { return hang; }
    public void setHang(String hang) { this.hang = hang; }

    public String getLoaiGhe() { return loaiGhe; }
    public void setLoaiGhe(String loaiGhe) { this.loaiGhe = loaiGhe; }

    public PhongChieu getPhongChieu() { return phongChieu; }
    public void setPhongChieu(PhongChieu phongChieu) { this.phongChieu = phongChieu; }

    @Override
    public String toString() {
        return soGhe + " (" + loaiGhe + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ghe)) return false;
        Ghe ghe = (Ghe) o;
        return Objects.equals(maGhe, ghe.maGhe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maGhe);
    }
}