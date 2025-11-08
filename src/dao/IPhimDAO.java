package dao;

import entity.Phim;
import java.util.List;

public interface IPhimDAO {
    // Thêm phim mới
    boolean themPhim(Phim phim);

    // Cập nhật thông tin phim
    boolean capNhatPhim(Phim phim);

    // Xóa phim
    boolean xoaPhim(int maPhim);

    // Tìm phim theo mã
    Phim timPhimTheoMa(int maPhim);

    // Lấy danh sách tất cả phim
    List<Phim> layDanhSachPhim();

    // Tìm phim theo tên
    List<Phim> timPhimTheoTen(String tenPhim);

    // Tìm phim theo thể loại
    List<Phim> timPhimTheoTheLoai(String theLoai);
}
