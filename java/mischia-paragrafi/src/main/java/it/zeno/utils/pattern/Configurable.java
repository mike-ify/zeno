package it.zeno.utils.pattern;

import java.util.List;

import it.zeno.utils.base.Get;

public interface Configurable{
	void conf();
	default void conf(List<String>args) {}
	default String get(String...key) {
		return Get.prop(key);
	}
}
