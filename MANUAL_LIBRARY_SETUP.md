# Hướng Dẫn Cài Đặt Thư Viện Thủ Công (IntelliJ IDEA)

Hướng dẫn này giúp bạn thêm các thư viện hiện đại vào dự án **KHÔNG SỬ DỤNG Maven/Gradle**.

---

## Các Thư Viện Cần Thiết

### 1. **FlatLaf** - Modern Look & Feel (TÙY CHỌN)
- **Mô tả**: Giao diện hiện đại, chuyên nghiệp cho Swing
- **Website**: https://www.formdev.com/flatlaf/
- **Download JAR**:
  - [flatlaf-3.2.5.jar](https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2.5/flatlaf-3.2.5.jar)
  - [flatlaf-extras-3.2.5.jar](https://repo1.maven.org/maven2/com/formdev/flatlaf-extras/3.2.5/flatlaf-extras-3.2.5.jar)

### 2. **JCalendar** - Date Picker (TÙY CHỌN)
- **Mô tả**: Component chọn ngày trực quan
- **Website**: https://toedter.com/jcalendar/
- **Download JAR**:
  - [jcalendar-1.4.jar](https://repo1.maven.org/maven2/com/toedter/jcalendar/1.4/jcalendar-1.4.jar)

### 3. **SwingX** - Advanced Components (TÙY CHỌN)
- **Mô tả**: Các component Swing nâng cao
- **Download JAR**:
  - [swingx-all-1.6.5-1.jar](https://repo1.maven.org/maven2/org/swinglabs/swingx/swingx-all/1.6.5-1/swingx-all-1.6.5-1.jar)

### 4. **MigLayout** - Better Layout Manager (TÙY CHỌN)
- **Mô tả**: Layout manager hiện đại, dễ sử dụng
- **Website**: http://www.miglayout.com/
- **Download JAR**:
  - [miglayout-swing-11.3.jar](https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar)

### 5. **Apache POI** - Excel Export (TÙY CHỌN)
- **Mô tả**: Xuất dữ liệu ra Excel (.xlsx)
- **Website**: https://poi.apache.org/
- **Download JAR** (CẦN TẤT CẢ):
  - [poi-5.2.5.jar](https://repo1.maven.org/maven2/org/apache/poi/poi/5.2.5/poi-5.2.5.jar)
  - [poi-ooxml-5.2.5.jar](https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml/5.2.5/poi-ooxml-5.2.5.jar)
  - [poi-ooxml-schemas-5.2.5.jar](https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml-schemas/5.2.5/poi-ooxml-schemas-5.2.5.jar)
  - [xmlbeans-5.1.1.jar](https://repo1.maven.org/maven2/org/apache/xmlbeans/xmlbeans/5.1.1/xmlbeans-5.1.1.jar)
  - [commons-compress-1.24.0.jar](https://repo1.maven.org/maven2/org/apache/commons/commons-compress/1.24.0/commons-compress-1.24.0.jar)
  - [commons-collections4-4.4.jar](https://repo1.maven.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar)
  - [commons-io-2.14.0.jar](https://repo1.maven.org/maven2/commons-io/commons-io/2.14.0/commons-io-2.14.0.jar)

### 6. **SLF4J** - Logging (Required by POI)
- **Download JAR**:
  - [slf4j-api-2.0.9.jar](https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar)
  - [slf4j-simple-2.0.9.jar](https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar)

### 7. **SQL Server JDBC Driver** - BẮT BUỘC
- **Mô tả**: Kết nối SQL Server
- **Download JAR**:
  - [mssql-jdbc-12.4.2.jre11.jar](https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/12.4.2.jre11/mssql-jdbc-12.4.2.jre11.jar)

---

## Cách Thêm Thư Viện Vào IntelliJ IDEA

### Bước 1: Tạo Thư Mục `lib`
```bash
# Tại thư mục gốc dự án
mkdir lib
```

### Bước 2: Download Tất Cả JAR Files
1. Click vào từng link trên để download JAR
2. Lưu tất cả vào thư mục `lib/`

### Bước 3: Thêm Vào Project Structure

#### Cách 1: Sử dụng GUI
1. **File** → **Project Structure** (`Ctrl + Alt + Shift + S`)
2. Chọn **Libraries** (bên trái)
3. Click **+** → **Java**
4. Chọn thư mục `lib/` (hoặc chọn từng JAR file)
5. Click **OK**

#### Cách 2: Drag & Drop
1. Mở Project View (`Alt + 1`)
2. Kéo tất cả JAR files vào thư mục `lib/`
3. Right-click vào thư mục `lib/`
4. Chọn **Add as Library...**
5. Click **OK**

### Bước 4: Verify
Kiểm tra trong Project Structure → Libraries, bạn sẽ thấy:
- ✓ flatlaf-3.2.5
- ✓ jcalendar-1.4
- ✓ swingx-all-1.6.5-1
- ✓ miglayout-swing-11.3
- ✓ poi-5.2.5
- ✓ poi-ooxml-5.2.5
- ✓ ... (các dependencies khác)
- ✓ mssql-jdbc-12.4.2.jre11

---

## Script Download Nhanh (Windows)

Tạo file `download-libs.bat`:

```batch
@echo off
mkdir lib
cd lib

echo Downloading FlatLaf...
curl -L -o flatlaf-3.2.5.jar "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2.5/flatlaf-3.2.5.jar"
curl -L -o flatlaf-extras-3.2.5.jar "https://repo1.maven.org/maven2/com/formdev/flatlaf-extras/3.2.5/flatlaf-extras-3.2.5.jar"

echo Downloading JCalendar...
curl -L -o jcalendar-1.4.jar "https://repo1.maven.org/maven2/com/toedter/jcalendar/1.4/jcalendar-1.4.jar"

echo Downloading SwingX...
curl -L -o swingx-all-1.6.5-1.jar "https://repo1.maven.org/maven2/org/swinglabs/swingx/swingx-all/1.6.5-1/swingx-all-1.6.5-1.jar"

echo Downloading MigLayout...
curl -L -o miglayout-swing-11.3.jar "https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar"

echo Downloading Apache POI...
curl -L -o poi-5.2.5.jar "https://repo1.maven.org/maven2/org/apache/poi/poi/5.2.5/poi-5.2.5.jar"
curl -L -o poi-ooxml-5.2.5.jar "https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml/5.2.5/poi-ooxml-5.2.5.jar"
curl -L -o poi-ooxml-schemas-5.2.5.jar "https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml-schemas/5.2.5/poi-ooxml-schemas-5.2.5.jar"
curl -L -o xmlbeans-5.1.1.jar "https://repo1.maven.org/maven2/org/apache/xmlbeans/xmlbeans/5.1.1/xmlbeans-5.1.1.jar"
curl -L -o commons-compress-1.24.0.jar "https://repo1.maven.org/maven2/org/apache/commons/commons-compress/1.24.0/commons-compress-1.24.0.jar"
curl -L -o commons-collections4-4.4.jar "https://repo1.maven.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar"
curl -L -o commons-io-2.14.0.jar "https://repo1.maven.org/maven2/commons-io/commons-io/2.14.0/commons-io-2.14.0.jar"

echo Downloading SLF4J...
curl -L -o slf4j-api-2.0.9.jar "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar"
curl -L -o slf4j-simple-2.0.9.jar "https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar"

echo Downloading SQL Server JDBC...
curl -L -o mssql-jdbc-12.4.2.jre11.jar "https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/12.4.2.jre11/mssql-jdbc-12.4.2.jre11.jar"

echo Done! All libraries downloaded to lib/
cd ..
pause
```

### Script Download Nhanh (Linux/Mac)

Tạo file `download-libs.sh`:

```bash
#!/bin/bash

mkdir -p lib
cd lib

echo "Downloading FlatLaf..."
wget https://repo1.maven.org/maven2/com/formdev/flatlaf/3.2.5/flatlaf-3.2.5.jar
wget https://repo1.maven.org/maven2/com/formdev/flatlaf-extras/3.2.5/flatlaf-extras-3.2.5.jar

echo "Downloading JCalendar..."
wget https://repo1.maven.org/maven2/com/toedter/jcalendar/1.4/jcalendar-1.4.jar

echo "Downloading SwingX..."
wget https://repo1.maven.org/maven2/org/swinglabs/swingx/swingx-all/1.6.5-1/swingx-all-1.6.5-1.jar

echo "Downloading MigLayout..."
wget https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar

echo "Downloading Apache POI..."
wget https://repo1.maven.org/maven2/org/apache/poi/poi/5.2.5/poi-5.2.5.jar
wget https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml/5.2.5/poi-ooxml-5.2.5.jar
wget https://repo1.maven.org/maven2/org/apache/poi/poi-ooxml-schemas/5.2.5/poi-ooxml-schemas-5.2.5.jar
wget https://repo1.maven.org/maven2/org/apache/xmlbeans/xmlbeans/5.1.1/xmlbeans-5.1.1.jar
wget https://repo1.maven.org/maven2/org/apache/commons/commons-compress/1.24.0/commons-compress-1.24.0.jar
wget https://repo1.maven.org/maven2/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar
wget https://repo1.maven.org/maven2/commons-io/commons-io/2.14.0/commons-io-2.14.0.jar

echo "Downloading SLF4J..."
wget https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar
wget https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar

echo "Downloading SQL Server JDBC..."
wget https://repo1.maven.org/maven2/com/microsoft/sqlserver/mssql-jdbc/12.4.2.jre11/mssql-jdbc-12.4.2.jre11.jar

echo "Done! All libraries downloaded to lib/"
cd ..
```

Chạy script:
```bash
chmod +x download-libs.sh
./download-libs.sh
```

---

## Tính Năng Hoạt Động Với/Không Có Thư Viện

### ✅ Hoạt Động KHÔNG CẦN Thư Viện Ngoài (Chỉ cần SQL Server JDBC):
- Quản lý Phim, Phòng Chiếu, Ghế, Lịch Chiếu
- Quản lý Khách Hàng, Hóa Đơn, Vé
- Đặt vé với sơ đồ ghế
- Thống kê Dashboard với biểu đồ custom
- Icons vector tự vẽ (IconFactory)
- Animation effects (AnimationUtils)
- Giao diện cơ bản (Swing mặc định)

### 🎨 Nâng Cao VỚI Thư Viện (TÙY CHỌN):
- **FlatLaf**: Giao diện hiện đại, đẹp mắt
- **JCalendar**: Chọn ngày trực quan (fallback: text field)
- **MigLayout**: Code layout ngắn gọn hơn
- **SwingX**: Components nâng cao
- **Apache POI**: Xuất Excel với formatting

---

## Lưu Ý Quan Trọng

### Auto-Detection (Tự Động Phát Hiện)
Code đã được thiết kế để **tự động phát hiện** thư viện:

```java
// MainFrame.java - Tự động dùng FlatLaf nếu có
try {
    Class<?> flatLafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
    UIManager.setLookAndFeel(...);
} catch (ClassNotFoundException e) {
    // Fallback to system Look & Feel
}
```

```java
// LichChieuFrameModern.java - Tự động dùng JCalendar nếu có
try {
    Class<?> jDateChooserClass = Class.forName("com.toedter.calendar.JDateChooser");
    dateChooser = ...; // Use JCalendar
} catch (ClassNotFoundException e) {
    txtNgayChieuFallback = new JTextField(); // Fallback to text field
}
```

### Thư Viện BẮT BUỘC
- **mssql-jdbc-12.4.2.jre11.jar** - KHÔNG THỂ thiếu, cần để kết nối SQL Server

### Thư Viện TÙY CHỌN
Tất cả thư viện khác đều **TÙY CHỌN**. Ứng dụng vẫn chạy được nhưng:
- Không có FlatLaf → Giao diện mặc định
- Không có JCalendar → Nhập ngày bằng text field
- Không có MigLayout → Vẫn có layout khác
- Không có Apache POI → Không xuất Excel được

---

## Kiểm Tra Thư Viện Đã Được Thêm

Chạy chương trình và xem console:

```
✓ FlatLaf initialized successfully!
✓ JCalendar detected - using visual date picker
✓ MigLayout detected
✓ Apache POI detected - Excel export available
```

Hoặc:

```
ℹ FlatLaf not found. Using system Look & Feel.
ℹ JCalendar not found. Using text field for date input.
```

---

## Troubleshooting

### Lỗi "ClassNotFoundException"
→ Thư viện chưa được thêm vào Project Libraries

### Lỗi "NoClassDefFoundError"
→ Thiếu dependency JARs (ví dụ: POI cần xmlbeans, commons-compress, etc.)

### Lỗi "UnsupportedClassVersionError"
→ JAR file được build với Java version cao hơn JDK hiện tại
→ Đảm bảo dùng Java 11 trở lên

---

## Kết Luận

- **Cách này KHÔNG dùng Maven/Gradle**
- Tất cả thư viện đều optional (trừ SQL Server JDBC)
- Code tự động phát hiện và fallback
- Download scripts giúp tải nhanh tất cả JARs
- Dễ dàng quản lý và update từng thư viện riêng lẻ

**Chúc bạn setup thành công! 🎉**
