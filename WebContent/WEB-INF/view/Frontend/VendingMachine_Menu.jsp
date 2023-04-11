<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:url value="/" var="WEB_PATH"/>   
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Language" content="zh-tw">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<!-- Bootstrap core CSS & JS -->
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />
	
<title>前臺Menu</title>
	<script type="text/javascript" src="js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="js/popper.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript">
		//查詢購物車功能
		function queryCartGoods(){
			const formData = new FormData();
			formData.append('action', 'queryCartGoods');
			// 送出查詢購物車商品請求
			const request = new XMLHttpRequest();
			request.open("POST", "MemberAction.do");			
			request.send(formData);
	// 		history.go(0);//重新導回原頁面
			
			location="FrontendAction.do?action=queryAllGoods";
		}
		//清空購物車
		function clearCartGoods(){
			const formData = new FormData();
			formData.append('action', 'clearCartGoods');
			// 送出清空購物車商品請求
			const request = new XMLHttpRequest();
			request.open("POST", "MemberAction.do");			
			request.send(formData);	
			history.go(0);//重新導回原頁面
		}
	</script>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	  <%--navbar-brand 縮到最小只會留下這個名稱 --%>
      <a class="navbar-brand" href="FrontendAction.do?action=queryAllGoods">前臺商品購物</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <a class="nav-link" href="FrontendAction.do?action=queryAllGoods">商品列表</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="FrontendAction.do?action=queryIdOrder">訂單紀錄</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="FrontendAction.do?action=updateMemberView">帳戶修改</a>
          </li>
          
          <li class="nav-item">
            <a class="nav-link" onclick="queryCartGoods()" id="queryCartGoods">購物車(<text style="color:red;">${fn:length(carGoods)}</text>)</a>
          </li>
          
          <li class="nav-item">
            <a class="nav-link" onclick="clearCartGoods()">清空購物車</a>
          </li>
          <c:if test="${fn:length(carGoods) ne 0}">
	          <li class="nav-item">
	            <a class="nav-link" href="MemberAction.do?action=updateCheckOutView"><text style="color:orange;">修改購物車</text></a>
	          </li>
          </c:if>
          
          <li class="nav-item">
            <a class="nav-link" href="BackendAction.do?action=searchGoodsPage">後臺</a>
          </li>
        </ul>
        <form class="form-inline my-2 my-lg-0" action="FrontendAction.do" method="get">
          <input class="form-control mr-sm-2" type="text" name="searchKeyword" placeholder="Search" aria-label="Search">
          <input type="hidden" name="action" value="searchGoods"/>
		  <input type="hidden" name="pageNo" value="1"/>
          <button class="btn btn-outline-success my-2 my-sm-0" type="submit">商品查詢</button>
        </form>
      </div>
</nav>
<p class="text-danger" align="center">歡迎光臨，${member.getCustomerName()} 先生/小姐您好!
	<a href="LoginAction.do?action=logout" class="badge badge-danger">(登出)</a>
</p>
</body>
</html>