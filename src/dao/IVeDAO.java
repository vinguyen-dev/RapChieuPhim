package dao;

import entity.Ve;
import java.util.List;

public interface IVeDAO {
    // Thêm vé mới
    boolean themVe(Ve ve);

    // Cập nhật thông tin vé
    boolean capNhatVe(Ve ve);

    // Xóa vé
    boolean xoaVe(int maVe);

    // Tìm vé theo mã
    Ve timVeTheoMa(int maVe);

    // Lấy danh sách tất cả vé
    List<Ve> layDanhSachVe();

    // Lấy vé theo hóa đơn
    List<Ve> layVeTheoHoaDon(int maHoaDon);

    // Lấy vé theo lịch chiếu
    List<Ve> layVeTheoLichChieu(int maLichChieu);

    // Kiểm tra ghế đã đặt chưa
    boolean kiemTraGheDaDat(int maLichChieu, int maGhe);

    // Đặt vé
    boolean datVe(int maLichChieu, int maGhe, int maHoaDon, double giaVe);

    // Hủy vé
    boolean huyVe(int maVe);

    // Lấy vé theo trạng thái
    List<Ve> layVeTheoTrangThai(String trangThai);
}
