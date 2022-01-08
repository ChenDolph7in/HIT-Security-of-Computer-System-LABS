package bank;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Audit {

    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/lab4?serverTimezone=GMT%2B8";

    // 数据库的用户名与密码
    public static String username = null;
    public static String password = null;

    //private int id;
    public Audit(String username, String password) {
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
            try {
                // 查询admin
                stmt = conn.createStatement();
                String sql = "SELECT id, username FROM audit_info";
                ResultSet rs = stmt.executeQuery(sql);
                rs.close();
                loginError();
                flag = true;
                System.out.println("登陆失败");
            } catch (Exception e) {
                System.out.println("登陆成功");
                menu();
            }
            if (flag) {
                return false;
            }

            java.util.Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output.write((current_time + ":(audit)" + Audit.username + " login success\n").getBytes(StandardCharsets.UTF_8));
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

        JFrame jf = new JFrame("Audit");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 选项按钮
        JButton auditButton = new JButton("查看审计日志");

        // 设置标签和按钮的大小和位置
        auditButton.setBounds(210, 80, 165, 30);

        // 加入面板中
        panel.add(auditButton);

        // 设置 存款 按钮监听事件
        auditButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // 关掉当前界面
                jf.dispose();
                // 跳转到存款页面
                depositPage();
            }
        });

        // 将面板加入窗口中
        jf.add(panel);
        // 设置窗口可见
        jf.setVisible(true);

    }

    private static void depositPage() {
        File file = new File("log.txt");
        BufferedReader reader = null;
        String result = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                result = result + "\n" + tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        JFrame jf = new JFrame("日志");
        jf.setSize(1000, 800);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton btSubmit = new JButton("确定");
        JTextArea jt = new JTextArea(result.toString(), 32, 32);

        jt.setBounds(160, 50, 600, 600);
        btSubmit.setBounds(800, 280, 80, 30);

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
