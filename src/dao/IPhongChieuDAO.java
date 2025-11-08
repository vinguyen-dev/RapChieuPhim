package dao;

import entity.PhongChieu;
import java.util.List;

public interface IPhongChieuDAO {
    // Thêm phòng chiếu mới
    boolean themPhongChieu(PhongChieu phongChieu);

    // Cập nhật thông tin phòng chiếu
    boolean capNhatPhongChieu(PhongChieu phongChieu);

    // Xóa phòng chiếu
    boolean xoaPhongChieu(int maPhong);

    // Tìm phòng chiếu theo mã
    PhongChieu timPhongChieuTheoMa(int maPhong);

    // Lấy danh sách tất cả phòng chiếu
    List<PhongChieu> layDanhSachPhongChieu();

    // Tìm phòng chiếu theo tên
    PhongChieu timPhongChieuTheoTen(String tenPhong);

    // Tìm phòng chiếu theo loại
    List<PhongChieu> timPhongChieuTheoLoai(String loaiPhong);
}
