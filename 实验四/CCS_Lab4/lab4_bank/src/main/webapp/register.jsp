<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>注册</title>
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
            <a href="register.jsp" class="navbar-brand" >注册</a>
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
                <h1 class="wow fadeInUp" data-wow-delay="0.6s" >注册</h1>
                <form action="registerResult.jsp" method="post">
                    <div align="center"><input type="text" name="username" placeholder="用户名"
                                               id="username"
                                               required="required"></div>
                    <br/>
                    <%--<div align="center"><input type="text" name="userid" placeholder="身份证号"
                                               id="userid"
                                               required="required"></div>
                    <br/>--%>
                    <div align="center"><input type="password" name="password" onKeyUp=pwStrength(this.value)
                                               onBlur=pwStrength(this.value)
                                               id="password"
                                               placeholder="用户密码" required="required"></div>
                    <br/>
                    <div align="center">
                        <input type="submit" value="确认"><br/>
                    </div>
                </form>
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