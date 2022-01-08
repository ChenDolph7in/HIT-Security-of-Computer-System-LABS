package com.example.lab4_bank;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Login {

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/lab3?serverTimezone=GMT%2B8";

    // 数据库的用户名与密码，需要根据自己的设置
    private String username;
    private String password;

    public Login(String username, String password) {
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

}
