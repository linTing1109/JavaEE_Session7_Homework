package com.training.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.dao.BeverageGoodsDao;
import com.training.formbean.MemberBuyForm;
import com.training.model.Goods;
import com.training.vo.ShoppingCartGoods;
import com.training.vo.ShoppingCartGoodsInfo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@MultipartConfig
public class MemberAction extends DispatchAction {

	private BeverageGoodsDao beverageGoodsDao = BeverageGoodsDao.getInstance();
	//判斷是否輸入大於庫存數量使用
	public ActionForward getGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long goodID=Long.parseLong(request.getParameter("id"));
		Goods goods = beverageGoodsDao.queryIdOrder(goodID);
		JSONObject queryResult = new JSONObject();
		queryResult.put("goodsQuantity", goods.getGoodsQuantity());
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(queryResult);
		out.flush();
		out.close();
		
		return null;
	}
	
	// 商品加入購物車addCartGoods
	public ActionForward addCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		MemberBuyForm memberBuyForm = (MemberBuyForm) form;
		Long goodsID = Long.parseLong(memberBuyForm.getGoodsID());
		Integer buyQuantity = Integer.parseInt(memberBuyForm.getBuyQuantity());
		String carMsg="";
		// 用goodsID抓商品資料
		Goods goods = beverageGoodsDao.queryIdOrder(goodsID);

		// 判斷購物車是否存在
		Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
		boolean existCar = session.getAttribute("carGoods") != null; // true 存在 ,false 不存在
		if (existCar) {// 如果購物車存在
			Goods good = new Goods(goodsID);// 用於判斷是否存在map的key
			if (carGoods.containsKey(good)) {// 購物車 原商品存在
//				System.out.println("購物車數量"+carGoods.get(good));
//				System.out.println("目前要買數量"+buyQuantity);
//				System.out.println("剩餘數量"+goods.getGoodsQuantity());
				if(goods.getGoodsQuantity()>=(carGoods.get(good)+buyQuantity)) {
					// v:購物車商品原數量
					carGoods.computeIfPresent(goods, (k, v) -> v + buyQuantity);// computeIfPresent:key存在 舊覆蓋value,覆寫GoodshashCode
					
					carMsg="商品數量更新成功";
				}else {
					//超出設定最多只能買剩餘數量
					carGoods.computeIfPresent(goods, (k, v) -> goods.getGoodsQuantity());
					carMsg="商品數量更新失敗(超出剩餘數量)";
				}
				
			} else {
				carGoods.put(goods, buyQuantity);
				carMsg="商品添加成功";
			}

		} else {// 購物車不存在
			carGoods = new LinkedHashMap<>();// 建立一個新的購物車
			carGoods.put(goods, buyQuantity);// 將剛剛購買商品與數量的放入
			carMsg="商品添加成功";
			session.setAttribute("carGoods", carGoods);
		}
		boolean carCheck = false;// 只要每次有加商品 carCheck=false 最後要去結帳頁面做確認才可以投幣送出
		session.setAttribute("carCheck", carCheck);
		session.setAttribute("carMsg", carMsg);

		return mapping.findForward("vendingMachine");// 重導回原頁面
//		return null;
	}

	// 查詢購物車商品
	public ActionForward queryCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean carCheck = false;// 購物車確認 若執行過queryCarGoods就會顯示true代表確認過金額
		HttpSession session = request.getSession();
		Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
		int total = 0;// 總金額

		if (carGoods != null) {
			// 運用id 查詢資料庫商品價錢
			Set<Goods> keys = carGoods.keySet();
			for (Iterator<Goods> i = keys.iterator(); i.hasNext();) {
				Goods good = i.next();
				Goods g = new Goods();
				Integer amount = carGoods.get(good);
				g = beverageGoodsDao.queryIdOrder(good.getGoodsID());
				good.setGoodsPrice(g.getGoodsPrice());// 全部重新設定一次新價錢
				total = total + good.getGoodsPrice() * amount;
			}
			carCheck = true;
		} 
		session.setAttribute("total", total);//結帳時預估總金額
		session.setAttribute("carCheck", carCheck);// 用來判斷投入金額時候是否經過這邊確認過商品最新金額

//		return null;
		return mapping.findForward("vendingMachine");// 重導回原頁面
	}

	// 清空購物車
	public ActionForward clearCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		// 清空購物車
		HttpSession session = request.getSession();
		session.removeAttribute("carGoods");
		session.removeAttribute("total");
		session.removeAttribute("carCheck");//避免先按購物車列表 清空馬上買
//		System.out.println("已清空購物車");
		return mapping.findForward("vendingMachine");// 重導回原頁面
	}

	
	//更新商品數量
    public ActionForward updateCheckOutView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
  		  HttpServletResponse response) throws IllegalAccessException, InvocationTargetException, IOException {
    	queryCartGoods(mapping, form, request, response);
  	  return mapping.findForward("updateCheckOutView");
    }
    
    // 更新商品數量
    public ActionForward updateCheckOut(ActionMapping mapping, ActionForm form, 
  		  HttpServletRequest request, HttpServletResponse response) throws IOException {
    	HttpSession session = request.getSession();
 		Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
 		Long goodsID=Long.parseLong(request.getParameter("goodsID"));
 		Integer quantity=Integer.parseInt(request.getParameter("quantity"));
// 		System.out.println(goodsID);
// 		System.out.println(quantity);
    	
 		Goods good = new Goods();
		good.setGoodsID(goodsID);
		carGoods.put(good, quantity);
 		
 		
  	  return mapping.findForward("updateCheckOut");
    }
	
    
    // 刪除購物車個別商品
    public void delCartGoods(ActionMapping mapping, ActionForm form, HttpServletRequest request,
 			HttpServletResponse response) throws IOException {
 		HttpSession session = request.getSession();
 		Map<Goods, Integer> carGoods = (Map<Goods, Integer>) session.getAttribute("carGoods");
 		Long goodsID=Long.parseLong(request.getParameter("goodsID"));
 		
 		if (carGoods != null && goodsID != null) {
 			
			Goods good = new Goods();
			good.setGoodsID(goodsID);
			carGoods.remove(good);
 			session.setAttribute("carGoods", carGoods);
 		}	
 	}

}
