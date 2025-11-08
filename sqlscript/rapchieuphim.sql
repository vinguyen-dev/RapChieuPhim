USE master;
GO
-- ============================================
-- XÓA DATABASE NẾU ĐÃ TỒN TẠI
-- ============================================
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'RapChieuPhim')
BEGIN
    ALTER DATABASE RapChieuPhim SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE RapChieuPhim;
END
GO

-- ============================================
-- TẠO DATABASE
-- ============================================
CREATE DATABASE RapChieuPhim
COLLATE Vietnamese_CI_AS;
GO
USE RapChieuPhim;
GO

PRINT N'========================================';
PRINT N'HỆ THỐNG BÁN VÉ RẠP CHIẾU PHIM - OOP TỐI ƯU';
PRINT N'Đúng UML | Không dư thừa | Toàn vẹn dữ liệu';
PRINT N'========================================';
GO

-- ============================================
-- 1. BẢNG PHIM
-- ============================================
PRINT N'[1/7] Tạo bảng Phim...';
GO
CREATE TABLE Phim (
                      maPhim INT PRIMARY KEY IDENTITY(1,1),
                      tenPhim NVARCHAR(200) NOT NULL,
                      theLoai NVARCHAR(100),
                      thoiLuong INT NOT NULL CHECK (thoiLuong > 0),
                      moTa NVARCHAR(MAX),
                      hinhAnh NVARCHAR(500)
);
GO

-- ============================================
-- 2. BẢNG PHÒNG CHIẾU
-- ============================================
PRINT N'[2/7] Tạo bảng PhongChieu...';
GO
CREATE TABLE PhongChieu (
                            maPhong INT PRIMARY KEY IDENTITY(1,1),
                            tenPhong NVARCHAR(100) NOT NULL UNIQUE,
                            soGhe INT NOT NULL CHECK (soGhe > 0),
                            loaiPhong NVARCHAR(50) DEFAULT N'2D'
        CHECK (loaiPhong IN (N'2D', N'3D', N'IMAX', N'4DX'))
);
GO

-- ============================================
-- 3. BẢNG GHẾ (COMPOSITION với PhongChieu)
-- ============================================
PRINT N'[3/7] Tạo bảng Ghe...';
GO
CREATE TABLE Ghe (
                     maGhe INT PRIMARY KEY IDENTITY(1,1),
                     maPhong INT NOT NULL,
                     soGhe NVARCHAR(10) NOT NULL,
                     hang NCHAR(1) NOT NULL CHECK (hang BETWEEN 'A' AND 'Z'),
                     loaiGhe NVARCHAR(20) DEFAULT N'Thuong'
        CHECK (loaiGhe IN (N'Thuong', N'VIP', N'Couple')),

                     CONSTRAINT FK_Ghe_PhongChieu FOREIGN KEY (maPhong)
                         REFERENCES PhongChieu(maPhong)
                         ON DELETE CASCADE -- COMPOSITION
                         ON UPDATE CASCADE,

                     CONSTRAINT UK_Ghe_PhongSoGhe UNIQUE (maPhong, soGhe)
);
GO
CREATE NONCLUSTERED INDEX IDX_Ghe_maPhong ON Ghe(maPhong);
GO

-- ============================================
-- 4. BẢNG LỊCH CHIẾU (AGGREGATION)
-- ============================================
PRINT N'[4/7] Tạo bảng LichChieu...';
GO
CREATE TABLE LichChieu (
                           maLichChieu INT PRIMARY KEY IDENTITY(1,1),
                           maPhim INT NOT NULL,
                           maPhong INT NOT NULL,
                           ngayChieu DATE NOT NULL,
                           gioChieu TIME NOT NULL,
                           giaVe DECIMAL(10,2) NOT NULL CHECK (giaVe >= 0),

                           CONSTRAINT FK_LichChieu_Phim FOREIGN KEY (maPhim)
                               REFERENCES Phim(maPhim)
                               ON DELETE NO ACTION -- AGGREGATION
                               ON UPDATE CASCADE,

                           CONSTRAINT FK_LichChieu_PhongChieu FOREIGN KEY (maPhong)
                               REFERENCES PhongChieu(maPhong)
                               ON DELETE NO ACTION -- AGGREGATION
                               ON UPDATE CASCADE,

                           CONSTRAINT UK_LichChieu UNIQUE (maPhong, ngayChieu, gioChieu)
);
GO
CREATE NONCLUSTERED INDEX IDX_LichChieu_maPhim ON LichChieu(maPhim);
CREATE NONCLUSTERED INDEX IDX_LichChieu_ngayChieu ON LichChieu(ngayChieu);
CREATE NONCLUSTERED INDEX IDX_LichChieu_maPhong ON LichChieu(maPhong);
GO

-- ============================================
-- 5. BẢNG KHÁCH HÀNG
-- ============================================
PRINT N'[5/7] Tạo bảng KhachHang...';
GO
CREATE TABLE KhachHang (
                           maKhachHang INT PRIMARY KEY IDENTITY(1,1),
                           tenKhachHang NVARCHAR(200) NOT NULL,
                           soDienThoai NVARCHAR(15) NOT NULL UNIQUE,
                           email NVARCHAR(100) UNIQUE
);
GO

-- ============================================
-- 6. BẢNG HÓA ĐƠN (AGGREGATION)
-- ============================================
PRINT N'[6/7] Tạo bảng HoaDon...';
GO
CREATE TABLE HoaDon (
                        maHoaDon INT PRIMARY KEY IDENTITY(1,1),
                        maKhachHang INT NOT NULL,
                        ngayLap DATETIME NOT NULL DEFAULT GETDATE(),
                        tongTien DECIMAL(12,2) NOT NULL DEFAULT 0 CHECK (tongTien >= 0),
                        trangThaiThanhToan NVARCHAR(50) DEFAULT N'Chua thanh toan'
        CHECK (trangThaiThanhToan IN (N'Chua thanh toan', N'Da thanh toan', N'Huy')),

                        CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKhachHang)
                            REFERENCES KhachHang(maKhachHang)
                            ON DELETE NO ACTION -- AGGREGATION
                            ON UPDATE CASCADE
);
GO
CREATE NONCLUSTERED INDEX IDX_HoaDon_maKhachHang ON HoaDon(maKhachHang);
GO

-- ============================================
-- 7. BẢNG VÉ (COMPOSITION với HoaDon + LichChieu)
-- ============================================
PRINT N'[7/7] Tạo bảng Ve...';
GO
CREATE TABLE Ve (
                    maVe INT PRIMARY KEY IDENTITY(1,1),
                    maLichChieu INT NOT NULL,
                    maGhe INT NOT NULL,
                    maHoaDon INT NOT NULL,
                    giaVe DECIMAL(10,2) NOT NULL CHECK (giaVe >= 0),
                    trangThai NVARCHAR(50) DEFAULT N'Da dat'
        CHECK (trangThai IN (N'Da dat', N'Da thanh toan', N'Huy')),

                    CONSTRAINT FK_Ve_LichChieu FOREIGN KEY (maLichChieu)
                        REFERENCES LichChieu(maLichChieu)
                        ON DELETE NO ACTION
                        ON UPDATE NO ACTION,

                    CONSTRAINT FK_Ve_Ghe FOREIGN KEY (maGhe)
                        REFERENCES Ghe(maGhe)
                        ON DELETE NO ACTION
                        ON UPDATE NO ACTION,

                    CONSTRAINT FK_Ve_HoaDon FOREIGN KEY (maHoaDon)
                        REFERENCES HoaDon(maHoaDon)
                        ON DELETE CASCADE -- COMPOSITION
                        ON UPDATE NO ACTION,

                    CONSTRAINT UK_Ve_LichGhe UNIQUE (maLichChieu, maGhe) -- 1 ghế chỉ đặt 1 lần trong 1 lịch
);
GO
CREATE NONCLUSTERED INDEX IDX_Ve_maLichChieu ON Ve(maLichChieu);
CREATE NONCLUSTERED INDEX IDX_Ve_maHoaDon ON Ve(maHoaDon);
CREATE NONCLUSTERED INDEX IDX_Ve_maGhe ON Ve(maGhe);
GO

PRINT N'✅ Tất cả bảng đã được tạo thành công!';
GO

-- ============================================
-- TRIGGERS: COMPOSITION & TỔNG TIỀN
-- ============================================

-- 1. XÓA PHIM → XÓA LỊCH CHIẾU
CREATE TRIGGER TRG_Phim_Delete
    ON Phim
    AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;
DELETE FROM LichChieu WHERE maPhim IN (SELECT maPhim FROM deleted);
END;
GO

-- 2. XÓA LỊCH CHIẾU → HỦY VÉ
CREATE TRIGGER TRG_LichChieu_Delete
    ON LichChieu
    AFTER DELETE
AS
BEGIN
    SET NOCOUNT ON;
UPDATE Ve
SET trangThai = N'Huy'
WHERE maLichChieu IN (SELECT maLichChieu FROM deleted)
  AND trangThai <> N'Huy';
END;
GO

-- 3. TỰ ĐỘNG TÍNH TỔNG TIỀN HÓA ĐƠN
CREATE TRIGGER TRG_Ve_TinhTongTien
    ON Ve
    AFTER INSERT, UPDATE, DELETE
    AS
BEGIN
    SET NOCOUNT ON;
UPDATE HoaDon
SET tongTien = (
    SELECT ISNULL(SUM(giaVe), 0)
    FROM Ve
    WHERE maHoaDon = COALESCE(i.maHoaDon, d.maHoaDon)
      AND trangThai <> N'Huy'
)
    FROM HoaDon hd
    LEFT JOIN inserted i ON hd.maHoaDon = i.maHoaDon
    LEFT JOIN deleted d ON hd.maHoaDon = d.maHoaDon
WHERE hd.maHoaDon IN (SELECT COALESCE(i.maHoaDon, d.maHoaDon) FROM inserted i FULL JOIN deleted d ON 1=1);
END;
GO

-- ============================================
-- DỮ LIỆU MẪU
-- ============================================
PRINT N'';
PRINT N'========================================';
PRINT N'THÊM DỮ LIỆU MẪU';
PRINT N'========================================';
GO

-- Phim
INSERT INTO Phim (tenPhim, theLoai, thoiLuong, moTa, hinhAnh) VALUES
(N'Avengers: Endgame', N'Hành động', 181, N'Cuộc chiến cuối cùng', N'avengers.jpg'),
(N'Parasite', N'Tâm lý', 132, N'Oscar 2020', N'parasite.jpg'),
(N'Inception', N'Viễn tưởng', 148, N'Giấc mơ trong mơ', N'inception.jpg');
GO

-- Phòng chiếu
INSERT INTO PhongChieu (tenPhong, soGhe, loaiPhong) VALUES
(N'Phòng 1', 60, N'2D'),
(N'Phòng 2', 80, N'3D');
GO

-- Ghế Phòng 1 (6 hàng x 10 cột)
DECLARE @maPhong INT = 1, @hang CHAR(1) = 'A', @cot INT;
WHILE @hang <= 'F'
BEGIN
    SET @cot = 1;
    WHILE @cot <= 10
BEGIN
INSERT INTO Ghe (maPhong, soGhe, hang, loaiGhe)
VALUES (@maPhong, @hang + CAST(@cot AS NVARCHAR), @hang,
        CASE
            WHEN @hang IN ('C','D') THEN N'VIP'
            WHEN @hang = 'E' THEN N'Couple'
            ELSE N'Thuong'
            END);
SET @cot += 1;
END
    SET @hang = CHAR(ASCII(@hang) + 1);
END
GO

-- Lịch chiếu
INSERT INTO LichChieu (maPhim, maPhong, ngayChieu, gioChieu, giaVe) VALUES
(1, 1, '2025-11-10', '14:00:00', 80000),
(1, 1, '2025-11-10', '18:00:00', 100000),
(2, 2, '2025-11-10', '15:30:00', 90000);
GO

-- Khách hàng
INSERT INTO KhachHang (tenKhachHang, soDienThoai, email) VALUES
(N'Nguyễn Văn A', '0123456789', 'a@email.com'),
(N'Trần Thị B', '0987654321', 'b@email.com');
GO

-- Hóa đơn + Vé mẫu
DECLARE @maHD1 INT, @maHD2 INT;

-- Hóa đơn 1: Đã thanh toán
INSERT INTO HoaDon (maKhachHang, trangThaiThanhToan) VALUES (1, N'Da thanh toan');
SET @maHD1 = SCOPE_IDENTITY();
INSERT INTO Ve (maLichChieu, maGhe, maHoaDon, giaVe, trangThai) VALUES
                                                                    (1, 21, @maHD1, 80000, N'Da thanh toan'),
                                                                    (1, 22, @maHD1, 80000, N'Da thanh toan');

-- Hóa đơn 2: Chưa thanh toán
INSERT INTO HoaDon (maKhachHang, trangThaiThanhToan) VALUES (2, N'Chua thanh toan');
SET @maHD2 = SCOPE_IDENTITY();
INSERT INTO Ve (maLichChieu, maGhe, maHoaDon, giaVe, trangThai) VALUES
    (2, 25, @maHD2, 100000, N'Da dat');
GO

-- ============================================
-- STORED PROCEDURES
-- ============================================

-- 1. ĐẶT VÉ MỚI
CREATE PROCEDURE sp_DatVe
    @maLichChieu INT,
    @maGhe INT,
    @maKhachHang INT,
    @giaVe DECIMAL(10,2),
    @maHoaDon INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
BEGIN TRANSACTION;

        -- Kiểm tra ghế thuộc phòng của lịch
        IF NOT EXISTS (
            SELECT 1 FROM LichChieu lc
            JOIN Ghe g ON lc.maPhong = g.maPhong
            WHERE lc.maLichChieu = @maLichChieu AND g.maGhe = @maGhe
        )
            THROW 50001, N'Ghế không thuộc phòng của lịch chiếu', 1;

        -- Kiểm tra ghế đã đặt
        IF EXISTS (
            SELECT 1 FROM Ve
            WHERE maLichChieu = @maLichChieu AND maGhe = @maGhe AND trangThai <> N'Huy'
        )
            THROW 50002, N'Ghế đã được đặt cho lịch này', 1;

        -- Tạo hóa đơn
INSERT INTO HoaDon (maKhachHang, trangThaiThanhToan)
VALUES (@maKhachHang, N'Chua thanh toan');
SET @maHoaDon = SCOPE_IDENTITY();

        -- Đặt vé
INSERT INTO Ve (maLichChieu, maGhe, maHoaDon, giaVe, trangThai)
VALUES (@maLichChieu, @maGhe, @maHoaDon, @giaVe, N'Da dat');

COMMIT;
SELECT @maHoaDon AS maHoaDon, N'Đặt vé thành công' AS message;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK;
        THROW;
END CATCH
END;
GO

-- 2. XEM GHẾ TRỐNG
CREATE PROCEDURE sp_XemGheTrong
    @maLichChieu INT
AS
BEGIN
SELECT g.maGhe, g.soGhe, g.hang, g.loaiGhe
FROM Ghe g
         JOIN LichChieu lc ON g.maPhong = lc.maPhong
WHERE lc.maLichChieu = @maLichChieu
  AND g.maGhe NOT IN (
    SELECT maGhe FROM Ve
    WHERE maLichChieu = @maLichChieu AND trangThai <> N'Huy'
)
ORDER BY g.hang, g.soGhe;
END;
GO

-- 3. THANH TOÁN HÓA ĐƠN
CREATE PROCEDURE sp_ThanhToanHoaDon
    @maHoaDon INT
AS
BEGIN
    SET NOCOUNT ON;
BEGIN TRY
BEGIN TRANSACTION;
UPDATE HoaDon SET trangThaiThanhToan = N'Da thanh toan' WHERE maHoaDon = @maHoaDon;
UPDATE Ve SET trangThai = N'Da thanh toan' WHERE maHoaDon = @maHoaDon AND trangThai = N'Da dat';
COMMIT;
END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK;
        THROW;
END CATCH
END;
GO

-- ============================================
-- VIEWS & FUNCTIONS
-- ============================================

-- View: Chi tiết vé
CREATE VIEW v_ChiTietVe AS
SELECT
    v.maVe, p.tenPhim, pc.tenPhong, lc.ngayChieu, lc.gioChieu,
    g.soGhe, g.hang, g.loaiGhe, v.giaVe, kh.tenKhachHang, hd.trangThaiThanhToan
FROM Ve v
         JOIN LichChieu lc ON v.maLichChieu = lc.maLichChieu
         JOIN Phim p ON lc.maPhim = p.maPhim
         JOIN PhongChieu pc ON lc.maPhong = pc.maPhong
         JOIN Ghe g ON v.maGhe = g.maGhe
         JOIN HoaDon hd ON v.maHoaDon = hd.maHoaDon
         JOIN KhachHang kh ON hd.maKhachHang = kh.maKhachHang;
GO

-- Function: Đếm ghế trống
CREATE FUNCTION fn_DemGheTrong(@maLichChieu INT)
    RETURNS INT
AS
BEGIN
    DECLARE @tong INT = (SELECT soGhe FROM LichChieu lc JOIN PhongChieu pc ON lc.maPhong = pc.maPhong WHERE maLichChieu = @maLichChieu);
    DECLARE @dat INT = (SELECT COUNT(*) FROM Ve WHERE maLichChieu = @maLichChieu AND trangThai <> N'Huy');
RETURN @tong - ISNULL(@dat, 0);
END;
GO

-- ============================================
-- KIỂM TRA
-- ============================================
PRINT N'';
PRINT N'========================================';
PRINT N'KẾT QUẢ KIỂM TRA';
PRINT N'========================================';
GO

PRINT N'1. Ghế trống lịch 1:';
EXEC sp_XemGheTrong 1;
GO

PRINT N'2. Chi tiết vé:';
SELECT TOP 5 * FROM v_ChiTietVe;
GO

PRINT N'3. Số ghế trống theo lịch:';
SELECT maLichChieu, dbo.fn_DemGheTrong(maLichChieu) AS gheTrong FROM LichChieu;
GO
