package it.zeno.utils.base;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Stream;

import it.zeno.utils.file.FILE;
import it.zeno.utils.sql.DB;


interface GetConsts{
	String CONFIG_PATH = "CONFIG_PATH"
	,APP_PROPERTIES = "app.properties"
	,EXTERNAL_CONF_LOADED = "external.conf.loaded";
	
}
public class Get implements GetConsts{
	static final private Properties conf = new Properties();

	private Get() {}
	
	static public Thread threadByName(final String threadName) {
		return Thread
		.getAllStackTraces()
		.keySet()
		.stream()
		.filter(t -> t.getName().equals(threadName))
		.findFirst()
		.get();
	}
	
	static public<T>List<T>orNew(List<T>list) {
		return Optional.of(list).orElse(new ArrayList<>());
	}
	
	static public <T>T newIstance(Class<T>cl){
		try {
			return cl.getConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static public Stream<Object> instanceStream(Predicate<Class<?>>pcl){
		try {
			return classStream(pcl)
			.map(Get::newIstance);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static public Stream<Class<?>> classStream(Predicate<Class<?>>pcl) throws Exception{
		return classStream()
		.filter(pcl);
	}
	
	static public Stream<Class<?>> classStream() throws Exception{
		final Path cp = Paths.get(Thread.currentThread().getContextClassLoader().getResource("").toURI());
		
		return Files.walk(cp)
		.filter(p -> !Files.isDirectory(p) && p.toString().endsWith(".class"))
		.map(p -> {
			try{
				String rel = cp
				.relativize(p)
				.toString();
				
				rel = rel
				.substring(0,rel.lastIndexOf('.'))
				.replace(File.separatorChar, '.');
				
				return Class.forName(rel);
				
			}catch(Exception e) {
				throw new RuntimeException(e);
			}
		});	
		
	}
	
	static public Properties prop()  {
		
		if(conf.isEmpty())
			try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties")){
				conf.load(is);
				conf.setProperty(EXTERNAL_CONF_LOADED, String.valueOf(0));
			}catch (Exception e) {
				throw new RuntimeException(e);
			}
		
		if(conf.getProperty(EXTERNAL_CONF_LOADED).equals(String.valueOf(0))) {
			
			if(FILE.loadProperties(conf,Paths.get(APP_PROPERTIES)))
				conf.setProperty(EXTERNAL_CONF_LOADED, String.valueOf(1));
			
			String configPath = System.getenv(CONFIG_PATH);

			if(configPath != null) {
				FILE.loadProperties(conf,Paths.get(configPath).resolve(APP_PROPERTIES));
				conf.setProperty(EXTERNAL_CONF_LOADED, String.valueOf(1));
			}
			
			configPath = System.getProperty(CONFIG_PATH);
			
			if(configPath != null) {
				FILE.loadProperties(conf,Paths.get(configPath).resolve(APP_PROPERTIES));	
				conf.setProperty(EXTERNAL_CONF_LOADED, String.valueOf(1));
			}
		}
			
		return conf;
	}
	
	static public String prop(String...key) {
		return prop().getProperty(String.join(Str.consts.VOID, key));
	}
	
	public DB db(String label) {
		if(prop("db.inited").equals(String.valueOf(false))) {
			prop().setProperty("db.inited", String.valueOf(true));
			String[] list = prop("db.list").split(Str.consts.COMMA);
			
			String temp;
			
			for (int i = 0, l = list.length; i < l; i++) {
				temp = list[i];
				try {
					DB db = DB.class.cast(Class
					.forName(Get.prop(temp + ".db.java.wrap.type"))
					.getConstructor()
					.newInstance(temp));
					db.conf();
					prop().put(temp,db);
				}catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return (DB)prop().get(label);
	}
	
	static public Map<Class<?>, StackTraceElement> callerClass(int st){
		
		Time.start();
		
		StackTraceElement e = Thread
		.currentThread()
		.getStackTrace()[st];
		
		try {
			return Collections.singletonMap(Class.forName(e.getClassName()),e);
		} catch (ClassNotFoundException cnf) {
			throw Log.error(cnf);
		}finally {
			Time.diffMillis();
		}
	}
	
}
