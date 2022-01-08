package bank;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/lab3?serverTimezone=GMT%2B8";

    // 数据库的用户名与密码，需要根据自己的设置
    private String username = null;
    private String password = null;

    //private int id;
    public Client(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("登陆中...");
            conn = DriverManager.getConnection(DB_URL, username, password);
            /*String sql;
            sql = "SELECT id FROM user_info where username = '"+username+"'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                id  = rs.getInt("id");
            }*/
            conn.close();
            System.out.println("登陆成功");
            java.util.Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output.write((current_time + " client : " + username + " login success\n").getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("登陆失败");
            return false;
        } catch (IOException e) {
            System.out.println("写入失败");
            return false;
        }finally {
            // 关闭资源
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void lookup() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, username, password);

            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT currency FROM user_info where username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int currency = rs.getInt("currency");

                System.out.println("username" + username + " currency:" + currency);
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void dealBills() {
        System.out.println("存款（1）/取款（2）");
        Scanner in = new Scanner(System.in);
        String ack = in.nextLine();
        int change_money = 0;
        if (ack.equals("1")) {
            System.out.println("存款数额：");
            change_money = in.nextInt();
        } else if (ack.equals("2")) {
            System.out.println("取款数额：");
            change_money = in.nextInt();
            change_money = -change_money;
        } else {
            System.out.println("输入错误");
            return;
        }

        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, username, password);

            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "insert into bill_info(username, change_money) values(" +
                    "'" + username + "', '" + change_money + "')";
            if (stmt.executeUpdate(sql) > 0) {
                System.out.println("create bill success");
                stmt.close();
                conn.close();
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String current_time = dateFormat.format(now);
                try {
                    Log.output.write((current_time + ":" + username + " create bill change_money = " + change_money + "\n").getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    System.out.println("写入失败");
                }
            } else {
                System.out.println("create bill failed");
            }
            // 完成后关闭
            stmt.close();
            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }

}
