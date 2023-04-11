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

<title>會員註冊</title>
	<script type="text/javascript">

	</script>
</head>
<body class="text-center" >
	
    <form class="form-signin" action="LoginAction.do" method="post">
      <input type="hidden" name="action" value="memberAdd" />
      
      <img class="mb-4" src="img/starbucksIcon.png" alt="" width="72" height="72">
      <h1 class="h3 mb-3 font-weight-normal">會員註冊</h1>
      
      <p style="color:blue;">${meberAddMsg}</p>
		<% session.removeAttribute("meberAddMsg"); %>	 <%--秀出後立即刪除 F5重整並不會出現 --%>
      
      <label for="inputId" class="sr-only">會員帳號</label>
      <input type="text" id="identificationNo" name="identificationNo" class="form-control" placeholder="Enter ID" required>
      <br/>
      <label for="inputPassword" class="sr-only">會員密碼</label>
      <input type="password" id="password" name="password" class="form-control" placeholder="Enter Password" required>
      <br/>
      <label for="inputCustomerName" class="sr-only">會員姓名</label>
      <input type="text" id="customerName" name="customerName" class="form-control" placeholder="Enter Name" required>
      <br/>
      <button class="btn btn-lg btn-primary btn-block" type="submit">註冊</button>
      <br/>
      <h4><a href="FrontLogin.jsp" class="badge badge-warning" >會員登入</a></h4>
      
      <p class="mt-5 mb-3 text-muted">&copy; 202302 Ting.</p>
    </form>
	
</body>
</html>