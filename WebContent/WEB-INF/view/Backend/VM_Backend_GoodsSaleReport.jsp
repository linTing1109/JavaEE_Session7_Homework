<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>後臺銷售報表</title>
</head>
<body>
	<div class="container"><%--主要讓畫面左右留空 不完全黏著 --%>
	<%@ include file="VM_Backend_Menu.jsp" %>
	
	<HR>
	<h3 class="h3 mb-3 font-weight-normal">銷售報表</h3>
	
	<div style="margin-left:25px;">
	<form action="BackendAction.do" method="get">
		<input type="hidden" name="action" value="querySalesReport"/>
		<p class="text-info">
		起 &nbsp; <input type="date" name="queryStartDate" value="${queryStartDate}" style="height:25px;width:180px;font-size:16px;text-align:center;"/>
		&nbsp;
		迄 &nbsp; <input type="date" name="queryEndDate" value="${queryEndDate}"  style="height:25px;width:180px;font-size:16px;text-align:center;"/>	
		<button type="submit" class="btn btn-outline-primary" style="margin-left:25px">查詢</button>
		</p>
		<p class="text-success" align="left">${reportMsg}</p>
		<% session.removeAttribute("reportMsg"); %>
	</form>
	
	
	<table class="table table-hover">
    <thead>
      <tr>
        <th scope="col">訂單編號</th>
        <th scope="col">顧客姓名</th>
        <th scope="col">購買日期</th>
        <th scope="col">飲料名稱</th>
        <th scope="col">購買單價</th>
        <th scope="col">購買數量</th>
        <th scope="col">購買金額</th>
      </tr>
    </thead>
    <tbody>
    	<c:forEach items="${reports}" var="report">
			<tr>
				<th scope="row">${report.orderID}</th>
				<td>${report.customerName}</td>
				<td>${report.orderDate}</td>
				<td>${report.goodsName}</td>
				<td>${report.goodsByPrice}</td> 
				<td>${report.buyQuantity}</td>
				<td>${report.buyAmount}</td>	
			</tr>
		</c:forEach>
    </tbody>
  </table>
	<%--使用完移除全部 避免畫面重整再次秀出 --%>
	<% session.removeAttribute("reports"); %>
	<% session.removeAttribute("queryStartDate"); %>
	<% session.removeAttribute("queryEndDate"); %>
	
	</div>
	</div>
</body>
</html>