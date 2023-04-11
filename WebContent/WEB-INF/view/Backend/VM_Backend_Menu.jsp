<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Language" content="zh-tw">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<!-- Bootstrap core CSS & JS -->
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link type="text/css" rel="stylesheet" href="css/bootstrap.min.css" />

<title>Insert title here</title>
	<script type="text/javascript" src="js/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="js/popper.min.js"></script>
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
	  <%--navbar-brand 縮到最小只會留下這個名稱 --%>
      <a class="navbar-brand" href="BackendAction.do?action=searchGoodsPage">後臺管理系統</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <a class="nav-link" href="BackendAction.do?action=searchGoodsPage">商品列表 </a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="BackendAction.do?action=updateGoodsView">商品維護/補貨</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="BackendAction.do?action=addGoodsView">商品新增上架</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="BackendAction.do?action=querySalesReportView">銷售報表</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="FrontendAction.do?action=queryAllGoods">前臺商品購物</a>
          </li>
        </ul>
      </div>
</nav>
<h3 align="left"> Vending Machine Backend Service </h3>

<p class="text-primary" align="right">${member.getCustomerName()} 先生/小姐您好!
	<a href="LoginAction.do?action=logout"" class="badge badge-danger">(登出)</a>
</p>
</body>
</html>