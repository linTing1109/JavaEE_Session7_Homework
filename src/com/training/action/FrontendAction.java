package com.training.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.dao.BeverageMembersDao;
import com.training.model.Goods;
import com.training.model.Member;
import com.training.model.Order;
import com.training.service.FrontendService;
import com.training.vo.BuyGoodsRtn;

import net.sf.json.JSONObject;

public class FrontendAction extends DispatchAction {
	private FrontendService frontendService = FrontendService.getInstance();
	private BeverageMembersDao beverageMembersDao = BeverageMembersDao.getInstance();

	//queryAllGoods:查詢全部商品(一開始進頁面使用)
	public ActionForward queryAllGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String action=request.getParameter("action");
		int pageNo = request.getParameter("pageNo")==null ? 1: Integer.parseInt(request.getParameter("pageNo"));
		List<Goods> goods = frontendService.queryGoods(pageNo);
		session.setAttribute("goods", goods);
		session.setAttribute("action", action);
		
		int count=frontendService.queryGoods();
		int pageCount=count%6 ==0 ? count/6 :count/6+1;
		
		session.setAttribute("pageNo", pageNo);//控制加入購物車使用
		session.setAttribute("pageCount", pageCount);//計算頁碼使用
		return mapping.findForward("vendingMachineView");
	}
	public ActionForward searchGoodsView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("vendingMachineView");
	}

	public ActionForward searchGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 商品名稱查詢&分頁功能
		HttpSession session = request.getSession();
		session.removeAttribute("pageNo");
		String searchKeyword = request.getParameter("searchKeyword");
		searchKeyword= searchKeyword!=null ? searchKeyword: (String) session.getAttribute("searchKeyword");
		String action=request.getParameter("action");
		int pageNo = Integer.parseInt(request.getParameter("pageNo"));
		if (searchKeyword == null)
			searchKeyword = "";// 若為null代表商品並無任何輸入文字 顯示全部搜尋結果(並分頁)

		Set<Goods> goods = frontendService.searchGoods(searchKeyword, pageNo);
		int count=frontendService.searchGoods(searchKeyword);
		int pageCount=count/6 ==0 ? count/6 :count/6+1;
		
		session.setAttribute("action", action);
		session.setAttribute("pageNo", pageNo);
		session.setAttribute("pageCount", pageCount);//計算頁碼使用
		session.setAttribute("goods", goods);//查詢後就覆蓋掉原本搜尋全部的商品
		session.setAttribute("searchKeyword", searchKeyword);
//		goods.stream().forEach(g -> System.out.println(g));
		return mapping.findForward("vendingMachineView");
	}

	// 商品購買
	public ActionForward buyGoodsView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("vendingMachineView");
	}

	// 商品購買
	public ActionForward buyGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 取出登入的身分證字號 並回傳至customerId (運用於最後新增訂單的帳號)
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		int inputMoney = Integer.parseInt(request.getParameter("inputMoney"));// 投入的金額
		String buyMsg = null;
		// 取到購物車內的資料
		Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
		if (session.getAttribute("carCheck") != null) {// 只先做個防呆 避免沒更新商品最新價格直接購買
			boolean carCheck = (boolean) session.getAttribute("carCheck");
			if(carCheck) {
				BuyGoodsRtn buyGoodsRtn  =  frontendService.buyGoodsProcess(member, carGoods, inputMoney);// 會員,購物車,投入金額
				if (buyGoodsRtn.checkSuccess) {// 若有購買成功需要清空購物車
					session.removeAttribute("carGoods");// 結帳完 購物車要清空
					session.removeAttribute("carCheck");// 結帳完 購物車確認要移除 下個購物確認才可以檢查
					session.removeAttribute("total");//結帳完 清理總金額
				}
				session.setAttribute("buyGoodsRtn", buyGoodsRtn);
				session.setAttribute("buyGoodsRtnMsg", buyGoodsRtn.getBuyGoodMsg());
			}
			else {
				buyMsg="請先加入購物車後<br>在按下 (執行購物車商品)列表 <br>做最後金額確認";
			}
		} else {
			buyMsg="請先加入購物車";
		}
		
		session.setAttribute("buyMsg", buyMsg);
		return mapping.findForward("vendingMachine");
	}

	// 查詢顧客訂單
	public ActionForward queryIdOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		String customerID = member.getIdentificationNo();
		String reportMsg="";
		
//		System.out.println("歷史訂單customerID:" + customerID);
		Set<Order> reports = frontendService.queryIdOrder(customerID);
		if (reports.isEmpty()) {
//			System.out.println("無任何訂購紀錄");
			reportMsg="無任何訂購紀錄";
		} 
//		else {
//			reports.stream().forEach(r -> System.out.println(r));
//		}
		
		
		
		session.setAttribute("reports", reports);
		return mapping.findForward("vendingMachineOrder");
	}
	
	public ActionForward queryIdSalesReportView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		return mapping.findForward("idSaleReportView");
	}
	
	public ActionForward queryIdSalesReport(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		Member member = (Member) session.getAttribute("member");
		String id=member.getIdentificationNo();
//		System.out.println();
		String queryStartDate = request.getParameter("queryStartDate");
		String queryEndDate = request.getParameter("queryEndDate");
		String reportMsg="";
		if(queryStartDate == "" || queryEndDate =="") {
			reportMsg="查詢起訖時間沒選,預設顯示今日訂單!";
		}
		Set<Order> reports = frontendService.queryOrderDate(queryStartDate, queryEndDate,id);
//		reports.stream().forEach(r -> System.out.println(r));
		reportMsg=reports.isEmpty() ? reportMsg+"查詢無結果": reportMsg+"共"+reports.size()+"筆訂單";
		session.setAttribute("reports", reports);
		session.setAttribute("queryStartDate", queryStartDate);
		session.setAttribute("queryEndDate", queryEndDate);
		session.setAttribute("reportMsg", reportMsg);
		return mapping.findForward("idSaleReport");
	}
	
	// 更新會員
  	public ActionForward updateMemberView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
  			HttpServletResponse response) throws IllegalAccessException, InvocationTargetException {
  		return mapping.findForward("updateMemberView");
  	}
  	
  	// 更新會員
      public ActionForward updateMember(ActionMapping mapping, ActionForm form, 
              HttpServletRequest request, HttpServletResponse response) throws IOException {
    	  HttpSession session = request.getSession();
      	// 被選擇要修改的member
    	Member member=(Member) session.getAttribute("member");
    	member.setIdentificationNo(request.getParameter("identificationNo"));
    	
    	member.setPassword(request.getParameter("password"));
    	member.setCustomerName(request.getParameter("customerName"));
//    	System.out.println("測試"+member.getIdentificationNo());
//    	System.out.println("測試"+member.getPassword());
//    	System.out.println("測試"+member.getCustomerName());
      	boolean updateSuccess = beverageMembersDao.updateMember(member);
      	String updateMsg=updateSuccess ? "會員資料更新成功":"會員資料更新失敗";
      	System.out.println(updateMsg);
      	session.setAttribute("updateMsg", updateMsg);
 		
      	return mapping.findForward("updateMember");
      }
      
      
}
