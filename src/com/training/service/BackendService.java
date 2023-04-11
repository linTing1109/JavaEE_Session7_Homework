package com.training.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.training.dao.BeverageGoodsDao;
import com.training.dao.BeverageOrderDao;
import com.training.model.Goods;
import com.training.model.Order;
import com.training.vo.GoodsSearchItem;

public class BackendService {
	private static BackendService backendService = new BackendService();

	private BackendService() {
	}

	public static BackendService getInstance() {
		return backendService;
	}

	private BeverageGoodsDao beverageGoodsDao = BeverageGoodsDao.getInstance();
	private BeverageOrderDao beverageOrderDao = BeverageOrderDao.getInstance();

	public List<Goods> queryGoods() {
		List<Goods> goods = beverageGoodsDao.queryGoodsAll();
		return goods;
	}
	public Goods queryGoodsId(String goodID) {
		Long id=Long.parseLong(goodID);
		Goods goods = beverageGoodsDao.queryGoodsQuantity(id);
		return goods;
	}

	public List<Goods> searchGoodsPage(int pageNo) {
		int startRowNo = 1 + (pageNo - 1) * 6;// 每6個為1頁
		int endRowNo = pageNo * 6;
		return beverageGoodsDao.searchAllGoodsPage(startRowNo, endRowNo);
	}

	public boolean updateGoods(Goods goods) {
		Goods queryGoodsQuantity = beverageGoodsDao.queryGoodsQuantity(goods.getGoodsID());
		int realQuantityquery = queryGoodsQuantity.getGoodsQuantity();// 目前背後有的庫存
		int inputQuantity = goods.getGoodsQuantity();// 輸入要更新的庫存
		int totalQuantity = realQuantityquery + inputQuantity; // 原庫存數+更新數量
		goods.setGoodsQuantity(totalQuantity);
		return beverageGoodsDao.updateGoods(goods);
	}

	public int addGoods(Goods goods) {
		return beverageGoodsDao.addGoods(goods);

	}

	public Set<Order> queryOrderDate(String queryStartDate, String queryEndDate) {
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
		return beverageOrderDao.queryOrderDate(outputStart, outputEnd);
	}
	public List<Goods> searchGoodsList(GoodsSearchItem goodsSearch) {
		return beverageGoodsDao.searchGoodsList(goodsSearch);
	}
	public List<Goods> searchGoodsListPage(GoodsSearchItem goodsSearch,int pageNo) {
		int startNo = 1 + (pageNo - 1) * 6;// 每6個為1頁
		int endNo = pageNo * 6;
		return beverageGoodsDao.searchGoodsListPage(goodsSearch,startNo,endNo);
	}
}
