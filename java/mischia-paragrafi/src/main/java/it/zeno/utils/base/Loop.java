package it.zeno.utils.base;

import java.util.function.Consumer;
import java.util.function.Predicate;

final public class Loop {
	private Loop() {}
	
	static public void instance(Predicate<Class<?>>theClass,Consumer<Object>instance){
		Get
		.instanceStream(theClass)
		.forEach(instance);
	}
}
