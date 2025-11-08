package util;

/**
 * Application constants for dropdowns and validation
 */
public class Constants {

    // Movie genres
    public static final String[] THE_LOAI_PHIM = {
        "Hành động",
        "Hài",
        "Tình cảm",
        "Kinh dị",
        "Khoa học viễn tưởng",
        "Hoạt hình",
        "Phiêu lưu",
        "Tâm lý",
        "Chiến tranh",
        "Lịch sử",
        "Gia đình",
        "Âm nhạc",
        "Thể thao",
        "Tài liệu"
    };

    // Theater types
    public static final String[] LOAI_PHONG = {
        "2D",
        "3D",
        "IMAX",
        "4DX",
        "Dolby Atmos",
        "VIP"
    };

    // Seat types
    public static final String[] LOAI_GHE = {
        "Thường",
        "VIP",
        "Couple"
    };

    // Ticket status
    public static final String[] TRANG_THAI_VE = {
        "Da dat",
        "Huy"
    };

    // Invoice payment status
    public static final String[] TRANG_THAI_THANH_TOAN = {
        "Chua thanh toan",
        "Da thanh toan"
    };

    // Seat rows
    public static final String[] HANG_GHE = {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"
    };

    // Time slots for screenings
    public static final String[] GIO_CHIEU = {
        "08:00", "09:00", "10:00", "11:00", "12:00",
        "13:00", "14:00", "15:00", "16:00", "17:00",
        "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
    };

    // Price ranges (for VIP multiplier)
    public static final double VIP_PRICE_MULTIPLIER = 1.5;
    public static final double COUPLE_PRICE_MULTIPLIER = 1.3;

    // Validation patterns
    public static final String PHONE_PATTERN = "^[0-9]{10}$";
    public static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";

    // Default values
    public static final int DEFAULT_MOVIE_DURATION = 90; // minutes
    public static final double DEFAULT_TICKET_PRICE = 50000; // VND
    public static final int DEFAULT_THEATER_CAPACITY = 100;

    /**
     * Validate phone number
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches(PHONE_PATTERN);
    }

    /**
     * Validate email
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_PATTERN);
    }

    /**
     * Format currency (VND)
     */
    public static String formatCurrency(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }

    /**
     * Format duration
     */
    public static String formatDuration(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        if (hours > 0) {
            return String.format("%d giờ %d phút", hours, mins);
        } else {
            return String.format("%d phút", mins);
        }
    }
}
