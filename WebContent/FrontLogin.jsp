<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Language" content="zh-tw">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- Bootstrap core CSS & JS -->
 	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
    <script type="text/javascript" src="js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="js/popper.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <!-- Bootstrap 登入頁面額外的css -->
    <link type="text/css" rel="stylesheet" href="css/signin.css" />
<title>會員登入</title>
</head>
<body class="text-center" >
	
    <form class="form-signin" action="LoginAction.do" method="post">
      <input type="hidden" name="action" value="login" />
      
      <img class="mb-4" src="img/starbucksIcon.png" alt="" width="72" height="72">
      <h1 class="h3 mb-3 font-weight-normal">會員登入</h1>
      
      <p style="color:blue;">${loginMsg}</p>
	<% session.removeAttribute("loginMsg"); %>	<%--秀出後立即刪除 F5重整並不會出現 --%>
      
      <label for="inputId" class="sr-only">會員帳號 ID</label>
      <input type="text" id="id" name="id" class="form-control" placeholder="Enter ID" required autofocus>
      <br>
      <label for="inputPassword" class="sr-only">會員密碼 Password</label>
      <input type="password" id="pwd" name="pwd" class="form-control" placeholder="Enter Password" required>
      <div class="checkbox mb-3">
        <label>
          <input type="checkbox" value="remember-me"> Remember me
        </label>
      </div>
      <button class="btn btn-lg btn-primary btn-block" type="submit">登入</button>
      <br>
      <h4><a href="FrontLoginRegister.jsp" class="badge badge-warning" >會員註冊</a></h4>
      <p class="mt-5 mb-3 text-muted">&copy; 202302 Ting.</p>
      
    </form>
	  
</body>
</html>