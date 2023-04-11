<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
	<!-- 當選擇商品的時候 整個form表單資料帶入 document.表單名稱 -->
	<script type="text/javascript">
		function goodsSelected(){
	        document.updateGoodstForm.action.value = "updateGoodsView";
	        document.updateGoodstForm.submit();
		}
	</script>
</head>
<body>
	<%@ include file="VM_Backend_Menu.jsp" %>
	<HR>
		
	<h2>商品維護作業</h2>
	<p style="color:blue;">${messageUpdate}</p>
	<% session.removeAttribute("messageUpdate"); %>
	
	<div style="margin-left:25px;">
	<!--  <form action="BackendAction.do?action=updateGoods" method="post">-->
	<form name="updateGoodstForm" action="BackendAction.do" method="post">
		<input type="hidden" name="action" value="updateGoods"/>
		<p>
			飲料名稱：
			 <select size="1" name="goodsID" onchange="goodsSelected();">
			 	<option value="">----- 請選擇 -----</option>
			 	<c:forEach items="${goods}" var="good">
			 		<option <c:if test="${ good.goodsID  eq modifyGood.goodsID}">selected</c:if> 
			 			value="${ good.goodsID }">
			 			${  good.goodsName  } <!-- 秀在網頁上的 -->
			 		</option>	
			 	</c:forEach>
			</select>
		</p>		
		<p>
			更改價格： 
			<input type="number" name="goodsPrice" value="${modifyGood.goodsPrice}" size="5" value="0" min="0" max="1000">
		</p>
		<p>
			商品庫存量: ${modifyGood.goodsQuantity}
		</p>
		<p>
			補貨數量：
			<input type="number" name="goodsQuantity" size="5" value="0" min="0" max="1000">
		</p>
		<p>
			商品狀態：
			<select name="status">
				<option <c:if test="${1 eq modifyGood.status}">selected</c:if>  value="1">上架</option>
				<option <c:if test="${0 eq modifyGood.status}">selected</c:if> value="0">下架</option>				
			</select>
		</p>
		<p>
			<input type="submit" value="送出">
		</p>
		<% session.removeAttribute("modifyGood"); %> 
	</form>
	</div>
</body>
</html>