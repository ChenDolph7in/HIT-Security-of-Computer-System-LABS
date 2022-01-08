package bank;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Admin {

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/lab3?serverTimezone=GMT%2B8";

    // 数据库的用户名与密码，需要根据自己的设置
    private String username;
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean login() {
        Connection conn = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("登陆中...");
            conn = DriverManager.getConnection(DB_URL, username, password);
            conn.close();
            System.out.println("登陆成功");
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output.write((current_time + " administer : " + username + " login success\n").getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("登陆失败");
            return false;
        } catch (IOException ioe) {
            System.out.println("写入失败");
            return false;
        } finally {
            // 关闭资源
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void lookupUsers() {
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
            sql = "SELECT id, username, currency FROM user_info where valid != 0";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("id");
                String name = rs.getString("username");
                int currency = rs.getInt("currency");

                System.out.println("id:" + id + " username" + name + " currency:" + currency);
            }
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
            sql = "SELECT bill_id, username, change_money FROM bill_info";
            ResultSet rs = stmt.executeQuery(sql);
            Scanner in = new Scanner(System.in);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int bill_id = rs.getInt("bill_id");
                String username = rs.getString("username");
                int change_money = rs.getInt("change_money");

                System.out.println("bill_id:" + bill_id + " user_name:" + username + " change_money:" + change_money);
                System.out.println("confirm?(y/n)");
                String ack = in.nextLine();
                if (ack.equals("y")) {
                    sql = "SELECT currency FROM user_info where username = " + "'" + username + "'";
                    Statement stmt1 = conn.createStatement();
                    ResultSet rs1 = stmt1.executeQuery(sql);
                    if (rs1.next()) {
                        int currency = rs1.getInt("currency");
                        currency = currency + change_money;
                        sql = "update user_info set currency = '" + currency + "' where username = '" + username + "'";
                        if (stmt1.executeUpdate(sql) > 0) {
                            System.out.println("update success");
                            Date now = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            String current_time = dateFormat.format(now);
                            try {
                                Log.output.write((current_time + ":" + username + " update " + username + " 's currency:" + currency + "\n").getBytes(StandardCharsets.UTF_8));
                            } catch (IOException e) {
                                System.out.println("写入失败");
                            }
                        } else {
                            System.out.println("update failed");
                        }
                    } else {
                        System.out.println("user doesn't exist");
                    }
                    rs1.close();
                    stmt1.close();
                }

                Statement stmt2 = conn.createStatement();
                sql = "delete FROM bill_info where bill_id = '" + bill_id + "'";
                if (stmt2.executeUpdate(sql) > 0) {
                    System.out.println("delete success:" + bill_id);
                    Date now = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String current_time = dateFormat.format(now);
                    try {
                        Log.output.write((current_time + ":" + username + " delete bill id = " + bill_id + ", username = " + username + ", change_money = " + change_money + "\n").getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        System.out.println("写入失败");
                    }
                } else {
                    System.out.println("delete failed:" + bill_id);
                }

            }
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
}
