# Hướng Dẫn Cài Đặt MigLayout

## Bước 1: Download JAR File

### Cách 1: Download Trực Tiếp
1. Mở link này trong trình duyệt:
   ```
   https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar
   ```

2. File sẽ tự động download về máy tính

### Cách 2: Dùng Script (Windows)
Tạo file `download-miglayout.bat`:
```batch
@echo off
mkdir lib
cd lib
curl -L -o miglayout-swing-11.3.jar "https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar"
echo Downloaded successfully!
pause
```

### Cách 3: Dùng Script (Linux/Mac)
```bash
mkdir -p lib
cd lib
wget https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar
```

---

## Bước 2: Thêm Vào IntelliJ IDEA

### Cách 1: Thêm Qua Project Structure (KHUYẾN NGHỊ)

1. **Mở Project Structure**
   - Menu: `File` → `Project Structure`
   - Hoặc phím tắt: `Ctrl + Alt + Shift + S` (Windows/Linux)
   - Hoặc: `Cmd + ;` (Mac)

2. **Chọn Libraries**
   - Bên trái, click vào `Libraries`

3. **Thêm Library Mới**
   - Click nút `+` (Add)
   - Chọn `Java`

4. **Chọn JAR File**
   - Tìm đến thư mục `lib/`
   - Chọn file `miglayout-swing-11.3.jar`
   - Click `OK`

5. **Choose Modules**
   - Chọn module của dự án (thường là `RapChieuPhim`)
   - Click `OK`

6. **Apply Changes**
   - Click `Apply` → `OK`

### Cách 2: Drag & Drop (NHANH HƠN)

1. **Tạo thư mục lib/**
   ```
   RapChieuPhim/
   ├── src/
   ├── lib/          ← Tạo thư mục này
   └── ...
   ```

2. **Copy JAR vào lib/**
   - Copy file `miglayout-swing-11.3.jar` vào `lib/`

3. **Add as Library trong IntelliJ**
   - Mở Project View (`Alt + 1`)
   - Right-click vào thư mục `lib/`
   - Chọn `Add as Library...`
   - Click `OK`

### Cách 3: Module Settings

1. **Click chuột phải vào Project**
   - Right-click vào tên project trong Project View

2. **Open Module Settings**
   - Chọn `Open Module Settings` (`F4`)

3. **Dependencies Tab**
   - Chọn tab `Dependencies`

4. **Add JAR**
   - Click `+` → `JARs or directories...`
   - Chọn file `miglayout-swing-11.3.jar`
   - Click `OK`

---

## Bước 3: Verify (Kiểm Tra)

### Kiểm tra trong Project Structure
1. `File` → `Project Structure` → `Libraries`
2. Bạn sẽ thấy: `miglayout-swing-11.3`
3. Expand ra sẽ thấy file JAR

### Kiểm tra trong External Libraries
1. Mở Project View
2. Expand `External Libraries`
3. Sẽ thấy `miglayout-swing-11.3`

---

## Bước 4: Sử Dụng MigLayout

### Import MigLayout
```java
import net.miginfocom.swing.MigLayout;
```

### Ví Dụ Cơ Bản
```java
import net.miginfocom.swing.MigLayout;
import javax.swing.*;

public class ExampleFrame extends JFrame {
    public ExampleFrame() {
        // Tạo panel với MigLayout
        JPanel panel = new JPanel(new MigLayout());

        // Thêm components
        panel.add(new JLabel("Tên:"));
        panel.add(new JTextField(20), "wrap"); // "wrap" = xuống dòng

        panel.add(new JLabel("Tuổi:"));
        panel.add(new JTextField(20));

        add(panel);
        pack();
    }
}
```

### Ví Dụ Nâng Cao (Form 2 Cột)
```java
JPanel panel = new JPanel(new MigLayout(
    "fillx, insets 15",                          // Layout constraints
    "[right]15[grow,fill]30[right]15[grow,fill]", // Column constraints (4 cột)
    "[]15[]15[]"                                  // Row constraints
));

// Row 1: Label + TextField | Label + TextField
panel.add(new JLabel("Họ:"));
panel.add(new JTextField());
panel.add(new JLabel("Tên:"));
panel.add(new JTextField(), "wrap"); // wrap = xuống dòng

// Row 2
panel.add(new JLabel("Email:"));
panel.add(new JTextField());
panel.add(new JLabel("SĐT:"));
panel.add(new JTextField(), "wrap");

// Row 3: Span across columns
panel.add(new JLabel("Địa chỉ:"));
panel.add(new JTextField(), "span 3"); // span = kéo dài 3 cột
```

### MigLayout Constraints Phổ Biến

#### Layout Constraints (tham số 1)
- `fillx` - Kéo dài theo chiều ngang
- `filly` - Kéo dài theo chiều dọc
- `insets 15` - Padding 15px xung quanh
- `debug` - Hiển thị grid để debug

#### Column Constraints (tham số 2)
- `[grow]` - Cột tự mở rộng
- `[fill]` - Component chiếm hết cột
- `[right]` - Căn phải
- `[100px]` - Chiều rộng cố định 100px
- `15` - Khoảng cách giữa các cột

#### Row Constraints (tham số 3)
- `[]` - Row mặc định
- `[top]` - Căn trên
- `[center]` - Căn giữa
- `15` - Khoảng cách giữa các row

#### Component Constraints (khi add)
- `wrap` - Xuống dòng mới
- `span 2` - Kéo dài 2 cột/dòng
- `skip` - Bỏ qua 1 cell
- `growx` - Component tự mở rộng theo X
- `w 200` - Chiều rộng 200px

---

## Ví Dụ Thực Tế: Form Lịch Chiếu

```java
import net.miginfocom.swing.MigLayout;

private JPanel createFormPanel() {
    JPanel panel = new JPanel();

    // MigLayout: 4 cột (Label - Field - Label - Field)
    panel.setLayout(new MigLayout(
        "fillx, insets 15",
        "[right]15[grow,fill]30[right]15[grow,fill]",
        "[]15[]15[]15[]"
    ));

    // Row 1
    panel.add(new JLabel("Mã Lịch Chiếu:"));
    panel.add(txtMaLichChieu);
    panel.add(new JLabel("Phim:"));
    panel.add(cboPhim, "wrap");

    // Row 2
    panel.add(new JLabel("Phòng Chiếu:"));
    panel.add(cboPhongChieu);
    panel.add(new JLabel("Ngày Chiếu:"));
    panel.add(dateChooser, "wrap");

    // Row 3
    panel.add(new JLabel("Giờ Chiếu:"));
    panel.add(cboGioChieu);
    panel.add(new JLabel("Giá Vé:"));
    panel.add(txtGiaVe, "wrap");

    // Row 4: Buttons span 3 columns
    panel.add(new JLabel()); // Skip first column
    panel.add(buttonPanel, "span 3");

    return panel;
}
```

---

## So Sánh: MigLayout vs GridBagLayout

### GridBagLayout (Không cần thư viện)
```java
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(5, 5, 5, 5);
gbc.fill = GridBagConstraints.HORIZONTAL;

gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
panel.add(label1, gbc);

gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
panel.add(textField1, gbc);

gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
panel.add(label2, gbc);
// ... 15+ dòng code cho form đơn giản
```

### MigLayout (Cần thư viện)
```java
panel.setLayout(new MigLayout(
    "fillx, insets 5",
    "[right][grow,fill]"
));

panel.add(label1);
panel.add(textField1, "wrap");
panel.add(label2);
panel.add(textField2, "wrap");
// Chỉ 6 dòng, dễ đọc hơn 70%!
```

---

## Troubleshooting

### Lỗi: "cannot access net.miginfocom.layout.LC"
**Nguyên nhân:** Chưa add JAR vào project
**Giải pháp:** Làm lại Bước 2

### Lỗi: "package net.miginfocom.swing does not exist"
**Nguyên nhân:** Sai JAR file hoặc chưa sync
**Giải pháp:**
- File → Invalidate Caches → Invalidate and Restart
- Hoặc Build → Rebuild Project

### Lỗi: "ClassNotFoundException: MigLayout"
**Nguyên nhân:** Runtime không tìm thấy JAR
**Giải pháp:**
- Đảm bảo JAR trong Dependencies
- Khi export JAR, include MigLayout

---

## Kết Luận

### Ưu Điểm MigLayout
- ✅ Code ngắn gọn, dễ đọc
- ✅ Linh hoạt, mạnh mẽ
- ✅ Hỗ trợ responsive layout
- ✅ Ít bug hơn GridBagLayout
- ✅ Có thể mix với other layouts

### Nhược Điểm
- ❌ Cần thêm external library (1 JAR file)
- ❌ Learning curve với constraints syntax
- ❌ Khi deploy phải đảm bảo JAR đi kèm

### Khi Nào Dùng MigLayout?
- ✅ Form phức tạp nhiều fields
- ✅ Layout cần responsive
- ✅ Muốn code ngắn gọn, maintainable
- ✅ Chấp nhận thêm 1 dependency

### Khi Nào KHÔNG Dùng?
- ❌ Form đơn giản (dùng GridBagLayout)
- ❌ Không muốn external dependencies
- ❌ Deploy environment hạn chế
- ❌ Team chưa quen với MigLayout syntax

---

**LƯU Ý:** Trong project này, code đã được viết để **TỰ ĐỘNG PHÁT HIỆN** MigLayout:
- Nếu có MigLayout → Dùng layout hiện đại
- Nếu không có → Fallback to GridBagLayout
- Vậy nên bạn có thể KHÔNG cần cài MigLayout, app vẫn chạy bình thường!
