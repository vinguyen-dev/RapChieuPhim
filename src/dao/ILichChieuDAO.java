package dao;

import entity.LichChieu;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface ILichChieuDAO {
    // Thêm lịch chiếu mới
    boolean themLichChieu(LichChieu lichChieu);

    // Cập nhật thông tin lịch chiếu
    boolean capNhatLichChieu(LichChieu lichChieu);

    // Xóa lịch chiếu
    boolean xoaLichChieu(int maLichChieu);

    // Tìm lịch chiếu theo mã
    LichChieu timLichChieuTheoMa(int maLichChieu);

    // Lấy danh sách tất cả lịch chiếu
    List<LichChieu> layDanhSachLichChieu();

    // Lấy lịch chiếu theo phim
    List<LichChieu> layLichChieuTheoPhim(int maPhim);

    // Lấy lịch chiếu theo phòng
    List<LichChieu> layLichChieuTheoPhong(int maPhong);

    // Lấy lịch chiếu theo ngày
    List<LichChieu> layLichChieuTheoNgay(Date ngayChieu);

    // Lấy lịch chiếu theo phòng và ngày
    List<LichChieu> layLichChieuTheoPhongVaNgay(int maPhong, Date ngayChieu);

    // Kiểm tra lịch chiếu trùng
    boolean kiemTraLichChieuTrung(int maPhong, Date ngayChieu, Time gioChieu);

    // Đếm số ghế trống
    int demSoGheTrong(int maLichChieu);
}
