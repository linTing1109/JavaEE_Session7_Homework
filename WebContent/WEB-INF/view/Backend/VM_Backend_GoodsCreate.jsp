<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>後臺商品新增上架</title>
<script type="text/javascript">

//判斷上傳是否使用圖檔
function validateImageFile(filePic) {
	var file = filePic.files[0];	
  // file.name:文件名稱,split:利用.做分隔 ,pop取出最後一個元素並統一換成小寫
   var extension = file.name.split('.').pop().toLowerCase();

  // 判斷是否為圖片
  if (extension != "jpg" && extension != "jpeg" && extension != "png" && file.type.indexOf("image/") != 0) {
    alert("請上傳圖檔 (例如:jpg, jpeg, or png)");
    filePic.value = "";//並且將原先的清空 讓使用者重選
    return false;
  }

  return true;
}

function showFileName(filePic) {
    var fileName = filePic.files[0].name;
    var fileNameSpan = document.getElementById("fileName");
    fileNameSpan.innerHTML = fileName;
}
</script>
<script>
// Bootstrap驗證:若有錯誤欄位禁止提交invalid fields
(function() {
  'use strict';
  window.addEventListener('load', function() {
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.getElementsByClassName('needs-validation');
    // Loop over them and prevent submission
    var validation = Array.prototype.filter.call(forms, function(form) {
      form.addEventListener('submit', function(event) {
        if (form.checkValidity() === false) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add('was-validated');
      }, false);
    });
  }, false);
})();
</script>
</head>
<body>
	<div class="container"><%--主要讓畫面左右留空 不完全黏著 --%>
	<%@ include file="VM_Backend_Menu.jsp" %>
	
	<HR>
	<h3 class="h3 mb-3 font-weight-normal">商品新增上架</h3>	
	<div style="margin-left:25px;">
	<%--新增訊息狀態 --%>
	<p class="text-success" align="left">${updateMsg}</p>
	<% session.removeAttribute("updateMsg"); %>
	
	<form action="BackendAction.do?action=addGoods" enctype="multipart/form-data" method="post" class="needs-validation" novalidate>
		<div class="form-row">
			<div class="form-group col-md-3">
		        	<label for="goodsName">飲料名稱</label>
		        	<input type="text" class="form-control" name="goodsName" id="goodsName" placeholder="Enter Goods Name" required>
<!-- 		        	<div class="valid-feedback">Looks good!</div> -->
		        	<div class="invalid-feedback">請輸入飲料名稱</div>
		    </div>
		    
			<div class="form-group col-md-3">
			    <label for="goodsPrice">設定價格</label>
			    <input type="number" class="form-control" name="goodsPrice" id="goodsPrice"  required>
			    <div class="invalid-feedback">請輸入"數字":飲料價格</div>
		    </div>
			<div class="form-group col-md-3">
			    <label for="goodsQuantity">初始數量</label>
			    <input type="number" class="form-control" name="goodsQuantity" id="goodsQuantity"  required>
			    <div class="invalid-feedback">請輸入"數字":初始數量</div>
		    </div>
		</div>    
		<div class="form-row">
			<div class="form-group col-md-3">
			    <label for="description">商品描述(選填)</label>
			    <textarea class="form-control" id="description"  name="description" rows="3" placeholder="Enter Goods description"></textarea>
		    </div>
			
		    <div class="form-group col-md-3">
	          <label for="status">商品狀態</label>
	          <select id="status" name="status" class="form-control">
	            <option value="1">上架</option>
				<option value="0">下架</option>
	          </select>
        	</div>
        	<div class="form-group col-md-3">
        	<br>
	        	<div class="custom-file">
				    <label class="custom-file-label" for="goodsImage">商品圖片</label>
				    <input type="file" accept=".jpg,.jpeg,.png" class="custom-file-input"  id="goodsImage" name="goodsImage" required onchange="validateImageFile(this); showFileName(this);">
				    <span id="fileName"></span>
				    <div class="invalid-feedback">請選擇圖片檔案(.jpg,.jpeg,.png)</div>
				</div>
			</div>
       </div>
       <button type="submit" class="btn btn-primary">新增</button> 	
	</form>
	</div>
	</div>
</body>
</html>