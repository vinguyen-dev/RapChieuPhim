# 🎨 Hướng Dẫn Cải Thiện Giao Diện - UI Setup Guide

## 📋 Tổng Quan (Overview)

Hệ thống đã được cập nhật với giao diện hiện đại, bao gồm:
- ✨ Thiết kế Material Design với màu sắc nhất quán
- 🎭 Hệ thống chọn ghế trực quan với hiệu ứng tương tác
- 📱 Bố cục responsive với card-based design
- 🎨 Biểu tượng emoji tích hợp sẵn
- ⚡ Hiệu ứng hover và animation mượt mà

## 🎨 Các Tính Năng UI Mới

### 1. UIStyles - Hệ Thống Styling Thống Nhất
File: `src/ui/UIStyles.java`

Cung cấp:
- Bảng màu chuẩn (PRIMARY, ACCENT, SUCCESS, ERROR, etc.)
- Fonts thống nhất (TITLE, HEADER, NORMAL, SMALL)
- Utility methods để style các components
- Dialog messages với thiết kế hiện đại

### 2. SeatButton - Component Ghế Ngồi Tùy Chỉnh
File: `src/ui/SeatButton.java`

Tính năng:
- Hiển thị trực quan trạng thái ghế (Available, Selected, Occupied, VIP)
- Bo góc mượt mà với anti-aliasing
- Tooltip thông tin chi tiết
- Màu sắc phân biệt rõ ràng

### 3. MainFrame - Trang Chủ Hiện Đại
File: `src/ui/MainFrame.java`

Cải tiến:
- Menu bar với icons
- Card-based navigation với hiệu ứng hover
- Gradient background
- Responsive layout

### 4. DatVeFrameModern - Đặt Vé Trực Quan
File: `src/ui/DatVeFrameModern.java`

Tính năng:
- Sơ đồ ghế ngồi 2D trực quan
- Chọn/bỏ chọn ghế tương tác
- Tóm tắt đặt vé real-time
- Legend hiển thị trạng thái ghế
- Thông tin phim và phòng chiếu rõ ràng

## 📚 Thư Viện Được Khuyến Nghị

### 1. FlatLaf - Modern Look & Feel (HIGHLY RECOMMENDED)

**Mô tả:** Look and Feel hiện đại cho Swing applications

**Cài đặt trong IntelliJ IDEA:**

1. Mở File → Project Structure → Libraries
2. Click "+" → From Maven
3. Nhập: `com.formdev:flatlaf:3.2.5`
4. Click OK

**Sử dụng:**
```java
// Thêm vào MainFrame.main() trước khi khởi tạo frame
try {
    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
    // Hoặc cho dark theme:
    // UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
} catch (Exception ex) {
    System.err.println("Failed to initialize LaF");
}
```

**Benefits:**
- Giao diện flat, hiện đại
- Hỗ trợ Light & Dark themes
- Tương thích tốt với high-DPI displays
- Cải thiện đáng kể UI trên Windows/Linux

### 2. JFreeChart - Biểu Đồ và Charts (Optional)

**Mô tả:** Thư viện vẽ biểu đồ chuyên nghiệp

**Cài đặt:**
```
com.jfree:jfreechart:1.5.4
```

**Use cases:**
- Thống kê doanh thu theo phim
- Biểu đồ tỷ lệ lấp đầy phòng chiếu
- Báo cáo xu hướng bán vé

### 3. MigLayout - Advanced Layout Manager (Optional)

**Mô tả:** Layout manager mạnh mẽ, linh hoạt

**Cài đặt:**
```
com.miglayout:miglayout-swing:11.0
```

**Benefits:**
- Dễ dàng tạo responsive layouts
- Code ngắn gọn hơn GridBagLayout
- Hỗ trợ constraints mạnh mẽ

### 4. SwingX - Extended Swing Components (Optional)

**Mô tả:** Bộ components Swing mở rộng

**Cài đặt:**
```
org.swinglabs.swingx:swingx-all:1.6.5-1
```

**Components hữu ích:**
- JXTable với searching, sorting mạnh mẽ
- JXDatePicker cho chọn ngày
- JXImagePanel cho hiển thị hình ảnh
- JXBusyLabel cho loading indicators

## 🖼️ Hỗ Trợ Hình Ảnh Phim (Movie Posters)

### Cấu Trúc Thư Mục
```
RapChieuPhim/
├── resources/
│   ├── images/
│   │   ├── movies/          # Poster phim
│   │   │   ├── phim1.jpg
│   │   │   ├── phim2.jpg
│   │   │   └── ...
│   │   ├── placeholder.png  # Ảnh mặc định khi không có poster
│   │   └── backgrounds/     # Ảnh nền
│   └── icons/               # Icons tùy chỉnh
│       ├── film.png
│       ├── ticket.png
│       └── ...
```

### Cách Thêm Hình Ảnh

**1. Cập nhật entity Phim:**
- Field `hinhAnh` trong database đã có sẵn
- Lưu tên file hoặc đường dẫn tương đối

**2. Load và hiển thị image:**
```java
// Trong PhimFrame hoặc component khác
private ImageIcon loadMovieImage(String imagePath) {
    try {
        String fullPath = "resources/images/movies/" + imagePath;
        File imgFile = new File(fullPath);
        if (imgFile.exists()) {
            Image img = ImageIO.read(imgFile);
            Image scaled = img.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    // Return placeholder if not found
    return new ImageIcon("resources/images/placeholder.png");
}
```

**3. Hiển thị trong JLabel:**
```java
JLabel lblPoster = new JLabel();
lblPoster.setIcon(loadMovieImage(phim.getHinhAnh()));
lblPoster.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
```

## 🎯 Best Practices

### 1. Sử Dụng UIStyles Consistently
```java
// GOOD
JButton btn = new JButton("Submit");
UIStyles.stylePrimaryButton(btn);

// BAD - hardcoded colors
btn.setBackground(new Color(33, 150, 243));
```

### 2. Tách Component Logic
```java
// Tạo method riêng cho từng phần UI
private JPanel createHeaderPanel() { ... }
private JPanel createContentPanel() { ... }
private JPanel createFooterPanel() { ... }
```

### 3. Responsive Sizing
```java
// Sử dụng Layout Managers thay vì absolute positioning
setLayout(new BorderLayout());
// hoặc
setLayout(new GridBagLayout());

// TRÁNH: setLayout(null); và setBounds()
```

### 4. Icon Best Practices
- Sử dụng emoji cho icons nhanh: "🎬", "📅", "👤"
- Hoặc load PNG icons cho chất lượng tốt hơn
- Kích thước chuẩn: 16x16, 24x24, 32x32, 48x48

## 🚀 Hướng Dẫn Setup Project Mới

### Bước 1: Open Project in IntelliJ
1. File → Open
2. Chọn folder `RapChieuPhim`
3. Click OK

### Bước 2: Configure Project SDK
1. File → Project Structure
2. Project Settings → Project
3. Set SDK: Java 11 hoặc cao hơn
4. Set Language Level: tương ứng với SDK

### Bước 3: Add SQL Server JDBC Driver
1. File → Project Structure → Libraries
2. Click "+" → From Maven
3. Nhập: `com.microsoft.sqlserver:mssql-jdbc:12.4.2.jre11`
4. Click OK

### Bước 4: (Optional) Add FlatLaf
1. File → Project Structure → Libraries
2. Click "+" → From Maven
3. Nhập: `com.formdev:flatlaf:3.2.5`
4. Click OK

### Bước 5: Configure Database Connection
1. Mở `src/util/JDBCUtil.java`
2. Cập nhật connection string với thông tin server của bạn:
   ```java
   private static final String URL =
       "jdbc:sqlserver://YOUR_SERVER:1433;" +
       "databaseName=RapChieuPhim;" +
       "encrypt=true;" +
       "trustServerCertificate=true;";
   private static final String USERNAME = "your_username";
   private static final String PASSWORD = "your_password";
   ```

### Bước 6: Run Application
1. Mở `src/ui/MainFrame.java`
2. Right-click → Run 'MainFrame.main()'
3. Hoặc nhấn Shift+F10

## 🎨 Tùy Chỉnh Theme

### Thay Đổi Màu Sắc Chủ Đạo

Mở `src/ui/UIStyles.java` và chỉnh sửa:

```java
// Ví dụ: Chuyển sang màu xanh lá
public static final Color PRIMARY_COLOR = new Color(76, 175, 80);  // Green
public static final Color PRIMARY_DARK = new Color(56, 142, 60);
public static final Color PRIMARY_LIGHT = new Color(129, 199, 132);

// Hoặc màu tím
public static final Color PRIMARY_COLOR = new Color(156, 39, 176); // Purple
```

### Custom Fonts

```java
// Trong UIStyles.java
public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 24);
// Thay "Arial" bằng font bạn muốn: "Roboto", "Montserrat", etc.
```

## 📝 Ghi Chú Quan Trọng

### Emoji Support
- Emoji hoạt động tốt trên Windows 10+, macOS, Linux hiện đại
- Nếu không hiển thị: Cài đặt font "Segoe UI Emoji" (Windows) hoặc "Noto Color Emoji" (Linux)

### High DPI Support
- FlatLaf tự động xử lý scaling
- Nếu không dùng FlatLaf: Thêm VM option `-Dsun.java2d.uiScale=1.5` (hoặc 2.0)

### Performance Tips
- Load images một lần và cache
- Sử dụng SwingWorker cho operations nặng
- Avoid updating UI on database operations thread

## 🔧 Troubleshooting

### UI không hiển thị đúng
- Kiểm tra Look & Feel đã được set
- Verify fonts có sẵn trên hệ thống
- Check console cho warnings/errors

### Images không load
- Kiểm tra đường dẫn file
- Verify resources folder trong classpath
- Check file permissions

### Màu sắc không nhất quán
- Đảm bảo dùng UIStyles thay vì hardcode colors
- Call `setOpaque(false)` cho transparent panels
- Check parent panel background colors

## 📞 Support & Resources

- **Java Swing Tutorial:** https://docs.oracle.com/javase/tutorial/uiswing/
- **FlatLaf GitHub:** https://github.com/JFormDesigner/FlatLaf
- **Material Design Colors:** https://materialui.co/colors
- **Icon Resources:**
  - https://emojipedia.org/ (Emoji)
  - https://material.io/resources/icons/ (Material Icons)
  - https://fontawesome.com/ (Font Awesome)

## 🎬 Next Steps - Cải Tiến Tiếp Theo

1. **PhimFrame Enhancement:**
   - Grid view với movie posters
   - Search và filter nâng cao
   - Preview poster khi hover

2. **Dashboard/Overview:**
   - Thống kê doanh thu
   - Top phim bán chạy
   - Biểu đồ xu hướng

3. **Print Support:**
   - In vé với barcode/QR code
   - Template hóa đơn đẹp

4. **Advanced Features:**
   - Notification system
   - Email confirmation
   - Online payment integration

---

**Created by:** Claude AI Assistant
**Date:** 2025-11-08
**Version:** 1.0
