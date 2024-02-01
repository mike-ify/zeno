package it.zeno.utils.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import it.zeno.utils.base.Get;

abstract public class DBImpl implements DB{
	protected String label;
	protected String dialect;
	protected String host;
	protected String url;
	
	public DBImpl(String label) {
		this.label = label;
		this.dialect = Get.prop(label + ".db.dialect");
		this.host = Get.prop(label + ".db.host"); 
	}
	
	public void conf() {
		try {
			Class.forName(Get.prop(label + ".jdbc.driver"));
			url = "jdbc:" + dialect + (':' + host);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Connection con() {
		try {
			return DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
