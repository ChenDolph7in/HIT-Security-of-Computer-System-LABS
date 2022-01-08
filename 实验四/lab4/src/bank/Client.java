package bank;

import com.sun.org.apache.xpath.internal.functions.FuncFalse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/lab4?serverTimezone=GMT%2B8";

    // 数据库的用户名与密码
    public static String username = null;
    public static String password = null;

    //private int id;
    public Client(String username, String password) {
        Client.username = username;
        Client.password = password;
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
            Log.output.write((current_time + ":(client)" + Client.username + " login success\n").getBytes(StandardCharsets.UTF_8));
            conn.close();
            stmt.close();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("登陆失败");
            loginError();
            return false;
        } catch (IOException e) {
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

        JFrame jf = new JFrame("Client");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 选项按钮
        JButton depositButton = new JButton("存款");
        JButton withdrawButton = new JButton("取款");
        JButton queryButton = new JButton("查询余额");

        // 设置标签和按钮的大小和位置
        depositButton.setBounds(210, 80, 165, 30);
        withdrawButton.setBounds(210, 120, 165, 30);
        queryButton.setBounds(210, 160, 165, 30);

        // 加入面板中
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(queryButton);

        // 设置 存款 按钮监听事件
        depositButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到存款页面
                depositPage();
            }
        });

        // 设置 取款 按钮监听事件
        withdrawButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到取款页面
                withdrawPage();
            }
        });

        // 设置 查询 按钮监听事件
        queryButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到查询界面
                lookup();
            }
        });


        // 将面板加入窗口中
        jf.add(panel);
        // 设置窗口可见
        jf.setVisible(true);

    }

    private static void depositPage() {
        JFrame jf = new JFrame("存款");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 标签 & 输入框 & 按钮
        JLabel depositRemindLabel = new JLabel("存款数额：");
        JTextField moneyNumberText = new JTextField(30);
        JButton ensureButton = new JButton("确定");
        JButton returnButton = new JButton("返回");

        // 设置标签和按钮的大小和位置
        depositRemindLabel.setBounds(200, 40, 75, 30);
        moneyNumberText.setBounds(275, 40, 75, 30);
        ensureButton.setBounds(200, 80, 150, 30);
        returnButton.setBounds(200, 120, 150, 30);

        // 加入面板中
        panel.add(depositRemindLabel);
        panel.add(moneyNumberText);
        panel.add(ensureButton);
        panel.add(returnButton);

        ensureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
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
                    sql = "INSERT INTO bill_info(username, change_money) VALUES(" +
                            "'" + username + "', '" + moneyNumberText.getText() + "')";
                    if (stmt.executeUpdate(sql) > 0) {
                        System.out.println("create bill success");
                        stmt.close();
                        conn.close();
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String current_time = dateFormat.format(now);
                        try {
                            Log.output.write((current_time + ":(client)" + username + " create bill, deposit money = " + moneyNumberText.getText() + "\n").getBytes(StandardCharsets.UTF_8));
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
                jf.dispose();
                menu();
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

        // 将面板加入窗口中
        jf.add(panel);
        // 设置窗口可见
        jf.setVisible(true);

    }

    private static void withdrawPage() {
        JFrame jf = new JFrame("取款");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 标签 & 输入框 & 按钮
        JLabel depositRemindLabel = new JLabel("取款数额：");
        JTextField moneyNumberText = new JTextField(30);
        JButton ensureButton = new JButton("确定");
        JButton returnButton = new JButton("返回");

        // 设置标签和按钮的大小和位置
        depositRemindLabel.setBounds(200, 40, 75, 30);
        moneyNumberText.setBounds(275, 40, 75, 30);
        ensureButton.setBounds(200, 80, 150, 30);
        returnButton.setBounds(200, 120, 150, 30);

        // 加入面板中
        panel.add(depositRemindLabel);
        panel.add(moneyNumberText);
        panel.add(ensureButton);
        panel.add(returnButton);

        ensureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
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
                    sql = "INSERT INTO bill_info(username, change_money) VALUES(" +
                            "'" + username + "', '" + "-" + moneyNumberText.getText() + "')";
                    if (stmt.executeUpdate(sql) > 0) {
                        System.out.println("create bill success");
                        stmt.close();
                        conn.close();
                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        String current_time = dateFormat.format(now);
                        try {
                            Log.output.write((current_time + ":(client)" + username + " create bill, withdraw money = " + moneyNumberText.getText() + "\n").getBytes(StandardCharsets.UTF_8));
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
                jf.dispose();
                menu();
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

        // 将面板加入窗口中
        jf.add(panel);
        // 设置窗口可见
        jf.setVisible(true);
    }

    private static void lookup() {
        Connection conn = null;
        Statement stmt = null;
        String result = null;
        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 连接数据库
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, username, password);

            // 执行查询
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT currency FROM user_info WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);
            // 展开结果集数据库
            if (rs.next()) {
                // 通过字段检索
                int currency = rs.getInt("currency");
                result = "username:" + username + " currency:" + currency;
            } else {
                result = "错误";
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
        JFrame jf = new JFrame("余额");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton btSubmit = new JButton("确定");
        JTextArea jt = new JTextArea(result, 16, 16);

        jt.setBounds(160, 50, 250, 120);
        btSubmit.setBounds(230, 280, 80, 30);

        panel.add(jt);
        panel.add(btSubmit);

        // 监听确认按钮
        btSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        jf.add(panel);
        jf.setVisible(true);
    }

}
