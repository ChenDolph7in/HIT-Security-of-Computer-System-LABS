package bank;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class main {

    public static void main(String[] args) {

        new Log();// 初始化日志输出流

        JFrame jf = new JFrame("登录");
        jf.setSize(600, 400);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(null);    // 面板布局

        // 创建 标签 & 输入框 & 按钮
        JLabel userLabel = new JLabel("用户名:");
        JLabel passwordLabel = new JLabel("密码:");
        JTextField userNameText = new JTextField(20);
        JTextField userPasswordText = new JTextField(20);
        JButton loginButton = new JButton("登录");
        // JButton registerButton = new JButton("注册");
        ButtonGroup option = new ButtonGroup();

        JRadioButton optionA = new JRadioButton("用户");
        JRadioButton optionB = new JRadioButton("管理员");
        JRadioButton optionC = new JRadioButton("主管");
        JRadioButton optionD = new JRadioButton("审计人员");

        option.add(optionA);
        option.add(optionB);
        option.add(optionC);
        option.add(optionD);

        // 设置标签的大小和位置
        userLabel.setBounds(110, 40, 80, 25);
        userNameText.setBounds(200, 40, 165, 25);
        passwordLabel.setBounds(110, 70, 80, 25);
        userPasswordText.setBounds(200, 70, 165, 25);
        optionA.setBounds(230, 110, 80, 25);
        optionB.setBounds(230, 140, 80, 25);
        optionC.setBounds(230, 170, 80, 25);
        optionD.setBounds(230, 200, 80, 25);
        loginButton.setBounds(230, 230, 80, 25);
        //registerButton.setBounds(230, 230, 80, 25);

        // 设置面板内容
        panel.add(userLabel);
        panel.add(userNameText);
        panel.add(passwordLabel);
        panel.add(userPasswordText);
        panel.add(loginButton);
        //panel.add(registerButton);
        panel.add(optionA);
        panel.add(optionB);
        panel.add(optionC);
        panel.add(optionD);

        // 将面板加入到窗口中
        jf.add(panel);

        // 监听确认按钮
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (optionA.isSelected() == true) {
                    Client.username = userNameText.getText();
                    Client.password = userPasswordText.getText();
                    if (Client.login()) {
                        jf.dispose();
                    }
                } else if (optionB.isSelected() == true) {
                    Admin.username = userNameText.getText();
                    Admin.password = userPasswordText.getText();
                    if (Admin.login()) {
                        jf.dispose();
                    }
                } else if (optionC.isSelected() == true) {
                    General.username = userNameText.getText();
                    General.password = userPasswordText.getText();
                    if (General.login()) {
                        jf.dispose();
                    }
                }else if (optionD.isSelected() == true) {
                    Audit.username = userNameText.getText();
                    Audit.password = userPasswordText.getText();
                    if (Audit.login()) {
                        jf.dispose();
                    }
                }
                else            //没有选按钮便提交
                {
                    error();
                }
                // 重置按钮的值
                option.clearSelection();
            }
        });

        // 设置窗口可见
        jf.setVisible(true);
    }

    private static void error() {
        JFrame jf = new JFrame("错误");
        jf.setSize(400, 200);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel jl = new JLabel("错误");
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
