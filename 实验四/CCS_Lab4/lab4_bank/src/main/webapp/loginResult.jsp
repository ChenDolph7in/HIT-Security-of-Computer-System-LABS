<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.sql.*" %>
<%@ page import="com.example.lab4_bank.Login" %>

<html>
<head>
    <title>注册结果</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/magnific-popup.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <!-- Main css -->
    <link rel="stylesheet" href="css/style.css">
</head>
<body data-spy="scroll" data-target=".navbar-collapse" data-offset="50">
<!-- PRE LOADER -->
<div class="preloader">
    <div class="spinner">
        <span class="spinner-rotate"></span>
    </div>
</div>

<!-- NAVIGATION SECTION -->
<div class="navbar custom-navbar navbar-fixed-top" role="navigation">
    <div class="container">

        <div class="navbar-header">
            <button class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon icon-bar"></span>
                <span class="icon icon-bar"></span>
                <span class="icon icon-bar"></span>
            </button>
            <!-- lOGO TEXT HERE -->
            <a href="loginResult.jsp" class="navbar-brand">CA-登录结果</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a class="smoothScroll" href="index.jsp">首页</a></li>
                <li><a class="smoothScroll" href="login.jsp">登录</a></li>
                <li><a class="smoothScroll" href="register.jsp">注册</a></li>
            </ul>
        </div>

    </div>
</div>

<section id="home">
    <div class="container">
        <%
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            Login login = new Login(username, password);
            if (!login.login()) {
        %>
        <tr>
            <div align="center">
                <h2 class="wow fadeInUp" data-wow-delay="0.6s">登录失败</h2>
                <h3 class="wow fadeInUp" data-wow-delay="0.6s">请从点击确认返回</h3>
                <form action="login.jsp" method="post">
                    <div align="center">
                        <button type="submit">确认</button>
                    </div>
                </form>
            </div>
        </tr>
        <%
        } else {
        %>
        <tr>
            <div align="center">
                <h2 class="wow fadeInUp" data-wow-delay="0.6s">登录成功</h2>
                <%
                    try {
                        Class.forName("com.mysql.jdbc.Driver");  //驱动程序名
                        String url = "jdbc:mysql://localhost:3306/lab4"; //数据库名
                        String basename = "root";  //数据库用户名
                        String db_password = "7757123";  //数据库用户密码
                        Connection conn = DriverManager.getConnection(url, basename, db_password);  //连接状态
                        if (conn != null) {
                            Statement stmt = null;
                            ResultSet rs = null;
                            String sql = "select rank from users where username = \"" + username + "\"";    //查询语句
                            stmt = conn.createStatement();
                            rs = stmt.executeQuery(sql);
                            if (!rs.next()) {
                                int rank = rs.getInt("rank");
                                if (rank == 1) {
                %>
                <div align="center">
                    <input type="submit" value="删除历史全部订单" onclick="deleteall()"/>
                </div>
                <form action="client.jsp" method="post">
                    <div align="center">
                        <button type="submit">确认</button>
                    </div>
                </form>
                <%
                } else if (rank == 2) {

                %>
                <form action="admin.jsp" method="post">
                    <div align="center">
                        <button type="submit">确认</button>
                    </div>
                </form>
                <%
                } else {

                %>
                <form action="boss.jsp" method="post">
                    <div align="center">
                        <button type="submit">确认</button>
                    </div>
                </form>
            </div>
        </tr>
        <%
                }
            }
        } else {
        %>
        <tr>
            <div align="center">
                <h2 class="wow fadeInUp" data-wow-delay="0.6s">连接失败</h2>
            </div>
        </tr>
        <%
            }
        } catch (Exception e) {
            e.printStackTrace();
        %>
        <tr>
            <div align="center">
                <h2 class="wow fadeInUp" data-wow-delay="0.6s">数据库连接异常</h2>
            </div>
        </tr>
        <%
            }
            }
        %>
    </div>
</section>
<!-- SCRIPTS -->
<script src="js/jquery.js"></script>
<script src="js/jquery.magnific-popup.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/magnific-popup-options.js"></script>
<script src="js/wow.min.js"></script>
<script src="js/custom.js"></script>
<script src="js/smoothscroll.js"></script>
</body>
</html>