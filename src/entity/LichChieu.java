package entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LichChieu implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer maLichChieu;
    private Date ngayChieu;
    private Time gioChieu;
    private Double giaVe;

    private Phim phim;
    private PhongChieu phongChieu;
    private List<Ve> danhSachVe = new ArrayList<>();

    // Constructors
    public LichChieu() {}

    public LichChieu(Date ngayChieu, Time gioChieu, Double giaVe, Phim phim, PhongChieu phongChieu) {
        this.ngayChieu = ngayChieu;
        this.gioChieu = gioChieu;
        this.giaVe = giaVe;
        this.phim = phim;
        this.phongChieu = phongChieu;
    }

    // Getters & Setters
    public Integer getMaLichChieu() { return maLichChieu; }
    public void setMaLichChieu(Integer maLichChieu) { this.maLichChieu = maLichChieu; }

    public Date getNgayChieu() { return ngayChieu; }
    public void setNgayChieu(Date ngayChieu) { this.ngayChieu = ngayChieu; }

    public Time getGioChieu() { return gioChieu; }
    public void setGioChieu(Time gioChieu) { this.gioChieu = gioChieu; }

    public Double getGiaVe() { return giaVe; }
    public void setGiaVe(Double giaVe) { this.giaVe = giaVe; }

    public Phim getPhim() { return phim; }
    public void setPhim(Phim phim) { this.phim = phim; }

    public PhongChieu getPhongChieu() { return phongChieu; }
    public void setPhongChieu(PhongChieu phongChieu) { this.phongChieu = phongChieu; }

    public List<Ve> getDanhSachVe() { return danhSachVe; }
    public void setDanhSachVe(List<Ve> danhSachVe) { this.danhSachVe = danhSachVe; }

    @Override
    public String toString() {
        return phim.getTenPhim() + " - " + gioChieu + " - Phòng " + phongChieu.getTenPhong();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LichChieu)) return false;
        LichChieu that = (LichChieu) o;
        return Objects.equals(maLichChieu, that.maLichChieu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maLichChieu);
    }
}