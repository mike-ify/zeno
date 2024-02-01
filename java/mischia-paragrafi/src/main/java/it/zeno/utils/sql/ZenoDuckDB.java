package it.zeno.utils.sql;

import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.duckdb.DuckDBConnection;
import org.duckdb.DuckDBDriver;

import it.zeno.utils.base.Get;

public class ZenoDuckDB extends DBImpl implements Serializable{
	private static final long serialVersionUID = 6330549280976495551L;

	public ZenoDuckDB(String label) {
		super(label);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DuckDBConnection con() {
		Get.prop().setProperty(DuckDBDriver.JDBC_STREAM_RESULTS, String.valueOf(true));
		try {
			return (DuckDBConnection) DriverManager.getConnection(url, Get.prop());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
