package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility for exporting data to Excel format
 */
public class ExcelExporter {

    /**
     * Export JTable to Excel file
     */
    public static boolean exportTable(JTable table, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");

            // Create header style
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);

            TableModel model = table.getModel();

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);

                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = dataRow.createCell(col);
                    Object value = model.getValueAt(row, col);

                    if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                        cell.setCellStyle(numberStyle);
                    } else if (value instanceof Date) {
                        cell.setCellValue((Date) value);
                        CellStyle dateStyle = workbook.createCellStyle();
                        dateStyle.setDataFormat(workbook.createDataFormat().getFormat("dd/mm/yyyy"));
                        cell.setCellStyle(dateStyle);
                    } else {
                        cell.setCellValue(value.toString());
                        cell.setCellStyle(dataStyle);
                    }
                }
            }

            // Auto-size columns
            for (int col = 0; col < model.getColumnCount(); col++) {
                sheet.autoSizeColumn(col);
                // Add some padding
                sheet.setColumnWidth(col, sheet.getColumnWidth(col) + 1000);
            }

            // Write to file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Export custom data to Excel
     */
    public static boolean exportData(String[][] data, String[] headers, String filePath, String sheetName) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            // Header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            for (int i = 0; i < data.length; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < data[i].length; j++) {
                    Cell cell = row.createCell(j);
                    cell.setCellValue(data[i][j]);
                    cell.setCellStyle(dataStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1000);
            }

            // Write to file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Create header cell style
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // Background color
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Font
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Arial");
        style.setFont(font);

        // Borders
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        // Alignment
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * Create data cell style
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // Font
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Arial");
        style.setFont(font);

        // Borders
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        // Alignment
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    /**
     * Create number cell style
     */
    private static CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    /**
     * Show save dialog and export
     */
    public static void exportTableWithDialog(JTable table, JFrame parent, String defaultFileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export to Excel");

        // Set default file name with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fileChooser.setSelectedFile(new java.io.File(defaultFileName + "_" + timestamp + ".xlsx"));

        // File filter for Excel files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Excel Files (*.xlsx)", "xlsx"
        ));

        int result = fileChooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Ensure .xlsx extension
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            // Show progress
            JDialog progressDialog = new JDialog(parent, "Đang xuất...", true);
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressDialog.add(progressBar);
            progressDialog.setSize(300, 80);
            progressDialog.setLocationRelativeTo(parent);

            final String finalPath = filePath;

            // Export in background thread
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() {
                    return exportTable(table, finalPath);
                }

                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(parent,
                                "Xuất file thành công!\n" + finalPath,
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(parent,
                                "Xuất file thất bại!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(parent,
                            "Lỗi: " + e.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
            progressDialog.setVisible(true);
        }
    }
}
