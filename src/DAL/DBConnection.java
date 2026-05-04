package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String HOST = "localhost";
    private static final String PORT = "3306"; 

    private static final String DB_NAME = "quanlyphongnet";
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "1234";     

    private static final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME 
                                         + "?useUnicode=true&characterEncoding=UTF-8";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
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

    public static void main(String[] args) {
        Connection testConn = DBConnection.getConnection();
        if (testConn != null) {
            System.out.println("Kết nối đến Database QuanLyQuanNet thành công rực rỡ!");
        } else {
            System.out.println("Kết nối thất bại!");
        }
    }
}