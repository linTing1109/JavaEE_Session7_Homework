package com.training.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.training.model.Goods;
import com.training.model.Member;

public class BeverageMembersDao {
	private static BeverageMembersDao beverageMemberDao = new BeverageMembersDao();

	public final static SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	private BeverageMembersDao() {
	}

	public static BeverageMembersDao getInstance() {
		return beverageMemberDao;
	}

	// 會員Id查Member
	public Member queryMemberById(String id) {
		Member member = null;
		String querySQL = "SELECT IDENTIFICATION_NO,PASSWORD,CUSTOMER_NAME FROM BEVERAGE_MEMBER WHERE IDENTIFICATION_NO=? ";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL)) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					member = new Member();
					member.setIdentificationNo(rs.getString("IDENTIFICATION_NO"));
					member.setPassword(rs.getString("PASSWORD"));
					member.setCustomerName(rs.getString("CUSTOMER_NAME"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return member;
	}

	// 新增會員
	public int createMember(Member member) {
		int result = 0;
		String insertSQL = "INSERT INTO BEVERAGE_MEMBER (IDENTIFICATION_NO,PASSWORD,CUSTOMER_NAME) VALUES(?,?,?) ";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
			pstmt.setString(1, member.getIdentificationNo());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getCustomerName());
			result = pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 更新會員
	public boolean updateMember(Member member) {
		boolean updateSuccess = false;
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();) {
			conn.setAutoCommit(false);// 設置交易不自動提交

			String updateSQL = "UPDATE BEVERAGE_MEMBER SET PASSWORD = ?, CUSTOMER_NAME = ? WHERE IDENTIFICATION_NO = ?";
			try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
				stmt.setString(1, member.getPassword());
				stmt.setString(2, member.getCustomerName());
				stmt.setString(3, member.getIdentificationNo());
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
}
