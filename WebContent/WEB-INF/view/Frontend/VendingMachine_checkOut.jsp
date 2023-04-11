<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>    
<c:url value="/js" var="JS_PATH"/>    
<!DOCTYPE html>
<html>
<head>
<title>前臺商品結帳頁面</title>
<!-- 使用Ajax來達成非同步 -->
	<script src="${JS_PATH}/jquery-1.11.1.min.js"></script>

<script type="text/javascript">
	$(document).ready(function(){
		//若此頁面無購物車商品 將重新導入前臺首頁
		if (${fn:length(carGoods) eq 0} ) {
			 $("#note").text("購物車目前無等待結帳商品 將於5秒後 引導回購物頁面");

// 			  alert("購物車目前為空 將重新導入購物頁面");
			  setTimeout(function() {
				  location="FrontendAction.do?action=queryAllGoods";
				}, 5000);
			  
		}
// 		// 初始狀態時(沒有做任何動作)，送出可點，更新按鈕不可點
// 		$("#goodSubmit").prop("disabled", false);
// 		$("#goodsUpdateSubmit").prop("disabled", true);


		// 監聽商品數量的變動事件  mouseleave:當滑鼠點擊完後 離開才會更新 避免每點一下就更新一次
		$(".goods-quantity").on("mouseleave",function(){
		var totalPrice = 0;
		var goodsID = $(this).data("id"); // 取得商品編號(goodID)
		
		var quantity = parseInt($(".goods-quantity[data-id='" + goodsID + "']").val()); // 取得商品數量
		
		//----------------------------------------------
		const formData = new FormData();
		formData.append('action', 'updateCheckOut');
		formData.append("goodsID", goodsID); 
		formData.append("quantity", quantity); 
		
		const request = new XMLHttpRequest();
		  request.onreadystatechange = function() {
		    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
		      // 如果成功刪除商品，重新載入結帳頁面
		      location.reload();
		    }
		  };
		  request.open("POST", "MemberAction.do");
		  request.send(formData);

		});

	});
		//購物車各品項刪除功能
		function delCartGood(goodsID){
			console.log("goodID:",goodsID);	
			const formData = new FormData();
			formData.append('action', 'delCartGoods');
			formData.append("goodsID", goodsID); 
			
			const request = new XMLHttpRequest();
			  request.onreadystatechange = function() {
			    if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
			      // 如果成功刪除商品，重新載入結帳頁面
			      location.reload();
			    }
			  };
			  request.open("POST", "MemberAction.do");
			  request.send(formData);
		}
	
</script>

</head>
<body align="center">
<div class="container"><%--主要讓畫面左右留空 不完全黏著 --%>
	<%@ include file="VendingMachine_Menu.jsp" %>
	<br/>
	  <div class="row">
	  	<div class="col-md-10 ">
	  		<p class="text-primary">
<!-- 	  		<form action="MemberAction.do?action=updateCheckOut" id="updateCheckOutForm" method="post">  -->
	  		
	  		<table class="table table-hover">
	  		<thead>
		      <tr>
		        <th scope="col">商品ID</th>
		        <th scope="col">商品名稱</th>
		        <th scope="col">商品價格</th>
		        <th scope="col">訂購數量</th>
		        <th scope="col">商品小計</th>
		        <th scope="col">商品刪除</th>
		        
		      </tr>
		    </thead>
	  		<tbody>	
	  			
	 			<c:forEach items="${carGoods}" var="good">
						<tr height="30">
							
							<td>${good.key.goodsID}</td>
							<td>${good.key.goodsName}</td>
							<td>
								<div class="goods-price" data-id="${good.key.goodsID}">${good.key.goodsPrice}</div>
							</td>
							
							<td>
								<input type="number" value="${good.value}" max="${good.key.goodsQuantity}" min="1" id="quantity" data-id="${good.key.goodsID}" class="goods-quantity">
								<p class="card-text" style="color: blue;" >(剩餘 ${good.key.goodsQuantity} )</p>
							</td>
							<td>
							<div class="good-total" data-id="${good.key.goodsID}">${good.key.goodsPrice * good.value}</div>
							</td>
							<td><button onclick="delCartGood(${good.key.goodsID})" class="btn btn-outline-danger">刪除</button></td>
						</tr>	
				</c:forEach>
	  				
	  		</tbody>
	  		</table>
	  		<p class="h5 text-info" align="center">購物車總金額:</p>
	  		<div id="totalPrice" class="h5 text-info" align="center">
	  		<fmt:formatNumber value="${total}" type="number" pattern="$ #,###"/>
	  		</div>
	  		<div id="note" class="h5 text-danger" align="center"></div>		
<!--  			 </form>   -->
	    </div>

	     <div class="col-md-2 order-md-2 mb-2">
 			<img border="0" src="DrinksImage/coffee.jpg" width="200" height="200" >
 			<h4 class="d-flex justify-content-between align-items-center mb-3">
	           
	            <span class="text-muted">結帳專區</span>
	            <span class="badge badge-secondary badge-pill">${fn:length(carGoods)}</span>
          	</h4>
 			
 			<form action="FrontendAction.do?action=buyGoods" id="addBuyForm" method="post"> 
					<p class="text-primary">投入金額:</p>
					<input type="number" class="form-control" name="inputMoney" id="inputMoney"  required>
					<br/>
					<p align="right">
						<button type="submit" name="goodSubmit" id="goodSubmit" class="btn btn-outline-primary" >結帳</button>
					</p>
					<br/><br/>

			</form>
			
			
			</div>
 		</div>
	</div>
	<br/><br/>
	
</div>
<footer class="footer">
      <div class="container">
      	<span class="text-muted">Copyright (C) WanTing Lin 2023ver.</span>
      </div>
</footer>

</body>

</html>