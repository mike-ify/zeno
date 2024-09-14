package it.zeno.utils.file;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class FILE {

	public static String nameLessExt(Path p) {
		String name = p.getFileName().toString();
		return name.substring(0,name.lastIndexOf('.'));
	}

	public static boolean loadProperties(Properties conf, Path configPath) {
		if(Files.exists(configPath)) 
			try(InputStream is = Files.newInputStream(configPath)){
				conf.load(is);
				return true;
			}catch(Exception e) {
				throw new RuntimeException(e);
			}	
		return false;
	}

}
