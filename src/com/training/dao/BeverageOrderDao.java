package com.training.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.training.model.Goods;
import com.training.model.Order;
/**
 * 功能指引:
 * 訂單時間查詢訂購紀錄 queryOrderDate
 * Id查詢顧客訂單 queryIdOrder
 * 批次更新訂單資料 batchCreateGoodsOrder
 *
 */
public class BeverageOrderDao {
	private static BeverageOrderDao beverageOrderDao = new BeverageOrderDao();

	public final static SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private BeverageOrderDao() {
	}

	public static BeverageOrderDao getInstance() {
		return beverageOrderDao;
	}

	// 訂單時間查詢訂購紀錄(全部)
	public Set<Order> queryOrderDate(String queryStartDate, String queryEndDate) {
		Set<Order> reports = new LinkedHashSet<>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append(
				"Select O.ORDER_ID,M.CUSTOMER_NAME,O.ORDER_DATE,G.GOODS_NAME,O.GOODS_BUY_PRICE,O.BUY_QUANTITY ");
		querySQL.append(" From BEVERAGE_ORDER O, BEVERAGE_MEMBER M, BEVERAGE_GOODS G ");
		querySQL.append(" Where O.CUSTOMER_ID = M.IDENTIFICATION_NO And O.GOODS_ID = G.GOODS_ID ");
		querySQL.append(" And ORDER_DATE BETWEEN TO_DATE(? || ' 00:00:00', 'yyyy/MM/dd HH24:mi:ss')  ");
		querySQL.append(" And TO_DATE(? || ' 23:59:59' ,'yyyy/MM/dd HH24:mi:ss') ");
		querySQL.append(" ORDER BY O.ORDER_ID ");

		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString())) {
			stmt.setString(1, queryStartDate);
			stmt.setString(2, queryEndDate);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Order report = new Order();
					report.setOrderID(rs.getInt("ORDER_ID"));
					report.setCustomerName(rs.getString("CUSTOMER_NAME"));
					report.setOrderDate(rs.getDate("ORDER_DATE"));
					report.setGoodsName(rs.getString("GOODS_NAME"));
					report.setGoodsByPrice(rs.getInt("GOODS_BUY_PRICE"));
					report.setBuyQuantity(rs.getInt("BUY_QUANTITY"));
					report.setBuyAmount(rs.getInt("GOODS_BUY_PRICE") * rs.getInt("BUY_QUANTITY"));
					reports.add(report);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reports;
	}
	
	// 訂單時間查詢訂購紀錄(個人)
	public Set<Order> queryOrderDate(String queryStartDate, String queryEndDate,String id) {
		Set<Order> reports = new LinkedHashSet<>();
		StringBuffer querySQL = new StringBuffer();
		querySQL.append(
				"Select O.ORDER_ID,M.CUSTOMER_NAME,O.ORDER_DATE,G.GOODS_NAME,O.GOODS_BUY_PRICE,O.BUY_QUANTITY ");
		querySQL.append(" From BEVERAGE_ORDER O, BEVERAGE_MEMBER M, BEVERAGE_GOODS G ");
		querySQL.append(" Where O.CUSTOMER_ID = M.IDENTIFICATION_NO And O.GOODS_ID = G.GOODS_ID ");
		querySQL.append(" And ORDER_DATE BETWEEN TO_DATE(? || ' 00:00:00', 'yyyy/MM/dd HH24:mi:ss')  ");
		querySQL.append(" And TO_DATE(? || ' 23:59:59' ,'yyyy/MM/dd HH24:mi:ss') ");
		querySQL.append(" AND CUSTOMER_ID=? ");
		querySQL.append(" ORDER BY O.ORDER_ID ");

		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString())) {
			stmt.setString(1, queryStartDate);
			stmt.setString(2, queryEndDate);
			stmt.setString(3, id);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Order report = new Order();
					report.setOrderID(rs.getInt("ORDER_ID"));
					report.setCustomerName(rs.getString("CUSTOMER_NAME"));
					report.setOrderDate(rs.getDate("ORDER_DATE"));
					report.setGoodsName(rs.getString("GOODS_NAME"));
					report.setGoodsByPrice(rs.getInt("GOODS_BUY_PRICE"));
					report.setBuyQuantity(rs.getInt("BUY_QUANTITY"));
					report.setBuyAmount(rs.getInt("GOODS_BUY_PRICE") * rs.getInt("BUY_QUANTITY"));
					reports.add(report);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reports;
	}
	
	// 查詢顧客個人訂單(不分時間)
	public Set<Order> queryIdOrder(String id) {
		 Set<Order> reports = new LinkedHashSet<>();
		// querySQL SQL
		StringBuffer querySQL = new StringBuffer();
		querySQL.append(
				"Select O.ORDER_ID,M.CUSTOMER_NAME,O.ORDER_DATE,G.GOODS_NAME,O.GOODS_BUY_PRICE,O.BUY_QUANTITY ");
		querySQL.append(" From BEVERAGE_ORDER O, BEVERAGE_MEMBER M, BEVERAGE_GOODS G ");
		querySQL.append(" Where O.CUSTOMER_ID = M.IDENTIFICATION_NO And O.GOODS_ID = G.GOODS_ID ");
		querySQL.append(" AND CUSTOMER_ID=? ");
		querySQL.append(" ORDER BY O.ORDER_ID ");
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL.toString());) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Order report=new Order();
					report.setOrderID(rs.getInt("ORDER_ID"));
					report.setCustomerName(rs.getString("CUSTOMER_NAME"));
					report.setOrderDate(rs.getDate("ORDER_DATE"));
					report.setGoodsName(rs.getString("GOODS_NAME"));
					report.setGoodsByPrice(rs.getInt("GOODS_BUY_PRICE"));
					report.setBuyQuantity(rs.getInt("BUY_QUANTITY"));
					report.setBuyAmount(rs.getInt("GOODS_BUY_PRICE") * rs.getInt("BUY_QUANTITY"));
					reports.add(report);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reports;
	}

	public boolean batchCreateGoodsOrder(String customerID, Map<Goods, Integer> goodsOrders) {
		boolean insertSuccess = false;
		try (Connection conn = DBConnectionFactory.getOracleDBConnection(); Statement stmt = conn.createStatement()) {

			conn.setAutoCommit(false); // 設置交易不自動提交
			String insertSQL = "INSERT INTO BEVERAGE_ORDER ";
			insertSQL += " (ORDER_ID,ORDER_DATE,CUSTOMER_ID,GOODS_ID,GOODS_BUY_PRICE, BUY_QUANTITY) ";
			insertSQL += "VALUES (BEVERAGE_ORDER_SEQ.NEXTVAL, TO_DATE('%s', 'yyyy/MM/dd HH24:mi:ss'), '%s', '%s', '%s', '%s')";
			String orderDate = BeverageOrderDao.sf.format(new Date());// 訂單日期
			// 訂單資料
			for (Goods good : goodsOrders.keySet()) {
				int buyQuantity = goodsOrders.get(good);
				stmt.addBatch(String.format(insertSQL, orderDate, customerID, good.getGoodsID(), good.getGoodsPrice(),
						buyQuantity));
			}
			int[] insertCounts = stmt.executeBatch();
			for (int count : insertCounts) {
				// 只要有一筆更新失敗視為整批更新失敗資料rollback
				if (count != 1) {
					insertSuccess = false;
					conn.rollback();
					break;
				}
				insertSuccess = true;
			}
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return insertSuccess;
	}
}
