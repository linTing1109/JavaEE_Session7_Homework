package com.training.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import com.training.formbean.BackedActionForm;
import com.training.formbean.BackedListForm;
import com.training.model.Goods;
import com.training.model.Order;
import com.training.service.BackendService;
import com.training.vo.GoodsSearchItem;

import net.sf.json.JSONObject;

@MultipartConfig
public class BackendAction extends DispatchAction {
	public final static SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
	private BackendService backendService = BackendService.getInstance();
	
	// 查詢全部的
	public ActionForward queryGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		List<Goods> goods = backendService.queryGoods();
		return mapping.findForward("goodsListView");
	}
	// 查詢分頁的功能
	public ActionForward searchGoodsPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		int pageNo = request.getParameter("pageNo")==null ? 1: Integer.parseInt(request.getParameter("pageNo"));
		List<Goods> goods = backendService.searchGoodsPage(pageNo);
		int count=backendService.queryGoods().size();
		String goodsMsg="全部商品列表 共"+count+"個商品";
		int pageBackend=count/6 ==0 ? count/6 :count/6+1;
		
		session.setAttribute("pageBackend", pageBackend);//計算頁碼使用
		session.setAttribute("goodsMsg", goodsMsg);
		session.setAttribute("action", request.getParameter("action"));
		session.setAttribute("goodsAll", goods);
		session.setAttribute("pageNo", pageNo);
//		goods.stream().forEach(g -> System.out.println(g));
		return mapping.findForward("goodsList");
	}
	//更新商品
	public ActionForward updateGoodsView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();
		//查詢到全部的商品(選單資料選擇使用)
		List<Goods> goods = backendService.queryGoods();
		session.setAttribute("goods", goods);
		
		// 被選擇要修改的商品id
		String goodID=request.getParameter("goodsID");
		//需要將Long轉型為String 否則送出修改會出錯
		String modifyGoodID =null;
		if(session.getAttribute("modifyGoodID") !=null) {
			modifyGoodID=String.valueOf(session.getAttribute("modifyGoodID"));
		}
		
		goodID = (goodID != null) ? goodID : modifyGoodID;
		
		if( goodID != null) {//用ID找出GOODS
			Goods good=backendService.queryGoodsId(goodID);
			session.setAttribute("modifyGood", good);
		}
		return mapping.findForward("goodsReplenishmentView");
	}
	
	// 更新商品(非同步):for AJAX 使用
    public ActionForward getModifyGoods(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws IOException {
    	// 被選擇要修改的商品id
    	String goodID=request.getParameter("id");
		Goods good=backendService.queryGoodsId(goodID);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		//轉換為JSON格式
		out.println(JSONObject.fromObject(good));
		out.flush();
		out.close();
		
    	return null;
    }
	
	public ActionForward updateGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();
		BackedActionForm backedActionForm = (BackedActionForm) form;
		Goods goods = new Goods();
		// 複製一份 避免汙染 資料來源:backedActionForm,複製到:goods
		BeanUtils.copyProperties(goods, backedActionForm);
//		System.out.println(goods.getDescription());
		boolean modifyResult = backendService.updateGoods(goods);
		String messageUpdate = modifyResult ? "商品編號:"+goods.getGoodsID()+" 商品維護作業成功！" : "商品維護作業失敗!";
		session.setAttribute("messageUpdate", messageUpdate);
		session.setAttribute("modifyGoodID",goods.getGoodsID());
		return mapping.findForward("goodsReplenishment");
	}
	
	public ActionForward addGoodsView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("goodsCreateView");
	}
	
	public ActionForward addGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		BackedActionForm backedActionForm = (BackedActionForm) form;
		Goods goods = new Goods();
//		// 避免有BigDecimal問題 所以增加下面兩行 (自定義轉換器)
//		BigDecimalConverter bd = new BigDecimalConverter(BigDecimal.ZERO);
//		ConvertUtils.register(bd, java.math.BigDecimal.class);
		BeanUtils.copyProperties(goods, backedActionForm);// struts 表單複製到goods
		// 得到圖片欄位 FormFile
		FormFile goodsImage = backedActionForm.getGoodsImage();
		goods.setGoodsImageName(goodsImage.getFileName());// 只放圖片的圖檔名稱
		int createResult = backendService.addGoods(goods);

		String updateMsg = createResult == 0 ? "商品上架失敗!" : "商品上架成功!商品編號:" + createResult;
		session.setAttribute("updateMsg", updateMsg);
		picUpload(goodsImage);// 上傳圖片
		return mapping.findForward("goodsCreate");
	}

	// 圖片上傳
	private void picUpload(FormFile goodsImage) throws ServletException, IOException {
		String goodsImgPath = getServlet().getInitParameter("GoodsImgPath");
		String serverGoodsImgPath = getServlet().getServletContext().getRealPath(goodsImgPath);
		String fileName = goodsImage.getFileName();
		Path serverImgPath = Paths.get(serverGoodsImgPath).resolve(fileName);
		try (InputStream fileContent = goodsImage.getInputStream();) {
			Files.copy(fileContent, serverImgPath, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	public ActionForward querySalesReportView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("goodsSaleReportView");
	}
	
	public ActionForward querySalesReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		String queryStartDate = request.getParameter("queryStartDate");
		String queryEndDate = request.getParameter("queryEndDate");
		String reportMsg="";
		if(queryStartDate == "" || queryEndDate =="") {
			reportMsg="查詢起訖時間沒選,預設顯示今日訂單!";
		}
		Set<Order> reports = backendService.queryOrderDate(queryStartDate, queryEndDate);
//		reports.stream().forEach(r -> System.out.println(r));
		reportMsg=reports.isEmpty() ? reportMsg+"查詢無結果": reportMsg+"共"+reports.size()+"筆訂單";
		session.setAttribute("reports", reports);
		session.setAttribute("queryStartDate", queryStartDate);
		session.setAttribute("queryEndDate", queryEndDate);
		session.setAttribute("reportMsg", reportMsg);
		return mapping.findForward("goodsSaleReport");
	}
	public ActionForward searchGoodsListView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		return mapping.findForward("goodsListView");
	}
	// 依照所有條件查出+page
	public ActionForward searchGoodsList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
		HttpSession session = request.getSession();
		GoodsSearchItem goodsSearchItem=(GoodsSearchItem) session.getAttribute("goodsSearchItem");
		
		//只要不為空 代表是輸入條件後送出的 所以就執行form部分
		if(request.getParameter("searchAction")!=null) {
			BackedListForm backedListForm = (BackedListForm) form;
			goodsSearchItem=new GoodsSearchItem();
			BeanUtils.copyProperties(goodsSearchItem, backedListForm);
		}
	
		int pageNo = request.getParameter("pageNo")==null ? 1: Integer.parseInt(request.getParameter("pageNo"));
		
		List<Goods> goods=backendService.searchGoodsListPage(goodsSearchItem,pageNo);
		int count=backendService.searchGoodsList(goodsSearchItem).size();
		String goodsMsg="搜尋商品 共"+count+"個符合";
		int pageBackend=count%6 ==0 ? count/6 :count/6+1;
		session.setAttribute("pageBackend", pageBackend);//計算頁碼使用
		
		session.setAttribute("goodsMsg", goodsMsg);
		session.setAttribute("action", request.getParameter("action"));
		session.setAttribute("goodsSearchItem", goodsSearchItem);//為了單純不搜尋只按分頁
		session.setAttribute("pageNo", pageNo);
		session.setAttribute("goodsAll", goods);//將查詢的部分完全蓋掉原本全部的
//		goods.stream().forEach(a -> System.out.println("商品編號:" + a.getGoodsID() + "  商品名稱:" + a.getGoodsName()
//				+ "  商品價格:" + a.getGoodsPrice() + "  現有庫存:" + a.getGoodsQuantity() + "  商品狀態:" + a.getStatus()));
		return mapping.findForward("goodsList");
	}
}
