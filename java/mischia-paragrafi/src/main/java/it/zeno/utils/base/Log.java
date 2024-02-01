package it.zeno.utils.base;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class Log{
	static private Map<Class<?>,Logger>loggers = new ConcurrentHashMap<>();
	private Log(){}
	
	static private Logger log(Class<?> caller) {
		Logger log = loggers.get(caller);
		if(log == null) {
			log = LoggerFactory.getLogger(caller);
			loggers.put(caller, log);
		}
		return log;
	}

	static public RuntimeException error(Throwable t) {
		Class<?> caller = Get.callerClass(3).keySet().toArray(Class[]::new)[0];
		log(caller).error(t.getMessage(),t);
		return new RuntimeException(t);
	}

	static public void info(String m,Object...p) {
		Map<Class<?>, StackTraceElement> callerStack = Get.callerClass(3);
		Class<?> caller = callerStack.keySet().stream().findFirst().get();
		StackTraceElement ste = callerStack.get(caller);
		m = ste.toString() + " - " + m;
		log(caller).info(m,p);
	}
	
	static public void info() {
		Map<Class<?>, StackTraceElement> callerStack = Get.callerClass(3);
		Class<?> caller = callerStack.keySet().stream().findFirst().get();
		StackTraceElement ste = callerStack.get(caller);
		log(caller).info(ste.toString());
	}

}
