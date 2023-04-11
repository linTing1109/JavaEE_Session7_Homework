<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/" var="WEB_PATH"/>  
<c:url value="/js" var="JS_PATH"/>
<!DOCTYPE html>
<html>
<head>

<title>後臺商品維護/補貨</title>

	<!-- 使用Ajax來達成非同步 -->
	<script src="${JS_PATH}/jquery-1.11.1.min.js"></script>
	<!-- 使用Ajax來達成非同步 -->
	<script type="text/javascript">
		$(document).ready(function(){
			$("#goodsID").bind("change",function(){
				
				var goodsID = $("#goodsID option:selected").val();
				var goodsParam = {id : goodsID};
				
				if(goodsID != ""){ 
					$.ajax({
					  url: '${WEB_PATH}BackendAction.do?action=getModifyGoods', // 指定要進行呼叫的位址
					  type: "GET", // 請求方式 POST/GET
					  data: goodsParam,// 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
					  dataType : 'JSON', // Server回傳的資料類型
					  success: function(goodsInfo) { // 請求成功時執行函式
					  	$("#goodsPrice").val(goodsInfo.goodsPrice);
					  	$("#goodsQuantityDiv").text(goodsInfo.goodsQuantity);
					  	$("#description").val(goodsInfo.description);
					  	$("#status").val(goodsInfo.status);
					  
					  },
					  error: function(error) { // 請求發生錯誤時執行函式
					  	alert("Ajax Error!");
					  }
					});
				}
				else{
				  	$("#goodsPrice").val('');
				  	$("#goodsQuantityDiv").empty();
				  	$("#goodsQuantity").val('');
					$("#description").val('');
				  	$("#status").val('');
				}
			});
		});
	</script>
</head>
<body>
	<div class="container"><%--主要讓畫面左右留空 不完全黏著 --%>
	<%@ include file="VM_Backend_Menu.jsp" %>
		
	<HR>
	
	<h3 class="h3 mb-3 font-weight-normal">商品維護作業</h3>
	<p class="text-success" align="left">${messageUpdate}</p>
	<% session.removeAttribute("messageUpdate"); %>
	
	<div style="margin-left:25px;">
	<form name="updateGoodstForm" action="BackendAction.do" method="post">
		<input type="hidden" name="action" value="updateGoods"/>
		<div class="form-group row ">
	          <label for="goodsID">飲料名稱</label>
	          <div class="col-md-3">
		          <select id="goodsID" name="goodsID" class="form-control" >
		          	<option value="">----- 請選擇 -----</option>
		            <c:forEach items="${goods}" var="good">
		            	
				 		<%--需判斷good.goodsID  eq modifyGood.goodsID 讓更新完後直接秀出全部修改後的 --%>
				 		<option <c:if test="${ good.goodsID  eq modifyGood.goodsID}">selected</c:if>  
				 			value="${ good.goodsID }">
				 			${  good.goodsName  } <!-- 秀在網頁上的 -->
				 		</option>	
				 	</c:forEach>
		          </select>
		       </div>   
        </div>
        <div class="form-group row">
	        <label for="goodsPrice">更改價格</label>
	        <div class="col-md-3">
	          <input type="number" class="form-control" id="goodsPrice" name="goodsPrice" value="${modifyGood.goodsPrice}">
			</div>		
		</div>
        <div class="form-group row">
	        <label for="goodsPrice">更改描述</label>
	        <div class="col-md-3">
	          <input type="text" class="form-control" id="description" name=description value="${modifyGood.description}">
			</div>		
		</div>
		<div class="form-group row">
	        <label for="goodsPrice">商品庫存量</label>
	        <div class="col-md-3" style="display:inline-block;">
	        	<div id="goodsQuantityDiv" style="display:inline-block;">${modifyGood.goodsQuantity}</div>	
			</div>		
		</div>
		<div class="form-group row">
	        <label for="goodsQuantity">補貨數量</label>
	        <div class="col-md-3">
	          <input type="number" class="form-control" id="goodsQuantity" name="goodsQuantity" value="0">
			</div>		
		</div>
		<div class="form-group row">
	        <label for="status">商品狀態</label>
	        <div class="col-md-3">
	        	<select name="status" id="status">
					<option <c:if test="${1 eq modifyGood.status}">selected</c:if>  value="1">上架</option>
					<option <c:if test="${0 eq modifyGood.status}">selected</c:if> value="0">下架</option>				
				</select>
			</div>		
		</div>
		<button type="submit" class="btn btn-outline-primary">修改商品</button> 

		<%--使用完移除 避免畫面重整帶入舊資料 --%>
		<% session.removeAttribute("modifyGoodID"); %>
		<% session.removeAttribute("modifyGood"); %> 
	</form>
	</div>
	<hr>
	</div>
</body>
</html>