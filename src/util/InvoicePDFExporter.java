package util;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for exporting invoices to PDF-like text file
 * Since we don't have PDF libraries, we create well-formatted text files
 */
public class InvoicePDFExporter {

    /**
     * Export invoice content to a formatted text file
     * @param content The invoice content
     * @param defaultFileName Default file name
     * @param parent Parent frame for dialogs
     * @return true if export successful
     */
    public static boolean exportToFile(String content, String defaultFileName, JFrame parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Hóa Đơn");

        // Set default file name with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fileChooser.setSelectedFile(new File(defaultFileName + "_" + timestamp + ".txt"));

        // File filter for text files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Text Files (*.txt)", "txt"
        ));

        int result = fileChooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Ensure .txt extension
            if (!filePath.toLowerCase().endsWith(".txt")) {
                filePath += ".txt";
            }

            try {
                // Write to file with UTF-8 encoding
                try (BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
                    writer.write(content);
                }

                JOptionPane.showMessageDialog(parent,
                    "Lưu hóa đơn thành công!\n" + filePath,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

                // Ask if user wants to open the file
                int openChoice = JOptionPane.showConfirmDialog(parent,
                    "Bạn có muốn mở file vừa lưu không?",
                    "Mở File",
                    JOptionPane.YES_NO_OPTION);

                if (openChoice == JOptionPane.YES_OPTION) {
                    // Open file with default text editor
                    try {
                        Desktop.getDesktop().open(new File(filePath));
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(parent,
                            "Không thể mở file: " + e.getMessage(),
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE);
                    }
                }

                return true;

            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent,
                    "Lỗi khi lưu file: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    /**
     * Export invoice content and print to PDF printer if available
     * Uses Java's print functionality with PDF printer
     */
    public static void exportAndPrintToPDF(String content, String defaultFileName, JFrame parent) {
        // First save to text file
        boolean saved = exportToFile(content, defaultFileName, parent);

        if (saved) {
            // Ask if user wants to print to PDF
            int printChoice = JOptionPane.showConfirmDialog(parent,
                "Bạn có muốn in hóa đơn không?\n(Chọn máy in PDF để tạo file PDF)",
                "In Hóa Đơn",
                JOptionPane.YES_NO_OPTION);

            if (printChoice == JOptionPane.YES_OPTION) {
                // Create printable text area
                JTextArea textArea = new JTextArea(content);
                textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

                try {
                    // This will show print dialog where user can select PDF printer
                    boolean complete = textArea.print(null, null, true, null, null, true);
                    if (complete) {
                        JOptionPane.showMessageDialog(parent,
                            "In hóa đơn thành công!\nNếu bạn chọn máy in PDF, file PDF đã được tạo.",
                            "Thành Công",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (PrinterException ex) {
                    JOptionPane.showMessageDialog(parent,
                        "Lỗi khi in: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Create a well-formatted invoice header
     */
    public static String createInvoiceHeader(String companyName, String address, String phone) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append(centerText(companyName, 59)).append("\n");
        if (address != null) {
            sb.append(centerText(address, 59)).append("\n");
        }
        if (phone != null) {
            sb.append(centerText("Hotline: " + phone, 59)).append("\n");
        }
        sb.append("═══════════════════════════════════════════════════════════\n");
        return sb.toString();
    }

    /**
     * Center text within a given width
     */
    private static String centerText(String text, int width) {
        int padding = (width - text.length()) / 2;
        if (padding <= 0) return text;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        return sb.toString();
    }

    /**
     * Create a separator line
     */
    public static String createSeparator(char c, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        sb.append("\n");
        return sb.toString();
    }
}
