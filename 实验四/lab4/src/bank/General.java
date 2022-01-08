package bank;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class General {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/lab4?serverTimezone=GMT%2B8";

    // 数据库的用户名与密码
    public static String username;
    public static String password;

    public General(String username, String password) {
        Admin.username = username;
        Admin.password = password;
    }

    public static boolean login() {
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
            Log.output.write((current_time + ":(general)" + username + " login success\n").getBytes(StandardCharsets.UTF_8));
            menu();
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

        JFrame jf = new JFrame("General");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 选项按钮
        JButton lookupButton = new JButton("查看用户");
        JButton upButton = new JButton("将client提升为admin");
        JButton downButton = new JButton("将admin降级为client");

        // 设置标签和按钮的大小和位置
        lookupButton.setBounds(200, 80, 165, 30);
        upButton.setBounds(200, 120, 165, 30);
        downButton.setBounds(200, 160, 165, 30);

        // 加入面板中
        panel.add(lookupButton);
        panel.add(upButton);
        panel.add(downButton);

        // 设置 存款 按钮监听事件
        lookupButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到存款页面
                lookupUsers();
            }
        });

        // 设置 取款 按钮监听事件
        upButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到取款页面
                promote();
            }
        });

        // 设置 取款 按钮监听事件
        downButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到取款页面
                demote();
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
        Statement stmt1 = null;
        String result = "";
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, username, password);

            // 查询client
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, username FROM user_info WHERE valid != 0";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            while (rs.next()) {
                // 通过字段检索
                int id = rs.getInt("id");
                String name = rs.getString("username");

                result = result + "client id:" + id + " username:" + name + "\n";
            }

            // 查询admin
            stmt1 = conn.createStatement();
            sql = "SELECT id, username FROM admin_info";
            ResultSet rs1 = stmt1.executeQuery(sql);
            // 展开结果集数据库
            while (rs1.next()) {
                // 通过字段检索
                int id = rs1.getInt("id");
                String name = rs1.getString("username");

                result = result + "admin id:" + id + " username:" + name + "\n";
            }
            rs.close();
            stmt.close();
            rs1.close();
            stmt1.close();
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
        JFrame jf = new JFrame("所有用户");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton btSubmit = new JButton("确定");
        JTextArea jt = new JTextArea(result, 16, 16);

        jt.setBounds(200, 50, 170, 120);
        btSubmit.setBounds(230, 280, 80, 30);

        panel.add(jt);
        panel.add(btSubmit);

        //监听确认按钮
        btSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                menu();
            }
        });

        jf.add(panel);
        jf.setVisible(true);
    }

    private static void promote() {
        JFrame jf = new JFrame("将client提升为admin");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 标签 & 输入框 & 按钮
        JLabel promoteLabel = new JLabel("选择用户：");
        JTextField userNameText = new JTextField(30);
        JButton ensureButton = new JButton("确定");
        JButton returnButton = new JButton("返回");

        // 设置标签和按钮的大小和位置
        promoteLabel.setBounds(200, 40, 75, 30);
        userNameText.setBounds(275, 40, 75, 30);
        ensureButton.setBounds(200, 80, 150, 30);
        returnButton.setBounds(200, 120, 150, 30);

        // 加入面板中
        panel.add(promoteLabel);
        panel.add(userNameText);
        panel.add(ensureButton);
        panel.add(returnButton);

        ensureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String change_username = userNameText.getText();
                Connection conn = null;
                Statement stmt = null;
                String result = "";
                try {
                    // 注册 JDBC 驱动
                    Class.forName("com.mysql.jdbc.Driver");

                    // 连接数据库
                    System.out.println("连接数据库...");
                    conn = DriverManager.getConnection(DB_URL, username, password);

                    // 查询client
                    stmt = conn.createStatement();
                    String sql;
                    sql = "SELECT id, username FROM user_info WHERE valid != 0 AND username=\"" + change_username + "\"";
                    ResultSet rs = stmt.executeQuery(sql);
                    // 展开结果集数据库
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        Statement stmt1 = conn.createStatement();
                        Statement stmt2 = conn.createStatement();
                        sql = "UPDATE user_info SET valid = 0,currency = 0 WHERE username=\"" + change_username + "\"";
                        if (stmt1.executeUpdate(sql) > 0) {
                            sql = "INSERT INTO admin_info(id,username) VALUES (" + id + ",\"" + change_username + "\")";
                            if (stmt2.executeUpdate(sql) > 0) {
                                Statement stmt3 = conn.createStatement();
                                sql = "REVOKE select(currency), select(username), select(valid) ON lab4.user_info FROM '" + change_username + "'@'localhost'";
                                stmt3.execute(sql);
                                Statement stmt4 = conn.createStatement();
                                sql = "REVOKE insert(username), insert(change_money) ON lab4.bill_info FROM '" + change_username + "'@'localhost'";
                                stmt4.execute(sql);
                                Statement stmt5 = conn.createStatement();
                                sql = "GRANT select,update ON lab4.user_info TO '" + change_username + "'@'localhost'";
                                stmt5.execute(sql);
                                Statement stmt6 = conn.createStatement();
                                sql = "GRANT select,delete ON lab4.bill_info TO '" + change_username + "'@'localhost'";
                                stmt6.execute(sql);
                                stmt.close();
                                stmt1.close();
                                stmt2.close();
                                stmt3.close();
                                stmt4.close();
                                stmt5.close();
                                stmt6.close();
                                conn.close();
                                rs.close();
                            } else {
                                SQLError();
                                System.out.println("error");
                            }
                        }
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String current_time = dateFormat.format(now);
                        try {
                            Log.output.write((current_time + ":(general)" + username + " promote " + change_username + "from client to admin" + "\n").getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            System.out.println("写入失败");
                        }
                        jf.dispose();
                        success();
                    } else {
                        noPersonError();
                        jf.dispose();
                    }

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
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 跳转到客户服务界面
                jf.dispose();
                menu();
            }
        });
        jf.add(panel);
        jf.setVisible(true);
    }

    private static void demote() {
        JFrame jf = new JFrame("将admin降级为client");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 标签 & 输入框 & 按钮
        JLabel promoteLabel = new JLabel("选择用户：");
        JTextField userNameText = new JTextField(30);
        JButton ensureButton = new JButton("确定");
        JButton returnButton = new JButton("返回");

        // 设置标签和按钮的大小和位置
        promoteLabel.setBounds(200, 40, 75, 30);
        userNameText.setBounds(275, 40, 75, 30);
        ensureButton.setBounds(200, 80, 150, 30);
        returnButton.setBounds(200, 120, 150, 30);

        // 加入面板中
        panel.add(promoteLabel);
        panel.add(userNameText);
        panel.add(ensureButton);
        panel.add(returnButton);

        ensureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String change_username = userNameText.getText();
                Connection conn = null;
                Statement stmt = null;
                String result = "";
                try {
                    // 注册 JDBC 驱动
                    Class.forName("com.mysql.jdbc.Driver");

                    // 连接数据库
                    System.out.println("连接数据库...");
                    conn = DriverManager.getConnection(DB_URL, username, password);

                    // 查询client
                    stmt = conn.createStatement();
                    String sql;
                    sql = "SELECT id, username FROM user_info WHERE valid = 0 AND username=\"" + change_username + "\"";
                    ResultSet rs = stmt.executeQuery(sql);
                    // 展开结果集数据库
                    if (rs.next()) {
                        Statement stmt1 = conn.createStatement();
                        Statement stmt2 = conn.createStatement();
                        sql = "UPDATE user_info SET valid = 1,currency = 0 WHERE username=\"" + change_username + "\"";
                        if (stmt1.executeUpdate(sql) > 0) {
                            sql = "DELETE FROM admin_info WHERE username=\"" + change_username + "\"";
                            if (stmt2.executeUpdate(sql) > 0) {
                                Statement stmt3 = conn.createStatement();
                                sql = "REVOKE select,update ON lab4.user_info FROM '" + change_username + "'@'localhost'";
                                stmt3.execute(sql);
                                Statement stmt4 = conn.createStatement();
                                sql = "REVOKE select,delete ON lab4.bill_info FROM '" + change_username + "'@'localhost'";
                                stmt4.execute(sql);
                                Statement stmt5 = conn.createStatement();
                                sql = "GRANT select(currency), select(username), select(valid) ON lab4.user_info TO '" + change_username + "'@'localhost'";
                                stmt5.execute(sql);
                                Statement stmt6 = conn.createStatement();
                                sql = "GRANT insert(username), insert(change_money) ON lab4.bill_info TO '" + change_username + "'@'localhost'";
                                stmt6.execute(sql);
                                stmt.close();
                                stmt1.close();
                                stmt2.close();
                                stmt3.close();
                                stmt4.close();
                                stmt5.close();
                                stmt6.close();
                                conn.close();
                                rs.close();
                            } else {
                                SQLError();
                                System.out.println("error");
                            }
                        }
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String current_time = dateFormat.format(now);
                        try {
                            Log.output.write((current_time + ":(general)" + username + " demote " + change_username + "from admin to client" + "\n").getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            System.out.println("写入失败");
                        }
                        jf.dispose();
                        success();
                    } else {
                        noPersonError();
                        jf.dispose();
                    }

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
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 跳转到客户服务界面
                jf.dispose();
                menu();
            }
        });
        jf.add(panel);
        jf.setVisible(true);
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
                menu();
            }
        });

        jf.add(panel);
        jf.setVisible(true);
    }

    private static void SQLError() {
        JFrame jf = new JFrame("错误");
        jf.setSize(400, 200);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel jl = new JLabel("数据库命令异常");
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

    private static void success() {
        JFrame jf = new JFrame("错误");
        jf.setSize(400, 200);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel jl = new JLabel("操作成功");
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

    private static void noPersonError() {
        JFrame jf = new JFrame("错误");
        jf.setSize(400, 200);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel jl = new JLabel("目标群体中没有该用户");
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
