<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.sql.*" %>
<%@page import="java.util.UUID" %>
<%@ page import="java.net.URLDecoder" %>

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
            <a href="registerResult.jsp" class="navbar-brand" >注册结果</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a class="smoothScroll" href="index.jsp" >首页</a></li>
                <li><a class="smoothScroll" href="login.jsp" >登录</a></li>
                <li><a class="smoothScroll" href="register.jsp" >注册</a></li>
            </ul>
        </div>

    </div>
</div>

<section id="home">
    <div class="container">
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

                    /*System.out.println(request.getParameter("userid"));
                    System.out.println(request.getParameter("username"));
                    System.out.println(request.getParameter("password"));
                    System.out.println(request.getParameter("phonenum"));*/

                    String username = request.getParameter("username");
                    String password = request.getParameter("password");

                    String sql = "SELECT * FROM clientinfo WHERE username = \"" + username + "\"";    //查询语句
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {%>
        <tr>
            <div align="center">
                <h2 class="wow fadeInUp" data-wow-delay="0.6s" >注册失败，该用户名已被注册</h2>
            </div>
        </tr>
        <%
        }   else {
            sql = "SELECT createClient(\"" + username + "\",\"" + password +"\");";
            stmt = conn.createStatement();
            stmt.execute(sql);
        %>
        <tr>
            <div align="center">
                <h2 class="wow fadeInUp" data-wow-delay="0.6s" >注册成功</h2>
                <h3 class="wow fadeInUp" data-wow-delay="0.6s" >请重新登录</h3>
            </div>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <div align="center">
                <h2 class="wow fadeInUp" data-wow-delay="0.6s" >连接失败</h2>
            </div>
        </tr>
        <%
            }
        } catch (Exception e) {
            e.printStackTrace();
        %>
        <tr>
            <div align="center">
                <h2 class="wow fadeInUp" data-wow-delay="0.6s" >数据库连接异常</h2>
            </div>
        </tr>
        <%
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