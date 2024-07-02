package thisisjava.db.transaction;

import java.sql.*;

public class Practice {
    public static void close(AutoCloseable ...closeables) throws Exception {
        for(AutoCloseable closeable: closeables){
            closeable.close();
        }
    }
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/thisisjava", "root", "root");
            conn.setAutoCommit(false);


            // 출금 작업
            String sql1 = "UPDATE accounts SET balance=balance-? WHERE ano=?";
            pstmt = conn.prepareStatement(sql1);
            pstmt.setInt(1, 10000);
            pstmt.setString(2, "111-111-1111");
            int rows1 = pstmt.executeUpdate();
            if (rows1 == 0) throw new Exception("출금 되지 않았음");

            // 입금 작업
            String sql2 = "UPDATE accounts SET balance=balance+? WHERE ano=?";
            pstmt = conn.prepareStatement(sql2);
            pstmt.setInt(1, 10000);
            pstmt.setString(2, "322-222-2222");
            int rows2 = pstmt.executeUpdate();
            if (rows2 == 0) throw new Exception("입금되지 않았음");
            conn.commit();
            // 수동 커밋 -> 트랜잭션 종료
        } catch (ClassNotFoundException e) {
            System.out.println("class load error");
        }catch (Exception e) {
            System.out.println("transaction exception");
            e.printStackTrace();
            try{
                conn.rollback();
            }catch(SQLException e1){

            }
        } finally {
            try{
                close(rs, pstmt, conn);
            }catch(Exception e){
                System.out.println("close exception");
            }
        }
    }
}
