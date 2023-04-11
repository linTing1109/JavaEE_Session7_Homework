<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>會員修改</title>
	<!-- 當選擇商品的時候 整個form表單資料帶入 document.表單名稱 -->
	<script type="text/javascript">
	</script>
</head>
<body class="text-center" >
	<div class="container"><%--主要讓畫面左右留空 不完全黏著 --%>
	<%@ include file="VendingMachine_Menu.jsp" %>
	<hr/>
	<h3 class="h3 mb-3 font-weight-normal" align="left">會員資料更新</h3>	

	<%--新增訊息狀態 --%>
	<p class="text-success" align="left">${updateMsg}</p>
	<% session.removeAttribute("updateMsg"); %>
	
	<div style="margin-left:25px;">
	<form action="FrontendAction.do" enctype="multipart/form-data" method="post" >
      <input type="hidden" name="action" value="updateMember" />
      <input type="hidden" name="identificationNo" value="${member.identificationNo}" />
	     <div class="form-group col-md-3">
		    <label>會員密碼</label>
		    <input type="password" class="form-control" name="password" id="password"  value="${member.password}">
		 </div>
	     <div class="form-group col-md-3">
		    <label>會員姓名</label>
		    <input type="text" class="form-control" name="customerName" id="customerName"  value="${member.customerName}">
		 </div>
	 	<div class="form-group col-md-3">
     	 <button type="submit" class="btn btn-outline-primary" align="left">修改</button> 	
		</div>
    </form>
	</div>

</body>
</html>