package it.zeno.utils.sql;

import java.sql.Connection;

import it.zeno.utils.pattern.Configurable;

public interface DB extends Configurable{
	Connection con();
}
