package ui;

import dao.PhimDAO;
import entity.Phim;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Modern movie management frame with grid view and poster support
 */
public class PhimFrameModern extends JFrame {
    private PhimDAO phimDAO;

    private JPanel moviesGridPanel;
    private JScrollPane gridScrollPane;
    private List<MovieCard> movieCards = new ArrayList<>();
    private MovieCard selectedCard = null;

    private JTextField txtMaPhim, txtTenPhim, txtTheLoai, txtThoiLuong, txtHinhAnh, txtTimKiem;
    private JTextArea txtMoTa;
    private JLabel lblPosterPreview;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnChonAnh, btnTimKiem;
    private JComboBox<String> cboTheLoai;

    public PhimFrameModern() {
        phimDAO = PhimDAO.getInstance();

        setTitle("🎬 Quản Lý Phim - Movie Management");
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadMovies();
    }

    private void initComponents() {
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(UIStyles.BG_PRIMARY);

        // Top panel - Search and Filter
        JPanel topPanel = createSearchPanel();
        add(topPanel, BorderLayout.NORTH);

        // Main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(950);
        splitPane.setBorder(null);

        // Left - Movie Grid
        JPanel leftPanel = createMovieGridPanel();
        splitPane.setLeftComponent(leftPanel);

        // Right - Form Panel
        JPanel rightPanel = createFormPanel();
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 10, 15),
            BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, UIStyles.PRIMARY_COLOR),
                "🔍 Tìm Kiếm & Lọc",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                UIStyles.FONT_HEADER,
                UIStyles.PRIMARY_DARK
            )
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search field
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblSearch = new JLabel("Tên phim:");
        lblSearch.setFont(UIStyles.FONT_NORMAL);
        panel.add(lblSearch, gbc);

        gbc.gridx = 1; gbc.weightx = 0.5;
        txtTimKiem = new JTextField();
        UIStyles.styleTextField(txtTimKiem);
        txtTimKiem.setPreferredSize(new Dimension(300, 36));
        panel.add(txtTimKiem, gbc);

        // Genre filter
        gbc.gridx = 2; gbc.weightx = 0;
        JLabel lblTheLoai = new JLabel("Thể loại:");
        lblTheLoai.setFont(UIStyles.FONT_NORMAL);
        panel.add(lblTheLoai, gbc);

        gbc.gridx = 3; gbc.weightx = 0.3;
        cboTheLoai = new JComboBox<>(new String[]{"Tất cả", "Hành động", "Hài", "Tình cảm", "Kinh dị", "Khoa học viễn tưởng", "Hoạt hình"});
        UIStyles.styleComboBox(cboTheLoai);
        panel.add(cboTheLoai, gbc);

        // Search button
        gbc.gridx = 4; gbc.weightx = 0;
        btnTimKiem = new JButton("🔍 Tìm Kiếm");
        UIStyles.stylePrimaryButton(btnTimKiem);
        btnTimKiem.addActionListener(e -> searchMovies());
        panel.add(btnTimKiem, gbc);

        return panel;
    }

    private JPanel createMovieGridPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 10),
            BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, UIStyles.PRIMARY_COLOR),
                "🎬 Danh Sách Phim",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                UIStyles.FONT_HEADER,
                UIStyles.PRIMARY_DARK
            )
        ));

        // Movies grid with FlowLayout wrapped in ScrollPane
        moviesGridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        moviesGridPanel.setBackground(UIStyles.BG_SECONDARY);
        moviesGridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        gridScrollPane = new JScrollPane(moviesGridPanel);
        gridScrollPane.setBorder(null);
        gridScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(gridScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        UIStyles.styleCard(panel);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 10, 15, 15),
            BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, UIStyles.ACCENT_COLOR),
                "📝 Thông Tin Phim",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                UIStyles.FONT_HEADER,
                UIStyles.ACCENT_COLOR
            )
        ));

        // Poster preview at top
        JPanel posterPanel = new JPanel(new BorderLayout(5, 5));
        posterPanel.setOpaque(false);
        posterPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        lblPosterPreview = new JLabel();
        lblPosterPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblPosterPreview.setVerticalAlignment(SwingConstants.CENTER);
        lblPosterPreview.setPreferredSize(new Dimension(220, 300));
        lblPosterPreview.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        lblPosterPreview.setBackground(new Color(245, 245, 245));
        lblPosterPreview.setOpaque(true);
        lblPosterPreview.setIcon(createPlaceholderIcon(220, 300));

        posterPanel.add(lblPosterPreview, BorderLayout.CENTER);

        // Form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // ID (read-only)
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lblMa = new JLabel("Mã:");
        lblMa.setFont(UIStyles.FONT_NORMAL);
        formPanel.add(lblMa, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMaPhim = new JTextField();
        UIStyles.styleTextField(txtMaPhim);
        txtMaPhim.setEditable(false);
        txtMaPhim.setBackground(new Color(240, 240, 240));
        formPanel.add(txtMaPhim, gbc);

        // Name
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblTen = new JLabel("Tên phim:");
        lblTen.setFont(UIStyles.FONT_NORMAL);
        formPanel.add(lblTen, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTenPhim = new JTextField();
        UIStyles.styleTextField(txtTenPhim);
        formPanel.add(txtTenPhim, gbc);

        // Genre
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblTheLoai = new JLabel("Thể loại:");
        lblTheLoai.setFont(UIStyles.FONT_NORMAL);
        formPanel.add(lblTheLoai, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTheLoai = new JTextField();
        UIStyles.styleTextField(txtTheLoai);
        formPanel.add(txtTheLoai, gbc);

        // Duration
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblThoiLuong = new JLabel("Thời lượng:");
        lblThoiLuong.setFont(UIStyles.FONT_NORMAL);
        formPanel.add(lblThoiLuong, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtThoiLuong = new JTextField();
        UIStyles.styleTextField(txtThoiLuong);
        formPanel.add(txtThoiLuong, gbc);

        // Image path
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        JLabel lblHinh = new JLabel("Hình ảnh:");
        lblHinh.setFont(UIStyles.FONT_NORMAL);
        formPanel.add(lblHinh, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel imagePanel = new JPanel(new BorderLayout(5, 0));
        imagePanel.setOpaque(false);
        txtHinhAnh = new JTextField();
        UIStyles.styleTextField(txtHinhAnh);
        btnChonAnh = new JButton("📁");
        btnChonAnh.setPreferredSize(new Dimension(40, 36));
        UIStyles.styleSecondaryButton(btnChonAnh);
        btnChonAnh.addActionListener(e -> chonFileAnh());
        imagePanel.add(txtHinhAnh, BorderLayout.CENTER);
        imagePanel.add(btnChonAnh, BorderLayout.EAST);
        formPanel.add(imagePanel, gbc);

        // Description
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblMoTa = new JLabel("Mô tả:");
        lblMoTa.setFont(UIStyles.FONT_NORMAL);
        formPanel.add(lblMoTa, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        txtMoTa = new JTextArea(3, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setFont(UIStyles.FONT_NORMAL);
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setPreferredSize(new Dimension(200, 80));
        formPanel.add(scrollMoTa, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        btnThem = new JButton("➕ Thêm");
        UIStyles.styleSuccessButton(btnThem);
        btnThem.addActionListener(e -> themPhim());

        btnSua = new JButton("✏️ Sửa");
        UIStyles.stylePrimaryButton(btnSua);
        btnSua.addActionListener(e -> suaPhim());

        btnXoa = new JButton("🗑️ Xóa");
        UIStyles.styleErrorButton(btnXoa);
        btnXoa.addActionListener(e -> xoaPhim());

        btnLamMoi = new JButton("🔄 Làm Mới");
        UIStyles.styleSecondaryButton(btnLamMoi);
        btnLamMoi.addActionListener(e -> lamMoi());

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        // Layout
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(posterPanel, BorderLayout.NORTH);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadMovies() {
        moviesGridPanel.removeAll();
        movieCards.clear();

        List<Phim> danhSachPhim = phimDAO.layDanhSachPhim();
        for (Phim phim : danhSachPhim) {
            MovieCard card = new MovieCard(phim);
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectMovieCard(card);
                }
            });
            movieCards.add(card);
            moviesGridPanel.add(card);
        }

        moviesGridPanel.revalidate();
        moviesGridPanel.repaint();
    }

    private void selectMovieCard(MovieCard card) {
        if (selectedCard != null) {
            selectedCard.setSelected(false);
        }

        selectedCard = card;
        card.setSelected(true);

        // Fill form with selected movie data
        Phim phim = card.getPhim();
        txtMaPhim.setText(String.valueOf(phim.getMaPhim()));
        txtTenPhim.setText(phim.getTenPhim());
        txtTheLoai.setText(phim.getTheLoai() != null ? phim.getTheLoai() : "");
        txtThoiLuong.setText(String.valueOf(phim.getThoiLuong()));
        txtMoTa.setText(phim.getMoTa() != null ? phim.getMoTa() : "");
        txtHinhAnh.setText(phim.getHinhAnh() != null ? phim.getHinhAnh() : "");

        // Load poster preview
        loadPosterPreview(phim.getHinhAnh());
    }

    private void loadPosterPreview(String imagePath) {
        try {
            if (imagePath != null && !imagePath.isEmpty()) {
                File imgFile = new File("resources/images/movies/" + imagePath);
                if (imgFile.exists()) {
                    Image img = ImageIO.read(imgFile);
                    Image scaled = img.getScaledInstance(220, 300, Image.SCALE_SMOOTH);
                    lblPosterPreview.setIcon(new ImageIcon(scaled));
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show placeholder
        lblPosterPreview.setIcon(createPlaceholderIcon(220, 300));
    }

    private Icon createPlaceholderIcon(int width, int height) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRect(x, y, width, height);

                g2d.setColor(new Color(180, 180, 180));
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
                String icon = "🎬";
                FontMetrics fm = g2d.getFontMetrics();
                int textX = x + (width - fm.stringWidth(icon)) / 2;
                int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(icon, textX, textY);

                g2d.setColor(new Color(150, 150, 150));
                g2d.setFont(UIStyles.FONT_SMALL);
                String text = "Chưa có poster";
                fm = g2d.getFontMetrics();
                textX = x + (width - fm.stringWidth(text)) / 2;
                textY = y + height - 20;
                g2d.drawString(text, textX, textY);

                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return width;
            }

            @Override
            public int getIconHeight() {
                return height;
            }
        };
    }

    private void searchMovies() {
        String keyword = txtTimKiem.getText().trim();
        String theLoai = (String) cboTheLoai.getSelectedItem();

        moviesGridPanel.removeAll();
        movieCards.clear();

        List<Phim> danhSachPhim;
        if (keyword.isEmpty() && "Tất cả".equals(theLoai)) {
            danhSachPhim = phimDAO.layDanhSachPhim();
        } else if (!keyword.isEmpty()) {
            danhSachPhim = phimDAO.timPhimTheoTen(keyword);
        } else {
            danhSachPhim = phimDAO.timPhimTheoTheLoai(theLoai);
        }

        for (Phim phim : danhSachPhim) {
            MovieCard card = new MovieCard(phim);
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectMovieCard(card);
                }
            });
            movieCards.add(card);
            moviesGridPanel.add(card);
        }

        moviesGridPanel.revalidate();
        moviesGridPanel.repaint();
    }

    private void chonFileAnh() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            txtHinhAnh.setText(selectedFile.getName());
            loadPosterPreview(selectedFile.getName());
        }
    }

    private void themPhim() {
        try {
            String tenPhim = txtTenPhim.getText().trim();
            String theLoai = txtTheLoai.getText().trim();
            String thoiLuongStr = txtThoiLuong.getText().trim();
            String moTa = txtMoTa.getText().trim();
            String hinhAnh = txtHinhAnh.getText().trim();

            if (tenPhim.isEmpty() || thoiLuongStr.isEmpty()) {
                UIStyles.showErrorMessage(this, "Vui lòng nhập tên phim và thời lượng!");
                return;
            }

            int thoiLuong = Integer.parseInt(thoiLuongStr);

            Phim phim = new Phim();
            phim.setTenPhim(tenPhim);
            phim.setTheLoai(theLoai);
            phim.setThoiLuong(thoiLuong);
            phim.setMoTa(moTa);
            phim.setHinhAnh(hinhAnh);

            if (phimDAO.themPhim(phim)) {
                UIStyles.showSuccessMessage(this, "Thêm phim thành công!");
                loadMovies();
                lamMoi();
            } else {
                UIStyles.showErrorMessage(this, "Thêm phim thất bại!");
            }
        } catch (NumberFormatException e) {
            UIStyles.showErrorMessage(this, "Thời lượng phải là số nguyên!");
        }
    }

    private void suaPhim() {
        try {
            String maPhimStr = txtMaPhim.getText().trim();
            if (maPhimStr.isEmpty()) {
                UIStyles.showErrorMessage(this, "Vui lòng chọn phim cần sửa!");
                return;
            }

            int maPhim = Integer.parseInt(maPhimStr);
            String tenPhim = txtTenPhim.getText().trim();
            String theLoai = txtTheLoai.getText().trim();
            String thoiLuongStr = txtThoiLuong.getText().trim();
            String moTa = txtMoTa.getText().trim();
            String hinhAnh = txtHinhAnh.getText().trim();

            if (tenPhim.isEmpty() || thoiLuongStr.isEmpty()) {
                UIStyles.showErrorMessage(this, "Vui lòng nhập tên phim và thời lượng!");
                return;
            }

            int thoiLuong = Integer.parseInt(thoiLuongStr);

            Phim phim = new Phim();
            phim.setMaPhim(maPhim);
            phim.setTenPhim(tenPhim);
            phim.setTheLoai(theLoai);
            phim.setThoiLuong(thoiLuong);
            phim.setMoTa(moTa);
            phim.setHinhAnh(hinhAnh);

            if (phimDAO.capNhatPhim(phim)) {
                UIStyles.showSuccessMessage(this, "Cập nhật phim thành công!");
                loadMovies();
                lamMoi();
            } else {
                UIStyles.showErrorMessage(this, "Cập nhật phim thất bại!");
            }
        } catch (NumberFormatException e) {
            UIStyles.showErrorMessage(this, "Dữ liệu không hợp lệ!");
        }
    }

    private void xoaPhim() {
        String maPhimStr = txtMaPhim.getText().trim();
        if (maPhimStr.isEmpty()) {
            UIStyles.showErrorMessage(this, "Vui lòng chọn phim cần xóa!");
            return;
        }

        if (UIStyles.showConfirmDialog(this, "Bạn có chắc muốn xóa phim này?")) {
            int maPhim = Integer.parseInt(maPhimStr);
            if (phimDAO.xoaPhim(maPhim)) {
                UIStyles.showSuccessMessage(this, "Xóa phim thành công!");
                loadMovies();
                lamMoi();
            } else {
                UIStyles.showErrorMessage(this, "Xóa phim thất bại!");
            }
        }
    }

    private void lamMoi() {
        txtMaPhim.setText("");
        txtTenPhim.setText("");
        txtTheLoai.setText("");
        txtThoiLuong.setText("");
        txtMoTa.setText("");
        txtHinhAnh.setText("");
        txtTimKiem.setText("");
        cboTheLoai.setSelectedIndex(0);

        if (selectedCard != null) {
            selectedCard.setSelected(false);
            selectedCard = null;
        }

        lblPosterPreview.setIcon(createPlaceholderIcon(220, 300));
        loadMovies();
    }
}
