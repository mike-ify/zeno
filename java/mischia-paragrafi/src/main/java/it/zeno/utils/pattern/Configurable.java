package it.zeno.utils.pattern;

import it.zeno.utils.base.Get;

public interface Configurable{
	default boolean conf() {return true;}
	default String get(String...key) {
		return Get.prop(key);
	}
}
