<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<!DOCTYPE html>
<html>
<head>
<title>前臺商品購物</title>

<%-- <script src="${JS_PATH}/jquery-1.11.1.min.js"></script> --%>
	<script type="text/javascript">

		//商品加入購物車功能
		function addCartGoods(goodsID, buyQuantityIdx){
			
			console.log("goodsID:", goodsID);	
			var buyQuantity = document.getElementsByName("buyQuantity")[buyQuantityIdx].value;
			console.log("buyQuantity:", buyQuantity);
			const formData = new FormData();//裝符合數量的購物車formData
			formData.append('action', 'addCartGoods');
			var goodsParam = {id : goodsID};
// 			alert(action);
			
			$.ajax({
				  url: '${WEB_PATH}MemberAction.do?action=getGoods', // 指定要進行呼叫的位址
				  type: "GET", // 請求方式 POST/GET
				  data: goodsParam, // 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
				  dataType : 'JSON', // Server回傳的資料類型
				  async : false, // 是否同部請求
				  cache : false, // 從瀏覽器中抓 cache
				  success: function(goodData) { // 請求成功時執行函式					  	
				  	// JSON.stringify：JSON ->　String
// 				  	alert(goodData.goodsQuantity);//回傳的即時庫存
// 				  	alert(buyQuantity);//購買的數量
				  	if(buyQuantity > goodData.goodsQuantity){
				  		alert('超出購買數量 請重新輸入');
				  		// 將畫面輸入不符合的歸零
				  		document.getElementsByName("buyQuantity")[buyQuantityIdx].value="0";
				  		buyQuantity=0;
				  		formData.append('buyQuantity', 0);
				  	}
				  	else{
				  		
			 			formData.append('goodsID', goodsID);
			 			formData.append('buyQuantity', buyQuantity);
				  	}
				  }, 
				  error: function() { // 請求發生錯誤時執行函式
				    alert("Ajax Error!");
				  }
			});
			
			// 送出商品加入購物車請求
			const request = new XMLHttpRequest();
			// 第三個參數是代表非同步
			request.open("POST", "MemberAction.do");
			request.send(formData);

			setTimeout(() => {
				location=location;
// 				history.go(0);
			}, 300);

		}
	
</script>

</head>
<body align="center">
<div class="container"><%--主要讓畫面左右留空 不完全黏著 --%>
	<%@ include file="VendingMachine_Menu.jsp" %>
	<br/>
	  <div class="row">
		<%--卡片疊(主要秀出商品使用) --%> 
		<div class="col-md-10 ">
			<h1 class="h1 mb-3 font-weight-normal">商品列表</h1>	
		    <div class="card-deck">
		    <c:forEach items="${goods}" begin="0" end="2" var="good" varStatus="status" >
		      <div class="card col-md-2.5">
		        <img class="card-img-top" src="DrinksImage/${good.goodsImageName}" alt="Card image cap"><!-- 各商品圖片 -->
		        <div class="card-body">
		          <h5 class="card-title">${good.goodsName}</h5><!--商品名稱 -->
		          <p class="card-text" style="color: blue;" >${good.goodsPrice} 元/罐<!-- 商品價格 --><p/>
		          <p class="card-text" style="color: red;"><small>(庫存 ${good.goodsQuantity} 罐)</small></p>
		          購買<input type="number" name="buyQuantity" min="0" max="${good.goodsQuantity}" size="5" value=0>罐
		          <button onclick="addCartGoods(${good.goodsID},${status.index})" class="btn btn-primary"
		          <c:if test="${good.goodsQuantity == 0}">disabled="disabled"</c:if>>加入購物車</button>
		        </div>        
		      </div>
		      </c:forEach>
		    </div>
		    <div class="card-deck">
		    <c:forEach items="${goods}" begin="3" end="5" var="good" varStatus="status" >
		      <div class="card col-md-2.5">
		        <img class="card-img-top" src="DrinksImage/${good.goodsImageName}" alt="Card image cap"><!-- 各商品圖片 -->
		        <div class="card-body">
		          <h4 class="card-title">${good.goodsName}</h4><!--商品名稱 -->
		          <p class="card-text" style="color: blue;">${good.goodsPrice} 元/罐<!-- 商品價格 --><p/>
		          <p class="card-text" style="color: red;"><small>(庫存 ${good.goodsQuantity} 罐)</small></p>
		          購買<input type="number" name="buyQuantity" min="0" max="${good.goodsQuantity}" size="5" value=0>罐	
		          <button onclick="addCartGoods(${good.goodsID},${status.index})" class="btn btn-primary"
		          <c:if test="${good.goodsQuantity == 0}">disabled="disabled"</c:if>>加入購物車</button>
		        </div>        
		      </div>
		      </c:forEach>
		    </div>
		    
	    </div>

	     <div class="col-md-2 order-md-2 mb-2">
 			<img border="0" src="DrinksImage/coffee.jpg" width="200" height="200" >
 			<h4 class="d-flex justify-content-between align-items-center mb-3">
	           
	            <span class="text-muted">快速結帳</span>
	            <span class="badge badge-secondary badge-pill">${fn:length(carGoods)}</span>
          	</h4>
 			
 			<form action="FrontendAction.do?action=buyGoods" id="addBuyForm" method="post"> 
					<p class="text-primary">投入金額:</p>
					<input type="number" class="form-control" name="inputMoney" id="inputMoney"  required>
					<br/>
					<p align="right">
						<%--判斷是否已經確認過目前價格 才可以結帳 --%>
						<button type="submit" name="goodSubmit" id="goodSubmit" class="btn btn-outline-primary" 
						<c:if test="${carCheck != true}">disabled="disabled"</c:if>
						<c:if test="${carCheck == true}"></c:if>
						
						>結帳</button>
					</p>
					<br/><br/>

			</form>
			
			<%--訊息顯示對話框 --%>
			<div style="border-width:2px;border-style:dashed;border-color:#FFAC55;padding:5px;width:250px;"> 
 				 <p class="text-secondary">購物流程:<br/>加入->確認商品->結帳</p>
 				
 				<%--購物車添加狀態--%>
 				<p class="text-primary">${carMsg}</p>
 				<% session.removeAttribute("carMsg"); %>
 				
				<%--購物車目前內容 (並且不為空 才秀出) --%>
 				<p class="text-primary">
	 				<c:if test="${carGoods ne null}"> 
						<c:forEach items="${carGoods}" var="good">
							${good.key.goodsName} : 
							<br/>${good.key.goodsPrice}元 *${good.value}個<br/>
						</c:forEach>
	  				</c:if> 
 				</p>
				
				<%--按購物車商品列表 同時有商品 則會秀出總金額 --%>
 				<c:if test="${carCheck eq true && total ne null}">總金額:
					<fmt:formatNumber value="${total}" type="number" pattern="$ #,###"/>
				</c:if>
				
				<%--購買中失敗訊息顯示(沒加入購物車或者沒進入到結帳頁 --%>
 				<p class="text-primary">${buyMsg}</p>
 				<% session.removeAttribute("buyMsg"); %>
				
				<%--結帳時的訊息顯示 --%>
 				<c:if test="${buyGoodsRtn.checkSuccess ne true}">
					<p class="text-primary">${buyGoodsRtnMsg}</p>
					<% session.removeAttribute("buyGoodsRtnMsg"); %>
				</c:if>
				
				<%--投入金額足夠 秀出購買明細 --%>
 				<c:if test="${buyGoodsRtn.checkSuccess}">
 					<p class="text-success">~~消費明細 ~~</p>
					<p class="text-secondary">
						投入金額：${buyGoodsRtn.inputMoney}
					</p>
					<p class="text-secondary">
						購買金額：${buyGoodsRtn.sumOrderAmount}
					</p>
					<p class="text-secondary">
						找零金額：${buyGoodsRtn.changeDollars}
					</p>
					
					<c:forEach items="${buyGoodsRtn.getGoodsOrders()}" var="goodItem">
					<p class="text-primary">
						${goodItem.key.goodsName} :
						<br/>${goodItem.key.goodsPrice} * ${goodItem.value}
					</p>
					</c:forEach>
					
					<% session.removeAttribute("buyGoodsRtn"); %>
				</c:if>
			</div>
 		</div>
	</div>
	<br/><br/>
	<h5 class="page">
	<div class="row">
	
	<div class="col-4">
	<%--頁碼顯示區域 --%>
	<ul class="pagination">
		<%--上一頁:只有當頁面>1時候才會顯示 --%>
		<li class="page-item">	
		<c:if test="${pageNo > 1 }">
<%-- 				<a href="FrontendAction.do?action=${action}&pageNo=${pageNo-1}">上一頁</a> --%>
				<a class="page-link" href="FrontendAction.do?action=${action}&pageNo=${pageNo-1}">上一頁</a>
		</c:if>
		</li>
		<%--每三頁為一組  若1/%3 =1  --%>
		<c:choose>
			<%--避免pageNo=1 沒寫沒有startPage 這樣會從0開始顯示 --%>
			<c:when test="${pageNo %3 == 1}">
				<c:set var="startPage" scope="session" value="${pageNo}"></c:set>
			</c:when>
			<c:when test="${pageNo %3 == 2}">
				<c:set var="startPage" scope="session" value="${pageNo-1}"></c:set>
			</c:when>
			<c:when test="${pageNo %3 == 0}">
				<c:set var="startPage" scope="session" value="${pageNo-2}"></c:set>
			</c:when>
		
		</c:choose>
		<%--每次只秀三頁 ${startPage}~${startPage+2} --%>
		<c:forEach var="i" begin="${startPage}" end="${startPage+2}" step="1" varStatus="status">
			<%--只有符合總頁數pageCount 才會顯示 --%>
			<c:if test="${i le pageCount}">
				<c:url value="/FrontendAction.do" var="searchPage">
					<c:param name="action" value="${action}"></c:param>
					<c:param name="pageNo" value="${i}"></c:param>
				</c:url>
				<!-- 如果當頁 顯示紅色數字 -->
				<li class="page-item"><a class="page-link" href="${searchPage}" <c:if test="${i eq pageNo }">style="color:red;"</c:if>>${i}</a></li>
<%-- 					<a href="${searchPage}" <c:if test="${i eq pageNo }">style="color:red;"</c:if>	>${i}</a> --%>
			</c:if>
		</c:forEach>
		<%--下一頁:只有當頁面<總頁數pageCount 時候才會顯示 --%>
		<li class="page-item">	
		<c:if test="${pageNo < pageCount }">
			 <a class="page-link" href="FrontendAction.do?action=${action}&pageNo=${pageNo+1}">下一頁</a>
<%-- 			<a href="FrontendAction.do?action=${action}&pageNo=${pageNo+1}">下一頁</a> --%>
					
		</c:if>
		</li>	
	</ul>
	</div>
		<%-- pageCount > 1 有一頁以上才會秀出跳出頁碼的部分 --%>
		<c:if test="${pageCount > 1}">
			<div class="col-5">
			<form action="FrontendAction.do?action=${action}" method="post"> 
		 		<span class="text-muted">頁碼( 1~ ${pageCount} ):</span>
				
				<input type="number" name="pageNo" id="pageNo" min="1" max="${pageCount}" value="1" required>
				<button type="submit" class="btn btn-outline-primary" >跳至頁面</button>
			</form>
		</div>
		</c:if>
	</div>
	
	</h5>
<!-- 首頁輪播 -->

	<div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">
      <ol class="carousel-indicators">
        <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="3"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="4"></li>
        <li data-target="#carouselExampleIndicators" data-slide-to="5"></li>
      </ol>
      <div class="carousel-inner">
        <div class="carousel-item active">
          <img class="d-block w-100" src="img/Snap0.png">
        </div>
        <div class="carousel-item">
          <img class="d-block w-100" src="img/Snap1.png">
        </div>
        <div class="carousel-item">
          <img class="d-block w-100" src="img/Snap2.png">
        </div>
        <div class="carousel-item">
          <img class="d-block w-100" src="img/Snap3.png">
        </div>
        <div class="carousel-item">
          <img class="d-block w-100" src="img/Snap4.png">
        </div>
        <div class="carousel-item">
          <img class="d-block w-100" src="img/Snap5.png">
        </div>   
      </div>
      <a class="carousel-control-prev" href="#carouselExampleIndicators" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
      </a>
      <a class="carousel-control-next" href="#carouselExampleIndicators" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
      </a>
	</div>


</div>
<footer class="footer">
      <div class="container">
      	<span class="text-muted">Copyright (C) WanTing Lin 2023ver.</span>
      </div>
</footer>

</body>

</html>