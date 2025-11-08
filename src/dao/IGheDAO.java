package dao;

import entity.Ghe;
import java.util.List;

public interface IGheDAO {
    // Thêm ghế mới
    boolean themGhe(Ghe ghe);

    // Cập nhật thông tin ghế
    boolean capNhatGhe(Ghe ghe);

    // Xóa ghế
    boolean xoaGhe(int maGhe);

    // Tìm ghế theo mã
    Ghe timGheTheoMa(int maGhe);

    // Lấy danh sách tất cả ghế
    List<Ghe> layDanhSachGhe();

    // Lấy danh sách ghế theo phòng chiếu
    List<Ghe> layDanhSachGheTheoPhong(int maPhong);

    // Lấy danh sách ghế theo loại
    List<Ghe> layDanhSachGheTheoLoai(String loaiGhe);

    // Tìm ghế theo số ghế và phòng
    Ghe timGheTheoSoGheVaPhong(String soGhe, int maPhong);
}
