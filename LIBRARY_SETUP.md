# 📚 Hướng Dẫn Cài Đặt Thư Viện Nâng Cao

## 🎨 Thư Viện Được Khuyến Nghị

### 1. FlatLaf - Modern Look & Feel ⭐⭐⭐⭐⭐

**Mô tả:** Look and Feel hiện đại nhất cho Java Swing, làm giao diện trông như native macOS/Windows 11

**Cài đặt trong IntelliJ IDEA:**

1. **File → Project Structure → Libraries**
2. **Click "+" → From Maven**
3. **Nhập:** `com.formdev:flatlaf:3.2.5`
4. **Click OK**

**Sử dụng:**

Mở `src/ui/MainFrame.java`, thêm vào `main()` method:

```java
public static void main(String[] args) {
    try {
        // FlatLaf Light Theme
        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());

        // Hoặc Dark Theme
        // UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());

        // Hoặc IntelliJ Theme
        // UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
    } catch (Exception ex) {
        System.err.println("Failed to initialize FlatLaf");
    }

    SwingUtilities.invokeLater(() -> {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    });
}
```

**Kết quả:**
- ✅ Giao diện flat, hiện đại
- ✅ Font rendering đẹp
- ✅ Button, TextField, ComboBox mượt mà
- ✅ Hỗ trợ high DPI
- ✅ Native menu bar trên macOS

---

### 2. JCalendar - Date Picker Component ⭐⭐⭐⭐

**Mô tả:** Component chọn ngày đẹp, dễ dùng

**Cài đặt:**

1. **File → Project Structure → Libraries**
2. **Click "+" → From Maven**
3. **Nhập:** `com.toedter:jcalendar:1.4`
4. **Click OK**

**Sử dụng:**

```java
import com.toedter.calendar.JDateChooser;

// Trong form
JDateChooser dateChooser = new JDateChooser();
dateChooser.setDateFormatString("dd/MM/yyyy");
dateChooser.setFont(UIStyles.FONT_NORMAL);

// Lấy ngày được chọn
Date selectedDate = dateChooser.getDate();
```

**Tính năng:**
- ✅ Calendar popup đẹp
- ✅ Format ngày tùy chỉnh
- ✅ Min/Max date validation
- ✅ Tích hợp dễ dàng

---

### 3. SwingX - Extended Swing Components ⭐⭐⭐⭐

**Mô tả:** Bộ components Swing mở rộng với nhiều tính năng

**Cài đặt:**

1. **File → Project Structure → Libraries**
2. **Click "+" → From Maven**
3. **Nhập:** `org.swinglabs.swingx:swingx-all:1.6.5-1`
4. **Click OK**

**Components hữu ích:**

**JXTable - Advanced Table:**
```java
import org.jdesktop.swingx.JXTable;

JXTable table = new JXTable();
table.setColumnControlVisible(true); // Show/hide columns
table.setHorizontalScrollEnabled(true);
table.packAll(); // Auto-size columns
table.setHighlighters(
    HighlighterFactory.createSimpleStriping(UIStyles.BG_DARK)
);
```

**JXDatePicker - Date Picker:**
```java
import org.jdesktop.swingx.JXDatePicker;

JXDatePicker datePicker = new JXDatePicker();
datePicker.setFormats("dd/MM/yyyy");
Date date = datePicker.getDate();
```

**JXSearchField - Search Field:**
```java
import org.jdesktop.swingx.JXSearchField;

JXSearchField searchField = new JXSearchField("Tìm kiếm...");
searchField.addActionListener(e -> {
    String query = searchField.getText();
    // Search logic
});
```

**JXBusyLabel - Loading Indicator:**
```java
import org.jdesktop.swingx.JXBusyLabel;

JXBusyLabel busyLabel = new JXBusyLabel();
busyLabel.setBusy(true); // Show spinner
busyLabel.setBusy(false); // Hide spinner
```

**Tính năng:**
- ✅ JXTable: Sorting, filtering, column hiding
- ✅ JXDatePicker: Date selection
- ✅ JXSearchField: Search với clear button
- ✅ JXBusyLabel: Loading spinner
- ✅ JXImagePanel: Image display
- ✅ Highlighters: Row striping, selection

---

### 4. MigLayout - Advanced Layout Manager ⭐⭐⭐⭐⭐

**Mô tả:** Layout manager mạnh mẽ nhất cho Swing

**Cài đặt:**

1. **File → Project Structure → Libraries**
2. **Click "+" → From Maven**
3. **Nhập:** `com.miglayout:miglayout-swing:11.3`
4. **Click OK**

**Sử dụng:**

```java
import net.miginfocom.swing.MigLayout;

JPanel panel = new JPanel(new MigLayout(
    "fillx, insets 15", // Layout constraints
    "[right]10[grow,fill]", // Column constraints
    "[]10[]10[]" // Row constraints
));

// Add components với constraints đơn giản
panel.add(new JLabel("Tên:"));
panel.add(txtTen, "wrap"); // wrap = new line

panel.add(new JLabel("Email:"));
panel.add(txtEmail, "wrap");

panel.add(btnSave, "skip,split 2"); // skip 1 cell, split into 2
panel.add(btnCancel);
```

**Ưu điểm:**
- ✅ Code ngắn gọn hơn GridBagLayout 70%
- ✅ Constraints dễ đọc
- ✅ Responsive tự động
- ✅ Dễ maintain

---

### 5. Animated Transitions (Custom) ⭐⭐⭐⭐

**Tạo file:** `src/ui/AnimationUtils.java`

```java
package ui;

import javax.swing.*;
import java.awt.*;

public class AnimationUtils {

    /**
     * Fade in animation
     */
    public static void fadeIn(Component component, int duration) {
        component.setVisible(true);

        Timer timer = new Timer(20, null);
        final float[] opacity = {0f};

        timer.addActionListener(e -> {
            opacity[0] += 0.05f;
            if (opacity[0] >= 1f) {
                opacity[0] = 1f;
                timer.stop();
            }

            if (component instanceof JComponent) {
                ((JComponent) component).setOpaque(opacity[0] >= 1f);
            }
            component.repaint();
        });

        timer.start();
    }

    /**
     * Slide in from right
     */
    public static void slideInFromRight(JComponent component, int duration) {
        final int targetX = component.getX();
        final int startX = component.getParent().getWidth();

        component.setLocation(startX, component.getY());
        component.setVisible(true);

        Timer timer = new Timer(10, null);
        final int[] currentX = {startX};
        final int step = (startX - targetX) / (duration / 10);

        timer.addActionListener(e -> {
            currentX[0] -= step;
            if (currentX[0] <= targetX) {
                currentX[0] = targetX;
                timer.stop();
            }
            component.setLocation(currentX[0], component.getY());
        });

        timer.start();
    }

    /**
     * Smooth scroll to component
     */
    public static void smoothScrollTo(JScrollPane scrollPane, int targetY, int duration) {
        final int startY = scrollPane.getVerticalScrollBar().getValue();
        final int distance = targetY - startY;

        Timer timer = new Timer(10, null);
        final long[] startTime = {System.currentTimeMillis()};

        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime[0];
            float progress = Math.min(1f, (float) elapsed / duration);

            // Ease out cubic
            progress = 1 - (float) Math.pow(1 - progress, 3);

            int currentY = startY + (int) (distance * progress);
            scrollPane.getVerticalScrollBar().setValue(currentY);

            if (progress >= 1f) {
                timer.stop();
            }
        });

        timer.start();
    }
}
```

---

### 6. Apache POI - Excel Export ⭐⭐⭐

**Mô tả:** Export báo cáo ra Excel

**Cài đặt:**

```
org.apache.poi:poi:5.2.5
org.apache.poi:poi-ooxml:5.2.5
```

**Sử dụng:**

```java
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public void exportToExcel(List<Phim> phims) {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Danh Sách Phim");

    // Header
    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("Mã Phim");
    headerRow.createCell(1).setCellValue("Tên Phim");
    headerRow.createCell(2).setCellValue("Thể Loại");

    // Data
    int rowNum = 1;
    for (Phim phim : phims) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(phim.getMaPhim());
        row.createCell(1).setCellValue(phim.getTenPhim());
        row.createCell(2).setCellValue(phim.getTheLoai());
    }

    // Save
    try (FileOutputStream fos = new FileOutputStream("phim.xlsx")) {
        workbook.write(fos);
    }
}
```

---

## 🚀 Quick Setup All Libraries

Tạo file `pom.xml` trong project root (nếu dùng Maven):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.cinema</groupId>
    <artifactId>RapChieuPhim</artifactId>
    <version>1.0</version>

    <dependencies>
        <!-- FlatLaf -->
        <dependency>
            <groupId>com.formdev</groupId>
            <artifactId>flatlaf</artifactId>
            <version>3.2.5</version>
        </dependency>

        <!-- JCalendar -->
        <dependency>
            <groupId>com.toedter</groupId>
            <artifactId>jcalendar</artifactId>
            <version>1.4</version>
        </dependency>

        <!-- SwingX -->
        <dependency>
            <groupId>org.swinglabs.swingx</groupId>
            <artifactId>swingx-all</artifactId>
            <version>1.6.5-1</version>
        </dependency>

        <!-- MigLayout -->
        <dependency>
            <groupId>com.miglayout</groupId>
            <artifactId>miglayout-swing</artifactId>
            <version>11.3</version>
        </dependency>

        <!-- SQL Server JDBC -->
        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <version>12.4.2.jre11</version>
        </dependency>

        <!-- Apache POI (Optional - for Excel export) -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>5.2.5</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>5.2.5</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 🎨 Áp Dụng Vào Project

### 1. Enable FlatLaf

File: `src/ui/MainFrame.java`

```java
public static void main(String[] args) {
    try {
        // Enable FlatLaf
        FlatLightLaf.setup();

        // Customize
        UIManager.put("Button.arc", 8); // Rounded buttons
        UIManager.put("Component.arc", 8); // Rounded components
        UIManager.put("TextComponent.arc", 8); // Rounded text fields
        UIManager.put("ScrollBar.thumbArc", 999); // Round scrollbar
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));

    } catch (Exception ex) {
        System.err.println("FlatLaf initialization failed");
    }

    SwingUtilities.invokeLater(() -> {
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    });
}
```

### 2. Use Advanced Table

File: `src/ui/PhimFrameModern.java`

```java
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

JXTable table = new JXTable(tableModel);
table.setColumnControlVisible(true);
table.packAll();
table.setHighlighters(
    HighlighterFactory.createSimpleStriping(UIStyles.BG_DARK)
);
```

### 3. Use Date Picker

File: `src/ui/LichChieuFrame.java`

```java
import com.toedter.calendar.JDateChooser;

JDateChooser dateChooser = new JDateChooser();
dateChooser.setDateFormatString("dd/MM/yyyy");
dateChooser.setMinSelectableDate(new Date()); // Cannot select past dates

// Get selected date
Date date = dateChooser.getDate();
```

---

## 📊 Performance Tips

1. **Lazy Loading:** Load data only when needed
2. **Virtual Scrolling:** For large tables
3. **Background Tasks:** Use SwingWorker for database operations
4. **Image Caching:** Cache loaded movie posters

---

## 🎯 Best Practices

1. **Consistent Spacing:** Use same margins everywhere (15px)
2. **Color Palette:** Stick to UIStyles colors
3. **Font Hierarchy:** Title > Header > Normal > Small
4. **Icons:** Always use IconFactory for consistency
5. **Validation:** Always validate before save
6. **Feedback:** Show loading indicators for long operations

---

**Tạo bởi:** Claude AI Assistant
**Ngày:** 2025-01-08
**Version:** 2.0
