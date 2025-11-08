package dao;

import entity.KhachHang;
import java.util.List;

public interface IKhachHangDAO {
    // Thêm khách hàng mới
    boolean themKhachHang(KhachHang khachHang);

    // Cập nhật thông tin khách hàng
    boolean capNhatKhachHang(KhachHang khachHang);

    // Xóa khách hàng
    boolean xoaKhachHang(int maKhachHang);

    // Tìm khách hàng theo mã
    KhachHang timKhachHangTheoMa(int maKhachHang);

    // Lấy danh sách tất cả khách hàng
    List<KhachHang> layDanhSachKhachHang();

    // Tìm khách hàng theo tên
    List<KhachHang> timKhachHangTheoTen(String tenKhachHang);

    // Tìm khách hàng theo số điện thoại
    KhachHang timKhachHangTheoSDT(String soDienThoai);

    // Tìm khách hàng theo email
    KhachHang timKhachHangTheoEmail(String email);
}
