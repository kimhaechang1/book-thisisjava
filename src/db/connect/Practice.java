package thisisjava.db.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Practice {
    public static void main(String[] args) {
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/thisisjava",
                    "root",
                    "root"
            );
            System.out.println("연결 성공");
        }catch(ClassNotFoundException e){
            System.out.println("class load error");
        } catch (SQLException e) {
            System.out.println("mysql connection error");
        }
    }
}
