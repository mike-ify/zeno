package it.zeno.utils.file;

import java.io.IOException;
import java.nio.file.Path;

public class FILE {

	public static String getNameWithOutExtension(Path dir) throws IOException {
		String name = dir.getFileName().toString();	
		int dotIndex = name.lastIndexOf('.');
		return name.substring(0,dotIndex);
	}

}
