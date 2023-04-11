package com.training.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.training.dao.BeverageGoodsDao;
import com.training.dao.BeverageOrderDao;
import com.training.model.Goods;
import com.training.model.Member;
import com.training.model.Order;
import com.training.vo.BuyGoodsRtn;

public class FrontendService {
	public final static SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
	private static FrontendService frontendService = new FrontendService();

	private FrontendService() {
	}

	public static FrontendService getInstance() {
		return frontendService;
	}

	private BeverageGoodsDao beverageGoodsDao = BeverageGoodsDao.getInstance();
	private BeverageOrderDao beverageOrderDao = BeverageOrderDao.getInstance();

	// 搜尋功能:總筆數
	public int searchGoods(String searchKeyword) {
		Set<Goods> goods=beverageGoodsDao.searchGoods(searchKeyword);
		return goods.size();
	}

	// 搜尋功能:分頁
	public Set<Goods> searchGoods(String searchKeyword, int pageNo) {
		int startRowNo = 1 + (pageNo - 1) * 6;// 每6個為1頁
		int endRowNo = pageNo * 6;
		return beverageGoodsDao.searchGoodsPage(searchKeyword, startRowNo, endRowNo);
	}

	// 商品購買(主幹):將購物車結帳後的購買情形來這邊判斷
	public BuyGoodsRtn buyGoodsProcess(Member member, Map<Goods, Integer> carGoods, int inputMoney) {
		BuyGoodsRtn buyGoodsRtn = new BuyGoodsRtn();
		String customerID = member.getIdentificationNo();// 顧客ID
		int realCost = 0;// 最後實際購買金額
		int outputMoney = 0;// 最後找回給顧客的金額 (投入-實際)
		boolean checkSuccess = false;// 是否購買成功
		String message = null;// 放入購物訊息使用

		if (carGoods != null) {
			// 建立訂單
			Set<Goods> keys = carGoods.keySet();
			List<Long> goodsIDs = new ArrayList<Long>();// 取得各商品id
			int buyTotalMoney =0;// 預計購買總金額
			for (Iterator<Goods> i = keys.iterator(); i.hasNext();) {
				Goods good = i.next();
				goodsIDs.add(good.getGoodsID());
				buyTotalMoney += carGoods.get(good) * good.getGoodsPrice();// 購買數量 *購買價格 並且累加
			}

			// 投入的金額是否可以購買
			boolean orderResult = inputMoney >= buyTotalMoney; // 判斷 投幣的 是否大於 預計購買的
			Map<Goods, Integer> goodsOrders = new HashMap<>();// 裝最後有購買的商品
			List<Integer> finalBuyQuantityAll = new ArrayList<>();// 裝最後有購買數量

			if (orderResult) {// 投入的金額>= 購買的金額
				// 計算最後實際可購買數量 & 更新good庫存數量 回傳實際購買總金額realCost
				realCost = findUpdateQuantity(carGoods,finalBuyQuantityAll, goodsOrders);
				// 批次更新商品庫存
				boolean updateSuccess = batchUpdateGoodsQuantity(
						carGoods.keySet().stream().collect(Collectors.toSet()));
//				message = updateSuccess ? message+"商品庫存更新成功" : message+"商品庫存更新失敗";
				checkSuccess = true;
			} else {
				message = "交易不成立,餘額不足!請重新投入";
				message +="<br/>投入金額:"+inputMoney+"<br/>找零金額:"+inputMoney;
			}
			outputMoney = inputMoney - realCost;// 投入的錢inputMoney-實際購買的錢realCost=找零的金額outputMoney
			buyGoodsRtn.setInputMoney(inputMoney);
			buyGoodsRtn.setSumOrderAmount(realCost);
			buyGoodsRtn.setChangeDollars(outputMoney);
			
			// 建立商品訂單
			boolean insertSuccess = beverageOrderDao.batchCreateGoodsOrder(customerID, goodsOrders);
//			message =insertSuccess ? message+" <br/>建立訂單成功!" : message+"  <br/>建立訂單失敗";
			
			buyGoodsRtn.setGoodsOrders(goodsOrders);
		} else {
			message = "無選擇任何商品,請先將商品放入購物車";
		}
		buyGoodsRtn.setBuyGoodMsg(message);
		buyGoodsRtn.setCheckSuccess(checkSuccess);
		return buyGoodsRtn;
	}


	// 各自商品 計算最後購買總數量 並更新後台實際數量(使用於buyGoodsProcess內)
	public int findUpdateQuantity(Map<Goods, Integer> carGoods,
			List<Integer> finalBuyQuantityAll, Map<Goods, Integer> goodsOrders) {
		int realCost = 0;// 最後實際購買金額
		Set<Goods> keys = carGoods.keySet();// 購物車內的所有商品
		for (Iterator<Goods> i = keys.iterator(); i.hasNext();) {// 商品個別做庫存判斷
			Goods g = i.next();
			int oracleGoodsQuantity = g.getGoodsQuantity();// 庫存數量
			int wantBuyQuantity = carGoods.get(g);// 顧客想購買的數量
			boolean buyResult = oracleGoodsQuantity >= wantBuyQuantity;
			int finalBuyQuantity = buyResult ? wantBuyQuantity : oracleGoodsQuantity;// 最後實際可以購買數量

			realCost += finalBuyQuantity * g.getGoodsPrice();
			finalBuyQuantityAll.add(finalBuyQuantity); // 將實際購買的數量 全部放到一個List內
			if (finalBuyQuantity != 0) {// 最後購買數量不為0 才可以放入訂單內
				goodsOrders.put(g, finalBuyQuantity);

				// 更新後台目前數量
				int updateQuantity = oracleGoodsQuantity - finalBuyQuantity;// 原先的庫存數-最後購買數
				g.setGoodsQuantity(updateQuantity);// 將更新完成數值塞回去
			}
//			else {// 若購買數量為0 則印出商品名稱跟客戶端說明
//				System.out.println(g.getGoodsName() + ":商品庫存為0,無法購買");
//			}
		}
		return realCost;
	}

	// 批次更新商品庫存 (使用於buyGoodsProcess內)
	public boolean batchUpdateGoodsQuantity(Set<Goods> goods) {
		return beverageGoodsDao.batchUpdateGoodsQuantity(goods);
	}
	
	public Set<Order> queryIdOrder(String customerID) {
		return beverageOrderDao.queryIdOrder(customerID);
	}

	// 查詢全部商品(有分頁)
	public List<Goods> queryGoods(int pageNo) {
		int startRowNo = 1 + (pageNo - 1) * 6;// 每6個為1頁
		int endRowNo = pageNo * 6;
		List<Goods> goods = beverageGoodsDao.searchGoodsPage(startRowNo, endRowNo);
		return goods;
	}

	// 查詢全部商品(沒分頁)
	public int queryGoods() {
		List<Goods> goods = beverageGoodsDao.queryGoodsStatus();
		int size = goods.size();
		return size;
	}
	
	public Set<Order> queryOrderDate(String queryStartDate, String queryEndDate,String id) {
		// 必須將 String 轉換為data類型 再轉成自己想要顯示的日期格式
		// 若直接轉會顯示IllegalArgumentException: Cannot format given Object as a Date
		DateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date dateStart = null;
		Date dateEnd = null;
		Date dateChange = null;
		if (queryStartDate == "" || queryEndDate == "") {
			dateStart = new Date();
			dateEnd = new Date();
		} else {
			try {
				dateStart = inputFormat.parse(queryStartDate);
				dateEnd = inputFormat.parse(queryEndDate);
				if (dateStart.after(dateEnd)) {// true為起訖時間相反 則兩者互換
					dateChange = dateStart;
					dateStart = dateEnd;
					dateEnd = dateChange;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		String outputStart = outputFormat.format(dateStart);
		String outputEnd = outputFormat.format(dateEnd);
		return beverageOrderDao.queryOrderDate(outputStart, outputEnd,id);
	}
}
