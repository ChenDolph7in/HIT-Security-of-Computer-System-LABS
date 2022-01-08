<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <title>首页</title>
    <link rel="icon" href="images/favicon.ico">
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
            <a href="index.jsp" class="navbar-brand" >首页</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li><a class="smoothScroll" href="" >首页</a></li>
                <li><a class="smoothScroll" href="login.jsp" >登录</a></li>
                <li><a class="smoothScroll" href="register.jsp" >注册</a></li>
            </ul>
        </div>

    </div>
</div>
<!-- HOME SECTION -->
<section id="home">
    <div class="container">
        <div class="row">
            <div align="center">
                <h1 class="wow fadeInUp" data-wow-delay="0.6s" style="color:white">欢迎使用银行</h1>
                <h2 class="wow fadeInUp" data-wow-delay="0.6s" style="color:white">请先进行登录/注册</h2>
            </div>
            <div align="center">
                <a href="register.jsp">
                    <button style="width:150px;height:60px;margin:60px">注册</button>
                </a>
                <a href="login.jsp">
                    <button style="width:150px;height:60px;margin:60px;">登录</button>
                </a>
            </div>
        </div>
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