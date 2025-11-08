# 🎬 Hệ Thống Quản Lý Rạp Chiếu Phim
## Cinema Management System

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![SQL Server](https://img.shields.io/badge/Database-SQL%20Server-blue.svg)](https://www.microsoft.com/sql-server)
[![Swing](https://img.shields.io/badge/UI-Java%20Swing-green.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Hệ thống quản lý rạp chiếu phim hiện đại với giao diện đồ họa đẹp mắt, được xây dựng bằng Java Swing và SQL Server.

## ✨ Tính Năng Chính

### 🎯 Quản Lý Cơ Bản
- **Quản lý Phim**: Thêm, sửa, xóa phim với poster và thông tin chi tiết
- **Quản lý Phòng Chiếu**: Cấu hình phòng chiếu và ghế ngồi
- **Quản lý Lịch Chiếu**: Lập lịch chiếu phim theo ngày giờ
- **Quản lý Khách Hàng**: Lưu trữ thông tin khách hàng

### 🎫 Đặt Vé & Thanh Toán
- **Đặt Vé Trực Quan**: Sơ đồ ghế 2D tương tác như rạp thật
- **Chọn Ghế Real-time**: Click để chọn/bỏ chọn ghế
- **Màu Sắc Phân Biệt**: Ghế trống (xanh), đã chọn (xanh dương), đã đặt (xám), VIP (cam)
- **Thanh Toán Tự Động**: Tính tổng tiền và tạo hóa đơn

### 🧾 Quản Lý Hóa Đơn
- **Theo Dõi Doanh Thu**: Xem chi tiết hóa đơn theo ngày, khách hàng
- **Trạng Thái Thanh Toán**: Theo dõi đã thanh toán/chưa thanh toán
- **In Vé**: (Tính năng sẵn sàng để mở rộng)

## 🎨 Giao Diện Hiện Đại

### Main Dashboard
- ✨ Card-based navigation với 6 modules chính
- 🌈 Gradient background đẹp mắt
- 🎯 Hover effects mượt mà
- 📱 Layout responsive

### Quản Lý Phim (PhimFrameModern)
- 🖼️ **Grid View** với movie cards (4 cột)
- 🎬 **Poster Display** cho mỗi phim
- 🔍 **Search & Filter** theo tên và thể loại
- 📝 **Form Panel** với poster preview lớn
- 📁 **File Picker** để chọn hình ảnh

### Đặt Vé (DatVeFrameModern)
- 🎭 **Sơ đồ ghế 2D** theo hàng (A, B, C...)
- 🖱️ **Interactive Selection** với click chuột
- 📊 **Booking Summary** real-time
- 💰 **Auto Calculate** tổng tiền
- 🎨 **Color-coded** trạng thái ghế

### Hệ Thống Styling (UIStyles)
- 🎨 Material Design color palette
- 🔤 Consistent typography
- 🖼️ Card, button, table styling
- 💬 Modern dialogs

## 🏗️ Kiến Trúc Hệ Thống

```
RapChieuPhim/
├── src/
│   ├── dao/              # Data Access Objects
│   │   ├── PhimDAO.java
│   │   ├── LichChieuDAO.java
│   │   ├── VeDAO.java
│   │   └── ...
│   ├── entity/           # Entity Classes
│   │   ├── Phim.java
│   │   ├── LichChieu.java
│   │   ├── Ve.java
│   │   └── ...
│   ├── ui/               # User Interface
│   │   ├── MainFrame.java
│   │   ├── PhimFrameModern.java
│   │   ├── DatVeFrameModern.java
│   │   ├── UIStyles.java
│   │   ├── SeatButton.java
│   │   ├── MovieCard.java
│   │   └── ...
│   └── util/             # Utilities
│       └── JDBCUtil.java
├── sqlscript/
│   └── rapchieuphim.sql  # Database Schema
├── resources/
│   ├── images/
│   │   └── movies/       # Movie Posters
│   └── icons/            # Application Icons
├── UI_SETUP_GUIDE.md     # UI Setup Guide
└── README.md
```

### Layer Architecture
```
┌─────────────────────────┐
│   UI Layer (Swing)      │  ← PhimFrame, DatVeFrame, MainFrame
├─────────────────────────┤
│   DAO Layer             │  ← PhimDAO, VeDAO, HoaDonDAO
├─────────────────────────┤
│   Entity Layer          │  ← Phim, Ve, HoaDon
├─────────────────────────┤
│   Database (SQL Server) │  ← Tables, Views, Procedures
└─────────────────────────┘
```

## 🚀 Bắt Đầu Nhanh

### Yêu Cầu Hệ Thống
- ☕ Java JDK 11 hoặc cao hơn
- 🗄️ Microsoft SQL Server 2016+
- 💻 IntelliJ IDEA (khuyến nghị) hoặc Eclipse
- 🔌 SQL Server JDBC Driver

### Cài Đặt

**1. Clone Repository**
```bash
git clone <repository-url>
cd RapChieuPhim
```

**2. Setup Database**
```bash
# Mở SQL Server Management Studio
# Execute script: sqlscript/rapchieuphim.sql
# Tạo database và tables
```

**3. Configure Connection**

Mở `src/util/JDBCUtil.java` và cập nhật:
```java
private static final String URL =
    "jdbc:sqlserver://YOUR_SERVER:1433;" +
    "databaseName=RapChieuPhim;" +
    "encrypt=true;" +
    "trustServerCertificate=true;";
private static final String USERNAME = "your_username";
private static final String PASSWORD = "your_password";
```

**4. Add Dependencies trong IntelliJ**

File → Project Structure → Libraries → "+"

Thêm từ Maven:
```
com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11
```

**5. (Optional) Add FlatLaf cho Modern Look**
```
com.formdev:flatlaf:3.2.5
```

Trong `MainFrame.main()`:
```java
try {
    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
} catch (Exception ex) {
    ex.printStackTrace();
}
```

**6. Run Application**
```bash
# Trong IntelliJ: Right-click MainFrame.java → Run
# Hoặc: Shift+F10
```

## 📊 Database Schema

### Core Tables
- **Phim** (Movies): ID, tên, thể loại, thời lượng, mô tả, hình ảnh
- **PhongChieu** (Theaters): ID, tên, số ghế, loại phòng
- **Ghe** (Seats): ID, số ghế, hàng, loại (VIP/Thường)
- **LichChieu** (Screenings): ID, phim, phòng, ngày, giờ, giá vé
- **KhachHang** (Customers): ID, tên, SĐT, email
- **Ve** (Tickets): ID, lịch chiếu, ghế, hóa đơn, trạng thái
- **HoaDon** (Invoices): ID, khách hàng, ngày lập, tổng tiền, trạng thái

### Views
- **v_ChiTietVe**: Chi tiết vé với thông tin phim, phòng, khách hàng

### Stored Procedures
- **sp_ThanhToanHoaDon**: Xử lý thanh toán hóa đơn
- **sp_DatVe**: Đặt vé (hiện không dùng - thay bằng direct INSERT)

### Functions
- **fn_DemGheTrong**: Đếm số ghế trống cho lịch chiếu

## 🎯 Sử Dụng

### Quản Lý Phim
1. Click **"🎬 Quản Lý Phim"** trên main dashboard
2. Xem grid view với tất cả các phim (kèm poster nếu có)
3. Click vào movie card để chọn và xem chi tiết
4. Sử dụng form bên phải để:
   - ➕ **Thêm**: Nhập thông tin phim mới
   - ✏️ **Sửa**: Chỉnh sửa phim đã chọn
   - 🗑️ **Xóa**: Xóa phim đã chọn
   - 🔄 **Làm Mới**: Reset form
5. Dùng **Search** và **Filter** để tìm phim nhanh

### Đặt Vé
1. Click **"🎟️ Đặt Vé"** trên main dashboard
2. **Chọn lịch chiếu** từ dropdown (phim + phòng + ngày/giờ)
3. **Chọn khách hàng** từ dropdown
4. Xem **sơ đồ ghế** hiển thị:
   - 🟢 Xanh lá = Ghế trống
   - 🔵 Xanh dương = Đã chọn
   - ⚫ Xám = Đã có người đặt
   - 🟠 Cam = Ghế VIP
5. **Click ghế** để chọn/bỏ chọn
6. Xem **tóm tắt vé** và **tổng tiền** bên phải
7. Click **"💳 Thanh Toán"** để hoàn tất

### Quản Lý Hóa Đơn
1. Click **"🧾 Quản Lý Hóa Đơn"**
2. Xem danh sách tất cả hóa đơn
3. Lọc theo:
   - Khách hàng
   - Ngày
   - Trạng thái (Đã/Chưa thanh toán)
4. Xem chi tiết các vé trong hóa đơn

## 🎨 Customization

### Thay Đổi Theme Colors

Mở `src/ui/UIStyles.java`:

```java
// Ví dụ: Theme màu tím
public static final Color PRIMARY_COLOR = new Color(156, 39, 176);
public static final Color PRIMARY_DARK = new Color(123, 31, 162);
public static final Color ACCENT_COLOR = new Color(255, 64, 129);
```

### Thêm Movie Posters

1. Đặt file ảnh vào: `resources/images/movies/`
2. Đặt tên file (VD: `avengers.jpg`)
3. Khi thêm/sửa phim, nhập tên file vào field "Hình ảnh"
4. Poster sẽ tự động hiển thị trong grid view và form

### Custom Seat Layout

Trong database, table **Ghe** (Seats):
- `soGhe`: Số ghế (VD: "A1", "B5")
- `hang`: Hàng (VD: "A", "B", "C")
- `loaiGhe`: "VIP" hoặc "Thuong"

Hệ thống tự động sắp xếp ghế theo hàng trong sơ đồ.

## 📚 Tài Liệu

- **[UI_SETUP_GUIDE.md](UI_SETUP_GUIDE.md)**: Hướng dẫn chi tiết về UI setup
  - Cài đặt IntelliJ IDEA
  - Thêm thư viện (FlatLaf, JFreeChart, etc.)
  - Customization guide
  - Best practices
  - Troubleshooting

## 🛠️ Công Nghệ Sử Dụng

| Công nghệ | Mô tả | Version |
|-----------|-------|---------|
| **Java** | Core language | 11+ |
| **Swing** | GUI framework | Built-in |
| **SQL Server** | Database | 2016+ |
| **JDBC** | Database connectivity | 12.4+ |
| **FlatLaf** | Modern Look & Feel (optional) | 3.2.5 |

## 🔥 Highlights

### Modern UI Components
- ✅ **UIStyles**: Centralized styling system
- ✅ **SeatButton**: Custom cinema seat component
- ✅ **MovieCard**: Movie display card with poster
- ✅ **Material Design**: Color palette and typography
- ✅ **Responsive Layouts**: Grid, Card, Split pane designs

### DAO Layer
- ✅ Singleton pattern
- ✅ Try-with-resources (auto-close connections)
- ✅ PreparedStatement (SQL injection prevention)
- ✅ Entity mapping from ResultSets

### Business Logic
- ✅ Real-time seat availability check
- ✅ Multi-ticket booking per invoice
- ✅ Auto-calculate total price
- ✅ Transaction management

## 🚧 Roadmap

### Phase 1: Core Features ✅ DONE
- [x] Database schema
- [x] DAO layer
- [x] Basic CRUD operations
- [x] Modern UI framework

### Phase 2: Enhanced UX ✅ DONE
- [x] Visual seat selection
- [x] Movie poster support
- [x] Grid view for movies
- [x] Modern styling system

### Phase 3: Future Enhancements
- [ ] Dashboard with charts (revenue, popular movies)
- [ ] Print tickets with QR code
- [ ] Email confirmation
- [ ] Advanced search with filters
- [ ] User authentication & roles
- [ ] Online payment integration
- [ ] Mobile responsive design

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Development Team** - Initial work
- **Claude AI** - UI Modernization & Architecture improvements

## 🙏 Acknowledgments

- Java Swing Documentation
- Material Design Guidelines
- FlatLaf Library
- SQL Server Documentation
- Community feedback and contributions

## 📧 Contact

For questions or support:
- 📧 Email: [your-email@example.com]
- 🐛 Issues: [GitHub Issues](https://github.com/your-repo/issues)
- 💬 Discussions: [GitHub Discussions](https://github.com/your-repo/discussions)

---

**⭐ If you find this project helpful, please give it a star!**

Made with ❤️ using Java & SQL Server
