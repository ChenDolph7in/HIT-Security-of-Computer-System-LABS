package bank;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Admin {

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/lab4?serverTimezone=GMT%2B8";

    // 数据库的用户名与密码
    public static String username;
    public static String password;

    public Admin(String username, String password) {
        Admin.username = username;
        Admin.password = password;
    }

    public static boolean login() {
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("登陆中...");
            conn = DriverManager.getConnection(DB_URL, username, password);

            // 检测是否为主管,是则退出
            Boolean flag = false;
            try{
                // 查询admin
                stmt = conn.createStatement();
                String sql = "SELECT id, username FROM admin_info";
                ResultSet rs = stmt.executeQuery(sql);
                rs.close();
                loginError();
                flag = true;
                System.out.println("登陆失败");
            }catch (Exception e){
                System.out.println("登陆成功");
                menu();
            }
            if(flag){
                return false;
            }

            java.util.Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output.write((current_time + ":(administer)" + username + " login success\n").getBytes(StandardCharsets.UTF_8));
            conn.close();
            stmt.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("登陆失败");
            loginError();
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

    private static void menu() {

        JFrame jf = new JFrame("Administer");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 选项按钮
        JButton lookupButton = new JButton("查看余额");
        JButton billButton = new JButton("处理账单");

        // 设置标签和按钮的大小和位置
        lookupButton.setBounds(210, 80, 165, 30);
        billButton.setBounds(210, 120, 165, 30);

        // 加入面板中
        panel.add(lookupButton);
        panel.add(billButton);

        // 设置 查看余额 按钮监听事件
        lookupButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到存款页面
                lookupUsers();
            }
        });

        // 设置 处理账单 按钮监听事件
        billButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到取款页面
                dealBills();
            }
        });

        // 将面板加入窗口中
        jf.add(panel);
        // 设置窗口可见
        jf.setVisible(true);

    }

    private static void lookupUsers() {
        Connection conn = null;
        Statement stmt = null;
        String result = "";
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, username, password);

            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, username, currency FROM user_info WHERE valid != 0";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("id");
                String name = rs.getString("username");
                int currency = rs.getInt("currency");

                result = result + "id:" + id + " username:" + name + " currency:" + currency + "\n";
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
        JFrame jf = new JFrame("余额");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton insureButton = new JButton("确定");
        JTextArea jt = new JTextArea(result, 16, 16);

        jt.setBounds(160, 50, 250, 120);
        insureButton.setBounds(230, 280, 80, 30);

        panel.add(jt);
        panel.add(insureButton);

        //监听确认按钮
        insureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                menu();
            }
        });

        jf.add(panel);
        jf.setVisible(true);
    }

    private static void dealBills() {
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("连接数据库...");
            final Connection conn = DriverManager.getConnection(DB_URL, username, password);
            Statement stmt = conn.createStatement();

            // 执行查询
            String sql;
            sql = "SELECT bill_id, username, change_money FROM bill_info";
            ResultSet rs = stmt.executeQuery(sql);
            Scanner in = new Scanner(System.in);
            // 展开结果集数据库
            if (rs.next()) {
                // 通过字段检索
                int bill_id = rs.getInt("bill_id");
                String username = rs.getString("username");
                int change_money = rs.getInt("change_money");

                JFrame jf = new JFrame("administer");
                jf.setSize(600, 400);
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.setLocationRelativeTo(null);

                // 创建面板
                JPanel panel = new JPanel();
                panel.setLayout(null);    // 面板布局

                // 创建 标签 和 按钮
                JLabel billMessage;
                if (change_money < 0) {
                    billMessage = new JLabel("账单：" + "用户 " + username + " 取出 " + Math.abs(change_money) + " 金额");
                } else {
                    billMessage = new JLabel("账单：" + "用户 " + username + " 存储 " + change_money + " 金额");
                }
                JButton agreeButton = new JButton("同意");
                JButton disagreeButton = new JButton("不同意");

                // 设置标签和按钮的大小和位置
                billMessage.setBounds(200, 80, 300, 30);
                agreeButton.setBounds(200, 120, 150, 30);
                disagreeButton.setBounds(200, 160, 150, 30);

                // 加入面板中
                panel.add(billMessage);
                panel.add(agreeButton);
                panel.add(disagreeButton);

                jf.add(panel);
                jf.setVisible(true);

                agreeButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        String sql = "SELECT currency FROM user_info WHERE username = " + "'" + username + "'";
                        Statement stmt1 = null;
                        try {
                            // 注册 JDBC 驱动
                            Class.forName("com.mysql.jdbc.Driver");

                            // 连接数据库
                            System.out.println("连接数据库...");
                            stmt1 = conn.createStatement();

                            ResultSet rs1 = stmt1.executeQuery(sql);
                            if (rs1.next()) {
                                int currency = rs1.getInt("currency");
                                currency = currency + change_money;
                                if(currency<0){
                                    moneyError();
                                    jf.dispose();
                                    Statement stmt2 = conn.createStatement();
                                    sql = "DELETE FROM bill_info WHERE bill_id = '" + bill_id + "'";
                                    if (stmt2.executeUpdate(sql) > 0) {
                                        System.out.println("delete success:" + bill_id);
                                        Date now = new Date();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                        String current_time = dateFormat.format(now);
                                        try {
                                            Log.output.write((current_time + ":(administer)" + username + " finished bill id = " + bill_id + ", username = " + username + ", change_money = " + change_money + "\n").getBytes(StandardCharsets.UTF_8));
                                        } catch (IOException e) {
                                            System.out.println("写入失败");
                                        }
                                    } else {
                                        System.out.println("delete failed:" + bill_id);
                                    }
                                    stmt2.close();
                                    conn.close();
                                    return;
                                }else{
                                    sql = "UPDATE user_info set currency = '" + currency + "' WHERE username = '" + username + "'";
                                    if (stmt1.executeUpdate(sql) > 0) {
                                        System.out.println("update success");
                                        Date now = new Date();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                        String current_time = dateFormat.format(now);
                                        try {
                                            Log.output.write((current_time + ":(administer)" + username + " update " + username + " 's currency:" + currency + "\n").getBytes(StandardCharsets.UTF_8));
                                        } catch (IOException e) {
                                            System.out.println("写入失败");
                                        }
                                    } else {
                                        System.out.println("update failed");
                                    }
                                }
                            } else {
                                System.out.println("user doesn't exist");
                            }
                            rs1.close();
                            stmt1.close();
                            Statement stmt2 = conn.createStatement();
                            sql = "DELETE FROM bill_info WHERE bill_id = '" + bill_id + "'";
                            if (stmt2.executeUpdate(sql) > 0) {
                                System.out.println("delete success:" + bill_id);
                                Date now = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String current_time = dateFormat.format(now);
                                try {
                                    Log.output.write((current_time + ":(administer)" + username + " finished bill id = " + bill_id + ", username = " + username + ", change_money = " + change_money + "\n").getBytes(StandardCharsets.UTF_8));
                                } catch (IOException e) {
                                    System.out.println("写入失败");
                                }
                            } else {
                                System.out.println("delete failed:" + bill_id);
                            }
                            stmt2.close();
                            conn.close();
                        } catch (SQLException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        jf.dispose();
                        menu();
                    }

                });

                disagreeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        try {
                            // 注册 JDBC 驱动
                            Class.forName("com.mysql.jdbc.Driver");

                            // 连接数据库
                            System.out.println("连接数据库...");
                            Statement stmt2 = conn.createStatement();
                            String sql = "DELETE FROM bill_info WHERE bill_id = '" + bill_id + "'";
                            if (stmt2.executeUpdate(sql) > 0) {
                                System.out.println("delete success:" + bill_id);
                                Date now = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                String current_time = dateFormat.format(now);
                                try {
                                    Log.output.write((current_time + ":(administer)" + username + " finished bill id = " + bill_id + ", username = " + username + ", change_money = " + change_money + "\n").getBytes(StandardCharsets.UTF_8));
                                } catch (IOException e) {
                                    System.out.println("写入失败");
                                }
                            } else {
                                System.out.println("delete failed:" + bill_id);
                            }
                            stmt2.close();
                            conn.close();
                        } catch (SQLException | ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        jf.dispose();
                        menu();
                    }
                });

            }else{
                noBillsError();
            }
            rs.close();
            stmt.close();
            //conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loginError() {
        JFrame jf = new JFrame("错误");
        jf.setSize(400, 200);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel jl = new JLabel("登陆出错");
        JButton jb = new JButton("确定");

        jl.setBounds(180, 30, 300, 30);
        jb.setBounds(155, 100, 80, 30);

        panel.add(jl);
        panel.add(jb);

        // 监听确认按钮
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
            }
        });

        jf.add(panel);
        jf.setVisible(true);
    }

    private static void moneyError() {
        JFrame jf = new JFrame("错误");
        jf.setSize(400, 200);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel jl = new JLabel("余额不足，无法取钱");
        JButton jb = new JButton("确定");

        jl.setBounds(180, 30, 300, 30);
        jb.setBounds(155, 100, 80, 30);

        panel.add(jl);
        panel.add(jb);

        // 监听确认按钮
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                menu();
            }
        });

        jf.add(panel);
        jf.setVisible(true);
    }

    private static void noBillsError(){
        JFrame jf = new JFrame("错误");
        jf.setSize(400, 200);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel jl = new JLabel("没有请求账单");
        JButton jb = new JButton("确定");

        jl.setBounds(180, 30, 300, 30);
        jb.setBounds(155, 100, 80, 30);

        panel.add(jl);
        panel.add(jb);

        // 监听确认按钮
        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                menu();
            }
        });

        jf.add(panel);
        jf.setVisible(true);
    }
}
