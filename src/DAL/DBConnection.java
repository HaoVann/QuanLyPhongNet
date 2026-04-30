package DAL; // Khai báo đúng package thuộc lớp DAL

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // 1. Cấu hình thông tin kết nối (Sửa lại cho khớp với máy của bạn)
    private static final String HOST = "localhost";
    private static final String PORT = "3307"; // Cổng mặc định của MySQL là 3306 . Do máy t bị lỗi nên t setting thành 3307

    private static final String DB_NAME = "QuanLyQuanNet";
    private static final String USERNAME = "root"; // Username mặc định của XAMPP/MySQL
    private static final String PASSWORD = "";     // Thay bằng mật khẩu của bạn (nếu dùng XAMPP thì thường để trống)

    // 2. Chuỗi kết nối có cấu hình sẵn ép kiểu tiếng Việt UTF-8
    private static final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME 
                                         + "?useUnicode=true&characterEncoding=UTF-8";

    // 3. Hàm cấp phát kết nối cho các class khác gọi đến
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Khai báo sử dụng Driver của MySQL (dành cho file .jar bản 8.x trở lên)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Thiết lập kết nối
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy thư viện MySQL Connector (chưa add file .jar vào thư mục lib/ !)");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi: Không thể kết nối đến cơ sở dữ liệu. Hãy kiểm tra lại XAMPP/MySQL đã bật chưa!");
            e.printStackTrace();
        }
        return conn;
    }

    // 4. Hàm main dùng tạm để test nhanh xem kết nối có sống không
    public static void main(String[] args) {
        Connection testConn = DBConnection.getConnection();
        if (testConn != null) {
            System.out.println("Kết nối đến Database QuanLyQuanNet thành công rực rỡ!");
        } else {
            System.out.println("Kết nối thất bại!");
        }
    }
}