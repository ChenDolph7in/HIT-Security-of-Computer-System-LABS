package bank;

import java.util.Scanner;

public class main {

    public static void main(String[] args) {
        System.out.println("欢迎进入银行，请输入账号密码：");
        System.out.println("请问你是管理员吗？(y/n)");
        Scanner in = new Scanner(System.in);
        String ack = in.nextLine();
        System.out.println("用户名：");
        String username = in.nextLine();
        System.out.println("密码：");
        String password = in.nextLine();
        new Log();// 初始化日志输出流

        if (ack.equals("y")) {
            Admin admin = new Admin(username, password);
            if(admin.login()){
                while (true) {
                    System.out.println("查看用户余额（1）/处理账单（2）/退出（quit）");
                    ack = in.nextLine();
                    if (ack.equals("1")) {
                        admin.lookupUsers();
                    } else if (ack.equals("2")) {
                        admin.dealBills();
                    } else if (ack.equals("quit")||ack.equals("q")) {
                        break;
                    }
                }
            }
        } else {
            Client client = new Client(username, password);
            if(client.login()){
                while (true) {
                    System.out.println("查看余额（1）/生成账单（2）/退出（quit）");
                    ack = in.nextLine();
                    if (ack.equals("1")) {
                        client.lookup();
                    } else if (ack.equals("2")) {
                        client.dealBills();
                    } else if (ack.equals("quit")||ack.equals("q")) {
                        break;
                    }
                }
            }
        }
        in.close();
    }
}
