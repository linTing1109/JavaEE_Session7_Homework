package com.training.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnectionFactory {
	// 取得 Oracle 資料庫連線
	public static Connection getOracleDBConnection() {
		Connection connection = null;
		try {
			Context ctx = new InitialContext();
			DataSource dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle");// 名稱須與 server.xml 相同
			connection = dataSource.getConnection();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}
}
