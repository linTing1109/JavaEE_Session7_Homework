package com.training.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.training.model.Goods;
import com.training.vo.GoodsSearchItem;
/**
 * 功能指引:
 * 搜尋全部商品 queryGoodsAll(目前沒有用到暫時放著)
 * 搜尋全部商品(前端秀出:只含上架 & 庫存大於0的) queryGoodsStatus
 * 搜尋全部商品(前端秀出:含上架&分頁&庫存大於0的):searchGoodsPage
 * 搜尋全部商品(含分頁):給後台頁面使用 searchAllGoodsPage
 * 關鍵字搜尋 searchGoods
 * 關鍵字搜尋(含分頁) searchGoodsPage
 * 商品Id查出Good商品 或現有庫存 (回傳Goods) :queryGoodsQuantity
 * 更新商品 updateGoods
 * 新增商品 addGoods
 * 查詢特定條件 searchGoodsList
 * 查詢特定條件(含分頁) searchGoodsListPage
 * 商品Id查出購買Good商品 queryBuyGoodsItem
 * 批次更新商品庫存 batchUpdateGoodsQuantity
 * 商品Id查出Good商品(只查上架) queryIdOrder
 */
public class BeverageGoodsDao {
	private static BeverageGoodsDao beverageGoodsDao = new BeverageGoodsDao();

	public final static SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private BeverageGoodsDao() {
	}

	public static BeverageGoodsDao getInstance() {
		return beverageGoodsDao;
	}

	// 搜尋全部商品
	public List<Goods> queryGoodsAll() {
		List<Goods> goods = new ArrayList<>();
		String querySQL = "SELECT GOODS_ID, GOODS_NAME, PRICE, QUANTITY, IMAGE_NAME, STATUS FROM BEVERAGE_GOODS ";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Goods good = new Goods();
				good.setGoodsID(rs.getLong("GOODS_ID"));
				good.setGoodsName(rs.getString("GOODS_NAME"));
				good.setGoodsPrice(rs.getInt("PRICE"));
				good.setGoodsQuantity(rs.getInt("QUANTITY"));
				good.setGoodsImageName(rs.getString("IMAGE_NAME"));
				good.setStatus(rs.getString("STATUS"));
				goods.add(good);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return goods;
	}

	// 搜尋全部商品(前端秀出:只含上架 & 庫存大於0的)
	public List<Goods> queryGoodsStatus() {
		List<Goods> goods = new ArrayList<>();
		String querySQL = "SELECT GOODS_ID, GOODS_NAME, PRICE, QUANTITY, IMAGE_NAME, STATUS FROM BEVERAGE_GOODS WHERE STATUS='1' and QUANTITY >0";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Goods good = new Goods();
				good.setGoodsID(rs.getLong("GOODS_ID"));
				good.setGoodsName(rs.getString("GOODS_NAME"));
				good.setGoodsPrice(rs.getInt("PRICE"));
				good.setGoodsQuantity(rs.getInt("QUANTITY"));
				good.setGoodsImageName(rs.getString("IMAGE_NAME"));
				good.setStatus(rs.getString("STATUS"));
				goods.add(good);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return goods;
	}

	// 搜尋全部商品(含上架&分頁):給前台頁面使用
	public List<Goods> searchGoodsPage(int startRowNo, int endRowNo) {
		List<Goods> goods = new ArrayList<>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append(" SELECT* FROM(SELECT ROWNUM ROW_NUM, B.* FROM BEVERAGE_GOODS B ");
		querySQL.append(" WHERE B.STATUS='1' AND B.QUANTITY >0  ORDER BY B.GOODS_ID ) WHERE ROW_NUM >=? AND ROW_NUM <= ? ");

		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			stmt.setInt(1, startRowNo);
			stmt.setInt(2, endRowNo);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Goods good = new Goods();
					good.setGoodsID(rs.getLong("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goods;
	}
	// 搜尋全部商品(含分頁):給後台頁面使用 
	public List<Goods> searchAllGoodsPage(int startRowNo, int endRowNo) {
		List<Goods> goods = new ArrayList<>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append(" SELECT* FROM(SELECT ROWNUM ROW_NUM, B.* FROM BEVERAGE_GOODS B ");
		querySQL.append(" ORDER BY B.GOODS_ID ) WHERE ROW_NUM >=? AND ROW_NUM <= ? ");
		
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			stmt.setInt(1, startRowNo);
			stmt.setInt(2, endRowNo);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Goods good = new Goods();
					good.setGoodsID(rs.getLong("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goods;
	}

	// 關鍵字搜尋
	public Set<Goods> searchGoods(String searchKeyword) {
		Set<Goods> goods = new LinkedHashSet<>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append(" SELECT ROWNUM ROW_NUM, B.* FROM BEVERAGE_GOODS B ");
		querySQL.append(" WHERE UPPER(B.GOODS_NAME) like ? AND B.STATUS='1' ");
		querySQL.append(" ORDER BY B.GOODS_ID "); // 不用ID 避免中間有漏 所以用ROW_NUM

		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			stmt.setString(1, "%" + searchKeyword.toUpperCase() + "%");
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Goods good = new Goods();
					good.setGoodsID(rs.getLong("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return goods;
	}

	// 關鍵字搜尋(含分頁)
	public Set<Goods> searchGoodsPage(String searchKeyword, int startRowNo, int endRowNo) {
		Set<Goods> goods = new LinkedHashSet<>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("SELECT* FROM(SELECT ROWNUM ROW_NUM, B.* FROM BEVERAGE_GOODS B ");
		querySQL.append(" WHERE UPPER(B.GOODS_NAME) like ? AND B.STATUS='1' ");
		querySQL.append(" ORDER BY B.GOODS_ID) WHERE ROW_NUM >=? AND ROW_NUM <= ?"); 

		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			stmt.setString(1, "%" + searchKeyword.toUpperCase() + "%");
			stmt.setInt(2, startRowNo);
			stmt.setInt(3, endRowNo);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Goods good = new Goods();
					good.setGoodsID(rs.getLong("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return goods;
	}

	// 商品Id查出Good商品 或現有庫存
	public Goods queryGoodsQuantity(Long searchID) {
		Goods goods = null;
		String querySQL = "SELECT GOODS_ID,GOODS_NAME,DESCRIPTION,PRICE,QUANTITY,IMAGE_NAME,STATUS FROM BEVERAGE_GOODS WHERE GOODS_ID=? ";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL)) {
			stmt.setLong(1, searchID);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					goods = new Goods();
					goods.setGoodsID(rs.getLong("GOODS_ID"));
					goods.setGoodsName(rs.getString("GOODS_NAME"));
					goods.setDescription(rs.getString("DESCRIPTION"));
					goods.setGoodsPrice(rs.getInt("PRICE"));
					goods.setGoodsQuantity(rs.getInt("QUANTITY"));
					goods.setGoodsImageName(rs.getString("IMAGE_NAME"));
					goods.setStatus(rs.getString("STATUS"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return goods;
	}

	// 更新商品
	public boolean updateGoods(Goods goods) {
		boolean updateSuccess = false;
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();) {
			conn.setAutoCommit(false);// 設置交易不自動提交

			String updateSQL = "UPDATE BEVERAGE_GOODS SET PRICE = ?, QUANTITY = ?, STATUS = ?, DESCRIPTION =?  WHERE GOODS_ID = ?";
			try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
				stmt.setInt(1, goods.getGoodsPrice());
				stmt.setInt(2, goods.getGoodsQuantity());
				stmt.setString(3, goods.getStatus());
				stmt.setString(4, goods.getDescription());
				stmt.setLong(5, goods.getGoodsID());
				int recordCount = stmt.executeUpdate();
				updateSuccess = (recordCount > 0) ? true : false;
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			updateSuccess = false;
			e.printStackTrace();
		}
		return updateSuccess;
	}
	//新增商品
	public int addGoods(Goods goods) {
		int goodsID = 0;
		String[] cols = { "GOODS_ID" };
		String insertSQL = "INSERT INTO BEVERAGE_GOODS(GOODS_ID,GOODS_NAME,DESCRIPTION,PRICE,QUANTITY,IMAGE_NAME,STATUS) VALUES (BEVERAGE_GOODS_SEQ.NEXTVAL,?, ?, ?, ?, ?,?)";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement pstmt = conn.prepareStatement(insertSQL, cols)) {
			pstmt.setString(1, goods.getGoodsName());
			pstmt.setString(2, goods.getDescription());
			pstmt.setInt(3, goods.getGoodsPrice());
			pstmt.setInt(4, goods.getGoodsQuantity());
			pstmt.setString(5, goods.getGoodsImageName());
			pstmt.setString(6, goods.getStatus());
			pstmt.executeUpdate();

			ResultSet rsKeys = pstmt.getGeneratedKeys();
			rsKeys.next();
			goodsID = rsKeys.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return goodsID;
	}

	public static final String EMPTY_STR = "";
	
	// 查詢特定條件
	public List<Goods> searchGoodsList(GoodsSearchItem goodsSearch) {
		List<Goods> goods = new ArrayList<>();
		StringBuffer querySQL = new StringBuffer();
		// ROW_NUMBER()使用方式需搭配OVER:Select ROW_NUMBER() OVER (ORDER BY [欄位名稱] (ASC or
		// DESC) as [ROW_NUMBER欄位名稱] ,* from [資料表名稱]
//			querySQL.append(" SELECT ROW_NUMBER() OVER (ORDER BY B.PRICE DESC ) ROW_NUM, ");
		//原本單純只有價格排序的部分:
//		querySQL.append(" SELECT ROW_NUMBER() OVER (ORDER BY B.PRICE ");
//		querySQL.append(goodsSearch.getOrderByPrice());
		querySQL.append(" SELECT ROW_NUMBER() OVER (ORDER BY ");
		switch (goodsSearch.getOrderByItem()) {
		case "":
			querySQL.append(" B.PRICE ASC ");
			break;
		case "priceDesc":
			querySQL.append(" B.PRICE DESC ");
			break;
		case "idAsc":
			querySQL.append(" B.GOODS_ID ASC ");
			break;
		case "idDesc":
			querySQL.append(" B.GOODS_ID DESC ");
			break;
		case "quantityAsc":
			querySQL.append(" B.QUANTITY ASC ");
			break;
		case "quantityDesc":
			querySQL.append(" B.QUANTITY DESC ");
			break;	
		default:
			querySQL.append(" B.PRICE ASC ");
			break;
		}
		
		querySQL.append(" ) ROW_NUM, ");
		querySQL.append(" B.GOODS_ID, B.GOODS_NAME,B.DESCRIPTION,B.PRICE,B.QUANTITY,B.IMAGE_NAME,B.STATUS ");
		querySQL.append(" FROM BEVERAGE_GOODS B ");
		querySQL.append(" WHERE B.GOODS_ID IS NOT NULL ");

		if (!EMPTY_STR.equals(goodsSearch.getGoodsId())) {
			querySQL.append(" AND B.GOODS_ID = ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getGoodsName())) {
			querySQL.append(" AND UPPER(B.GOODS_NAME) like ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getPriceMin())) {
			querySQL.append(" AND B.PRICE >= ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getPriceMax())) {
			querySQL.append(" AND B.PRICE <= ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getStockQuantity())) {
			querySQL.append(" AND B.QUANTITY <= ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getStatus())) {
			querySQL.append(" AND B.STATUS=? ");
		}

		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			int paramIndex = 0;
			if (!EMPTY_STR.equals(goodsSearch.getGoodsId())) {
				stmt.setInt(++paramIndex, Integer.parseInt(goodsSearch.getGoodsId()));
			}
			if (!EMPTY_STR.equals(goodsSearch.getGoodsName())) {
				stmt.setString(++paramIndex, "%" + goodsSearch.getGoodsName().toUpperCase() + "%");
			}
			if (!EMPTY_STR.equals(goodsSearch.getPriceMin())) {
				stmt.setInt(++paramIndex, Integer.parseInt(goodsSearch.getPriceMin()));
			}
			if (!EMPTY_STR.equals(goodsSearch.getPriceMax())) {
				stmt.setInt(++paramIndex, Integer.parseInt(goodsSearch.getPriceMax()));
			}
			if (!EMPTY_STR.equals(goodsSearch.getStockQuantity())) {
				stmt.setInt(++paramIndex, Integer.parseInt(goodsSearch.getStockQuantity()));
			}
			if (!EMPTY_STR.equals(goodsSearch.getStatus())) {
				stmt.setString(++paramIndex, goodsSearch.getStatus());
			}

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Goods good = new Goods();
					good.setGoodsID(rs.getLong("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goods;
	}
	
	// 查詢特定條件(含分頁)
	public List<Goods> searchGoodsListPage(GoodsSearchItem goodsSearch, int startNo, int endNo) {
		List<Goods> goods = new ArrayList<>();
		StringBuffer querySQL = new StringBuffer();
		// ROW_NUMBER()使用方式需搭配OVER:Select ROW_NUMBER() OVER (ORDER BY [欄位名稱] (ASC or
		// DESC) as [ROW_NUMBER欄位名稱] ,* from [資料表名稱]
//				querySQL.append(" SELECT ROW_NUMBER() OVER (ORDER BY B.PRICE DESC ) ROW_NUM, ");
		//原本單純只有價格排序的部分:
//		querySQL.append(" SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY B.PRICE ");
//		querySQL.append(goodsSearch.getOrderByPrice());
		querySQL.append(" SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY ");
		switch (goodsSearch.getOrderByItem()) {
		case "":
			querySQL.append(" B.PRICE ASC ");
			break;
		case "priceDesc":
			querySQL.append(" B.PRICE DESC ");
			break;
		case "idAsc":
			querySQL.append(" B.GOODS_ID ASC ");
			break;
		case "idDesc":
			querySQL.append(" B.GOODS_ID DESC ");
			break;
		case "quantityAsc":
			querySQL.append(" B.QUANTITY ASC ");
			break;
		case "quantityDesc":
			querySQL.append(" B.QUANTITY DESC ");
			break;
		default:
			querySQL.append(" B.PRICE ASC ");
			break;
		}
		
		querySQL.append(" ) ROW_NUM, ");
		querySQL.append(" B.GOODS_ID, B.GOODS_NAME,B.DESCRIPTION,B.PRICE,B.QUANTITY,B.IMAGE_NAME,B.STATUS ");
		querySQL.append(" FROM BEVERAGE_GOODS B ");
		querySQL.append(" WHERE B.GOODS_ID IS NOT NULL ");

		if (!EMPTY_STR.equals(goodsSearch.getGoodsId())) {
			querySQL.append(" AND B.GOODS_ID = ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getGoodsName())) {
			querySQL.append(" AND UPPER(B.GOODS_NAME) like ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getPriceMin())) {
			querySQL.append(" AND B.PRICE >= ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getPriceMax())) {
			querySQL.append(" AND B.PRICE <= ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getStockQuantity())) {
			querySQL.append(" AND B.QUANTITY <= ? ");
		}
		if (!EMPTY_STR.equals(goodsSearch.getStatus())) {
			querySQL.append(" AND B.STATUS=? ");
		}
		querySQL.append(" )WHERE ROW_NUM >=? and ROW_NUM <= ? ORDER BY ROW_NUM ");

		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			int paramIndex = 0;
			if (!EMPTY_STR.equals(goodsSearch.getGoodsId())) {
				stmt.setInt(++paramIndex, Integer.parseInt(goodsSearch.getGoodsId()));
			}
			if (!EMPTY_STR.equals(goodsSearch.getGoodsName())) {
				stmt.setString(++paramIndex, "%" + goodsSearch.getGoodsName().toUpperCase() + "%");
			}
			if (!EMPTY_STR.equals(goodsSearch.getPriceMin())) {
				stmt.setInt(++paramIndex, Integer.parseInt(goodsSearch.getPriceMin()));
			}
			if (!EMPTY_STR.equals(goodsSearch.getPriceMax())) {
				stmt.setInt(++paramIndex, Integer.parseInt(goodsSearch.getPriceMax()));
			}
			if (!EMPTY_STR.equals(goodsSearch.getStockQuantity())) {
				stmt.setInt(++paramIndex, Integer.parseInt(goodsSearch.getStockQuantity()));
			}
			if (!EMPTY_STR.equals(goodsSearch.getStatus())) {
				stmt.setString(++paramIndex, goodsSearch.getStatus());
			}
			stmt.setInt(++paramIndex, startNo);
			stmt.setInt(++paramIndex, endNo);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Goods good = new Goods();
					good.setGoodsID(rs.getLong("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goods;
	}

	// 商品Id查出購買Good商品
	public Map<Long, Goods> queryBuyGoodsItem(List<Long> goodsIDs) {
		Map<Long, Goods> goods = new LinkedHashMap<>();
		Function<Long, String> mapper = id -> "?"; // Function<T,R> 接收T 返回R
		String idParams = goodsIDs.stream().map(mapper).collect(Collectors.joining(","));
		String querySQL = "SELECT GOODS_ID, GOODS_NAME, PRICE, QUANTITY, IMAGE_NAME, STATUS FROM BEVERAGE_GOODS WHERE GOODS_ID IN ("
				+ idParams + ")";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL)) {
			int parameterIndex = 0;
			for (Long goodsID : goodsIDs) {
				stmt.setLong(++parameterIndex, goodsID);
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Goods good = new Goods();
					good.setGoodsID(rs.getLong("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.put(rs.getLong("GOODS_ID"), good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return goods;
	}

	// 批次更新商品庫存
	public boolean batchUpdateGoodsQuantity(Set<Goods> goods) {
		boolean updateSuccess = false;
		try (Connection conn = DBConnectionFactory.getOracleDBConnection(); Statement stmt = conn.createStatement()) {

			conn.setAutoCommit(false);// 設置交易不自動提交
			// addBatch 批次執行SQL指令
			String updateGoodsSql = "UPDATE BEVERAGE_GOODS SET QUANTITY = %s WHERE GOODS_ID = %s";
			for (Goods good : goods) {
				stmt.addBatch(String.format(updateGoodsSql, good.getGoodsQuantity(), good.getGoodsID()));
			}
			int[] updateCounts = stmt.executeBatch();
			for (int count : updateCounts) {
				// 只要有一筆更新失敗視為整批更新失敗資料rollback
				if (count != 1) {
					updateSuccess = false;
					conn.rollback();
					break;
				}
				updateSuccess = true;
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return updateSuccess;
	}

	// 商品Id查出Good商品(只查上架)
	public Goods queryIdOrder(Long goodsId) {
		Goods goods = new Goods();
		// querySQL SQL
		StringBuffer querySQL = new StringBuffer();
		querySQL.append("SELECT GOODS_ID,GOODS_NAME,DESCRIPTION,PRICE,QUANTITY,IMAGE_NAME,STATUS FROM BEVERAGE_GOODS ");
		querySQL.append(" WHERE STATUS=1 AND GOODS_ID=? ");// STATUS=1避免買到未上架商品
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			stmt.setLong(1, goodsId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					goods.setGoodsID(rs.getLong("GOODS_ID"));
					goods.setGoodsName(rs.getString("GOODS_NAME"));
					goods.setDescription(rs.getString("DESCRIPTION"));
					goods.setGoodsPrice(rs.getInt("PRICE"));
					goods.setGoodsQuantity(rs.getInt("QUANTITY"));
					goods.setGoodsImageName(rs.getString("IMAGE_NAME"));
					goods.setStatus(rs.getString("STATUS"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return goods;
	}
}
