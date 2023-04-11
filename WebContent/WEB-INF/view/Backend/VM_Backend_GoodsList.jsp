<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/js" var="JS_PATH"/>    
<!DOCTYPE html>
<html>
<head>
<title>後臺商品列表</title>

<script src="${JS_PATH}/jquery-1.11.1.min.js"></script>	
<script type="text/javascript">
  // 取得當前網址
  $(document).ready(function() {
    var currentURL = window.location.href;
    // 檢查網址是否符合 'queryGoods' 或 'searchGoodsPage'  只點選查詢全部網頁頁面
    if (currentURL.includes('queryGoods') || currentURL.includes('searchGoodsPage')) {
      clearTable();// 呼叫函式
    }
    
  });
  //欄位全部清空
  function clearTable() {
	  // 取得表單元素
      var form = document.getElementById("searchForm");

      // 取得所有輸入欄位元素
      var inputs = form.getElementsByTagName("input");
      var selects = form.getElementsByTagName("select"); // 取得所有 <select> 元素

      // 將每個輸入欄位的值設為空字串
      for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].type === "text" || inputs[i].type === "number") {
          inputs[i].value = "";
        }
      }

      // 將每個 <select> 元素的值設為空字串
      for (var i = 0; i < selects.length; i++) {
        selects[i].value = "";
      }
    }
  
</script>

</head>
<body>
	<div class="container"><%--主要讓畫面左右留空 不完全黏著 --%>
	<%@ include file="VM_Backend_Menu.jsp" %>
	
	<HR>
	<h3 class="h3 mb-3 font-weight-normal">商品列表</h3>
	<p class="text-success" align="left">${goodsMsg}</p>
	<% session.removeAttribute("goodsMsg"); %>
	
	<div style="margin-left:25px;">
	<form action="BackendSearch.do" method="post" id="searchForm">
		<input type="hidden" name="action" value="searchGoodsList"/>
		<input type="hidden" name="pageNo" value="1"/>
		<input type="hidden" name="searchAction" value="open"/>
		<div class="form-row">
			<div class="form-group col-md-3">
	        	<label for="goodsId">商品編號</label>
	        	<input type="number" class="form-control" name="goodsId" id="goodsId" placeholder="Enter Goods Number" value="${goodsSearchItem.goodsId}">
	      	</div>
			<div class="form-group col-md-3">
	        	<label for="goodsName">商品名稱(不區分大小寫)</label>
	        	<input type="text" class="form-control" name="goodsName" id="goodsName" placeholder="Enter Goods Name" value="${goodsSearchItem.goodsName}">
	      	</div>
			<div class="form-group col-md-3">
	        	<label for="priceMin">商品價格最低價</label>
	        	<input type="number" class="form-control" name="priceMin" id="priceMin" placeholder="Enter goods price Min" value="${goodsSearchItem.priceMin}">
	      	</div>
			<div class="form-group col-md-3">
	        	<label for="priceMax">商品價格最高價</label>
	        	<input type="number" class="form-control" name="priceMax" id="priceMax" placeholder="Enter goods price Max" value="${goodsSearchItem.priceMax}">
	      	</div>
		</div>
		<div class="form-row">
			<div class="form-group col-md-3">
	          <label for="orderByItem">排序方式</label>
	          <select id="orderByItem" name="orderByItem" class="form-control">
	            <option value="" <c:if test="${ goodsSearchItem.orderByItem == ''}">selected</c:if>>價格(低->高)</option>
				<option value="priceDesc" <c:if test="${ goodsSearchItem.orderByItem == 'priceDesc'}">selected</c:if>>價格(高->低)</option>
	            <option value="idAsc" <c:if test="${ goodsSearchItem.orderByItem == 'idAsc'}">selected</c:if>>編號(低->高)</option>
				<option value="idDesc" <c:if test="${ goodsSearchItem.orderByItem == 'idDesc'}">selected</c:if>>編號(高->低)</option>
	            <option value="quantityAsc" <c:if test="${ goodsSearchItem.orderByItem == 'quantityAsc'}">selected</c:if>>庫存(低->高)</option>
				<option value="quantityDesc" <c:if test="${ goodsSearchItem.orderByItem == 'quantityDesc'}">selected</c:if>>庫存(高->低)</option>
	          </select>
        	</div>
			<div class="form-group col-md-3">
	        	<label for="stockQuantity">商品低於庫存量</label>
	        	<input type="number" class="form-control" name="stockQuantity" id="stockQuantity" placeholder="Enter goods stock quantity" value="${goodsSearchItem.stockQuantity}">
	      	</div>
			<div class="form-group col-md-3">
	          <label for="status">商品狀態</label>
	          <select id="status" name="status" class="form-control">
				<option value="" <c:if test="${ goodsSearchItem.status == ''}">selected</c:if>>ALL</option>
				<option value="1"<c:if test="${ goodsSearchItem.status == '1'}">selected</c:if>>上架</option>
				<option value="0"<c:if test="${ goodsSearchItem.status == '0'}">selected</c:if>>下架</option>
	          </select>
        	</div>
        	<div class="form-group col-md-3">
        		<div class="form-group col-md-9">
	        	<input type="submit" class="form-control" >
	        	<button class="form-control" onclick="clearTable()">清空</button>
	        	</div>
	      	</div>
		</div>
		
	</form>
	
	<table class="table table-hover">
    <thead>
      <tr>
        <th scope="col">商品編號</th>
        <th scope="col">商品名稱</th>
        <th scope="col">商品價格</th>
        <th scope="col">現有庫存</th>
        <th scope="col">商品狀態</th>
      </tr>
    </thead>
    <tbody>
    	<c:forEach items="${goodsAll}" var="good">
			<tr height="30">
					<th scope="row">${good.goodsID}</th>
					<td>${good.goodsName}</td>
					<td>${good.goodsPrice}</td>
					<td>${good.goodsQuantity}</td>
					<td>
					<c:if test="${good.status eq 1}"><font color="blue">上架</font></c:if>
					<c:if test="${good.status eq 0}"><font color="red">下架</font></c:if>
					</td>
			</tr>
		</c:forEach>
    </tbody>
  </table>
  <div class="row">
	
	<div class="col-4">
  	<c:choose>
			<c:when test="${pageNo %3 ==1}">
				<c:set var="startPage" scope="session" value="${pageNo}"></c:set>
			</c:when>
			<c:when test="${pageNo %3 ==2}">
				<c:set var="startPage" scope="session" value="${pageNo-1}"></c:set>
			</c:when>
			<c:when test="${pageNo %3 ==0}">
				<c:set var="startPage" scope="session" value="${pageNo-2}"></c:set>
			</c:when>
		
	</c:choose>
	<ul class="pagination">
      	<c:if test="${pageNo > 1 }">
      		<li class="page-item">
        		<a class="page-link" href="BackendAction.do?action=${action}&pageNo=${pageNo-1}">上一頁</a>
     		 </li>
		</c:if>
		<c:forEach var="i" begin="${startPage}" end="${startPage+2}" step="1" varStatus="status">
			<c:if test="${i le pageBackend}">
				<c:url value="/BackendAction.do" var="searchPage">
					<c:param name="action" value="${action}"></c:param>
					<c:param name="pageNo" value="${i}"></c:param>
				</c:url>
				<li class="page-item"><a class="page-link" href="${searchPage}" <c:if test="${i eq pageNo }">style="color:red;"</c:if> >${i}</a></li>
			</c:if>
		</c:forEach>
      	<c:if test="${pageNo < pageBackend }">
	      <li class="page-item">
	        <a class="page-link" href="BackendAction.do?action=${action}&pageNo=${pageNo+1}">下一頁</a>
	      </li>
		</c:if>	
    </ul>
	</div>
	<%-- pageCount > 1 有一頁以上才會秀出跳出頁碼的部分 --%>
		<c:if test="${pageBackend > 1}">
			<div class="col-5">
			<form action="BackendAction.do?action=${action}" method="post"> 
		 		<span class="text-muted">頁碼( 1~ ${pageBackend} ):</span>
				
				<input type="number" name="pageNo" id="pageNo" min="1" max="${pageBackend}" value="1" required>
				<button type="submit" class="btn btn-outline-primary" >跳至頁面</button>
			</form>
		</div>
		</c:if>
	</div>
	
						
	</div>
	</div>
</body>
</html>