package dao;

import entity.HoaDon;
import java.sql.Timestamp;
import java.util.List;

public interface IHoaDonDAO {
    // Thêm hóa đơn mới
    int themHoaDon(HoaDon hoaDon);

    // Cập nhật thông tin hóa đơn
    boolean capNhatHoaDon(HoaDon hoaDon);

    // Xóa hóa đơn
    boolean xoaHoaDon(int maHoaDon);

    // Tìm hóa đơn theo mã
    HoaDon timHoaDonTheoMa(int maHoaDon);

    // Lấy danh sách tất cả hóa đơn
    List<HoaDon> layDanhSachHoaDon();

    // Lấy hóa đơn theo khách hàng
    List<HoaDon> layHoaDonTheoKhachHang(int maKhachHang);

    // Lấy hóa đơn theo ngày
    List<HoaDon> layHoaDonTheoNgay(Timestamp tuNgay, Timestamp denNgay);

    // Lấy hóa đơn theo trạng thái
    List<HoaDon> layHoaDonTheoTrangThai(String trangThai);

    // Thanh toán hóa đơn
    boolean thanhToanHoaDon(int maHoaDon);

    // Hủy hóa đơn
    boolean huyHoaDon(int maHoaDon);

    // Tính tổng tiền hóa đơn
    double tinhTongTien(int maHoaDon);
}
